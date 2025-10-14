package ru.samtakoy.common.di

import com.google.gson.Gson
import dagger.Component
import ru.samtakoy.common.resources.Resources
import ru.samtakoy.platform.di.PlatformApiComponent

@CommonUtilsScope
@Component(
    modules = [CommonUtilsModule::class],
    dependencies = [PlatformApiComponent::class]
)
interface CommonUtilsComponent {
    fun resources(): Resources
    fun gson(): Gson
}