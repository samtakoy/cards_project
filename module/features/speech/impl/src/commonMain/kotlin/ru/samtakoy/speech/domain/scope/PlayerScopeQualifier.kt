package ru.samtakoy.speech.domain.scope

import org.koin.core.qualifier.Qualifier
import org.koin.core.qualifier.QualifierValue
import ru.samtakoy.common.utils.scope.LinkedQualifier

object PlayerScopeQualifier : LinkedQualifier {

    override val value: QualifierValue = PlayerScopeQualifier::class.qualifiedName.orEmpty()

    /** Использует скоуп состояния плейера. */
    override val links: List<Qualifier> = listOf(PlayerStateScopeQualifier)
}