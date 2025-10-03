package ru.samtakoy.core.presentation.progress_dialog

interface ProgressDialogPresenter {

    /**
     * TODO refactor
     * */
    interface IProgressWorker {
        /** создание работника */
        suspend fun doWork()

        /** id текста - заголовка прогресс диалога */
        fun getTitle(): String

        /** id текста при ошибке */
        fun getErrorText(): String

        /** действие при успехе */
        fun onComplete() {}
    }
}