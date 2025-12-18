package ru.samtakoy.data.remote.di

import org.koin.core.qualifier.Qualifier
import org.koin.core.qualifier.QualifierValue

object NoBaseUrlKtorfit : Qualifier {
    override val value: QualifierValue = "NoUrlKtorfit"
}