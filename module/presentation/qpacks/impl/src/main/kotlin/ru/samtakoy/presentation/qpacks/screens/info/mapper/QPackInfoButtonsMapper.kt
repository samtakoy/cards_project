package ru.samtakoy.presentation.qpacks.screens.info.mapper

import ru.samtakoy.common.resources.Resources
import ru.samtakoy.presentation.core.design_system.base.model.AnyUiId
import ru.samtakoy.presentation.core.design_system.button.MyButtonUiModel
import ru.samtakoy.presentation.qpacks.impl.R
import ru.samtakoy.presentation.utils.asA

internal interface QPackInfoButtonsMapper {
    fun map(uncompleted: Uncompleted?): List<MyButtonUiModel>

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

internal class QPackInfoButtonsMapperImpl(
    private val resources: Resources
) : QPackInfoButtonsMapper {

    private val viewCardsBtn by lazy {
        MyButtonUiModel(
            id = QPackInfoButtonsMapper.IdBtnViewCards,
            text = resources.getString(R.string.qpack_btn_view_cards).asA()
        )
    }

    private val viewUncompletedBtn by lazy {
        MyButtonUiModel(
            id = QPackInfoButtonsMapper.IdBtnViewUncompleted,
            text = "".asA()
        )
    }

    private val addToNewCourseBtn by lazy {
        MyButtonUiModel(
            id = QPackInfoButtonsMapper.IdBtnAddToNewCourse,
            text = resources.getString(R.string.qpack_btn_add_to_new_course).asA()
        )
    }

    private val addToCourseBtn by lazy {
        MyButtonUiModel(
            id = QPackInfoButtonsMapper.IdBtnAddToCourse,
            text = resources.getString(R.string.qpack_btn_add_to_course).asA()
        )
    }

    private val viewCourses by lazy {
        MyButtonUiModel(
            id = QPackInfoButtonsMapper.IdBtnViewCourses,
            text = resources.getString(R.string.qpack_btn_view_courses).asA()
        )
    }

    override fun map(uncompleted: QPackInfoButtonsMapper.Uncompleted?): List<MyButtonUiModel> {
        return buildList {
            add(viewCardsBtn)
            if (uncompleted != null) {
                add(
                    viewUncompletedBtn.copy(
                        text = resources.getString(
                            R.string.qpack_btn_view_uncompleted,
                            "${uncompleted.viewed}/${uncompleted.total}"
                        ).asA()
                    )
                )
            }
        }
    }
}