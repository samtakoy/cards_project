package ru.samtakoy.common.utils

import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

class WeakRef<T>(target: T?) : ReadWriteProperty<Any?, T?> {
    private var ref = target?.let { WeakReference(it) }
    override fun getValue(thisRef: Any?, property: KProperty<*>): T? = ref?.get()
    override fun setValue(thisRef: Any?, property: KProperty<*>, value: T?) {
        ref = value?.let { WeakReference(it) }
    }
}
fun <T> weak(obj: T? = null) = WeakRef(obj)