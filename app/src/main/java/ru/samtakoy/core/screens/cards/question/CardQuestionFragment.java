package ru.samtakoy.core.screens.cards;

import android.content.ContentResolver;
import android.content.Context;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import ru.samtakoy.R;
import ru.samtakoy.core.business.events.CardUpdateEvent;
import ru.samtakoy.core.business.events.EventBusHolder;
import ru.samtakoy.core.model.Card;
import ru.samtakoy.core.business.impl.ContentProviderHelper;
import ru.samtakoy.core.screens.cards.types.CardViewMode;

public class CardQuestionFragment extends Fragment {

    private static final String ARG_QPACK_ID = "ARG_QPACK_ID";
    private static final String ARG_CARD_ID = "ARG_CARD_ID";
    private static final String ARG_LAST_CARD = "ARG_LAST_CARD";
    private static final String ARG_VIEW_MODE = "ARG_VIEW_MODE";

    public static CardQuestionFragment newFragment(Long qPackId, Long cardId, boolean lastCard, CardViewMode viewMode){
        CardQuestionFragment result = new CardQuestionFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_QPACK_ID, qPackId);
        args.putLong(ARG_CARD_ID, cardId);
        args.putBoolean(ARG_LAST_CARD, lastCard);
        args.putSerializable(ARG_VIEW_MODE, viewMode);
        result.setArguments(args);
        return result;
    }

    public interface Callbacks{
        void onPrevCard();
        void onViewAnswer();
        void onNextCard();
        void onEditQuestionText();
    }

    private View mView;

    //private QPack mQPack;
    private Card mCard;
    private boolean mLastCard;
    private CardViewMode mViewMode;

    private TextView mText;
    private Button mPrevCardButton;
    private Button mNextCardButton;
    private Button mViewAnswerButton;

    private EventBusHolder mEventBusHolder;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        readArgs(getArguments());
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        mEventBusHolder = (EventBusHolder) context;
    }

    @Override
    public void onDetach() {

        mEventBusHolder = null;

        super.onDetach();
    }

    private Callbacks getCallbacks(){
        return (Callbacks) getParentFragment();
    }

    private void readArgs(Bundle arguments) {
        Long qPackId = arguments.getLong(ARG_QPACK_ID, -1);
        //mQPack = getQPack(qPackId);
        ContentResolver cr = getContext().getContentResolver();
        mCard = ContentProviderHelper.getConcreteCard(cr, arguments.getLong(ARG_CARD_ID));
        mLastCard = arguments.getBoolean(ARG_LAST_CARD);
        mViewMode = (CardViewMode) arguments.getSerializable(ARG_VIEW_MODE);
    }

    /*
    private QPack getQPack(Long qPackId){
        //return mCardsRepository.getQPack(qPackId);
        return ContentProviderHelper.getConcretePack(getContext().getContentResolver(), qPackId);
    }/**/

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_card_question, container, false);
        mView = v;

        mText = v.findViewById(R.id.text);



        mPrevCardButton = v.findViewById(R.id.prev_card_btn);
        mPrevCardButton.setOnClickListener(view -> getCallbacks().onPrevCard());
        mNextCardButton = v.findViewById(R.id.next_card_btn);
        mNextCardButton.setOnClickListener(view -> getCallbacks().onNextCard());
        mViewAnswerButton = v.findViewById(R.id.view_answer_btn);
        mViewAnswerButton.setOnClickListener(view -> getCallbacks().onViewAnswer());

        mText.setLongClickable(true);
        registerForContextMenu(mView);

        setButtonsVisibility();

        return v;
    }

    @Override
    public void onDestroyView() {

        unregisterForContextMenu(mView);
        mView = null;

        super.onDestroyView();
    }

    @Override
    public void onCreateContextMenu(@NonNull ContextMenu menu, @NonNull View v, @Nullable ContextMenu.ContextMenuInfo menuInfo) {
        getActivity().getMenuInflater().inflate(R.menu.edit_text_context, menu);
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_item_edit:
                getCallbacks().onEditQuestionText();
                return true;
        }
        return super.onContextItemSelected(item);
    }

    private void setButtonsVisibility() {
        if(mViewMode == CardViewMode.LEARNING) {
            mPrevCardButton.setVisibility(View.VISIBLE);
            mViewAnswerButton.setVisibility(View.VISIBLE);
            mNextCardButton.setVisibility(View.GONE);
        }else
        if(mViewMode == CardViewMode.REPEATING) {
            mPrevCardButton.setVisibility(View.VISIBLE);
            mViewAnswerButton.setVisibility(View.VISIBLE);
            mNextCardButton.setVisibility(View.GONE);
        }else
        if(mViewMode == CardViewMode.REPEATING_FAST) {
            mPrevCardButton.setVisibility(View.GONE);
            mViewAnswerButton.setVisibility(View.VISIBLE);
            mNextCardButton.setVisibility(View.VISIBLE);
        }

    }

    private void updateView(){
        mText.setText(mCard.getQuestion());
    }

    @Override
    public void onStart() {
        super.onStart();

        mEventBusHolder.getEventBus().register(this);

        updateView();
    }

    @Override
    public void onStop() {

        mEventBusHolder.getEventBus().unregister(this);

        super.onStop();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onCardUpdateEvent(CardUpdateEvent event){
        if(mCard.getId().equals(event.getCard().getId())){
            mCard = event.getCard();
            updateView();
        }
    }


}
