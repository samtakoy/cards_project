package ru.samtakoy.core.screens.cards;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.text.MessageFormat;

import ru.samtakoy.R;
import ru.samtakoy.core.model.LearnCourse;
import ru.samtakoy.core.model.elements.Schedule;
import ru.samtakoy.core.business.impl.ContentProviderHelper;
import ru.samtakoy.core.screens.ScheduleEditFragment;
import ru.samtakoy.core.screens.cards.types.CardViewMode;

public class CardsViewResultFragment extends Fragment {

    private static final String TAG = "CardsViewResultFragment";

    private static final String ARG_LEARN_COURSE_ID = "ARG_LEARN_COURSE_ID";
    private static final String ARG_VIEW_MODE = "ARG_VIEW_MODE";

    private static final String SAVED_SCHEDULE = "SAVED_SCHEDULE";

    private static final int REQ_SCHEDULE_EDIT = 1;

    public static CardsViewResultFragment newFragment(Long learnCourseId, CardViewMode viewMode){
        CardsViewResultFragment result = new CardsViewResultFragment();
        Bundle args = new Bundle();
        args.putLong(ARG_LEARN_COURSE_ID, learnCourseId);
        args.putSerializable(ARG_VIEW_MODE, viewMode);
        result.setArguments(args);
        return result;
    }

    public interface Callbacks{
        void onResultOk(Schedule newSchedule);
    }


    private LearnCourse mLearnCourse;
    private CardViewMode mViewMode;

    private TextView mViewedCardsText;
    private TextView mErrorCardsText;
    private TextView mScheduleLabelText;
    private Button mScheduleBtn;
    private Button mOkBtn;

    private Schedule mNewSchedule;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        readArgs(getArguments());

        mNewSchedule = new Schedule();
        if (savedInstanceState != null) {
            mNewSchedule.initFromString(savedInstanceState.getString(SAVED_SCHEDULE));
        }

    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putString(SAVED_SCHEDULE, mNewSchedule.toString());
    }

    @Override
    public void onDestroy() {
        mLearnCourse = null;

        mViewedCardsText = null;
        mErrorCardsText = null;
        mScheduleBtn = null;
        mOkBtn = null;

        super.onDestroy();
    }

    private Callbacks getCallbacks(){
        return (Callbacks) getParentFragment();
    }

    private void readArgs(Bundle arguments) {
        Long learnCourseId = arguments.getLong(ARG_LEARN_COURSE_ID, -1);
        mLearnCourse = ContentProviderHelper.getConcreteCourse(getContext(), learnCourseId);
        mViewMode = (CardViewMode) arguments.getSerializable(ARG_VIEW_MODE);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_cards_view_result, container, false);

        mViewedCardsText = v.findViewById(R.id.viewed_cards_text);
        mViewedCardsText.setText(
                MessageFormat.format(
                        getResources().getString(R.string.cards_view_res_viewed_cards),
                        mLearnCourse.getViewedCardsCount()
                )
        );


        mErrorCardsText = v.findViewById(R.id.err_cards_text);
        mErrorCardsText.setText(
                MessageFormat.format(getResources().getString(R.string.cards_view_res_err_cards), mLearnCourse.getErrorCardsCount())
        );
        //mErrorCardsText.setVisibility(mViewMode == CardViewMode.LEARNING ? View.INVISIBLE : View.VISIBLE);


        mScheduleLabelText = v.findViewById(R.id.schedule_label);
        mScheduleBtn = v.findViewById(R.id.schedule_btn);
        mScheduleBtn.setOnClickListener(view -> onScheduleClick());

        if(mViewMode == CardViewMode.LEARNING){
            mErrorCardsText.setVisibility(View.INVISIBLE);
            mScheduleLabelText.setVisibility(View.INVISIBLE);
            mScheduleBtn.setVisibility(View.INVISIBLE);
        }

        mOkBtn = v.findViewById(R.id.ok_btn);
        mOkBtn.setOnClickListener(view -> onOkClick());

        updateNewScheduleString();

        return v;
    }

    private void updateNewScheduleString() {
        if(!mNewSchedule.isEmpty()){
            mScheduleBtn.setText( mNewSchedule.toStringView(getResources()) );
        } else {
            mScheduleBtn.setText(R.string.schedule_none);
        }
    }

    private void onScheduleClick(){
        ScheduleEditFragment dialog = ScheduleEditFragment.newFragment(mNewSchedule);
        dialog.setTargetFragment(this, REQ_SCHEDULE_EDIT);
        dialog.show(getFragmentManager(), ScheduleEditFragment.TAG);
    }

    private void onOkClick(){
        getCallbacks().onResultOk(mNewSchedule);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == REQ_SCHEDULE_EDIT && resultCode == Activity.RESULT_OK){
            String scheduleString = data.getStringExtra(ScheduleEditFragment.RESULT_SCHEDULE_STRING);
            mNewSchedule.initFromString(scheduleString);
            updateNewScheduleString();
        }

    }
}
