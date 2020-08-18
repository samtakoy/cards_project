package ru.samtakoy.core.screens.courses;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import ru.samtakoy.R;
import ru.samtakoy.core.model.LearnCourse;

public class CoursesAdapter extends RecyclerView.Adapter <CoursesAdapter.CoursesItemHolder>{

    public interface CourseClickListener{
        void onCourseClick(LearnCourse course);
    }

    class CoursesItemHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private LearnCourse mCourse;
        private TextView mText;
        private CourseClickListener mCourseClickListener;

        public CoursesItemHolder(@NonNull View itemView, CourseClickListener clickListener) {
            super(itemView);
            mCourseClickListener = clickListener;
            mText = itemView.findViewById(R.id.list_item_text);
            itemView.setOnClickListener(this);
        }

        public void bindCourse(LearnCourse t){
            mCourse = t;
            mText.setText(t.getDynamicTitle());

        }

        @Override
        public void onClick(View view) {
            mCourseClickListener.onCourseClick(mCourse);
        }

    }

    private CourseClickListener mCourseClickListener;
    private List<LearnCourse> mCurCourses;

    public CoursesAdapter(CourseClickListener clickListener){
        mCurCourses = new ArrayList<>();
        mCourseClickListener = clickListener;
    }

    public void setCurCourses(List<LearnCourse> curCourses) {
        mCurCourses = curCourses;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CoursesItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater li = LayoutInflater.from(parent.getContext());
        return new CoursesItemHolder(
                li.inflate(R.layout.courses_list_item, parent, false),
                mCourseClickListener
        );
    }

    @Override
    public void onBindViewHolder(@NonNull CoursesItemHolder holder, final int position) {
        holder.bindCourse(mCurCourses.get(position));
        holder.itemView.setOnCreateContextMenuListener(null);
        holder.itemView.setOnLongClickListener(null);
    }

    @Override
    public int getItemCount() {
        return mCurCourses.size();
    }

    @Override
    public void onViewRecycled(@NonNull CoursesItemHolder holder) {
        holder.itemView.setOnLongClickListener(null);
        super.onViewRecycled(holder);
    }

}
