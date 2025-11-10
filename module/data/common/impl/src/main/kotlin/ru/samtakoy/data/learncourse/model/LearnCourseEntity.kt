package ru.samtakoy.data.learncourse.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import ru.samtakoy.data.common.db.converters.ListOfLongConverter
import ru.samtakoy.data.learncourse.converters.CourseTypeConverter
import ru.samtakoy.data.learncourse.converters.LearnCourseModeConverter
import ru.samtakoy.data.learncourse.converters.ScheduleStringConverter
import ru.samtakoy.data.learncourse.model.schedule.ScheduleEntity
import ru.samtakoy.data.qpack.QPackEntity

@Entity(
    tableName = LearnCourseEntity.table,
    foreignKeys = [
        ForeignKey(
            entity = QPackEntity::class,
            parentColumns = ["_id"],
            childColumns = [LearnCourseEntity._qpack_id],
            onDelete = ForeignKey.Companion.RESTRICT
        )
    ],
    indices = [Index(LearnCourseEntity._mode), Index(LearnCourseEntity._course_type)]
)
/**
 * TODO убрать data класс после добавлени мапперов
 * */
internal class LearnCourseEntity(

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = _id)
    val id: Long,

    // пока привязываем к паку
    @ColumnInfo(name = _qpack_id, index = true)
    val qPackId: Long,

    @field:TypeConverters(CourseTypeConverter::class)
    @ColumnInfo(name = _course_type)
    val courseType: CourseTypeEntity,

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
    val mode: LearnCourseModeEntity,
    @ColumnInfo(name = _repeated_count)
    val repeatedCount: Int,

    @field:TypeConverters(ListOfLongConverter::class)
    @ColumnInfo(name = _card_ids)
    val cardIds: List<Long>,

    /** следующее расписание после выполения текущего запланируемого  */
    @field:TypeConverters(ScheduleStringConverter::class)
    @ColumnInfo(name = _rest_schedule)
    val restSchedule: ScheduleEntity,

    /** то, что уже было запланировано  */
    @field:TypeConverters(ScheduleStringConverter::class)
    @ColumnInfo(name = _realized_schedule)
    val realizedSchedule: ScheduleEntity,
    // дата с которой стартовать очередную итерацию
    // для активного плана дата в прошлом,
    // для неактивированного плана список TODO == пустой строке
    @ColumnInfo(name = _repeat_date)
    val repeatDate: Long

) {

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

        /*
        fun initNew(
            qPackId: Long,
            courseType: CourseTypeEntity,
            title: String,
            mode: LearnCourseModeEntity,
            cardIds: List<Long>?,
            restSchedule: ScheduleEntity?,
            repeatDate: Instant?
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
                restSchedule ?: ScheduleEntity.Companion.createEmpty(),
                ScheduleEntity(emptyList()),
                repeatDate ?: DateUtils.currentTimeDate
            )
        }*/
        //==
    }
}