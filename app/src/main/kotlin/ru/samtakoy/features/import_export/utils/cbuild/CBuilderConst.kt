package ru.samtakoy.features.import_export.utils.cbuild

object CBuilderConst {
    const val QPACK_ID_PREFIX: String = "id:"
    const val TITLE_PREFIX: String = "title:"
    const val DESC_PREFIX: String = "desc:"
    const val DATE_PREFIX: String = "date:"
    const val VIEWS_PREFIX: String = "views:"
    const val QUESTION_PREFIX: String = "q:"
    const val ANSWER_PREFIX: String = "a:"
    const val TAGS_PREFIX: String = "#"
    const val TAGS_PREFIX2: String = "tags:"
    const val IMAGE_PREFIX: String = "img:"

    const val LINE_BREAK: String = "\n"

    const val CARD_REMOVE_TAG: String = "[remove]"
    const val CARD_REMOVE_TAG2: String = "[r]"

    const val NO_ID: Long = 0L
    val NO_ID_STR: String = NO_ID.toString()
}
