package ru.samtakoy.features.import_export

import android.net.Uri
import io.reactivex.Completable
import ru.samtakoy.domain.importcards.model.ImportCardsOpts

interface ImportApi {

    fun loadCardsFromFile(selectedFileUri: Uri, targetThemeId: Long, opts: ImportCardsOpts): Completable

    fun batchLoadFromFolder(dirPath: String, targetThemeId: Long, opts: ImportCardsOpts): Completable

    fun batchUpdateFromZip(zipFileUri: Uri, opts: ImportCardsOpts): Completable
}