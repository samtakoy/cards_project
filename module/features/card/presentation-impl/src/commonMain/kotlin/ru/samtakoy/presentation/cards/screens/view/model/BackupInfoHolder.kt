package ru.samtakoy.presentation.cards.screens.view.model

import kotlinx.serialization.Serializable

@Serializable
internal data class BackupInfoHolder(
    val cardIdToBackupMap: Map<Long, BackupInfo>
)