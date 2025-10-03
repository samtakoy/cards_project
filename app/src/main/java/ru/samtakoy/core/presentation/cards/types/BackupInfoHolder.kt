package ru.samtakoy.core.presentation.cards.types

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlin.collections.mutableMapOf

@Parcelize
data class BackupInfoHolder(
    val cardIdToBackupMap: Map<Long, BackupInfo>
) : Parcelable