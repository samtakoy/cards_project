package ru.samtakoy.importcards.domain.batch.utils

class ImportCardsException(
    val errorId: Int,
    message: String?
) : Exception(message) {
    companion object {
        const val ERR_PACK_ID_MISSING: Int = 1
        const val ERR_PACK_WITH_ID_CREATION_NOT_ALLOWED: Int = 2
        const val ERR_WRONG_CARD_PACK: Int = 3
    }
}