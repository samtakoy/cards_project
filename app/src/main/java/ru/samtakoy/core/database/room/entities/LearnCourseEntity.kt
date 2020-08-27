package ru.samtakoy.core.database.room.entities

import androidx.room.*
import ru.samtakoy.core.database.room.converters.*
import ru.samtakoy.core.database.room.entities.elements.Schedule
import ru.samtakoy.core.database.room.entities.types.CourseType
import ru.samtakoy.core.database.room.entities.types.LearnCourseMode
import ru.samtakoy.core.screens.log.MyLog
import ru.samtakoy.core.utils.DateUtils
import java.io.Serializable
import java.util.*

@Entity(tableName = LearnCourseEntity.table,

        foreignKeys = [
            ForeignKey(
                    entity = QPackEntity::class,
                    parentColumns = ["_id"],
                    childColumns = [LearnCourseEntity._qpack_id],
                    onDelete = ForeignKey.RESTRICT)
        ],
        indices = [Index(LearnCourseEntity._mode), Index(LearnCourseEntity._course_type)]
)
class LearnCourseEntity(

        @PrimaryKey(autoGenerate = true)
        @ColumnInfo(name = LearnCourseEntity._id) var id: Long,

        // пока привязываем к паку
        @ColumnInfo(name = LearnCourseEntity._qpack_id) var qPackId: Long,
        @field:TypeConverters(CourseTypeConverter::class)
        @ColumnInfo(name = LearnCourseEntity._course_type) var courseType: CourseType,

        // исходный курс, который догоняем дополнительными карточками
        @ColumnInfo(name = LearnCourseEntity._primary_course_id) var primaryCourseId: Long,
        //
        @ColumnInfo(name = LearnCourseEntity._title) var title: String,

        // 0 - ожидание первого просмотра,
        // 1 - незаконченный первый просмотр,
        // 2 - ожидание повторения
        // 3 - незаконченное повторение
        @field:TypeConverters(LearnCourseModeConverter::class)
        @ColumnInfo(name = LearnCourseEntity._mode) var mode: LearnCourseMode,
        @ColumnInfo(name = LearnCourseEntity._repeated_count) var repeatedCount: Int,

        @field:TypeConverters(CardIdListConverter::class)
        @ColumnInfo(name = LearnCourseEntity._card_ids) var cardIds: MutableList<Long>,
        @field:TypeConverters(CardIdDequeConverter::class)
        @ColumnInfo(name = LearnCourseEntity._todo_card_ids) var todoCardIds: Deque<Long>,
        // это для перепланирования, по окончании 1го цикла
        @field:TypeConverters(CardIdListConverter::class)
        @ColumnInfo(name = LearnCourseEntity._error_card_ids) var errorCardIds: MutableList<Long>,

        /** следующее расписание после выполения текущего запланируемого  */
        @field:TypeConverters(ScheduleStringConverter::class)
        @ColumnInfo(name = LearnCourseEntity._rest_schedule) var restSchedule: Schedule,

        /** то, что уже было запланировано  */
        @field:TypeConverters(ScheduleStringConverter::class)
        @ColumnInfo(name = LearnCourseEntity._realized_schedule) var realizedSchedule: Schedule,
        // дата с которой стартовать очередную итерацию
        // для активного плана дата в прошлом,
        // для неактивированного плана список TODO == пустой строке
        @field:TypeConverters(DateLongConverter::class)
        @ColumnInfo(name = LearnCourseEntity._repeat_date) var repeatDate: java.util.Date

) : Serializable {


    //@Ignore
    //private var viewedCardIds: Deque<Long>? = null

    companion object {

        const val table = "learn_course"

        const val _id = "_id"
        const val _qpack_id = "qpack_id"
        const val _course_type = "course_type"
        const val _primary_course_id = "primary_course_id"
        const val _title = "title"
        const val _mode = "mode"
        const val _repeated_count = "repeated_count"
        const val _card_ids = "card_ids"
        const val _todo_card_ids = "todo_card_ids"
        const val _error_card_ids = "error_card_ids"
        const val _rest_schedule = "rest_schedule"
        const val _realized_schedule = "realized_schedule"
        const val _repeat_date = "repeat_date"


        //==

        fun initNew(
                qPackId: Long, title: String, mode: LearnCourseMode, cardIds: List<Long>?,
                restSchedule: Schedule?,
                repeatDate: Date?
        ): LearnCourseEntity {

            return LearnCourseEntity(
                    0L, qPackId, CourseType.PRIMARY, 0L,
                    title, mode, 0,
                    if (cardIds == null) createNewEmptyCardIds() else ArrayList(cardIds),
                    createNewEmptyTodoIds(),
                    createNewEmptyErrorIds(),
                    restSchedule ?: Schedule.createEmpty(),
                    Schedule(),
                    repeatDate ?: DateUtils.getCurrentTimeDate()
            )
        }


        fun createNewPreparing(
                qPackId: Long, title: String,
                mode: LearnCourseMode,
                cardIds: List<Long>,
                restSchedule: Schedule,
                repeatDate: Date
        ): LearnCourseEntity {
            return initNew(qPackId, title,
                    mode,
                    cardIds, restSchedule, repeatDate)
        }

        fun createNewAdditional(
                qPackId: Long,
                title: String,
                cardIds: List<Long>,
                restSchedule: Schedule,
                deltaMillis: Int
        ): LearnCourseEntity {
            return initNew(
                    qPackId, title, LearnCourseMode.LEARN_WAITING,
                    ArrayList(cardIds), restSchedule,
                    DateUtils.dateFromDbSerialized(DateUtils.getCurrentTimeLong() + deltaMillis)
            )
        }

        private fun createNewEmptyCardIds(): ArrayList<Long> {
            return ArrayList()
        }

        private fun createNewEmptyErrorIds(): MutableList<Long> {
            return ArrayList()
        }

        private fun createNewEmptyTodoIds(): Deque<Long> {
            return ArrayDeque()
        }


        //==
    }


    fun hasNotInCards(cardIds: List<Long>): Boolean {
        for (cardId in cardIds) {
            if (!this.cardIds.contains(cardId)) {
                return true
            }
        }
        return false
    }

    fun getNotInCards(cardIds: List<Long>): List<Long>? {
        val result: MutableList<Long> = ArrayList()
        for (cardId in cardIds) {
            if (!this.cardIds.contains(cardId)) {
                result.add(cardId)
            }
        }
        return result
    }

    fun addCardsToCourse(newCardsToAdd: List<Long>) {
        this.cardIds.addAll(newCardsToAdd)
    }

    fun getProgress(): Float {
        return 1 - this.todoCardIds.size.toFloat() / (this.cardIds.size.toFloat())
    }

    fun hasRealizedSchedule(): Boolean {
        return !realizedSchedule.isEmpty()
    }

    fun hasRestSchedule(): Boolean {
        return restSchedule != null && !restSchedule.isEmpty()
    }

    fun hasQPackId(): Boolean {
        return qPackId > 0
    }

    fun prepareToCardsView(shuffleCards: Boolean) {
        makeInitialTodos(shuffleCards)
        errorCardIds = createNewEmptyErrorIds()
    }

    private fun makeInitialTodos(shuffleCards: Boolean) {
        val shuffledIds: List<Long?>
        if (shuffleCards) {
            shuffledIds = createShuffledIds()
        } else {
            shuffledIds = cardIds
        }
        todoCardIds = createNewEmptyTodoIds()
        for (id in shuffledIds) {
            todoCardIds.addLast(id)
        }
    }

    private fun createShuffledIds(): List<Long?> {
        val shuffledIds: List<Long?> = ArrayList<Long>(cardIds)
        Collections.shuffle(shuffledIds)
        return shuffledIds
    }

    fun change(qPackId: Long, cardIds: List<Long>, repeatDate: Date) {
        this.qPackId = qPackId
        this.cardIds = ArrayList(cardIds)
        this.repeatDate = repeatDate
    }

    fun hasTodoCards(): Boolean {
        return todoCardIds.size > 0
    }

    fun finishLearnOrRepeat(currentTime: Date) {
        if (restSchedule.isEmpty()) {
            mode = LearnCourseMode.COMPLETED
        } else {
            if (mode != LearnCourseMode.LEARNING) {
                repeatedCount++
            }
            mode = LearnCourseMode.REPEAT_WAITING
            setNextRepeatDateFrom(currentTime)
        }
    }

    private fun setNextRepeatDateFrom(currentTime: Date) {
        if (!restSchedule.isEmpty()) {
            val currentSystemTimeMillis = DateUtils.getTimeLong(currentTime)
            realizedSchedule.addItem(restSchedule.getFirstItem())
            val nextDateLong: Long = restSchedule.extractFirstItemInMillis() + currentSystemTimeMillis
            repeatDate = DateUtils.dateFromDbSerialized(nextDateLong)
            MyLog.add("setNextRepeatDateFrom, cur:" + currentSystemTimeMillis / 1000 + ", newD:" + getRepeatDateDebug(this))
        } else {
            MyLog.add("restSchedule is Empty");
        }
    }

    fun getRepeatDateDebug(learnCourse: LearnCourseEntity): Long {
        return DateUtils.dateToDbSerialized(learnCourse.repeatDate) / 1000
    }

    fun hasRealId(): Boolean {
        return id > 0
    }

    fun peekCurCardToView(): Long? {
        return if (todoCardIds.isEmpty()) {
            // TODO exception
            -1L
        } else todoCardIds.peekFirst()
    }

    fun isLastCard(): Boolean {
        return todoCardIds.size == 1
    }

    fun getViewedCardsCount(): Int {
        return cardIds.size - todoCardIds.size
    }

    fun getCardsCount(): Int {
        return cardIds.size
    }


    @Throws(Exception::class)
    fun makeViewedWithError(viewedCardIds: Deque<Long>) {
        if (todoCardIds.isEmpty()) {
            throw Exception("todo cards is empty")
        }
        val cardId: Long = todoCardIds.peekFirst()!!
        // ошибочную в конец - пока просто так
        if (!isAlreadyInErrors(cardId)) {
            errorCardIds.add(cardId)
        }
        makeViewedCurCard(viewedCardIds)
    }


    fun isAlreadyInErrors(cardId: Long): Boolean {
        return errorCardIds.contains(cardId)
    }

    fun getErrorCardsCount(): Int = errorCardIds.size

    fun getRepeatDateUTCMillis(): Long {
        return DateUtils.dateToDbSerialized(repeatDate)
    }

    fun toLearnMode() {
        mode = LearnCourseMode.LEARNING
        makeInitialTodos(false)
        errorCardIds = createNewEmptyErrorIds()
    }

    fun toRepeatMode() {
        mode = LearnCourseMode.REPEATING
        makeInitialTodos(true)
        errorCardIds = createNewEmptyErrorIds()
    }

    // ======================================================================
    // ======================================================================

    private fun createNewEmptyViewedCardIds(): ArrayDeque<Long> {
        return ArrayDeque()
    }

    fun rollback(viewedCardIds: Deque<Long>): Boolean {
        if (!canRollback(viewedCardIds)) {
            return false
        }
        todoCardIds.addFirst(viewedCardIds.removeLast())
        return true
    }

    fun canRollback(viewedCardIds: Deque<Long>): Boolean {
        return viewedCardIds.size > 0
    }

    @Throws(java.lang.Exception::class)
    fun makeViewedCurCard(viewedCardIds: Deque<Long>) {
        if (todoCardIds.isEmpty()) {
            throw java.lang.Exception("todo cards is empty")
        }
        viewedCardIds.addLast(todoCardIds.removeFirst())
    }

    // TODO временно
    fun getDynamicTitle(): String {
        // активные
        if (mode == LearnCourseMode.LEARNING || mode == LearnCourseMode.REPEATING) {
            return "(a)" + title
        } else if (mode == LearnCourseMode.LEARN_WAITING || mode == LearnCourseMode.REPEAT_WAITING) {
            if (getMillisToStart() <= 0) {
                return "(!)" + title
            }
        }
        return title
    }

    fun getMillisToStart(): Long {
        return DateUtils.getMillisTo(repeatDate)
    }


}