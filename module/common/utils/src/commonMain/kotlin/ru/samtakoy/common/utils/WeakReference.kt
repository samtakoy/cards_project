package ru.samtakoy.common.utils

expect class WeakReference<T>(value: T) {
    fun get(): T?
}
