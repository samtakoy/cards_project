package ru.samtakoy.core.screens.cards;

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

import moxy.MvpAppCompatFragment;
import moxy.presenter.InjectPresenter;
import moxy.presenter.ProvidePresenter;
import ru.samtakoy.R;
import ru.samtakoy.core.MyApp;
import ru.samtakoy.core.business.events.EventBusHolder;
import ru.samtakoy.core.model.elements.Schedule;
import ru.samtakoy.core.navigation.RouterHolder;
import ru.samtakoy.core.screens.cards.answer.CardAnswerFragment;
import ru.samtakoy.core.screens.cards.answer.CardAnswerPresenter;
import ru.samtakoy.core.screens.cards.question.CardQuestionFragment;
import ru.samtakoy.core.screens.cards.question.CardQuestionPresenter;
import ru.samtakoy.core.screens.cards.result.CardsViewResultFragment;
import ru.samtakoy.core.screens.cards.result.CardsViewResultPresenter;
import ru.samtakoy.core.screens.cards.types.CardViewMode;
import ru.samtakoy.core.screens.cards.types.CardViewSource;
import ru.samtakoy.core.screens.log.LogActivity;
import ru.samtakoy.core.screens.misc.edit_text_block.EditTextBlockDialogFragment;

public class CardsViewFragment extends MvpAppCompatFragment
        implements CardQuestionPresenter.Callbacks, CardAnswerPresenter.Callbacks, CardsViewResultPresenter.Callbacks, CardsViewView {


    private static final String ARG_COURSE_ID = "ARG_COURSE_ID";
    private static final String ARG_VIEW_MODE = "ARG_VIEW_MODE";

    private static final int REQ_CODE_EDIT_Q_TEXT = 1;
    private static final int REQ_CODE_EDIT_A_TEXT = 2;

    public static CardsViewFragment newInstance(
            Long qLearnPlanId,
            CardViewSource viewSource,
            CardViewMode viewMode
    ) {

        Bundle args = new Bundle();
        args.putLong(ARG_COURSE_ID, qLearnPlanId);
        //args.putExtra(ARG_VIEW_SOURCE, viewSource);
        args.putSerializable(ARG_VIEW_MODE, viewMode);

        CardsViewFragment fragment = new CardsViewFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private ProgressIndicator mProgressIndicator;
    private FloatingActionButton mFab;

    private RouterHolder mRouterHolder;
    private EventBusHolder mEventBusHolder;

    @InjectPresenter
    CardsViewPresenter mPresenter;


    @ProvidePresenter
    CardsViewPresenter providePresenter() {
        return new CardsViewPresenter(MyApp.getInstance().getAppComponent(), readLearnCourseId(), readViewMode());
    }

    private CardViewMode readViewMode() {
        return (CardViewMode) getArguments().getSerializable(ARG_VIEW_MODE);
    }

    private Long readLearnCourseId() {
        Long learnCourseId = getArguments().getLong(ARG_COURSE_ID, -1);
        return learnCourseId;
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
        mEventBusHolder = (EventBusHolder) context;
    }

    @Override
    public void onDetach() {

        mRouterHolder = null;
        mEventBusHolder = null;

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
            Fragment newF, @AnimatorRes @AnimRes int enter, @AnimatorRes @AnimRes int exit
    ){

        FragmentManager fm = getChildFragmentManager();
        if(fm.findFragmentById(R.id.fragment_cont) == null){
            fm.beginTransaction()
                    .add(R.id.fragment_cont, newF)
                    .commit();
        } else {
            fm.beginTransaction()
                    .setCustomAnimations(enter, exit)
                    .replace(R.id.fragment_cont, newF)
                    .commit();
        }
    }

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
        switchScreen(newF,
                back ? R.animator.slide_in_right : R.animator.slide_in_left,
                back ? R.animator.slide_out_left : R.animator.slide_out_right
        );
    }

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
    public void switchScreenToResults(Long learnCourseId, CardViewMode viewMode){

        showProgress(100);

        Fragment newF = CardsViewResultFragment.newFragment(learnCourseId, viewMode);
        switchScreen(newF, R.animator.slide_in_down, R.animator.slide_out_right);
    }

    @Override
    public void showEditTextDialog(String text, boolean question) {
        EditTextBlockDialogFragment.newInstance(text,
                this,
                question ? REQ_CODE_EDIT_Q_TEXT : REQ_CODE_EDIT_A_TEXT)
                .show(getFragmentManager(), EditTextBlockDialogFragment.TAG);
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
    public void showError(int stringId){
        Toast.makeText(getContext(), stringId, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void closeScreen() {
        //getActivity().getFragmentManager().popBackStack();
        //finish();
        mRouterHolder.getRouter().exit();
    }

    @Override
    public void showRevertButton(boolean visibility) {
        mFab.setVisibility(
                visibility ? View.VISIBLE : View.INVISIBLE
        );
    }

    // -----------------------------------------------------


}
