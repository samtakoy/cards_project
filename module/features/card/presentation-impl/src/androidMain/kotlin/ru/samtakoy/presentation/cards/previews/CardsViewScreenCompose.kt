package ru.samtakoy.presentation.cards.previews

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.runBlocking
import org.jetbrains.compose.resources.PreviewContextConfigurationEffect
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.android.ext.koin.androidContext
import org.koin.core.KoinApplication
import ru.samtakoy.common.utils.di.commonUtilsModule
import ru.samtakoy.presentation.cards.di.cardPresentationModule
import ru.samtakoy.presentation.cards.screens.view.CardsViewScreenInternal
import ru.samtakoy.presentation.cards.screens.view.model.CodeType
import ru.samtakoy.presentation.cards.screens.view.model.ContentPart
import ru.samtakoy.presentation.cards.screens.view.vm.CardsViewViewModel.CardState
import ru.samtakoy.presentation.cards.screens.view.vm.CardsViewViewModel.State
import ru.samtakoy.presentation.cards.screens.view.vm.mapper.AnswerButtonsMapper
import ru.samtakoy.presentation.cards.screens.view.vm.mapper.QuestionButtonsMapper
import ru.samtakoy.presentation.cards.view.model.CardViewMode
import ru.samtakoy.presentation.core.design_system.base.theme.MyTheme
import ru.samtakoy.presentation.utils.asA
import ru.samtakoy.presentation.utils.getALoremIpsum

@Preview
@Composable
private fun CardsViewScreenInternal_Preview() = MyTheme {
    // для текстовых ресурсов
    PreviewContextConfigurationEffect()
    val koin = KoinApplication.init()
        .androidContext(LocalContext.current)
        .modules(
            commonUtilsModule(),
            cardPresentationModule()
        )
    val questionMapper = koin.koin.get<QuestionButtonsMapper>()
    val answerMapper = koin.koin.get<AnswerButtonsMapper>()
    val parts: ImmutableList<ContentPart> = listOf(
        ContentPart.Text(getALoremIpsum()),
        ContentPart.Code("val s: String = \"Kotlin\"", CodeType.Kotlin, "kotlin"),
        ContentPart.Code("val s: String = \"Kotlin\"", CodeType.AutoParsedKotlin, "some"),
        ContentPart.Code("some text\n and some text", CodeType.Text, "text"),
        ContentPart.Code("val s: String = \"Unknown\"", CodeType.Kotlin, ""),
        ContentPart.Code("val s: String = \"Swift\"", CodeType.Swift, "swift"),
        ContentPart.Text(getALoremIpsum()),
    ).toImmutableList()

    val cards = remember {
        listOf<CardState>(
            CardState(
                id = 1,
                isQuestion = true,
                content = CardState.Content(
                    isFavorite = true,
                    parts = parts,
                    hasRevertButton = true
                )
            ),
            CardState(
                id = 1,
                isQuestion = false,
                content = CardState.Content(
                    isFavorite = true,
                    parts = parts,
                    hasRevertButton = true
                )
            )
        ).toImmutableList()
    }
    CardsViewScreenInternal(
        viewState = State(
            type = State.Type.Card(
                cardsCountTitle = "2/10".asA(),
                cardIndex = 0,
            ),
            isLoading = false,
            cardItems = cards,
            questionButtons = runBlocking { questionMapper.map(CardViewMode.LEARNING).toImmutableList() },
            answerButtons = runBlocking { answerMapper.map(CardViewMode.LEARNING).toImmutableList() },
        ),
        onEvent = {},
        snackbarHostState = remember { SnackbarHostState() }
    )
}