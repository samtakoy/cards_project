package ru.samtakoy.core.model.utils;

import java.io.IOException;
import java.io.Writer;
import java.util.List;

import ru.samtakoy.core.model.Card;
import ru.samtakoy.core.model.QPack;
import ru.samtakoy.core.model.Tag;
import ru.samtakoy.core.model.utils.cbuild.CBuilderConst;

public class QPackExporter {


    public static void export(QPack qPack, List<Card> cards, Writer writer) throws IOException {

        writer.write(CBuilderConst.QPACK_ID_PREFIX);
        writer.write(String.valueOf(qPack.getId()));
        writer.write(CBuilderConst.LINE_BREAK);

        if(qPack.hasTitle()){
            writer.write(CBuilderConst.TITLE_PREFIX);
            writer.write(qPack.getTitle());
            writer.write(CBuilderConst.LINE_BREAK);
        }
        if(qPack.hasDesc()){
            writer.write(CBuilderConst.DESC_PREFIX);
            writer.write(qPack.getDesc());
            writer.write(CBuilderConst.LINE_BREAK);
        }

        writer.write(CBuilderConst.DATE_PREFIX);
        writer.write(qPack.getCreationDateAsString());
        writer.write(CBuilderConst.LINE_BREAK);

        writer.write(CBuilderConst.VIEWS_PREFIX);
        writer.write(String.valueOf(qPack.getViewCount()));
        writer.write(CBuilderConst.LINE_BREAK);

        writer.write(CBuilderConst.LINE_BREAK);
        for(Card card:cards){
            exportOneCard(card, writer);
            writer.write(CBuilderConst.LINE_BREAK);
        }
    }

    private static void exportOneCard(Card card, Writer writer) throws IOException {

//        q:[13213][removed]
//
//        q:[13213]: dependencies
//        a:
//        #


        writer.write(CBuilderConst.QUESTION_PREFIX);
        writer.write("[");
        writer.write(String.valueOf(card.getId()));
        writer.write("]");
        writer.write(card.getQuestion());
        writer.write(CBuilderConst.LINE_BREAK);

        writer.write(CBuilderConst.ANSWER_PREFIX);
        writer.write(card.getAnswer());
        writer.write(CBuilderConst.LINE_BREAK);

        for(String img:card.getImages()){
            writer.write(CBuilderConst.IMAGE_PREFIX);
            writer.write(img);
            writer.write(CBuilderConst.LINE_BREAK);
        }

        exportTags(card.getTags(), writer);

    }

    private static void exportTags(List<Tag> tags, Writer writer) throws IOException {


        writer.write(CBuilderConst.TAGS_PREFIX);
        if (tags.size() == 0) {
            writer.write(CBuilderConst.LINE_BREAK);
            return;
        }


        writer.write(tags.get(0).getTitle());

        for(int i=1; i<tags.size(); i++){
            writer.write(" ");
            writer.write(CBuilderConst.TAGS_PREFIX);
            writer.write(tags.get(i).getTitle());
        }
        writer.write(CBuilderConst.LINE_BREAK);
    }


}
