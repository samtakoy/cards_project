package ru.samtakoy.core.screens.courses;

import android.content.ContentResolver;
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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.apache.commons.lang3.ArrayUtils;

import java.io.Serializable;
import java.sql.Date;
import java.util.LinkedList;
import java.util.List;

import ru.samtakoy.R;
import ru.samtakoy.core.database.DbContentProvider;
import ru.samtakoy.core.model.LearnCourse;
import ru.samtakoy.core.model.LearnCourseMode;
import ru.samtakoy.core.model.QPack;
import ru.samtakoy.core.model.elements.Schedule;
import ru.samtakoy.core.business.impl.ContentProviderHelper;
import ru.samtakoy.core.navigation.RouterHolder;
import ru.samtakoy.core.navigation.Screens;
import ru.samtakoy.core.screens.export_cards.BatchExportDialogFragment;


public class CoursesListFragment extends Fragment implements CoursesAdapter.CourseClickListener {

    private static final String ARG_TARGET_QPACK = "ARG_TARGET_QPACK";
    private static final String ARG_TARGET_MODES = "ARG_TARGET_MODES";
    private static final String ARG_TARGET_COURSE_IDS = "ARG_TARGET_COURSE_IDS";
    private static final String RESULT_EXTRA_COURSE_ID = "RESULT_EXTRA_COURSE_ID";

    private static final int REQ_CODE_ADD_COURSE = 1;
    private static final int REQ_CODE_EXPORT_COURSES = 2;
    private static final String TAG_DIALOG_ADD_COURSE = "TAG_DIALOG_ADD_COURSE";
    private static final String TAG_DIALOG_EXPORT_COURSES = "TAG_DIALOG_EXPORT_COURSES";

    private static final String SAVED_NEW_COURSE_DEFAULT_TITLE = "SAVED_NEW_COURSE_DEFAULT_TITLE";


    /*
    public interface Callbacks{
        //void onCourseSelected(LearnCourse course);
        //void backToThemes();
    }/**/

    public static CoursesListFragment newFragment(
            @Nullable QPack targetQPack,
            @Nullable List<LearnCourseMode> targetModes,
            @Nullable Long[] targetCourseIds
    ){
        CoursesListFragment result = new CoursesListFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_TARGET_QPACK, targetQPack);
        args.putSerializable(ARG_TARGET_MODES, (Serializable) targetModes);
        args.putLongArray(ARG_TARGET_COURSE_IDS, ArrayUtils.toPrimitive(targetCourseIds));
        result.setArguments(args);
        return result;
    }

    private TextView mCoursesIsEmptyLabel;
    private RecyclerView mCoursesRecycler;
    private CoursesAdapter mCoursesAdapter;

    //private Callbacks mCallbacks;
    private RouterHolder mRouterHolder;

    @Nullable private QPack mTargetQPack;
    @Nullable private List<LearnCourseMode> mTargetModes;
    @Nullable private Long[] mTargetCourseIds;
    private String mNewCourseDefaultTitle;



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_courses, container, false);



        mTargetQPack = (QPack) getArguments().getSerializable(ARG_TARGET_QPACK);
        mTargetModes = (List<LearnCourseMode>) getArguments().getSerializable(ARG_TARGET_MODES);

        mTargetCourseIds = ArrayUtils.toObject(getArguments().getLongArray(ARG_TARGET_COURSE_IDS));
        if(mTargetQPack != null){
            if(savedInstanceState != null){
                mNewCourseDefaultTitle = savedInstanceState.getString(SAVED_NEW_COURSE_DEFAULT_TITLE);
            } else {
                mNewCourseDefaultTitle = mTargetQPack.getTitle();
            }
        } else {
            mNewCourseDefaultTitle = "";
        }

        mCoursesIsEmptyLabel = v.findViewById(R.id.courses_is_empty_label);

        mCoursesAdapter = new CoursesAdapter(this);

        mCoursesRecycler = v.findViewById(R.id.courses_list_recycler);
        mCoursesRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        mCoursesRecycler.setAdapter(mCoursesAdapter);

        updateCurCourses();

        setHasOptionsMenu(true);

        return v;
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

        outState.putString(SAVED_NEW_COURSE_DEFAULT_TITLE, mNewCourseDefaultTitle);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        //mCallbacks = (CoursesListFragment.Callbacks)context;
        mRouterHolder = (RouterHolder) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        //mCallbacks = null;
        mRouterHolder = null;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.fragment_courses, menu);
        MenuItem item;

        item = menu.findItem(R.id.menu_item_add);
        item.setVisible(mTargetQPack != null);

        item = menu.findItem(R.id.fragment_courses_menu_to_themes);
        item.setVisible(mTargetQPack == null);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_item_add:
                tryAddCourse();
                return true;
            case R.id.fragment_courses_menu_to_themes:
                //toThemes();
                return true;
            case R.id.menu_item_send_courses:
                exportToEmail();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void exportToEmail() {
        BatchExportDialogFragment dialog = BatchExportDialogFragment.newCoursesFragment(
                "", this, REQ_CODE_EXPORT_COURSES
        );
        dialog.show(getActivity().getSupportFragmentManager(), TAG_DIALOG_EXPORT_COURSES);
    }

    /*
    private void toThemes() {
        mCallbacks.backToThemes();
    }/***/

    private void tryAddCourse() {
        CourseEditDialogFragment dialog = CourseEditDialogFragment.newDialog(mNewCourseDefaultTitle);
        dialog.setTargetFragment(this, REQ_CODE_ADD_COURSE);
        dialog.show(getActivity().getSupportFragmentManager(), TAG_DIALOG_ADD_COURSE);
    }

    private void updateCurCourses() {

        List<LearnCourse> curCourses;
        if(mTargetQPack == null && mTargetModes == null && mTargetCourseIds == null){
            curCourses = ContentProviderHelper.getAllCourses(getActivity());
        } else if (mTargetCourseIds != null) {
            curCourses = ContentProviderHelper.getCoursesByIds(getActivity(), mTargetCourseIds);
        } else if (mTargetModes != null) {
            curCourses = ContentProviderHelper.getCoursesByModes(getActivity(), mTargetModes);
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case REQ_CODE_ADD_COURSE:
                String courseTitle = data.getStringExtra(CourseEditDialogFragment.RESULT_EXTRA_TEXT);
                if(courseTitle != null && courseTitle.length()>0){
                    addCourse(courseTitle);
                    // TODO and SELECT it!?
                }
                break;
        }
    }

    private void addCourse(String courseTitle) {

        LearnCourse newCourse = LearnCourse.createNewPreparing(
                mTargetQPack.getId(), courseTitle, LearnCourseMode.PREPARING,
                new LinkedList<Long>(), Schedule.DEFAULT, new Date(0)
        );
        ContentResolver resolver = getActivity().getContentResolver();
        resolver.insert(DbContentProvider.CONTENT_URI_COURSES, newCourse.getContentValues(false));
        updateCurCourses();
        mCoursesRecycler.getAdapter().notifyDataSetChanged();


        mNewCourseDefaultTitle = "";
    }

    private void gotoCourse(LearnCourse course){
        mRouterHolder.getRouter().navigateTo(new Screens.CourseInfoScreen(course.getId()));
    }

    @Override
    public void onCourseClick(LearnCourse course) {
        gotoCourse(course);
    }

    // +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    // +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++



}
