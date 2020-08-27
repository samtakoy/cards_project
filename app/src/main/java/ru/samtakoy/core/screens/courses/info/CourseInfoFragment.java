package ru.samtakoy.core.screens.courses.info;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;

import org.jetbrains.annotations.NotNull;

import java.text.MessageFormat;

import javax.inject.Inject;
import javax.inject.Provider;

import moxy.MvpAppCompatFragment;
import moxy.presenter.InjectPresenter;
import moxy.presenter.ProvidePresenter;
import ru.samtakoy.R;
import ru.samtakoy.core.MyApp;
import ru.samtakoy.core.database.room.entities.LearnCourseEntity;
import ru.samtakoy.core.database.room.entities.types.LearnCourseMode;
import ru.samtakoy.core.screens.DialogHelper;
import ru.samtakoy.core.screens.RouterHolder;
import ru.samtakoy.core.screens.cards.CardsViewFragment;
import ru.samtakoy.core.screens.cards.types.CardViewMode;
import ru.samtakoy.core.screens.cards.types.CardViewSource;
import ru.samtakoy.core.screens.log.LogActivity;
import ru.samtakoy.core.utils.TimeViewUtils;

public class CourseInfoFragment extends MvpAppCompatFragment implements CourseInfoView {

    private static final String ARG_LEARN_COURSE_ID = "ARG_LEARN_COURSE_ID";


    public static CourseInfoFragment newFragment(Long learnCourseId) {
        CourseInfoFragment result = new CourseInfoFragment();
        Bundle args = buildBundle(learnCourseId);
        result.setArguments(args);
        return result;
    }

    @NotNull
    public static Bundle buildBundle(Long learnCourseId) {
        Bundle args = new Bundle();
        args.putLong(ARG_LEARN_COURSE_ID, learnCourseId);
        return args;
    }

    // ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    // ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

    private TextView mTitleText;
    private TextView mCardCountText;
    private TextView mStatusContentText;
    private Button mScheduleButton;
    private Button mActionButton;


    private RouterHolder mRouterHolder;

    @InjectPresenter
    CourseInfoPresenter mPresenter;
    @Inject
    Provider<CourseInfoPresenter.Factory> mFactoryProvider;

    @ProvidePresenter
    CourseInfoPresenter providePresenter() {
        return mFactoryProvider.get().create(readLearnCourseId());
    }

    // ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    // ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {

        MyApp.getInstance().getAppComponent().inject(this);

        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);
    }

    private Long readLearnCourseId() {
        return getArguments().getLong(ARG_LEARN_COURSE_ID, -1);
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

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        inflater.inflate(R.menu.course_info, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_item_delete:
                mPresenter.onUiDeleteCourse();
                return true;
            case R.id.menu_item_log:
                startActivity(LogActivity.newActivityIntent(getContext()));
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void exit() {
        mRouterHolder.getNavController().navigateUp();
        //mRouterHolder.getRouter().exit();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_course_info, container, false);

        // android:id="@+id/title"
        mTitleText = v.findViewById(R.id.title);
        // android:id="@+id/cards_count"
        mCardCountText = v.findViewById(R.id.cards_count);
        // android:id="@+id/status_content"
        mStatusContentText = v.findViewById(R.id.status_content);
        // android:id="@+id/schedule"
        mScheduleButton = v.findViewById(R.id.schedule);
        mScheduleButton.setOnClickListener(view -> {
        });
        // android:id="@+id/action_button"
        mActionButton = v.findViewById(R.id.action_button);
        mActionButton.setOnClickListener(view -> mPresenter.onUiActionButtonClick());

        return v;
    }

    @Override
    public void navigateToCardsViewScreen(
            long learnCourseId,
            CardViewSource viewSource,
            @NotNull CardViewMode viewMode
    ) {

        mRouterHolder.getNavController().navigate(
                R.id.action_courseInfoFragment_to_cardsViewFragment,
                CardsViewFragment.buildBundle(learnCourseId, viewSource, viewMode)
        );
        //mRouterHolder.getRouter().navigateTo(new Screens.CardsViewScreen(learnCourseId, viewSource, viewMode));
    }

    @Override
    public void requestExtraordinaryRepeating() {
        DialogHelper.showYesNoDialog(
                getContext(),
                getResources().getString(R.string.confirm_dialog_title),
                getResources().getString(R.string.course_info_extra_repeating_confirm),
                (dialogInterface, i) -> mPresenter.onUiStartRepeatingExtraConfirm(),
                null
        );
    }

    @Override
    public void showLearnCourseInfo(LearnCourseEntity learnCourse) {

        mTitleText.setText(learnCourse.getTitle());
        mCardCountText.setText(
                MessageFormat.format(getResources().getString(R.string.course_info_card_count), learnCourse.getCardsCount())
        );
        mStatusContentText.setText(getStatusString(learnCourse));
        mScheduleButton.setText(getScheduleButtonText(learnCourse));
        mActionButton.setText(getActionButtonText(learnCourse));
    }

    private String getScheduleButtonText(LearnCourseEntity learnCourse) {
        if (learnCourse.getMode() == LearnCourseMode.COMPLETED) {
            return getResources().getString(R.string.course_info_schedule_is_completed);
        }
        if (learnCourse.getMode() == LearnCourseMode.PREPARING) {
            if (learnCourse.getRestSchedule().isEmpty()) {
                return getResources().getString(R.string.course_info_schedule_is_empty);
            } else {
                return learnCourse.getRestSchedule().toStringView(getResources());
            }
        }
        if (learnCourse.getMode() == LearnCourseMode.REPEATING || learnCourse.getMode() == LearnCourseMode.REPEAT_WAITING) {
            return learnCourse.getRestSchedule().toStringViewWithPrev(getResources(), learnCourse.getRealizedSchedule());
        }
        return learnCourse.getRestSchedule().toStringView(getResources());
    }

    private String getStatusString(LearnCourseEntity learnCourse) {

        String resString;

        switch (learnCourse.getMode()) {
            case PREPARING:
                return getResources().getString(R.string.course_info_status_preparing);
            case LEARN_WAITING:
                resString = getResources().getString(R.string.course_info_status_learn_waiting);
                return MessageFormat.format(resString, TimeViewUtils.getTimeView(getResources(), (int) learnCourse.getMillisToStart()));
            case LEARNING:
                resString = getResources().getString(R.string.course_info_status_learning);
                return MessageFormat.format(resString, learnCourse.getViewedCardsCount() + "/" + learnCourse.getCardsCount());
            case REPEAT_WAITING:
                resString = getResources().getString(R.string.course_info_status_repeat_waiting);
                return MessageFormat.format(resString, TimeViewUtils.getTimeView(getResources(), (int) learnCourse.getMillisToStart()));
            case REPEATING:
                resString = getResources().getString(R.string.course_info_status_repeating);
                return MessageFormat.format(resString, learnCourse.getViewedCardsCount() + "/" + learnCourse.getCardsCount());
            case COMPLETED:
                return getResources().getString(R.string.course_info_status_completed);
            default:
                return "...";
        }
    }

    private String getActionButtonText(LearnCourseEntity learnCourse) {

        @StringRes int resId;
        switch (learnCourse.getMode()) {
            case PREPARING:
                resId = R.string.course_info_btn_complete_preparing;
                break;
            case LEARN_WAITING:
                resId = R.string.course_info_btn_start_learning;
                break;
            case LEARNING:
                if (learnCourse.getViewedCardsCount() > 0) {
                    resId = R.string.course_info_btn_continue_learning;
                } else {
                    resId = R.string.course_info_btn_learn;
                }
                break;
            case REPEAT_WAITING:
                resId = R.string.course_info_btn_repeat_now;
                break;
            case REPEATING:
                if (learnCourse.getViewedCardsCount() > 0) {
                    resId = R.string.course_info_btn_continue_repeating;
                } else {
                    resId = R.string.course_info_btn_repeat;
                }
                break;
            case COMPLETED:
                return getResources().getString(R.string.course_info_btn_repeat);
            default:
                return "...";
        }
        return getResources().getString(resId);
    }


}
