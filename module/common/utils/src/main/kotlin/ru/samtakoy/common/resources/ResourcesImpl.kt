package ru.samtakoy.common.resources

import android.content.Context
import javax.inject.Inject

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