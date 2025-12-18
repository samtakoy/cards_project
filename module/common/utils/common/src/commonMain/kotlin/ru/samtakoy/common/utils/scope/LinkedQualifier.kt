package ru.samtakoy.common.utils.scope

import org.koin.core.qualifier.Qualifier

/**
 * @param links scope с [LinkedQualifier] при использовании [useScope]
 * автоматически запросит и прилинкует к себе скоупы с квалификаторами из links.
 * Эти прилинкованные скоупы автоматически будут "отпущены" внутри [releaseScope].
 * */
interface LinkedQualifier : Qualifier {
    val links: List<Qualifier>
}