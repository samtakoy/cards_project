package ru.samtakoy.common.resources

interface Resources {
    fun getString(resId: Int): String
    fun getString(resId: Int, vararg formatArgs: Any?): String
}