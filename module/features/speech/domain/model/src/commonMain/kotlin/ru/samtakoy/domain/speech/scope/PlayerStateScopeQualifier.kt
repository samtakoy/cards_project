package ru.samtakoy.domain.speech.scope

import org.koin.core.qualifier.Qualifier
import org.koin.core.qualifier.QualifierValue

/**
 * В этом скоупе живет состояние плейера.
 * Отделено от PlayerScopeQualifier, т.к. состояние может существовать и в тот момент,
 * когда активного плейера нет.
 * */
object PlayerStateScopeQualifier : Qualifier {
    override val value: QualifierValue = PlayerStateScopeQualifier::class.qualifiedName.orEmpty()
}