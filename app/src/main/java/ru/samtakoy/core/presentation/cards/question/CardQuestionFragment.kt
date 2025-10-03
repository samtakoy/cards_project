package ru.samtakoy.core.presentation.cards.question

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
import androidx.fragment.app.viewModels
import ru.samtakoy.R
import ru.samtakoy.core.app.di.Di
import ru.samtakoy.core.presentation.base.observe
import ru.samtakoy.core.presentation.base.viewmodel.AbstractViewModel
import ru.samtakoy.core.presentation.base.viewmodel.ViewModelOwner
import ru.samtakoy.core.presentation.cards.CardsViewFragment
import ru.samtakoy.core.presentation.cards.question.vm.CardQuestionViewModel.Action
import ru.samtakoy.core.presentation.cards.question.vm.CardQuestionViewModel.Event
import ru.samtakoy.core.presentation.cards.question.vm.CardQuestionViewModel.State
import ru.samtakoy.core.presentation.cards.question.vm.CardQuestionViewModelFactory
import ru.samtakoy.core.presentation.cards.question.vm.CardQuestionViewModelImpl
import ru.samtakoy.core.presentation.cards.types.CardViewMode
import javax.inject.Inject

class CardQuestionFragment : Fragment(), ViewModelOwner {

    @Inject
    internal lateinit var viewModelFactory: CardQuestionViewModelFactory.Factory
    private val viewModel: CardQuestionViewModelImpl by viewModels {
        viewModelFactory.create(
            cardId = requireArguments().getLong(ARG_CARD_ID, -1),
            viewMode = requireArguments().getSerializable(ARG_VIEW_MODE) as CardViewMode
        )
    }
    override fun getViewModel(): AbstractViewModel = viewModel

    private var mView: View? = null
    private var mFavoriteCheckBox: CheckBox? = null
    private var mText: TextView? = null
    private var mPrevCardButton: Button? = null
    private var mNextCardButton: Button? = null
    private var mViewAnswerButton: Button? = null
    private val mCheckListener = CompoundButton.OnCheckedChangeListener { buttonView, isChecked ->
        viewModel.onEvent(Event.FavoriteChange(isChecked = isChecked))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        Di.appComponent.inject(this)
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(R.layout.fragment_card_question, container, false)
        mView = v

        mFavoriteCheckBox = v.findViewById(R.id.favorite_check_box)
        mFavoriteCheckBox?.setOnCheckedChangeListener(mCheckListener)
        mText = v.findViewById(R.id.text)
        mPrevCardButton = v.findViewById(R.id.prev_card_btn)
        mPrevCardButton?.setOnClickListener { view: View? ->
            viewModel.onEvent(Event.PrevCardClick)
        }
        mNextCardButton = v.findViewById(R.id.next_card_btn)
        mNextCardButton?.setOnClickListener { view: View? ->
            viewModel.onEvent(Event.NextCardClick)
        }
        mViewAnswerButton = v.findViewById(R.id.view_answer_btn)
        mViewAnswerButton?.setOnClickListener { view: View? ->
            viewModel.onEvent(Event.ViewAnswerClick)
        }

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
        activity?.menuInflater?.inflate(R.menu.edit_text_context, menu)
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_item_edit -> {
                viewModel.onEvent(Event.EditQuestionTextClick)
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

    private fun onAction(action: Action) {
        when (action) {
            is Action.ShowErrorMessage -> showError(action.message)
            Action.StartEditQuestionText -> resolveCallbacks()?.onEditQuestionText()
            Action.ToNextCard -> resolveCallbacks()?.onNextCard()
            Action.ToPrevCard -> resolveCallbacks()?.onPrevCard()
            Action.ViewAnswer -> resolveCallbacks()?.onViewAnswer()
        }
    }

    private fun onViewState(state: State) {
        mText!!.text = state.question.text
        setPrevCardButtonVisible(state.prevCardButton != null)
        setViewAnswerButtonVisible(state.viewAnswerButton != null)
        setNextCardButtonVisible(state.nextCardButton != null)
        setFavorite(isChecked = state.isFavorite?.isChecked == true)
    }

    private fun setPrevCardButtonVisible(visible: Boolean) {
        mPrevCardButton!!.visibility = if (visible) View.VISIBLE else View.GONE
    }

    private fun setViewAnswerButtonVisible(visible: Boolean) {
        mViewAnswerButton!!.visibility = if (visible) View.VISIBLE else View.GONE
    }

    private fun setNextCardButtonVisible(visible: Boolean) {
        mNextCardButton!!.visibility = if (visible) View.VISIBLE else View.GONE
    }

    private fun setFavorite(isChecked: Boolean) {
        mFavoriteCheckBox?.setOnCheckedChangeListener(null)
        mFavoriteCheckBox?.isChecked = isChecked
        mFavoriteCheckBox?.setOnCheckedChangeListener(mCheckListener)
    }

    private fun showError(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    private fun resolveCallbacks(): CardQuestionPresenter.Callbacks? {
        return parentFragment as CardQuestionPresenter.Callbacks?
    }

    companion object {
        private const val ARG_QPACK_ID = "ARG_QPACK_ID"
        private const val ARG_CARD_ID = "ARG_CARD_ID"
        private const val ARG_VIEW_MODE = "ARG_VIEW_MODE"

        @JvmStatic fun newFragment(
            qPackId: Long?,
            cardId: Long,
            cardIndex: Int,
            cardsCount: Int,
            viewMode: CardViewMode?
        ): CardQuestionFragment {
            val result = CardQuestionFragment()
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
