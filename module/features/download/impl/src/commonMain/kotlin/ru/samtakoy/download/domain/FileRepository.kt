package ru.samtakoy.download.domain

import io.github.vinceglb.filekit.PlatformFile

internal interface FileRepository {
    suspend fun downloadToTempFile(url: String): PlatformFile
}