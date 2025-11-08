package ru.samtakoy.resources

import androidx.compose.ui.text.AnnotatedString
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.getString

suspend fun getFormatted(resource: StringResource, vararg args: Any?): String {
    val template = getString(resource)
    return if (args.isEmpty()) {
        template
    } else {
        String.format(template, *args)
    }
}

suspend fun getFormattedA(resource: StringResource, vararg args: Any?): AnnotatedString {
    return AnnotatedString(getFormatted(resource, *args))
}