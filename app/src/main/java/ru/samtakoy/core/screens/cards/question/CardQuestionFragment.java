package ru.samtakoy.core.screens.cards.question;

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

import javax.inject.Inject;
import javax.inject.Provider;

import moxy.MvpAppCompatFragment;
import moxy.presenter.InjectPresenter;
import moxy.presenter.ProvidePresenter;
import ru.samtakoy.R;
import ru.samtakoy.core.MyApp;
import ru.samtakoy.core.screens.cards.types.CardViewMode;

public class CardQuestionFragment extends MvpAppCompatFragment implements CardQuestionView {

    private static final String ARG_QPACK_ID = "ARG_QPACK_ID";
    private static final String ARG_CARD_ID = "ARG_CARD_ID";
    private static final String ARG_LAST_CARD = "ARG_LAST_CARD";
    private static final String ARG_VIEW_MODE = "ARG_VIEW_MODE";

    public static CardQuestionFragment newFragment(Long qPackId, Long cardId, boolean lastCard, CardViewMode viewMode) {
        CardQuestionFragment result = new CardQuestionFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_QPACK_ID, qPackId);
        args.putLong(ARG_CARD_ID, cardId);
        args.putBoolean(ARG_LAST_CARD, lastCard);
        args.putSerializable(ARG_VIEW_MODE, viewMode);
        result.setArguments(args);
        return result;
    }


    private View mView;

    private TextView mText;
    private Button mPrevCardButton;
    private Button mNextCardButton;
    private Button mViewAnswerButton;

    @InjectPresenter
    CardQuestionPresenter mPresenter;
    @Inject
    Provider<CardQuestionPresenter.Factory> mPresenterProvider;

    @ProvidePresenter
    CardQuestionPresenter providePresenter() {
        return mPresenterProvider.get().create(
                getCallbacks(), readCardId(), readViewMode(), readLastCard()
        );
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        MyApp.getInstance().getAppComponent().inject(this);
        super.onCreate(savedInstanceState);
    }

    private CardQuestionPresenter.Callbacks getCallbacks() {
        return (CardQuestionPresenter.Callbacks) getParentFragment();
    }

    private Long readCardId() {
        return getArguments().getLong(ARG_CARD_ID, -1);
    }

    private boolean readLastCard() {
        return getArguments().getBoolean(ARG_LAST_CARD);
    }

    private CardViewMode readViewMode() {
        return (CardViewMode) getArguments().getSerializable(ARG_VIEW_MODE);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_card_question, container, false);
        mView = v;

        mText = v.findViewById(R.id.text);

        mPrevCardButton = v.findViewById(R.id.prev_card_btn);
        mPrevCardButton.setOnClickListener(view -> mPresenter.onUiPrevCard());
        mNextCardButton = v.findViewById(R.id.next_card_btn);
        mNextCardButton.setOnClickListener(view -> mPresenter.onUiNextCard());
        mViewAnswerButton = v.findViewById(R.id.view_answer_btn);
        mViewAnswerButton.setOnClickListener(view -> mPresenter.onUiViewAnswer());

        mText.setLongClickable(true);
        registerForContextMenu(mView);

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
        switch (item.getItemId()) {
            case R.id.menu_item_edit:
                mPresenter.onUiEditQuestionText();
                return true;
        }
        return super.onContextItemSelected(item);
    }

    @Override
    public void setPrevCardButtonVisible(boolean visible) {
        mPrevCardButton.setVisibility(visible ? View.VISIBLE : View.GONE);
    }

    @Override
    public void setViewAnswerButtonVisible(boolean visible) {
        mViewAnswerButton.setVisibility(visible ? View.VISIBLE : View.GONE);
    }

    @Override
    public void setNextCardButtonVisible(boolean visible) {
        mNextCardButton.setVisibility(visible ? View.VISIBLE : View.GONE);
    }

    @Override
    public void setQuestionText(String text) {
        mText.setText(text);
    }


}
