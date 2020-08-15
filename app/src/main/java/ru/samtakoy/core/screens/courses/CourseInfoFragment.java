package ru.samtakoy.core.screens.courses;

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
import androidx.fragment.app.Fragment;

import java.text.MessageFormat;

import ru.samtakoy.R;
import ru.samtakoy.core.model.LearnCourse;
import ru.samtakoy.core.model.LearnCourseMode;
import ru.samtakoy.core.model.elements.ScheduleTimeUnit;
import ru.samtakoy.core.business.impl.ContentProviderHelper;
import ru.samtakoy.core.model.utils.DateUtils;
import ru.samtakoy.core.model.utils.TimeViewUtils;
import ru.samtakoy.core.navigation.RouterHolder;
import ru.samtakoy.core.navigation.Screens;
import ru.samtakoy.core.screens.DialogHelper;
import ru.samtakoy.core.screens.cards.types.CardViewMode;
import ru.samtakoy.core.screens.cards.types.CardViewSource;
import ru.samtakoy.core.screens.log.LogActivity;

public class CourseInfoFragment extends Fragment {

    private static final String ARG_LEARN_COURSE_ID = "ARG_LEARN_COURSE_ID";


    public static CourseInfoFragment newFragment(Long learnCourseId){
        CourseInfoFragment result = new CourseInfoFragment();
        Bundle args = new Bundle();
        args.putLong(ARG_LEARN_COURSE_ID, learnCourseId);
        result.setArguments(args);
        return result;
    }

    private LearnCourse mLearnCourse;


    // ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    // ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

    // android:id="@+id/title"
    private TextView mTitleText;
    // android:id="@+id/cards_count"
    private TextView mCardCountText;
    // android:id="@+id/status_content"
    private TextView mStatusContentText;
    // android:id="@+id/schedule"
    private Button mScheduleButton;
    // android:id="@+id/action_button"
    private Button mActionButton;


    private RouterHolder mRouterHolder;

    // ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    // ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //
        Long courseId = getArguments().getLong(ARG_LEARN_COURSE_ID);
        mLearnCourse = ContentProviderHelper.getConcreteCourse(getContext(), courseId);

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

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        inflater.inflate(R.menu.course_info, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_item_delete:
                deleteCourse();
                return true;
            case R.id.menu_item_log:
                startActivity(LogActivity.newActivityIntent(getContext()));
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void deleteCourse() {

        ContentProviderHelper.deleteCourse(getContext(), mLearnCourse.getId());
        getActivity().finish();
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
        mActionButton.setOnClickListener(view -> {
            onDoAction();
        });

        return v;
    }

    @Override
    public void onStart() {
        super.onStart();

        applyDataToView();
    }

    private void onDoAction() {
        switch (mLearnCourse.getMode()){
            case PREPARING:
            case LEARN_WAITING:
                startLearning();
                break;
            case LEARNING:
                continueLearning();
                break;
            case REPEAT_WAITING:
                startRepeatingExtraordinaryOrNext();
                break;
            case REPEATING:
                continueRepeating();
                break;
            case COMPLETED:
                startRepeatingExtraordinary();
                break;
        }
    }

    private void showCards() {

        CardViewMode viewMode = mLearnCourse.getMode() == LearnCourseMode.LEARNING ?
                CardViewMode.LEARNING : CardViewMode.REPEATING;
        mRouterHolder.getRouter().navigateTo(
                new Screens.CardsViewScreen(
                        mLearnCourse.getId(),
                        CardViewSource.ROUTINE_REPEATING,
                        viewMode
                )
        );
    }

    private void showCardsExtraordinaryRepeating() {

        //MyApp myApp = (MyApp)getActivity().getApplication();
        LearnCourse tempLearnCourse = ContentProviderHelper.getTempCourseFor(
                getContext(),
                mLearnCourse.getQPackId(),
                mLearnCourse.getCardIds(),
                true
        );
        tempLearnCourse.toRepeatMode();

        mRouterHolder.getRouter().navigateTo(
                new Screens.CardsViewScreen(
                        tempLearnCourse.getId(),
                        CardViewSource.EXTRA_REPEATING,
                        CardViewMode.REPEATING)
        );
    }


    private void startLearning() {
        // TODO отдебажить,
        // сначала импорт файла - курс просмотреть, заьем batch
        // android карточки - запустить курс -> эксепшен с id курса
        mLearnCourse.toLearnMode();
        ContentProviderHelper.saveCourse(getContext(), mLearnCourse);
        applyDataToView();
        showCards();
    }

    private void continueLearning() {
        showCards();
    }

    private void continueRepeating() {
        //mLearnCourse.prepareToCardsView();
        showCards();
    }

    private void startRepeatingExtraordinaryOrNext() {
        long timeDelta = DateUtils.dateToDbSerialized(mLearnCourse.getRepeatDate()) - DateUtils.getCurrentTimeLong();
        if(timeDelta < ScheduleTimeUnit.MINUTE.getMillis()){
            startRepeating();
        } else {
            requestExtraordinaryRepeating();
        }
    }

    private void startRepeating() {
        mLearnCourse.toRepeatMode();
        ContentProviderHelper.saveCourse(getContext(), mLearnCourse);
        applyDataToView();
        showCards();
    }

    private void requestExtraordinaryRepeating() {
        DialogHelper.showYesNoDialog(
                getContext(),
                getResources().getString(R.string.confirm_dialog_title),
                getResources().getString(R.string.course_info_extra_repeating_confirm),
                (dialogInterface, i) -> {
                    startRepeatingExtraordinary();
                },
                null
        );
    }

    private void startRepeatingExtraordinary() {
        showCardsExtraordinaryRepeating();
    }

    private void applyDataToView() {

        mTitleText.setText(mLearnCourse.getTitle());
        // android:id="@+id/cards_count"
        mCardCountText.setText(
                MessageFormat.format(getResources().getString(R.string.course_info_card_count), mLearnCourse.getCardsCount())
        );
        // android:id="@+id/status_content"
        mStatusContentText.setText(getStatusString());
        // android:id="@+id/schedule"
        mScheduleButton.setText(getScheduleButtonText());
        // android:id="@+id/action_button"
        mActionButton.setText(getActionButtonText());
    }

    private String getScheduleButtonText() {
        if(mLearnCourse.getMode() == LearnCourseMode.COMPLETED){
            return getResources().getString(R.string.course_info_schedule_is_completed);
        }
        if(mLearnCourse.getMode() == LearnCourseMode.PREPARING){
            if(mLearnCourse.getRestSchedule().isEmpty()){
                return getResources().getString(R.string.course_info_schedule_is_empty);
            } else {
                return mLearnCourse.getRestSchedule().toStringView(getResources());
            }
        }
        if(mLearnCourse.getMode() == LearnCourseMode.REPEATING || mLearnCourse.getMode() == LearnCourseMode.REPEAT_WAITING) {
            return mLearnCourse.getRestSchedule().toStringViewWithPrev(getResources(), mLearnCourse.getRealizedSchedule());
        }
        return mLearnCourse.getRestSchedule().toStringView(getResources());
    }

    private String getStatusString() {

        String resString;

        switch (mLearnCourse.getMode()){
            case PREPARING:
                return getResources().getString(R.string.course_info_status_preparing);
            case LEARN_WAITING:
                resString = getResources().getString(R.string.course_info_status_learn_waiting);
                return MessageFormat.format(resString, TimeViewUtils.getTimeView(getResources(), (int) mLearnCourse.getMillisToStart()));
            case LEARNING:
                resString = getResources().getString(R.string.course_info_status_learning);
                return MessageFormat.format(resString, mLearnCourse.getViewedCardsCount()+"/"+ mLearnCourse.getCardsCount());
            case REPEAT_WAITING:
                resString = getResources().getString(R.string.course_info_status_repeat_waiting);
                return MessageFormat.format(resString, TimeViewUtils.getTimeView(getResources(), (int) mLearnCourse.getMillisToStart()));
            case REPEATING:
                resString = getResources().getString(R.string.course_info_status_repeating);
                return MessageFormat.format(resString, mLearnCourse.getViewedCardsCount()+"/"+ mLearnCourse.getCardsCount());
            case COMPLETED:
                return getResources().getString(R.string.course_info_status_completed);
            default:
                return "...";
        }
    }

    private String getActionButtonText() {

        @StringRes int resId;
        switch (mLearnCourse.getMode()){
            case PREPARING:
                resId = R.string.course_info_btn_complete_preparing;
                break;
            case LEARN_WAITING:
                resId = R.string.course_info_btn_start_learning;
                break;
            case LEARNING:
                if(mLearnCourse.getViewedCardsCount() > 0){
                    resId = R.string.course_info_btn_continue_learning;
                } else{
                    resId = R.string.course_info_btn_learn;
                }
                break;
            case REPEAT_WAITING:
                resId = R.string.course_info_btn_repeat_now;
                break;
            case REPEATING:
                if(mLearnCourse.getViewedCardsCount() > 0){
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
