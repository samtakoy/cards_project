package ru.samtakoy.common.utils

import kotlin.experimental.ExperimentalNativeApi
import kotlin.native.ref.WeakReference

@OptIn(ExperimentalNativeApi::class)
actual class WeakReference<T : Any> actual constructor(value: T) {  // value как в expect
    private val nativeWeakRef = WeakReference(value)

    actual fun get(): T? = nativeWeakRef.get()
}