package ru.samtakoy.common.di

import com.google.gson.Gson
import dagger.Component
import ru.samtakoy.common.resources.Resources
import ru.samtakoy.platform.di.PlatformComponent

@CommonUtilsScope
@Component(
    modules = [CommonUtilsModule::class],
    dependencies = [PlatformComponent::class]
)
interface CommonUtilsComponent {
    fun resources(): Resources
    fun gson(): Gson
}