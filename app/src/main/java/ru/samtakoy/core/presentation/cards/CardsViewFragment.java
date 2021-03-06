package ru.samtakoy.core.presentation.cards;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.AnimRes;
import androidx.annotation.AnimatorRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.progressindicator.ProgressIndicator;

import org.jetbrains.annotations.NotNull;

import javax.inject.Inject;

import moxy.MvpAppCompatFragment;
import moxy.presenter.InjectPresenter;
import moxy.presenter.ProvidePresenter;
import ru.samtakoy.R;
import ru.samtakoy.core.app.di.Di;
import ru.samtakoy.core.data.local.database.room.entities.elements.Schedule;
import ru.samtakoy.core.presentation.FragmentHelperKt;
import ru.samtakoy.core.presentation.RouterHolder;
import ru.samtakoy.core.presentation.cards.answer.CardAnswerFragment;
import ru.samtakoy.core.presentation.cards.answer.CardAnswerPresenter;
import ru.samtakoy.core.presentation.cards.question.CardQuestionFragment;
import ru.samtakoy.core.presentation.cards.question.CardQuestionPresenter;
import ru.samtakoy.core.presentation.cards.result.CardsViewResultFragment;
import ru.samtakoy.core.presentation.cards.result.CardsViewResultPresenter;
import ru.samtakoy.core.presentation.cards.types.CardViewMode;
import ru.samtakoy.core.presentation.cards.types.CardViewSource;
import ru.samtakoy.core.presentation.log.LogActivity;
import ru.samtakoy.core.presentation.misc.edit_text_block.EditTextBlockDialogFragment;

public class CardsViewFragment extends MvpAppCompatFragment
        implements CardQuestionPresenter.Callbacks, CardAnswerPresenter.Callbacks, CardsViewResultPresenter.Callbacks, CardsViewView {


    private static final String ARG_COURSE_ID = "ARG_COURSE_ID";
    private static final String ARG_VIEW_MODE = "ARG_VIEW_MODE";

    private static final int REQ_CODE_EDIT_Q_TEXT = 1;
    private static final int REQ_CODE_EDIT_A_TEXT = 2;

    private static final String SAVE_KEY_PRESENTER_STATE = "SAVE_KEY_PRESENTER_STATE";

    public static CardsViewFragment newInstance(
            Long qLearnPlanId,
            CardViewSource viewSource,
            CardViewMode viewMode
    ) {

        Bundle args = buildBundle(qLearnPlanId, viewSource, viewMode);

        CardsViewFragment fragment = new CardsViewFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @NotNull
    public static Bundle buildBundle(Long qLearnPlanId, CardViewSource viewSource, CardViewMode viewMode) {
        Bundle args = new Bundle();
        args.putLong(ARG_COURSE_ID, qLearnPlanId);
        args.putInt(ARG_VIEW_MODE, viewMode.ordinal());
        return args;
    }

    private ProgressIndicator mProgressIndicator;
    private FloatingActionButton mFab;

    private RouterHolder mRouterHolder;
    //private EventBusHolder mEventBusHolder;

    @InjectPresenter
    CardsViewPresenter mPresenter;
    @Inject
    CardsViewPresenter.Factory mPresenterFactory;

    @ProvidePresenter
    CardsViewPresenter providePresenter() {
        //return new CardsViewPresenter(MyApp.getInstance().getAppComponent(), readLearnCourseId(), readViewMode());
        return mPresenterFactory.create(readLearnCourseId(), readViewMode());
    }

    private CardViewMode readViewMode() {
        int cardViewModeOrdinal = getArguments().getInt(ARG_VIEW_MODE);
        return CardViewMode.get(cardViewModeOrdinal);
    }

    private Long readLearnCourseId() {
        Long learnCourseId = getArguments().getLong(ARG_COURSE_ID, -1);
        return learnCourseId;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        Di.appComponent.inject(this);

        super.onCreate(savedInstanceState);

        if (savedInstanceState != null) {
            mPresenter.onRestoreState(savedInstanceState.getParcelable(SAVE_KEY_PRESENTER_STATE));
        } else {
            mPresenter.onNoRestoreState();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putParcelable(SAVE_KEY_PRESENTER_STATE, mPresenter.getStateToSave());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_cards_view, container, false);

        mProgressIndicator = v.findViewById(R.id.progress_indicator);
        mProgressIndicator.setProgress(0);

        mFab = v.findViewById(R.id.fab);
        mFab.setOnClickListener(view -> mPresenter.onUiRevertClick());

        return v;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        mRouterHolder = (RouterHolder) context;
        //mEventBusHolder = (EventBusHolder) context;
    }

    @Override
    public void onDetach() {

        mRouterHolder = null;
        //mEventBusHolder = null;

        super.onDetach();
    }

    private Fragment createCardFragment(
            Long qPackId,
            Long cardId,
            CardViewMode viewMode,
            boolean onAnswer,
            boolean lastCard
    ){
        if(onAnswer){
            return CardAnswerFragment.newFragment(qPackId, cardId, lastCard, viewMode);
        } else {
            return CardQuestionFragment.newFragment(qPackId, cardId, lastCard, viewMode);
        }
    }

    private void switchScreen(
            Fragment newFragment,
            boolean allowAnimations,
            @AnimatorRes @AnimRes int enter, @AnimatorRes @AnimRes int exit
    ) {

        FragmentManager fm = getChildFragmentManager();
        boolean firstTimeFragmentAdding = fm.findFragmentById(R.id.fragment_cont) == null;

        if (firstTimeFragmentAdding) {
            fm.beginTransaction()
                    .add(R.id.fragment_cont, newFragment)
                    .commit();
        } else {
            if (allowAnimations) {
                fm.beginTransaction()
                        .setCustomAnimations(enter, exit)
                        .replace(R.id.fragment_cont, newFragment)
                        .commit();
            } else {
                fm.beginTransaction()
                        .replace(R.id.fragment_cont, newFragment)
                        .commit();
            }

        }
    }

    @Override
    public void switchScreenToCard(
            Long qPackId,
            Long cardId,
            CardViewMode viewMode,
            boolean onAnswer,
            CardsViewPresenter.AnimationType aType,
            boolean lastCard
    ) {
        Fragment newF = createCardFragment(qPackId, cardId, viewMode, onAnswer, lastCard);

        switch (aType) {
            case FORWARD:
                switchScreen(newF, true, R.animator.slide_in_left, R.animator.slide_out_right);
                break;
            case BACK:
                switchScreen(newF, true, R.animator.slide_in_right, R.animator.slide_out_left);
                break;
            case OFF:
                switchScreen(newF, false, 0, 0);
                break;
        }
    }
/*
    @Override
    public void switchScreenToCard(
            Long qPackId,
            Long cardId,
            CardViewMode viewMode,
            boolean onAnswer,
            boolean back,
            boolean lastCard
    ) {
        Fragment newF = createCardFragment(qPackId, cardId, viewMode, onAnswer, lastCard);
        switchScreen(newF);
    }*/

    @Override
    public void showProgress(int viewedCardCount, int totalCardCount, boolean onAnswer) {

        int addVirtualCards = onAnswer ? 1 : 0;
        float progress = (viewedCardCount*2 + addVirtualCards) / (2*(float)totalCardCount);
        int intProgress = (int)Math.min(Math.floor(progress*100), 100);

        showProgress(intProgress);

        if(!onAnswer){
            Toast.makeText(getContext(), (viewedCardCount+1)+"/"+totalCardCount, Toast.LENGTH_SHORT).show();
        }
    }

    private void showProgress(int intProgress) {
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.N){
            mProgressIndicator.setProgress(intProgress);
        } else{
            mProgressIndicator.setProgress(intProgress, true);
        }
    }

    @Override
    public void switchScreenToResults(Long learnCourseId, CardViewMode viewMode, CardsViewPresenter.AnimationType aType) {

        showProgress(100);

        Fragment newF = CardsViewResultFragment.newFragment(learnCourseId, viewMode);

        switchScreen(newF, aType != CardsViewPresenter.AnimationType.OFF, R.animator.slide_in_down, R.animator.slide_out_right);
    }

    @Override
    public void showEditTextDialog(String text, boolean question) {

        FragmentHelperKt.showDialogFragment(
                EditTextBlockDialogFragment.newInstance(
                        text,
                        this,
                        question ? REQ_CODE_EDIT_Q_TEXT : REQ_CODE_EDIT_A_TEXT),
                this, EditTextBlockDialogFragment.TAG
        );
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        switch (requestCode){
            case REQ_CODE_EDIT_Q_TEXT:
                if(resultCode == Activity.RESULT_OK){
                    mPresenter.onNewQuestionText(data.getExtras().getString(EditTextBlockDialogFragment.RESULT_TEXT));
                }
                break;
            case REQ_CODE_EDIT_A_TEXT:
                if(resultCode == Activity.RESULT_OK){
                    mPresenter.onNewAnswerText(data.getExtras().getString(EditTextBlockDialogFragment.RESULT_TEXT));
                }
                break;
        }

    }

    // -----------------------------------------------------


    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.log, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){
            case R.id.menu_item_log:
                //
                //switchScreen(LogFragment.newQPacksFragment(), R.animator.slide_in_down, R.animator.slide_out_right);
                startActivity(LogActivity.newActivityIntent(getContext()));
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    // -----------------------------------------------------

    @Override
    public void onPrevCard() {
        mPresenter.onUiPrevCard();
    }

    @Override
    public void onViewAnswer() {
        mPresenter.onUiViewAnswer();
    }

    @Override
    public void onNextCard() {
        mPresenter.onUiNextCard();
    }


    @Override
    public void onEditQuestionText() {
        mPresenter.onUiQuestionEditTextClick();
    }

    @Override
    public void onBackToQuestion() {
        mPresenter.onUiBackToQuestion();
    }

    @Override
    public void onWrongAnswer() {
        mPresenter.onUiWrongAnswer();
    }

    @Override
    public void onEditAnswerText() {
        mPresenter.onUiAnswerEditTextClick();
    }


    @Override
    public void onResultOk(Schedule newSchedule) {
        mPresenter.onUiResultOk(newSchedule);
    }

    @Override
    public void closeScreen() {
        mRouterHolder.getNavController().navigateUp();
    }

    @Override
    public void showRevertButton(boolean visibility) {
        mFab.setVisibility(
                visibility ? View.VISIBLE : View.INVISIBLE
        );
    }

    @Override
    public void showError(int stringId) {
        Toast.makeText(getContext(), stringId, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void blockScreenOnOperation() {
    }

    @Override
    public void unblockScreenOnOperation() {
    }

    // -----------------------------------------------------


}
