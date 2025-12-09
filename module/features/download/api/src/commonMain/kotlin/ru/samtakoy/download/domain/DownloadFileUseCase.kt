package ru.samtakoy.download.domain

import io.github.vinceglb.filekit.PlatformFile

interface DownloadFileUseCase {
    suspend fun downloadToTempFile(url: String): PlatformFile
}