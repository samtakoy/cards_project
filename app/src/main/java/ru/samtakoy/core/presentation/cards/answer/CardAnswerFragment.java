package ru.samtakoy.core.presentation.cards.answer;

import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import javax.inject.Inject;
import javax.inject.Provider;

import moxy.MvpAppCompatFragment;
import moxy.presenter.InjectPresenter;
import moxy.presenter.ProvidePresenter;
import ru.samtakoy.R;
import ru.samtakoy.core.app.di.Di;
import ru.samtakoy.core.presentation.cards.types.CardViewMode;

public class CardAnswerFragment extends MvpAppCompatFragment implements CardAnswerView {

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

    private TextView mText;

    private Button mBackButton;
    private Button mWrongButton;
    private Button mNextCardButton;

    private View mView;

    @InjectPresenter
    CardAnswerPresenter mPresenter;
    @Inject
    Provider<CardAnswerPresenter.Factory> presenterFactoryProvider;

    @ProvidePresenter
    CardAnswerPresenter providePresenter() {
        return presenterFactoryProvider.get().create(
                getCallbacks(), readQPackId(), readCardId(), readViewMode(), readIsLastCard()
        );
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {

        Di.appComponent.inject(this);
        super.onCreate(savedInstanceState);
    }

    private Long readCardId(){
        return getArguments().getLong(ARG_CARD_ID, -1);
    }

    private Long readQPackId(){
        return getArguments().getLong(ARG_QPACK_ID, -1);
    }

    private boolean readIsLastCard(){
        return getArguments().getBoolean(ARG_LAST_CARD);
    }

    private CardViewMode readViewMode(){
        return  (CardViewMode) getArguments().getSerializable(ARG_VIEW_MODE);
    }

    private CardAnswerPresenter.Callbacks getCallbacks(){
        return (CardAnswerPresenter.Callbacks) getParentFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v;

        v = inflater.inflate(R.layout.fragment_card_answer, container, false);
        mView = v;

        mBackButton = v.findViewById(R.id.back_btn);
        mBackButton.setOnClickListener(view -> mPresenter.onUiBackToQuestion());

        mWrongButton = v.findViewById(R.id.wrong_btn);
        mWrongButton.setOnClickListener(view -> mPresenter.onUiWrongAnswer());

        mNextCardButton = v.findViewById(R.id.next_card_btn);
        mNextCardButton.setOnClickListener(view -> mPresenter.onUiNextCard());

        mText = v.findViewById(R.id.text);
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

        switch (item.getItemId()){
            case R.id.menu_item_edit:
                mPresenter.onUiEditAnswerText();
                return true;
        }
        return super.onContextItemSelected(item);
    }

    @Override
    public void setAnswerText(String text) {
        mText.setText(text);
    }

    @Override
    public void setBackButtonVisible(boolean visible) {
        mBackButton.setVisibility(visible ? View.VISIBLE : View.GONE);
    }

    @Override
    public void setWrongButtonVisible(boolean visible) {
        mWrongButton.setVisibility(visible ? View.VISIBLE : View.GONE);
    }

    @Override
    public void setNextCardButtonVisible(boolean visible) {
        mNextCardButton.setVisibility(visible ? View.VISIBLE : View.GONE);
    }

    @Override
    public void showError(int codeResId) {
        Toast.makeText(getContext(), codeResId, Toast.LENGTH_SHORT).show();
    }

}
