package ru.samtakoy.download.domain

import io.github.vinceglb.filekit.PlatformFile

internal class DownloadFileUseCaseImpl(
    private val fileRepository: FileRepository
) : DownloadFileUseCase {
    override suspend fun downloadToTempFile(url: String): PlatformFile {
        return fileRepository.downloadToTempFile(url = url)
    }
}