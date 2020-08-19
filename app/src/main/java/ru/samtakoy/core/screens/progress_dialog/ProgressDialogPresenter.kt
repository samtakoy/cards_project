package ru.samtakoy.core.screens.progress_dialog

import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import moxy.InjectViewState
import moxy.MvpPresenter
import org.apache.commons.lang3.exception.ExceptionUtils
import ru.samtakoy.core.screens.log.MyLog
import javax.inject.Inject

@InjectViewState
class ProgressDialogPresenter(val workerImpl: WorkerImpl) : MvpPresenter<ProgressDialogView>() {

    interface WorkerImpl {
        /** создание работника */
        fun createObservable(): Completable

        /** id текста - заголовка прогресс диалога */
        fun getTitleTextId(): Int

        /** id текста при ошибке */
        fun getErrorTextId(): Int

        /** действие при успехе */
        fun onComplete() {}
    }

    class Factory @Inject constructor() {
        fun create(workerImpl: WorkerImpl) = ProgressDialogPresenter(workerImpl)
    }

    private val mSubscriber: Disposable

    init {

        viewState.showTitle(workerImpl.getTitleTextId())

        mSubscriber = workerImpl.createObservable()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        {
                            exitOk()
                        },
                        {
                            onError(it)
                        }
                )

    }

    override fun onDestroy() {

        mSubscriber.dispose()
        super.onDestroy()
    }

    private fun exitOk() {
        workerImpl.onComplete()
        viewState.exitOk()
    }

    protected fun onError(err: Throwable) {
        MyLog.add("""progress dialog  error: ${err.message}, ${ExceptionUtils.getStackTrace(err)}""")
        viewState.showError(workerImpl.getErrorTextId())
        viewState.exitCanceled()
    }


}