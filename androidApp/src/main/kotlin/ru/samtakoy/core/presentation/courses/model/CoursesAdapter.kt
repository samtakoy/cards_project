package ru.samtakoy.core.presentation.courses.model

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ru.samtakoy.R
import ru.samtakoy.core.presentation.courses.model.CoursesAdapter.CoursesItemHolder

class CoursesAdapter(
    private val clickListener: CourseClickListener
) : RecyclerView.Adapter<CoursesItemHolder>() {

    fun interface CourseClickListener {
        fun onCourseClick(item: CourseItemUiModel)
    }

    inner class CoursesItemHolder(
        itemView: View,
        private val mCourseClickListener: CourseClickListener
    ) : RecyclerView.ViewHolder(itemView), View.OnClickListener {

        private var mCourse: CourseItemUiModel? = null
        private val mText: TextView

        init {
            mText = itemView.findViewById<TextView>(R.id.list_item_text)
            itemView.setOnClickListener(this)
        }

        fun bindCourse(data: CourseItemUiModel) {
            mCourse = data
            mText.setText(data.title)
        }

        override fun onClick(view: View?) {
            mCourse?.let {
                mCourseClickListener.onCourseClick(it)
            }
        }
    }

    private var mCurCourses: List<out CourseItemUiModel>

    init {
        mCurCourses = ArrayList<CourseItemUiModel>()
    }

    fun setCurCourses(curCourses: List<out CourseItemUiModel>) {
        mCurCourses = curCourses
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CoursesItemHolder {
        val li = LayoutInflater.from(parent.getContext())
        return CoursesItemHolder(
            li.inflate(R.layout.courses_list_item, parent, false),
            clickListener
        )
    }

    override fun onBindViewHolder(holder: CoursesItemHolder, position: Int) {
        holder.bindCourse(mCurCourses.get(position))
        holder.itemView.setOnLongClickListener(null)
    }

    override fun getItemCount(): Int {
        return mCurCourses.size
    }

    override fun onViewRecycled(holder: CoursesItemHolder) {
        holder.itemView.setOnLongClickListener(null)
        super.onViewRecycled(holder)
    }
}
