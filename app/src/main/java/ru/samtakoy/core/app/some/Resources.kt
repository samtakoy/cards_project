package ru.samtakoy.core.app.some

import android.content.Context
import androidx.annotation.StringRes
import javax.inject.Inject

interface Resources {
    fun getString(@StringRes resId: Int): String
    fun getString(resId: Int, vararg formatArgs: Any?): String
}

internal class ResourcesImpl @Inject constructor(
    private val context: Context
) : Resources {
    override fun getString(resId: Int): String {
        return context.getString(resId)
    }

    override fun getString(resId: Int, vararg formatArgs: Any?): String {
        return context.getString(resId, *formatArgs)
    }
}