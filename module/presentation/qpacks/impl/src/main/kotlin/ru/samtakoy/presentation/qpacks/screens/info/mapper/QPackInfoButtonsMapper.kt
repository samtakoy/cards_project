package ru.samtakoy.presentation.qpacks.screens.info.mapper

import org.jetbrains.compose.resources.getString
import ru.samtakoy.common.coroutines.SuspendLazy
import ru.samtakoy.presentation.core.design_system.base.model.AnyUiId
import ru.samtakoy.presentation.core.design_system.button.usual.MyButtonUiModel
import ru.samtakoy.presentation.utils.asA
import ru.samtakoy.resources.Res
import ru.samtakoy.resources.getFormatted
import ru.samtakoy.resources.qpack_btn_add_to_course
import ru.samtakoy.resources.qpack_btn_add_to_new_course
import ru.samtakoy.resources.qpack_btn_view_cards
import ru.samtakoy.resources.qpack_btn_view_courses
import ru.samtakoy.resources.qpack_btn_view_uncompleted

internal interface QPackInfoButtonsMapper {
    suspend fun map(uncompleted: Uncompleted?): List<MyButtonUiModel>

    class Uncompleted(
        val viewed: Int,
        val total: Int
    )

    companion object {
        val IdBtnViewCards = AnyUiId()
        val IdBtnViewUncompleted = AnyUiId()
        val IdBtnAddToNewCourse = AnyUiId()
        val IdBtnAddToCourse = AnyUiId()
        val IdBtnViewCourses = AnyUiId()
    }
}

internal class QPackInfoButtonsMapperImpl : QPackInfoButtonsMapper {

    private val viewCardsBtn = SuspendLazy {
        MyButtonUiModel(
            id = QPackInfoButtonsMapper.IdBtnViewCards,
            text = getString(Res.string.qpack_btn_view_cards).asA()
        )
    }

    private val viewUncompletedBtn = SuspendLazy {
        MyButtonUiModel(
            id = QPackInfoButtonsMapper.IdBtnViewUncompleted,
            text = "".asA()
        )
    }

    private val addToNewCourseBtn = SuspendLazy {
        MyButtonUiModel(
            id = QPackInfoButtonsMapper.IdBtnAddToNewCourse,
            text = getString(Res.string.qpack_btn_add_to_new_course).asA()
        )
    }

    private val addToCourseBtn = SuspendLazy {
        MyButtonUiModel(
            id = QPackInfoButtonsMapper.IdBtnAddToCourse,
            text = getString(Res.string.qpack_btn_add_to_course).asA()
        )
    }

    private val viewCourses = SuspendLazy {
        MyButtonUiModel(
            id = QPackInfoButtonsMapper.IdBtnViewCourses,
            text = getString(Res.string.qpack_btn_view_courses).asA()
        )
    }

    override suspend fun map(uncompleted: QPackInfoButtonsMapper.Uncompleted?): List<MyButtonUiModel> {
        return buildList {
            add(viewCardsBtn.getValue())
            if (uncompleted != null) {
                add(
                    viewUncompletedBtn.getValue().copy(
                        text = getFormatted(
                            Res.string.qpack_btn_view_uncompleted,
                            "${uncompleted.viewed}/${uncompleted.total}"
                        ).asA()
                    )
                )
            }
        }
    }
}