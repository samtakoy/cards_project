package ru.samtakoy.core.presentation.cards.answer

import android.os.Bundle
import android.view.ContextMenu
import android.view.ContextMenu.ContextMenuInfo
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.CompoundButton
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import ru.samtakoy.R
import ru.samtakoy.core.presentation.cards.CardsViewFragment
import ru.samtakoy.core.presentation.cards.answer.vm.CardAnswerViewModel
import ru.samtakoy.core.presentation.cards.answer.vm.CardAnswerViewModel.Event
import ru.samtakoy.core.presentation.cards.answer.vm.CardAnswerViewModelImpl
import ru.samtakoy.core.presentation.cards.types.CardViewMode
import ru.samtakoy.presentation.base.observe
import ru.samtakoy.presentation.base.viewmodel.AbstractViewModel
import ru.samtakoy.presentation.base.viewmodel.ViewModelOwner

class CardAnswerFragment : Fragment(), ViewModelOwner {

    private val viewModel: CardAnswerViewModel by viewModel<CardAnswerViewModelImpl> {
        parametersOf(
            // qPackId
            requireArguments().getLong(ARG_QPACK_ID, -1),
            // cardId
            requireArguments().getLong(ARG_CARD_ID, -1),
            // viewMode
            requireArguments().getSerializable(ARG_VIEW_MODE) as CardViewMode
        )
    }
    override fun getViewModel(): AbstractViewModel = viewModel

    private var mFavoriteCheckBox: CheckBox? = null
    private var mText: TextView? = null
    private var mBackButton: Button? = null
    private var mWrongButton: Button? = null
    private var mNextCardButton: Button? = null
    private var mView: View? = null
    private val mCheckListener = CompoundButton.OnCheckedChangeListener { buttonView, isChecked ->
        viewModel.onEvent(Event.FavoriteChange(isChecked = isChecked))
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(R.layout.fragment_card_answer, container, false)
        mView = v

        mFavoriteCheckBox = v.findViewById(R.id.favorite_check_box)
        mFavoriteCheckBox?.setOnCheckedChangeListener(mCheckListener)

        mBackButton = v.findViewById(R.id.back_btn)
        mBackButton?.setOnClickListener { view: View? ->
            viewModel.onEvent(Event.BackToQuestionClick)
        }

        mWrongButton = v.findViewById(R.id.wrong_btn)
        mWrongButton?.setOnClickListener { view: View? ->
            viewModel.onEvent(Event.WrongAnswerClick)
        }

        mNextCardButton = v.findViewById(R.id.next_card_btn)
        mNextCardButton?.setOnClickListener { view: View? ->
            viewModel.onEvent(Event.NextCardClick)
        }

        mText = v.findViewById(R.id.text)
        mText?.setLongClickable(true)
        registerForContextMenu(mView!!)

        return v
    }

    override fun onDestroyView() {
        unregisterForContextMenu(mView!!)
        mView = null

        super.onDestroyView()
    }

    override fun onCreateContextMenu(menu: ContextMenu, v: View, menuInfo: ContextMenuInfo?) {
        requireActivity().menuInflater.inflate(R.menu.edit_text_context, menu)
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_item_edit -> {
                viewModel.onEvent(Event.EditAnswerTextClick)
                return true
            }
        }
        return super.onContextItemSelected(item)
    }

    override fun onObserveViewModel() {
        super.onObserveViewModel()
        viewModel.getViewActionsAsFlow().observe(viewLifecycleOwner, ::onAction)
        viewModel.getViewStateAsFlow().observe(viewLifecycleOwner, ::onViewState)
    }

    private fun onAction(action: CardAnswerViewModel.Action) {
        when (action) {
            CardAnswerViewModel.Action.BackToQuestion -> getCallbacks()?.onBackToQuestion()
            is CardAnswerViewModel.Action.ShowErrorMessage -> showError(action.message)
            CardAnswerViewModel.Action.StartAnswerTextEdit -> getCallbacks()?.onEditAnswerText()
            CardAnswerViewModel.Action.ToNextCard -> getCallbacks()?.onNextCard()
            CardAnswerViewModel.Action.WrongAnswer -> getCallbacks()?.onWrongAnswer()
        }
    }

    private fun onViewState(state: CardAnswerViewModel.State) {
        setAnswerText(state.answer.text)
        setFavorite(state.isFavorite?.isChecked == true)
        setBackButtonVisible(state.backButton != null)
        setWrongButtonVisible(state.wrongButton != null)
        setNextCardButtonVisible(state.nextCardButton != null)
    }

    private fun setAnswerText(text: String) {
        mText!!.text = text
    }

    private fun setFavorite(isChecked: Boolean) {
        mFavoriteCheckBox?.setOnCheckedChangeListener(null)
        mFavoriteCheckBox?.isChecked = isChecked
        mFavoriteCheckBox?.setOnCheckedChangeListener(mCheckListener)
    }

    private fun setBackButtonVisible(visible: Boolean) {
        mBackButton!!.visibility = if (visible) View.VISIBLE else View.GONE
    }

    private fun setWrongButtonVisible(visible: Boolean) {
        mWrongButton!!.visibility = if (visible) View.VISIBLE else View.GONE
    }

    private fun setNextCardButtonVisible(visible: Boolean) {
        mNextCardButton!!.visibility = if (visible) View.VISIBLE else View.GONE
    }

    private fun showError(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    private fun getCallbacks(): CardAnswerPresenter.Callbacks? {
        return parentFragment as? CardAnswerPresenter.Callbacks
    }

    companion object {
        const val TAG: String = "CardAnswerFragment"

        private const val ARG_QPACK_ID = "ARG_QPACK_ID"
        private const val ARG_CARD_ID = "ARG_CARD_ID"
        private const val ARG_VIEW_MODE = "ARG_VIEW_MODE"

        @JvmStatic
        fun newFragment(
            qPackId: Long?,
            cardId: Long,
            cardIndex: Int,
            cardsCount: Int,
            viewMode: CardViewMode?
        ): CardAnswerFragment {
            val result = CardAnswerFragment()
            val args = Bundle()
            args.putSerializable(ARG_QPACK_ID, qPackId)
            args.putLong(ARG_CARD_ID, cardId)
            args.putInt(CardsViewFragment.ARG_CARD_INDEX, cardIndex)
            args.putInt(CardsViewFragment.ARG_CARDS_COUNT, cardsCount)
            args.putSerializable(ARG_VIEW_MODE, viewMode)
            result.arguments = args
            return result
        }

        @JvmStatic
        fun getCardIndexArg(fragment: Fragment): Int {
            return fragment.requireArguments().getInt(CardsViewFragment.ARG_CARD_INDEX, -1)
        }

        @JvmStatic
        fun getCardsCountArg(fragment: Fragment): Int {
            return fragment.requireArguments().getInt(CardsViewFragment.ARG_CARDS_COUNT, 0)
        }
    }
}
