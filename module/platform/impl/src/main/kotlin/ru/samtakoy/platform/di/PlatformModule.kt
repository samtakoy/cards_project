package ru.samtakoy.platform.di

import android.content.ContentResolver
import android.content.Context
import org.koin.dsl.module

fun platformModule() = module {
    // TODO удалить? и модуль?
    factory<ContentResolver> { get<Context>().getContentResolver() }
}