package ru.samtakoy.core.presentation.cards.result;

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

import java.text.MessageFormat;

import javax.inject.Inject;
import javax.inject.Provider;

import moxy.MvpAppCompatFragment;
import moxy.presenter.InjectPresenter;
import moxy.presenter.ProvidePresenter;
import ru.samtakoy.R;
import ru.samtakoy.core.MyApp;
import ru.samtakoy.core.database.room.entities.elements.Schedule;
import ru.samtakoy.core.presentation.ScheduleEditFragment;
import ru.samtakoy.core.presentation.cards.types.CardViewMode;

public class CardsViewResultFragment extends MvpAppCompatFragment implements CardsViewResultView {

    private static final String TAG = "CardsViewResultFragment";

    private static final String ARG_LEARN_COURSE_ID = "ARG_LEARN_COURSE_ID";
    private static final String ARG_VIEW_MODE = "ARG_VIEW_MODE";

    private static final String SAVE_KEY_PRESENTER_STATE = "SAVE_KEY_PRESENTER_STATE";

    private static final int REQ_SCHEDULE_EDIT = 1;

    public static CardsViewResultFragment newFragment(Long learnCourseId, CardViewMode viewMode){
        CardsViewResultFragment result = new CardsViewResultFragment();
        Bundle args = new Bundle();
        args.putLong(ARG_LEARN_COURSE_ID, learnCourseId);
        args.putSerializable(ARG_VIEW_MODE, viewMode);
        result.setArguments(args);
        return result;
    }


    private TextView mViewedCardsText;
    private TextView mErrorCardsText;
    private TextView mScheduleLabelText;
    private Button mScheduleBtn;
    private Button mOkBtn;


    @InjectPresenter
    CardsViewResultPresenter mPresenter;
    @Inject
    Provider<CardsViewResultPresenter.Factory> mPresenterFactoryProvider;

    @ProvidePresenter
    CardsViewResultPresenter providePresenter() {
        return mPresenterFactoryProvider.get().create(
                getCallbacks(), readLearnCourseId(), readCardViewMode()
        );
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {

        MyApp.getInstance().getAppComponent().inject(this);

        super.onCreate(savedInstanceState);

        if (savedInstanceState != null) {
            mPresenter.onRestoreState(savedInstanceState.getString(SAVE_KEY_PRESENTER_STATE));
        }

    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putString(SAVE_KEY_PRESENTER_STATE, mPresenter.getStateToSave());
    }

    private CardsViewResultPresenter.Callbacks getCallbacks() {
        return (CardsViewResultPresenter.Callbacks) getParentFragment();
    }

    private Long readLearnCourseId() {
        return getArguments().getLong(ARG_LEARN_COURSE_ID, -1);
    }

    private CardViewMode readCardViewMode() {
        return (CardViewMode) getArguments().getSerializable(ARG_VIEW_MODE);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_cards_view_result, container, false);

        mViewedCardsText = v.findViewById(R.id.viewed_cards_text);
        mErrorCardsText = v.findViewById(R.id.err_cards_text);

        mScheduleLabelText = v.findViewById(R.id.schedule_label);
        mScheduleBtn = v.findViewById(R.id.schedule_btn);
        mScheduleBtn.setOnClickListener(view -> mPresenter.onUiScheduleClick());

        mOkBtn = v.findViewById(R.id.ok_btn);
        mOkBtn.setOnClickListener(view -> mPresenter.onUiOkClick());

        return v;
    }

    @Override
    public void setLearnView(boolean value) {
        int visible = value ? View.INVISIBLE : View.VISIBLE;
        mErrorCardsText.setVisibility(visible);
        mScheduleLabelText.setVisibility(visible);
        mScheduleBtn.setVisibility(visible);
    }

    @Override
    public void setViewedCardsCount(int count) {
        mViewedCardsText.setText(
                MessageFormat.format(
                        getResources().getString(R.string.cards_view_res_viewed_cards),
                        count
                )
        );
    }

    @Override
    public void setErrorCardsCount(int count) {
        mErrorCardsText.setText(
                MessageFormat.format(getResources().getString(R.string.cards_view_res_err_cards), count)
        );
    }

    @Override
    public void showNewScheduleString(Schedule schedule) {
        if (!schedule.isEmpty()) {
            mScheduleBtn.setText(schedule.toStringView(getResources()));
        } else {
            mScheduleBtn.setText(R.string.schedule_none);
        }
    }

    @Override
    public void showScheduleEditDialog(Schedule schedule) {
        ScheduleEditFragment dialog = ScheduleEditFragment.newFragment(schedule);
        dialog.setTargetFragment(this, REQ_SCHEDULE_EDIT);
        dialog.show(getActivity().getSupportFragmentManager(), ScheduleEditFragment.TAG);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQ_SCHEDULE_EDIT && resultCode == Activity.RESULT_OK) {
            String scheduleString = data.getStringExtra(ScheduleEditFragment.RESULT_SCHEDULE_STRING);
            Schedule schedule = new Schedule();
            schedule.initFromString(scheduleString);
            mPresenter.onNewScheduleSet(schedule);
        }

    }
}
