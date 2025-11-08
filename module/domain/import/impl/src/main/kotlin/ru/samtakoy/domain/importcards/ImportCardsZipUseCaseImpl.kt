package ru.samtakoy.domain.importcards

import io.github.vinceglb.filekit.PlatformFile
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flatMapMerge
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.getString
import ru.samtakoy.common.utils.MyLog
import ru.samtakoy.data.common.transaction.TransactionRepository
import ru.samtakoy.data.importcards.CardsSourceRepository
import ru.samtakoy.data.importcards.model.QPackSource
import ru.samtakoy.domain.cardtag.ConcurrentTagMap
import ru.samtakoy.domain.cardtag.TagInteractor
import ru.samtakoy.domain.importcards.batch.ImportCardsZipUseCase
import ru.samtakoy.domain.importcards.batch.utils.ImportCardsException
import ru.samtakoy.domain.importcards.batch.utils.builder.QPackBuilder
import ru.samtakoy.domain.importcards.model.ImportCardsOpts
import ru.samtakoy.domain.task.model.TaskStateData
import ru.samtakoy.resources.Res
import ru.samtakoy.resources.batch_zip_import_status_cards_saving
import ru.samtakoy.resources.batch_zip_import_status_qpacks_preparing
import ru.samtakoy.resources.batch_zip_import_status_qpacks_saving
import ru.samtakoy.resources.batch_zip_import_status_reading_from_zip
import ru.samtakoy.resources.batch_zip_import_status_tags_saving
import ru.samtakoy.resources.batch_zip_import_status_theme_actualization
import ru.samtakoy.resources.common_err_message
import ru.samtakoy.resources.getFormatted
import kotlin.concurrent.atomics.AtomicInt
import kotlin.concurrent.atomics.ExperimentalAtomicApi

private typealias StateReportCallback = suspend (state: TaskStateData) -> Unit

internal class ImportCardsZipUseCaseImpl(
    private val qPackBuilderInteractor: QPackBuilderInteractorImpl,
    private val tagInteractor: TagInteractor,
    private val sourceRepository: CardsSourceRepository,
    private val transactionRepository: TransactionRepository
) : ImportCardsZipUseCase {

    @OptIn(ExperimentalAtomicApi::class, ExperimentalCoroutinesApi::class)
    override suspend fun import(
        zipFile: PlatformFile,
        opts: ImportCardsOpts,
        reportCallback: suspend (state: TaskStateData) -> Unit
    ) {
        try {
            transactionRepository.withTransaction {
                importImpl(reportCallback, zipFile, opts)
            }
            reportCallback(TaskStateData.Success)
        } catch (ex: ImportCardsException) {
            val errMessage = ex.message ?: getString(Res.string.common_err_message)
            reportCallback(TaskStateData.Error(errMessage))
        } catch (t: Throwable) {
            MyLog.add(t.message.orEmpty(), t)
            val errMessage = getString(Res.string.common_err_message)
            reportCallback(TaskStateData.Error(errMessage))
        }
    }

    @OptIn(ExperimentalAtomicApi::class)
    private suspend fun importImpl(
        reportCallback: suspend (TaskStateData) -> Unit,
        zipFile: PlatformFile,
        opts: ImportCardsOpts
    ) {
        reportCallback.status(0f, Res.string.batch_zip_import_status_reading_from_zip)

        val sourceList = sourceRepository
            .getFromZip(zipFile)
            .toList().also {
                // Сохранить темы
                reportCallback.status(0f, Res.string.batch_zip_import_status_theme_actualization)
            }.map { source: QPackSource ->
                val themeId = qPackBuilderInteractor.actualizeThemePaths(source.parentThemeNames)
                source.copy(themeId = themeId)
            }

        // Подготовка построителей моделек
        reportCallback.status(0f, Res.string.batch_zip_import_status_qpacks_preparing)
        val allTagMap = tagInteractor.buildTagMap()
        var buildersList = prepareBuilders(sourceList, allTagMap, opts)

        // Сохранить паки
        reportCallback.status(0f, Res.string.batch_zip_import_status_qpacks_saving)
        buildersList = saveQPacks(buildersList, opts)

        val newTags = allTagMap.getNewTags()
        if (newTags.isNotEmpty()) {
            reportCallback.status(0f, Res.string.batch_zip_import_status_tags_saving, newTags.size)
            // Сохранить новые теги
            val updatedTags = tagInteractor.addTags(newTags)
            allTagMap.addTags(updatedTags)
        }

        val cardsCount = buildersList.sumOf { it.cardBuilders.size }
        val processedCards = AtomicInt(0)

        // Сохранить карточки
        reportCallback.status(
            progress = 0f,
            stringId = Res.string.batch_zip_import_status_cards_saving,
            processedCards.load(),
            cardsCount
        )
        saveCards(buildersList, allTagMap) { builder ->
            processedCards.addAndFetch(builder.cardBuilders.size)
            reportCallback.status(
                progress = processedCards.load().toFloat() / cardsCount,
                stringId = Res.string.batch_zip_import_status_cards_saving,
                processedCards.load(),
                cardsCount
            )
        }
    }

    @OptIn(ExperimentalAtomicApi::class, ExperimentalCoroutinesApi::class)
    private suspend fun saveCards(
        buildersList: List<QPackBuilder>,
        allTagMap: ConcurrentTagMap,
        onFinish: suspend (QPackBuilder) -> Unit
    ): List<QPackBuilder> {
        return buildersList.asFlow()
            .flatMapMerge<QPackBuilder, QPackBuilder> { qPackBuilder ->
                qPackBuilder.cardBuilders.forEach {
                    qPackBuilderInteractor.saveCardToDatabase(it, allTagMap)
                }
                onFinish(qPackBuilder)
                flowOf(qPackBuilder)
            }
            .toList()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private suspend fun saveQPacks(
        buildersList: List<QPackBuilder>,
        opts: ImportCardsOpts
    ): List<QPackBuilder> {
        return buildersList.asFlow()
            .flatMapMerge<QPackBuilder, QPackBuilder> { qPackBuilder ->
                qPackBuilderInteractor.saveQPackToDatabase(qPackBuilder, opts)
                flowOf(qPackBuilder.build())
            }
            .toList()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private suspend fun prepareBuilders(
        sourceList: List<QPackSource>,
        allTagMap: ConcurrentTagMap,
        opts: ImportCardsOpts
    ): List<QPackBuilder> {
        return sourceList.asFlow()
            .flatMapMerge<QPackSource, QPackBuilder> { source ->
                flowOf(
                    qPackBuilderInteractor.createQPackBuilder(
                        source,
                        allTagMap,
                        opts
                    )
                )
            }
            .filter {
                qPackBuilderInteractor.isAllowedByOpts(it, opts)
            }
            .toList()
    }

    private suspend fun StateReportCallback.status(
        progress: Float,
        stringId: StringResource,
        vararg formatArgs: Any?
    ) {
        invoke(
            TaskStateData.ActiveStatus(
                message = getFormatted(stringId, *formatArgs),
                progress = progress
            )
        )
    }
}