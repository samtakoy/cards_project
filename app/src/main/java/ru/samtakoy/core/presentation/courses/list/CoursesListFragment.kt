package ru.samtakoy.core.presentation.courses.list;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.apache.commons.lang3.ArrayUtils;
import org.jetbrains.annotations.NotNull;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Provider;

import moxy.MvpAppCompatFragment;
import moxy.presenter.InjectPresenter;
import moxy.presenter.ProvidePresenter;
import ru.samtakoy.R;
import ru.samtakoy.core.app.di.Di;
import ru.samtakoy.core.data.local.database.room.entities.LearnCourseEntity;
import ru.samtakoy.core.data.local.database.room.entities.types.LearnCourseMode;
import ru.samtakoy.core.presentation.FragmentHelperKt;
import ru.samtakoy.core.presentation.RouterHolder;
import ru.samtakoy.core.presentation.courses.CourseEditDialogFragment;
import ru.samtakoy.core.presentation.courses.info.CourseInfoFragment;
import ru.samtakoy.core.presentation.export_cards.BatchExportDialogFragment;


public class CoursesListFragment extends MvpAppCompatFragment
        implements CoursesAdapter.CourseClickListener, CoursesListView {

    private static final String ARG_TARGET_QPACK_ID = "ARG_TARGET_QPACK_ID";
    private static final String ARG_TARGET_MODES = "ARG_TARGET_MODES";
    private static final String ARG_TARGET_COURSE_IDS = "ARG_TARGET_COURSE_IDS";
    //private static final String RESULT_EXTRA_COURSE_ID = "RESULT_EXTRA_COURSE_ID";

    private static final int REQ_CODE_ADD_COURSE = 1;
    private static final int REQ_CODE_EXPORT_COURSES = 2;
    private static final String TAG_DIALOG_ADD_COURSE = "TAG_DIALOG_ADD_COURSE";
    private static final String TAG_DIALOG_EXPORT_COURSES = "TAG_DIALOG_EXPORT_COURSES";

    private static final String SAVED_NEW_COURSE_DEFAULT_TITLE = "SAVED_NEW_COURSE_DEFAULT_TITLE";

    public static CoursesListFragment newFragment(
            @Nullable Long targetQPackId,
            @Nullable List<LearnCourseMode> targetModes,
            @Nullable Long[] targetCourseIds
    ) {
        CoursesListFragment result = new CoursesListFragment();
        result.setArguments(buildBundle(targetQPackId, targetModes, targetCourseIds));
        return result;
    }

    public static Bundle buildBundle(@NotNull Long targetQPackId, @Nullable List<LearnCourseMode> targetModes, @Nullable Long[] targetCourseIds) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_TARGET_QPACK_ID, targetQPackId);
        args.putIntArray(ARG_TARGET_MODES, LearnCourseMode.listToPrimitiveArray(targetModes));
        args.putLongArray(ARG_TARGET_COURSE_IDS, ArrayUtils.toPrimitive(targetCourseIds));
        return args;
    }


    private TextView mCoursesIsEmptyLabel;
    private RecyclerView mCoursesRecycler;
    private CoursesAdapter mCoursesAdapter;

    private RouterHolder mRouterHolder;

    @InjectPresenter
    CoursesListPresenter mPresenter;
    @Inject
    Provider<CoursesListPresenter.Factory> mPresenterFactoryProvider;

    @ProvidePresenter
    CoursesListPresenter providePresenter() {
        return mPresenterFactoryProvider.get().create(
                readTargetPackId(), readTargetModes(), readTargetCourseIds()
        );
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {

        Di.appComponent.inject(this);

        super.onCreate(savedInstanceState);

        if (savedInstanceState != null) {
            mPresenter.onRestoreState(savedInstanceState.getString(SAVED_NEW_COURSE_DEFAULT_TITLE));
        }

    }

    @Nullable
    private Long readTargetPackId() {
        Long result = (Long) getArguments().getSerializable(ARG_TARGET_QPACK_ID);
        return result == 0 ? null : result;
    }

    @Nullable
    private List<LearnCourseMode> readTargetModes() {
        int[] result = getArguments().getIntArray(ARG_TARGET_MODES);
        return LearnCourseMode.primitiveArrayToList(result);
    }

    @Nullable
    private Long[] readTargetCourseIds() {
        return ArrayUtils.toObject(getArguments().getLongArray(ARG_TARGET_COURSE_IDS));
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_courses, container, false);

        initViews(v);
        setHasOptionsMenu(true);

        return v;
    }

    private void initViews(View v) {
        mCoursesIsEmptyLabel = v.findViewById(R.id.courses_is_empty_label);

        mCoursesAdapter = new CoursesAdapter(this);

        mCoursesRecycler = v.findViewById(R.id.courses_list_recycler);
        mCoursesRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        mCoursesRecycler.setAdapter(mCoursesAdapter);
    }

    @Override
    public void onStart() {
        super.onStart();

        // TODO вместо этого отслеживать изменение статуса курсов
        // TODO причем это не помогает
        mCoursesRecycler.getAdapter().notifyDataSetChanged();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putString(SAVED_NEW_COURSE_DEFAULT_TITLE, mPresenter.getStateToSave());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        mRouterHolder = (RouterHolder) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();

        mRouterHolder = null;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.fragment_courses, menu);
        MenuItem item;

        item = menu.findItem(R.id.menu_item_add);
        item.setVisible(mPresenter.hasQPack());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_item_add:
                mPresenter.onUiAddCourseRequestClick();
                return true;
            case R.id.menu_item_send_courses:
                mPresenter.onUiBatchExportToEmailClick();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void showBatchExportToEmailDialog() {
        BatchExportDialogFragment dialog = BatchExportDialogFragment.newCoursesFragment(
                "", this, REQ_CODE_EXPORT_COURSES
        );
        FragmentHelperKt.showDialogFragment(dialog, this, TAG_DIALOG_EXPORT_COURSES);
    }

    @Override
    public void showAddCourseDialog(String defaultTitle) {
        CourseEditDialogFragment dialog = CourseEditDialogFragment.newDialog(defaultTitle);
        dialog.setTargetFragment(this, REQ_CODE_ADD_COURSE);
        FragmentHelperKt.showDialogFragment(dialog, this, TAG_DIALOG_ADD_COURSE);
    }

    @Override
    public void showCourses(@NotNull List<LearnCourseEntity> curCourses) {
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQ_CODE_ADD_COURSE:
                String courseTitle = data.getStringExtra(CourseEditDialogFragment.RESULT_EXTRA_TEXT);
                if (courseTitle != null && courseTitle.length() > 0) {
                    mPresenter.onUiAddNewCourseConfirm(courseTitle);
                }
                break;
        }
    }

    @Override
    public void navigateToCourseInfo(long courseId) {

        //Navigation.findNavController()
        //action_coursesListFragment_to_courseInfoFragment

        mRouterHolder.getNavController().navigate(
                R.id.action_coursesListFragment_to_courseInfoFragment,
                CourseInfoFragment.buildBundle(courseId)
        );
        //mRouterHolder.getRouter().navigateTo(new Screens.CourseInfoScreen(courseId));
    }

    @Override
    public void onCourseClick(LearnCourseEntity course) {
        mPresenter.onUiCourseClick(course);
    }

    // +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    // +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++



}
