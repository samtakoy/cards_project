package ru.samtakoy.core.presentation.qpack;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.transition.AutoTransition;
import androidx.transition.TransitionManager;

import com.google.android.material.bottomsheet.BottomSheetBehavior;

import java.text.MessageFormat;
import java.util.List;

import javax.inject.Inject;

import moxy.MvpAppCompatFragment;
import moxy.presenter.InjectPresenter;
import moxy.presenter.ProvidePresenter;
import ru.samtakoy.R;
import ru.samtakoy.core.app.di.Di;
import ru.samtakoy.core.data.local.database.room.entities.CardEntity;
import ru.samtakoy.core.presentation.FragmentHelperKt;
import ru.samtakoy.core.presentation.RouterHolder;
import ru.samtakoy.core.presentation.cards.CardsViewFragment;
import ru.samtakoy.core.presentation.cards.types.CardViewMode;
import ru.samtakoy.core.presentation.cards.types.CardViewSource;
import ru.samtakoy.core.presentation.courses.CourseEditDialogFragment;
import ru.samtakoy.core.presentation.courses.info.CourseInfoFragment;
import ru.samtakoy.core.presentation.courses.list.CoursesListFragment;
import ru.samtakoy.core.presentation.courses.select.SelectCourseDialogFragment;

public class QPackInfoFragment extends MvpAppCompatFragment implements QPackInfoView, CardViewingTypeSelector.CardViewingTypeSelectorListener {

    private static final String TAG = "QPackInfoFragment";

    private static final int AREQUEST_NEW_COURSE = 1;
    private static final int AREQUEST_SELECT_COURSE_TO_ADD_TO = 2;
    private static final int AREQUEST_SELECT_VIEWING_TYPE = 3;

    private static final String ARG_QPACK_ID = "ARG_QPACK_ID";


    public static QPackInfoFragment createFragment(Long qPackId) {
        QPackInfoFragment result = new QPackInfoFragment();
        result.setArguments(buildBundle(qPackId));
        return result;
    }

    public static Bundle buildBundle(Long qPackId) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_QPACK_ID, qPackId);
        return args;
    }


    @InjectPresenter
    QPackInfoPresenter mQPackInfoPresenter;
    @Inject
    QPackInfoPresenter.Factory mPresenterFactory;

    @ProvidePresenter
    QPackInfoPresenter providePresenter() {
        Long qPackId = getArguments().getLong(ARG_QPACK_ID, -1);
        return mPresenterFactory.create(qPackId);
    }


    private TextView mQPackTitle;
    private TextView mQPackCardsCount;

    private Button mViewCardsBtn;
    private Button mAddToNewCourseBtn;
    private Button mAddToCourseBtn;
    private Button mViewCoursesBtn;

    private ConstraintLayout mConstraintLayout;

    private BottomSheetBehavior mBottomSheetBehavior;
    private LinearLayout mLinearLayoutBSheet;
    private ToggleButton mTbUpDown;
    private RecyclerView mCardsFastViewList;
    private CardsFastViewAdapter mCardsFastViewAdapter;

    private BottomSheetBehavior.BottomSheetCallback mBottomSheetCallback;

    private RouterHolder mRouterHolder;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {

        Di.appComponent.inject(this);

        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        mRouterHolder = (RouterHolder) context;
    }

    @Override
    public void onDetach() {

        mRouterHolder = null;

        super.onDetach();
    }



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_qpack_info, container, false);

        initBottomSheet(v);
        initAnimationContainers(v);

        mQPackTitle = v.findViewById(R.id.qpack_title);
        mQPackCardsCount = v.findViewById(R.id.qpack_cards_count);

        mViewCardsBtn = v.findViewById(R.id.qpack_btn_view_cards);
        mViewCardsBtn.setOnClickListener(view -> mQPackInfoPresenter.onUiViewPackCards());

        mAddToNewCourseBtn = v.findViewById(R.id.qpack_btn_add_to_new_course);
        mAddToNewCourseBtn.setOnClickListener(view -> mQPackInfoPresenter.onUiAddToNewCourse());

        mAddToCourseBtn = v.findViewById(R.id.qpack_btn_add_to_course);
        mAddToCourseBtn.setOnClickListener(view -> mQPackInfoPresenter.onUiAddToExistsCourse());

        mViewCoursesBtn = v.findViewById(R.id.qpack_btn_view_courses);
        mViewCoursesBtn.setOnClickListener(view -> mQPackInfoPresenter.onUiShowPackCourses());

        if (savedInstanceState == null) {
            prepareAnim();
            new Handler().post(() -> startAnim());
        }

        return v;
    }

    private void prepareAnim() {
        ConstraintSet cs = new ConstraintSet();
        cs.clone(getContext(), R.layout.fragment_qpack_info_frame1);
        cs.applyTo(mConstraintLayout);
    }

    private void startAnim() {

        ConstraintSet cs = new ConstraintSet();
        cs.clone(getContext(), R.layout.fragment_qpack_info_frame2);


        AutoTransition transition = new AutoTransition();
        transition.setDuration(300);
        transition.setInterpolator(new DecelerateInterpolator());

        TransitionManager.beginDelayedTransition(mConstraintLayout, transition);
        cs.applyTo(mConstraintLayout);

    }

    private void initAnimationContainers(View v) {
        //mExternalContainer = v.findViewById(R.id.externalContainer);
        mConstraintLayout = v.findViewById(R.id.innerContainer);
    }

    @Override
    public void onDestroyView() {

        if (mBottomSheetCallback != null) {
            mBottomSheetBehavior.removeBottomSheetCallback(mBottomSheetCallback);
            mBottomSheetCallback = null;
        }

        super.onDestroyView();
    }

    private void initBottomSheet(View v) {

        mLinearLayoutBSheet = v.findViewById(R.id.bottomSheet);
        mBottomSheetBehavior = BottomSheetBehavior.from(mLinearLayoutBSheet);
        mTbUpDown = v.findViewById(R.id.toggleButton);
        mCardsFastViewList = v.findViewById(R.id.list);

        mTbUpDown.setOnCheckedChangeListener((compoundButton, isChecked) -> {
            if(isChecked){
                mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            } else {
                mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            }
        });

        mBottomSheetBehavior.addBottomSheetCallback(mBottomSheetCallback = new BottomSheetBehavior.BottomSheetCallback() {

            private boolean mFirstTimeOpened = false;

            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if(newState == BottomSheetBehavior.STATE_EXPANDED){
                    mTbUpDown.setChecked(true);
                } else {
                    mTbUpDown.setChecked(false);
                }

                if(newState == BottomSheetBehavior.STATE_HIDDEN || newState == BottomSheetBehavior.STATE_COLLAPSED){
                    setInnerContainerPercent(1);
                } else if(!mFirstTimeOpened ){
                    mFirstTimeOpened = true;
                    onFirstTimeOpen();
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                setInnerContainerPercent(1 - slideOffset);
            }

            private void onFirstTimeOpen(){
                mQPackInfoPresenter.onUiCardsFastView();
            }

        });
    }

    private void setInnerContainerPercent(float percent){
        final float invisiblePercent = 0.1f;
        float alpha = Math.max((percent-invisiblePercent)/(1-invisiblePercent), 0);
        mQPackTitle.setAlpha(alpha);
        mQPackCardsCount.setAlpha(alpha);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.qpack_info, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_delete:
                mQPackInfoPresenter.onUiDeletePack();
                return true;
            case R.id.menu_item_add_fake_card:
                mQPackInfoPresenter.onUiAddFakeCard();
                return true;

        }
        return false;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        switch (requestCode){
            case AREQUEST_NEW_COURSE:
                if(resultCode == Activity.RESULT_OK){
                    String courseName = data.getStringExtra(CourseEditDialogFragment.RESULT_EXTRA_TEXT);
                    mQPackInfoPresenter.onUiNewCourseCommit(courseName);
                }
                break;
            case AREQUEST_SELECT_COURSE_TO_ADD_TO:
                if(resultCode == Activity.RESULT_OK){
                    Long courseId = data.getLongExtra(SelectCourseDialogFragment.RESULT_EXTRA_COURSE_ID, -1);
                    mQPackInfoPresenter.onUiAddCardsToCourseCommit(courseId);
                }
                break;
        }
    }

    public void showCourseScreen(Long courseId) {
        mRouterHolder.getNavController().navigate(R.id.courseInfoFragment, CourseInfoFragment.buildBundle(courseId));
    }

    public void showMessage(int messageId){
        Toast.makeText(getContext(), messageId, Toast.LENGTH_SHORT).show();
    }

    public void closeScreen() {
        mRouterHolder.getNavController().navigateUp();
    }

    @Override
    public void initView(String title, String cardsCount) {

        mQPackTitle.setText(title);
        mQPackCardsCount.setText(getCardsCountText(cardsCount));
    }

    private String getCardsCountText(String cardsCount) {
        String result = getContext().getResources().getString(R.string.qpack_cards_count);
        return MessageFormat.format(result, cardsCount);
    }

    @Override
    public void navigateToPackCourses(Long qPackId) {
        mRouterHolder.getNavController().navigate(
                R.id.coursesListFragment, CoursesListFragment.buildBundle(qPackId, null, null)
        );
    }

    public void requestNewCourseCreation(String title) {
        CourseEditDialogFragment dialog = CourseEditDialogFragment.newDialog(title);
        dialog.setTargetFragment(this, AREQUEST_NEW_COURSE);
        FragmentHelperKt.showDialogFragment(dialog, this, TAG);
    }

    @Override
    public void requestsSelectCourseToAdd(@Nullable Long qPackId) {
        DialogFragment dialog = SelectCourseDialogFragment.newFragment(
                qPackId, this, AREQUEST_SELECT_COURSE_TO_ADD_TO
        );
        FragmentHelperKt.showDialogFragment(dialog, this, "SelectCourseDialogFragment");
    }

    @Override
    public void showLearnCourseCardsViewingType() {
        DialogFragment dialogFragment = CardViewingTypeSelector.newFragment(this, AREQUEST_SELECT_VIEWING_TYPE);
        FragmentHelperKt.showDialogFragment(dialogFragment, this, "CardViewingTypeSelector");
    }

    @Override
    public void navigateToCardsView(Long learnCourseId) {

        mRouterHolder.getNavController().navigate(
                R.id.action_qPackInfoFragment_to_cardsViewFragment,
                CardsViewFragment.buildBundle(
                        learnCourseId,
                        CardViewSource.SIMPLE_VIEW,
                        CardViewMode.LEARNING
                )
        );
    }

    @Override
    public void openLearnCourseCardsInList(Long learnCourseId) {

        mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
    }

    @Override
    public void OnCardViewingTypeSelect(CardViewingTypeSelector.CardViewingType type) {
        switch (type){
            case RANDOM:
                mQPackInfoPresenter.onUiViewPackCardsRandomly();
                break;
            case SIMPLE:
                mQPackInfoPresenter.onUiViewPackCardsOrdered();
                break;
            case LIST:
                mQPackInfoPresenter.onUiViewPackCardsInList();
                break;
        }
    }

    @Override
    public void setFastViewCards(List<CardEntity> cards) {
        setFastCardsViewAdapter();
        mCardsFastViewAdapter.setCards(cards);
    }

    private void setFastCardsViewAdapter() {
        if (mCardsFastViewAdapter == null) {
            mCardsFastViewAdapter = new CardsFastViewAdapter();
            mCardsFastViewList.setHasFixedSize(false);
            mCardsFastViewList.setAdapter(mCardsFastViewAdapter);
            mCardsFastViewList.setLayoutManager(new LinearLayoutManager(getContext()));
        }
    }
}
