package ru.samtakoy.core.screens.cards;

import android.content.ContentResolver;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
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
import ru.samtakoy.core.model.QPack;
import ru.samtakoy.core.screens.cards.types.CardViewMode;

public class CardAnswerFragment extends Fragment {

    public static final String TAG = "CardAnswerFragment";


    private static final String ARG_QPACK_ID = "ARG_QPACK_ID";
    private static final String ARG_CARD_ID = "ARG_CARD_ID";
    private static final String ARG_LAST_CARD = "ARG_LAST_CARD";
    private static final String ARG_VIEW_MODE = "ARG_VIEW_MODE";

    public static CardAnswerFragment newFragment(Long qPackId, Long cardId, boolean lastCard, CardViewMode viewMode){
        CardAnswerFragment result = new CardAnswerFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_QPACK_ID, qPackId);
        args.putLong(ARG_CARD_ID, cardId);
        args.putBoolean(ARG_LAST_CARD, lastCard);
        args.putSerializable(ARG_VIEW_MODE, viewMode);
        result.setArguments(args);
        return result;
    }

    public interface Callbacks{

        void onBackToQuestion();
        void onWrongAnswer();
        void onNextCard();
        void onEditAnswerText();
    }

    private QPack mQPack;
    private Card mCard;
    private boolean mLastCard;
    private CardViewMode mViewMode;

    private TextView mText;

    private Button mBackButton;
    private Button mWrongButton;
    private Button mNextCardButton;

    private EventBusHolder mEventBusHolder;

    private View mView;

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


    private void readArgs(Bundle arguments) {

        Long cardId = arguments.getLong(ARG_CARD_ID, -1);
        Long qPackId = arguments.getLong(ARG_QPACK_ID, -1);
        mQPack = getQPack(qPackId);

        ContentResolver cr = getContext().getContentResolver();
        mCard = ContentProviderHelper.getConcreteCard(cr, cardId);
Log.e(TAG, "readArgs:"+cardId+"_"+(mCard==null) );

        mLastCard = arguments.getBoolean(ARG_LAST_CARD);
        mViewMode = (CardViewMode) arguments.getSerializable(ARG_VIEW_MODE);
    }

    private QPack getQPack(Long qPackId){
        //return mCardsRepository.getQPack(qPackId);
        return ContentProviderHelper.getConcretePack(getContext().getContentResolver(), qPackId);
    }

    private Callbacks getCallbacks(){
        return (Callbacks) getParentFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v;

        v = inflater.inflate(R.layout.fragment_card_answer, container, false);
        mView = v;


        mBackButton = v.findViewById(R.id.back_btn);
        mBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getCallbacks().onBackToQuestion();
            }
        });

        mWrongButton = v.findViewById(R.id.wrong_btn);
        mWrongButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getCallbacks().onWrongAnswer();
            }
        });

        mNextCardButton = v.findViewById(R.id.next_card_btn);
        mNextCardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getCallbacks().onNextCard();
            }
        });

        mText = v.findViewById(R.id.text);

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
                getCallbacks().onEditAnswerText();
                return true;
        }
        return super.onContextItemSelected(item);
    }

    private void updateView() {
        mText.setText(mCard.getAnswer());
    }

    private void setButtonsVisibility() {
        if(mViewMode == CardViewMode.LEARNING) {
            mBackButton.setVisibility(View.VISIBLE);
            mWrongButton.setVisibility(View.GONE);
            mNextCardButton.setVisibility(View.VISIBLE);
        }else
        if(mViewMode == CardViewMode.REPEATING) {
            mBackButton.setVisibility(View.GONE);
            mWrongButton.setVisibility(View.VISIBLE);
            mNextCardButton.setVisibility(View.VISIBLE);
        }else
        if(mViewMode == CardViewMode.REPEATING_FAST) {
            mBackButton.setVisibility(View.GONE);
            mWrongButton.setVisibility(View.VISIBLE);
            mNextCardButton.setVisibility(View.VISIBLE);
        }
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
