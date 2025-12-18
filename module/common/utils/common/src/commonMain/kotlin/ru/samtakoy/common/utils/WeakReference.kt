package ru.samtakoy.common.utils

expect class WeakReference<T: Any>(value: T) {
    fun get(): T?
}
