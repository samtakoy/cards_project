package ru.samtakoy.common.utils

class Dispatcher<T>() {

    private val listeners = mutableSetOf<WeakReference<T>>()

    fun addListener(listener: T) {
        removeListener(listener)
        listeners.add(
            WeakReference<T>(listener)
        )
    }

    fun removeListener(listener: T) {
        listeners.removeAll {
            val value = it.get()
            value == listener || value == null
        }
    }

    fun dispatch(block: T.() -> Unit) {
        val iterator = listeners.iterator()
        while (iterator.hasNext()) {
            val weakRef = iterator.next()
            val listener = weakRef.get()
            if (listener == null) {
                iterator.remove()
            } else {
                listener.block()
            }
        }
    }
}