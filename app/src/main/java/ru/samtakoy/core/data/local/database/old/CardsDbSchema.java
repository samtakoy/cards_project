package ru.samtakoy.core.data.local.database.old;

// больше не используется (перешли на Room), оставил для истории
public class CardsDbSchema {

    public static final class Themes {
        public static final String NAME = "themes";

        public static class Cols {
            public static final String ID = "_id";
            public static final String TITLE = "title";
            public static final String PARENT = "parent";
        }
    }

    public static final class Tags {
        public static final String NAME = "tags";

        public static class Cols {
            public static final String ID = "_id";
            public static final String TITLE = "title";
        }
    }

    public static final class ThemesTags {
        public static final String NAME = "themes_tags";

        public static class Cols {
            public static final String THEME_ID = "theme_id";
            public static final String TAG_ID= "tag_id";
        }
    }

    public static final class QPacks {
        public static final String NAME = "qpacks";

        public static class Cols {
            public static final String ID = "_id";
            public static final String THEME_ID = "theme_id";
            public static final String PATH = "path";
            public static final String TITLE = "title";
            public static final String FILE_NAME = "file_name";
            public static final String DESC = "desc";
            public static final String CREATION_DATE = "creation_date";
            public static final String VIEW_COUNTER = "view_counter";
            public static final String LAST_VIEW_DATE = "last_view_date";

        }
    }

    public static final class Cards {
        public static final String NAME = "cards";

        public static class Cols {
            public static final String ID = "_id";
            public static final String QPACK_ID = "qpack_id";
            public static final String QUESTION = "question";
            public static final String ANSWER = "answer";
            public static final String AIMAGES = "aimages";
            public static final String COMMENT = "comment";
            public static final String VIEWS = "views";
            public static final String ERRORS = "errors";
            public static final String LAST_GOOD_VIEWS = "last_good_views";
            public static final String LAST_ERRORS = "last_errors";
            public static final String LAST_VIEW_DATE = "last_view_date";

        }
    }

    public static final class CardsTags {
        public static final String NAME = "cards_tags";

        public static class Cols {
            public static final String CARD_ID = "card_id";
            public static final String TAG_ID = "tag_id";
        }
    }

    /* забыл как хотел использовать, слишком много внешних ключей
    public static final class Todos {
        public static final String NAME = "todos";
        public static class Cols {
            public static final String ID = "_id";
            // @
            public static final String THEME_ID= "theme_id";
            // @
            public static final String CARD_ID= "card_id";
            public static final String TEXT= "text";
        }
    }*/

    public static final class LearnCourse {
        public static final String NAME = "learn_course";

        public static class Cols {
            public static final String ID = "_id";
            // из какого набора, если есть
            // @
            public static final String QPACK_ID = "qpack_id";
            public static final String COURSE_TYPE = "course_type";
            // @
            public static final String PRIMARY_COURSE_ID = "primary_course_id";
            public static final String TITLE = "title";
            // 0 - ожидание первого просмотра,
            // 1 - незаконченный первый просмотр,
            // 2 - ожидание повторения
            // 3 - незаконченное повторение
            public static final String MODE = "mode";

            // сколько раз повторили
            public static final String REPEATED_COUNT = "repeated_count";

            // всегда одно и тоже - список карт
            public static final String CARD_IDS = "card_ids";
            // сколько осталось до реализации плана,
            public static final String TODO_CARD_IDS = "todo_card_ids";
            // какие вызвали ошибку
            public static final String ERROR_CARD_IDS = "error_card_ids";

            // интервалы последующих повторений
            public static final String REST_SCHEDULE = "rest_schedule";
            public static final String REALIZED_SCHEDULE = "realized_schedule";
            // дата с которой стартовать очередную итерацию
            // для активного плана дата в прошлом,
            // для неактивированного плана список TODO == пустой строке
            public static final String REPEAT_DATE = "repeat_date";

        }
    }

}
