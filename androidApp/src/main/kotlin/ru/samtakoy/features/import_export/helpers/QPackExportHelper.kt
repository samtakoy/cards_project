package ru.samtakoy.features.import_export.helpers

import ru.samtakoy.common.utils.DateUtils
import ru.samtakoy.domain.card.domain.model.CardWithTags
import ru.samtakoy.importcards.domain.batch.utils.builder.CBuilderConst
import ru.samtakoy.domain.qpack.QPack
import ru.samtakoy.domain.cardtag.Tag
import kotlin.time.ExperimentalTime
import java.io.IOException
import java.io.Writer

object QPackExportHelper {
    @Throws(IOException::class)
    fun export(qPack: QPack, cards: List<CardWithTags>, writer: Writer) {
        writer.write(CBuilderConst.QPACK_ID_PREFIX)
        writer.write(qPack.id.toString())
        writer.write(CBuilderConst.LINE_BREAK)

        if (!qPack.title.isEmpty()) {
            writer.write(CBuilderConst.TITLE_PREFIX)
            writer.write(qPack.title)
            writer.write(CBuilderConst.LINE_BREAK)
        }
        if (!qPack.desc.isEmpty()) {
            writer.write(CBuilderConst.DESC_PREFIX)
            writer.write(qPack.desc)
            writer.write(CBuilderConst.LINE_BREAK)
        }

        writer.write(CBuilderConst.DATE_PREFIX)
        writer.write(qPack.getCreationDateAsString())
        writer.write(CBuilderConst.LINE_BREAK)

        writer.write(CBuilderConst.VIEWS_PREFIX)
        writer.write(qPack.viewCount.toString())
        writer.write(CBuilderConst.LINE_BREAK)

        writer.write(CBuilderConst.LINE_BREAK)
        for (card in cards) {
            exportOneCard(card, writer)
            writer.write(CBuilderConst.LINE_BREAK)
        }
    }

    @Throws(IOException::class)
    private fun exportOneCard(cardWithTags: CardWithTags, writer: Writer) {
        //        q:[13213][removed]
        //
        //        q:[13213]: dependencies
        //        a:
        //        #

        val card = cardWithTags.card

        writer.write(CBuilderConst.QUESTION_PREFIX)
        writer.write("[")
        writer.write(card.id.toString())
        writer.write("]")
        writer.write(card.question)
        writer.write(CBuilderConst.LINE_BREAK)

        writer.write(CBuilderConst.ANSWER_PREFIX)
        writer.write(card.answer)
        writer.write(CBuilderConst.LINE_BREAK)

        for (img in card.aImages) {
            writer.write(CBuilderConst.IMAGE_PREFIX)
            writer.write(img)
            writer.write(CBuilderConst.LINE_BREAK)
        }

        QPackExportHelper.exportTags(cardWithTags.tags, writer)
    }

    @Throws(IOException::class)
    private fun exportTags(tags: List<Tag>, writer: Writer) {
        writer.write(CBuilderConst.TAGS_PREFIX)
        if (tags.size == 0) {
            writer.write(CBuilderConst.LINE_BREAK)
            return
        }


        writer.write(tags.get(0)!!.title)

        for (i in 1..<tags.size) {
            writer.write(" ")
            writer.write(CBuilderConst.TAGS_PREFIX)
            writer.write(tags.get(i)!!.title)
        }
        writer.write(CBuilderConst.LINE_BREAK)
    }

    private fun QPack.getCreationDateAsString(): String {
        @OptIn(ExperimentalTime::class)
        return DateUtils.formatToString(creationDate)
    }
}
