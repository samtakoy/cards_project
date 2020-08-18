package ru.samtakoy.core.screens.courses.select;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Provider;

import moxy.MvpAppCompatDialogFragment;
import moxy.presenter.InjectPresenter;
import moxy.presenter.ProvidePresenter;
import ru.samtakoy.R;
import ru.samtakoy.core.MyApp;
import ru.samtakoy.core.model.LearnCourse;
import ru.samtakoy.core.model.QPack;
import ru.samtakoy.core.screens.courses.list.CoursesAdapter;
import ru.samtakoy.core.screens.qpack.QPackInfoFragment;

public class SelectCourseDialogFragment extends MvpAppCompatDialogFragment
        implements CoursesAdapter.CourseClickListener, SelectCourseView {

    private static final String ARG_TARGET_QPACK = "ARG_TARGET_QPACK";

    public static final String RESULT_EXTRA_COURSE_ID = "RESULT_EXTRA_COURSE_ID";

    public static SelectCourseDialogFragment newFragment(
            @Nullable QPack targetQPack,
            @NotNull QPackInfoFragment targetFragment,
            int requestCode
    ) {
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


    @InjectPresenter
    SelectCoursePresenter mPresenter;
    @Inject
    Provider<SelectCoursePresenter.Factory> mFactoryProvider;

    @ProvidePresenter
    SelectCoursePresenter providePresenter() {
        return mFactoryProvider.get().create(readQPack());
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {

        MyApp.getInstance().getAppComponent().inject(this);

        super.onCreate(savedInstanceState);
    }

    private void initView(View v) {

        mCoursesIsEmptyLabel = v.findViewById(R.id.courses_is_empty_label);

        mCoursesAdapter = new CoursesAdapter(this);

        mCoursesRecycler = v.findViewById(R.id.courses_list_recycler);
        mCoursesRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        mCoursesRecycler.setAdapter(mCoursesAdapter);

    }

    @Nullable
    private QPack readQPack() {
        return (QPack) getArguments().getSerializable(ARG_TARGET_QPACK);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        View v = LayoutInflater.from(getContext()).inflate(R.layout.fragment_courses, null);
        initView(v);

        return new AlertDialog.Builder(getContext())
                .setTitle(R.string.courses_list_select_title)
                .setView(v)
                .setNegativeButton(R.string.btn_cancel, (dialogInterface, i) -> exitCanceled())
                .create();
    }

    @Override
    public void showCourses(@NotNull List<? extends LearnCourse> curCourses) {
        mCoursesAdapter.setCurCourses(curCourses);
        updateListVisibility(curCourses.size() > 0);
    }

    @Override
    public void showError(int codeResId) {
        Toast.makeText(getContext(), codeResId, Toast.LENGTH_SHORT).show();
    }

    private void updateListVisibility(boolean coursesIsVisible) {
        if (coursesIsVisible) {
            mCoursesRecycler.setVisibility(View.VISIBLE);
            mCoursesIsEmptyLabel.setVisibility(View.GONE);
        } else {
            mCoursesRecycler.setVisibility(View.GONE);
            mCoursesIsEmptyLabel.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void exitCanceled() {
        if (getTargetFragment() != null) {

            Intent result = new Intent();
            getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_CANCELED, result);
        }
        dismiss();
    }

    @Override
    public void exitOk(long courseId) {
        if (getTargetFragment() != null) {
            Intent result = new Intent();
            result.putExtra(RESULT_EXTRA_COURSE_ID, courseId);
            getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, result);
        }

        dismiss();
    }

    @Override
    public void onCourseClick(LearnCourse course) {
        mPresenter.onUiCourseClick(course);
    }
}
