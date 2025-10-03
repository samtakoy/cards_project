package ru.samtakoy.core.data.local.database.room.entities

import androidx.room.*
import ru.samtakoy.core.app.utils.DateUtils
import ru.samtakoy.core.data.local.database.room.converters.*
import ru.samtakoy.core.data.local.database.room.entities.elements.Schedule
import ru.samtakoy.core.data.local.database.room.entities.types.CourseType
import ru.samtakoy.core.data.local.database.room.entities.types.LearnCourseMode
import ru.samtakoy.core.presentation.log.MyLog
import ru.samtakoy.core.utils.CollectionUtils
import java.io.Serializable
import java.util.*

@Entity(
    tableName = LearnCourseEntity.table,
    foreignKeys = [
        ForeignKey(
            entity = QPackEntity::class,
            parentColumns = ["_id"],
            childColumns = [LearnCourseEntity._qpack_id],
            onDelete = ForeignKey.RESTRICT
        )
    ],
    indices = [Index(LearnCourseEntity._mode), Index(LearnCourseEntity._course_type)]
)
/**
 * TODO убрать data класс после добавлени мапперов
 * */
data class LearnCourseEntity(

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = _id)
    val id: Long,

    // пока привязываем к паку
    @ColumnInfo(name = _qpack_id, index = true)
    val qPackId: Long,

    @field:TypeConverters(CourseTypeConverter::class)
    @ColumnInfo(name = _course_type)
    val courseType: CourseType,

    // исходный курс, который догоняем дополнительными карточками
    @ColumnInfo(name = _primary_course_id)
    val primaryCourseId: Long,
    //
    @ColumnInfo(name = _title)
    val title: String,

    // 0 - ожидание первого просмотра,
    // 1 - незаконченный первый просмотр,
    // 2 - ожидание повторения
    // 3 - незаконченное повторение
    @field:TypeConverters(LearnCourseModeConverter::class)
    @ColumnInfo(name = _mode)
    val mode: LearnCourseMode,
    @ColumnInfo(name = _repeated_count)
    val repeatedCount: Int,

    @field:TypeConverters(CardIdListConverter::class)
    @ColumnInfo(name = _card_ids)
    val cardIds: List<Long>,

    /** следующее расписание после выполения текущего запланируемого  */
    @field:TypeConverters(ScheduleStringConverter::class)
    @ColumnInfo(name = _rest_schedule)
    val restSchedule: Schedule,

    /** то, что уже было запланировано  */
    @field:TypeConverters(ScheduleStringConverter::class)
    @ColumnInfo(name = _realized_schedule)
    val realizedSchedule: Schedule,
    // дата с которой стартовать очередную итерацию
    // для активного плана дата в прошлом,
    // для неактивированного плана список TODO == пустой строке
    @field:TypeConverters(DateLongConverter::class)
    @ColumnInfo(name = _repeat_date)
    val repeatDate: Date

) : Serializable {

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
        const val _rest_schedule = "rest_schedule"
        const val _realized_schedule = "realized_schedule"
        const val _repeat_date = "repeat_date"

        //==

        fun initNew(
            qPackId: Long,
            courseType: CourseType,
            title: String,
            mode: LearnCourseMode,
            cardIds: List<Long>?,
            restSchedule: Schedule?,
            repeatDate: Date?
        ): LearnCourseEntity {

            return LearnCourseEntity(
                0L,
                qPackId,
                courseType,
                0L,
                title,
                mode,
                0,
                if (cardIds == null) ArrayList() else ArrayList(cardIds),
                restSchedule ?: Schedule.createEmpty(),
                Schedule(),
                repeatDate ?: DateUtils.getCurrentTimeDate()
            )
        }
        //==
    }
}