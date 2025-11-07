package ru.samtakoy.presentation.cards.screens.view.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
internal data class BackupInfoHolder(
    val cardIdToBackupMap: Map<Long, BackupInfo>
) : Parcelable