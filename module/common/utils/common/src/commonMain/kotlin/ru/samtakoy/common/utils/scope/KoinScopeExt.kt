package ru.samtakoy.common.utils.scope

import io.github.aakira.napier.Napier
import org.koin.core.Koin
import org.koin.core.qualifier.Qualifier
import org.koin.core.scope.Scope
import ru.samtakoy.common.utils.log.MyLog

/**
 * Возвращает существующий Scope по [qualifier] либо создает новый, если такого еще не создано.
 * Считает количество использований.
 * Для [LinkedQualifier] вызывает связанные scope из [LinkedQualifier.links]
 * и прилинковывает целевой scope к ним.
 *
 * Для каждого [useScope] обязателен симметричный вызов [releaseScope].
 * */
fun Koin.useScope(qualifier: Qualifier): Scope {
    val scope = getOrCreateScope(
        scopeId = qualifier.value,
        qualifier = qualifier
    )
    increaseScopeRefCounter(qualifier)
    if (qualifier is LinkedQualifier) {
        useParentLinks(qualifier, scope)
    }
    return scope
}

/**
 * Освобождает scope связанный с [qualifier] от использования.
 * Уменьшает счетчик использований.
 * При достижении счетчиком 0 - закрывает scope и прилинкованные scopes для [LinkedQualifier].
 * */
fun Koin.releaseScope(qualifier: Qualifier) {
    val scope = getScopeOrNull(
        scopeId = qualifier.value
    )
    if (scope == null) {
        throw Exception("Scope not found in detachScope() for ${qualifier.value}")
    }
    if (scope.closed) {
        throw Exception("Scope already closed in detachScope() for ${qualifier.value}")
    }
    val scopeRefCounter = decreaseScopeRefCounter(qualifier)
    if (scopeRefCounter < 0) {
        throw Exception(
            "Wrong scope ref counter in detachScope() for ${qualifier.value}, counter: ${scopeRefCounter}"
        )
    }
    if (qualifier is LinkedQualifier) {
        unuseParentLinks(qualifier)
    }
    if (scopeRefCounter == 0) {
        scope.close()
        Napier.d(tag = MyLog.tag) { "scope is closed: ${qualifier.value}" }
    }
}

fun Scope.releaseScope() {
    getKoin().releaseScope(qualifier = scopeQualifier)
}

private fun Koin.useParentLinks(
    qualifier: LinkedQualifier,
    scope: Scope
) {
    qualifier.links.forEach { parentScopeQualifier ->
        val parentScope = useScope(parentScopeQualifier)
        scope.linkTo(parentScope)
    }
}

private fun Koin.unuseParentLinks(
    qualifier: LinkedQualifier
) {
    qualifier.links.forEach { parentScopeQualifier ->
        releaseScope(parentScopeQualifier)
    }
}

private fun Koin.increaseScopeRefCounter(qualifier: Qualifier) {
    val counterKey = qualifier.getScopeRefCounterName()
    val newCounterValue = getProperty<Int>(counterKey, 0) + 1
    setProperty(
        key = counterKey,
        value = newCounterValue
    )
    Napier.d(tag = MyLog.tag) { "++scope: $newCounterValue, for: ${qualifier.value}" }
}

private fun Koin.decreaseScopeRefCounter(qualifier: Qualifier): Int {
    val counterKey = qualifier.getScopeRefCounterName()
    val newCounterValue = getProperty<Int>(counterKey, 0) - 1
    setProperty(
        key = counterKey,
        value = newCounterValue
    )
    Napier.d(tag = MyLog.tag) { "--scope: $newCounterValue, for: ${qualifier.value}" }
    return newCounterValue
}

private fun Qualifier.getScopeRefCounterName(): String {
    return "scopeCounter[${value}]"
}
