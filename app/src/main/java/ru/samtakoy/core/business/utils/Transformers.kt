package ru.samtakoy.core.business.utils

import io.reactivex.CompletableTransformer
import io.reactivex.FlowableTransformer
import io.reactivex.SingleTransformer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

fun c_io_mainThread() = CompletableTransformer {
    it
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
}


fun <T> s_io_mainThread() = SingleTransformer<T, T> {
    it
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
}

fun <T> f_io_mainThread() = FlowableTransformer<T, T> {
    it
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
}

/*
internal class CompletableIoToMain : CompletableTransformer {
    override fun apply(upstream: Completable): CompletableSource {
        return upstream
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
    }
}*/

/*
internal class SingleIoToMain<T> : SingleTransformer<T, T> {
    override fun apply(upstream: Single<T>): SingleSource<T> {
        return upstream
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
    }
}*/
/*
internal class FlowableIoToMain<T> : FlowableTransformer<T, T> {
    override fun apply(upstream: Flowable<T>): Publisher<T> {
        return upstream
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
    }
}*/
