package ru.samtakoy.core.screens.courses;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ru.samtakoy.R;
import ru.samtakoy.core.model.LearnCourse;
import ru.samtakoy.core.model.QPack;
import ru.samtakoy.core.business.impl.ContentProviderHelper;
import ru.samtakoy.core.screens.qpack.QPackInfoFragment;

public class SelectCourseDialogFragment extends DialogFragment
        implements CoursesAdapter.CourseClickListener{

    private static final String ARG_TARGET_QPACK = "ARG_TARGET_QPACK";

    public static final String RESULT_EXTRA_COURSE_ID = "RESULT_EXTRA_COURSE_ID";

    public static SelectCourseDialogFragment newFragment(
            @Nullable QPack targetQPack,
            QPackInfoFragment targetFragment,
            int requestCode
    ){
        SelectCourseDialogFragment result = new SelectCourseDialogFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_TARGET_QPACK, targetQPack);
        result.setArguments(args);
        result.setTargetFragment(targetFragment, requestCode);
        return result;
    }

    private TextView mCoursesIsEmptyLabel;
    private RecyclerView mCoursesRecycler;
    private CoursesAdapter mCoursesAdapter;

    @Nullable private QPack mTargetQPack;

    private void initView(View v) {
        mTargetQPack = (QPack) getArguments().getSerializable(ARG_TARGET_QPACK);

        mCoursesIsEmptyLabel = v.findViewById(R.id.courses_is_empty_label);

        mCoursesAdapter = new CoursesAdapter(this);

        mCoursesRecycler = v.findViewById(R.id.courses_list_recycler);
        mCoursesRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        mCoursesRecycler.setAdapter(mCoursesAdapter);

        updateCurCourses();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        View v = LayoutInflater.from(getContext()).inflate(R.layout.fragment_courses, null);
        initView(v);

        return new AlertDialog.Builder(getContext())
                .setTitle(R.string.courses_list_select_title)
                .setView(v)
                .setNegativeButton(R.string.btn_cancel, (dialogInterface, i) -> onCancel())
                .create();
    }

    private void updateCurCourses() {
        List<LearnCourse> curCourses;
        if(mTargetQPack == null){
            curCourses = ContentProviderHelper.getAllCourses(getActivity());
        } else {
            curCourses = ContentProviderHelper.getCoursesFor(getActivity(), mTargetQPack.getId());
        }
        mCoursesAdapter.setCurCourses(curCourses);

        updateListVisibility(curCourses.size() > 0);
    }

    private void updateListVisibility(boolean coursesIsVisible) {
        if(coursesIsVisible){
            mCoursesRecycler.setVisibility(View.VISIBLE);
            mCoursesIsEmptyLabel.setVisibility(View.GONE);
        } else {
            mCoursesRecycler.setVisibility(View.GONE);
            mCoursesIsEmptyLabel.setVisibility(View.VISIBLE);
        }
    }

    private void onCancel(){
        if(getTargetFragment() == null){
            return;
        }
        Intent result = new Intent();
        getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_CANCELED, result);
        dismiss();
    }

    @Override
    public void onCourseClick(LearnCourse course) {
        if(getTargetFragment() == null){
            return;
        }
        Intent result = new Intent();
        result.putExtra(RESULT_EXTRA_COURSE_ID, course.getId());
        getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, result);

        dismiss();
    }
}
