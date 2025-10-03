package ru.samtakoy.core.presentation.cards

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.AnimRes
import androidx.annotation.AnimatorRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.android.material.floatingactionbutton.FloatingActionButton
import ru.samtakoy.R
import ru.samtakoy.core.app.di.Di
import ru.samtakoy.core.data.local.database.room.entities.elements.Schedule
import ru.samtakoy.core.presentation.RouterHolder
import ru.samtakoy.core.presentation.base.observe
import ru.samtakoy.core.presentation.base.viewmodel.AbstractViewModel
import ru.samtakoy.core.presentation.base.viewmodel.ViewModelOwner
import ru.samtakoy.core.presentation.cards.answer.CardAnswerFragment
import ru.samtakoy.core.presentation.cards.answer.CardAnswerPresenter
import ru.samtakoy.core.presentation.cards.question.CardQuestionFragment
import ru.samtakoy.core.presentation.cards.question.CardQuestionPresenter
import ru.samtakoy.core.presentation.cards.result.CardsViewResultFragment
import ru.samtakoy.core.presentation.cards.result.CardsViewResultPresenter
import ru.samtakoy.core.presentation.cards.types.AnimationType
import ru.samtakoy.core.presentation.cards.types.CardViewMode
import ru.samtakoy.core.presentation.cards.vm.CardsViewViewModel
import ru.samtakoy.core.presentation.cards.vm.CardsViewViewModel.Event
import ru.samtakoy.core.presentation.cards.vm.CardsViewViewModelFactory
import ru.samtakoy.core.presentation.cards.vm.CardsViewViewModelImpl
import ru.samtakoy.core.presentation.log.LogActivity
import ru.samtakoy.core.presentation.misc.edit_text_block.EditTextBlockDialogFragment
import ru.samtakoy.core.presentation.showDialogFragment
import kotlin.math.floor
import kotlin.math.min
import javax.inject.Inject

class CardsViewFragment : Fragment(),
    CardQuestionPresenter.Callbacks,
    CardAnswerPresenter.Callbacks,
    CardsViewResultPresenter.Callbacks,
    ViewModelOwner {
    // TODO
    // private ProgressIndicator mProgressIndicator;
    private var mFab: FloatingActionButton? = null

    private var mRouterHolder: RouterHolder? = null

    @Inject
    internal lateinit var viewModelFactory: CardsViewViewModelFactory.Factory
    private val viewModel: CardsViewViewModelImpl by viewModels {
        viewModelFactory.create(
            readViewHistoryItemId(),
            readViewMode()
        )
    }
    override fun getViewModel(): AbstractViewModel = viewModel

    private fun readViewMode(): CardViewMode {
        val cardViewModeOrdinal = requireArguments().getInt(ARG_VIEW_MODE)
        return CardViewMode.get(cardViewModeOrdinal)
    }

    private fun readViewHistoryItemId(): Long {
        return requireArguments().getLong(ARG_VIEW_HISTORY_ITEM_ID, 0)
    }

    override fun onObserveViewModel() {
        super.onObserveViewModel()
        viewModel.getViewActionsAsFlow().observe(viewLifecycleOwner, ::onAction)
        viewModel.getViewStateAsFlow().observe(viewLifecycleOwner, ::onViewState)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        Di.appComponent.inject(this)
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(R.layout.fragment_cards_view, container, false)

        // mProgressIndicator = v.findViewById(R.id.progress_indicator);
        // mProgressIndicator.setProgress(0);
        mFab = v.findViewById<FloatingActionButton>(R.id.fab)
        mFab!!.setOnClickListener(
            View.OnClickListener { view: View? -> viewModel.onEvent(Event.RevertClick) }
        )

        return v
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mRouterHolder = context as RouterHolder
    }

    override fun onDetach() {
        mRouterHolder = null
        super.onDetach()
    }

    private fun onAction(action: CardsViewViewModel.Action) {
        when (action) {
            is CardsViewViewModel.Action.ShowErrorMessage -> {
                showError(action.message)
            }
            CardsViewViewModel.NavigationAction.CloseScreen -> {
                mRouterHolder!!.getNavController().navigateUp()
            }
            is CardsViewViewModel.Action.ShowEditTextDialog -> {
                showEditTextDialog(action.text, action.question)
            }
        }
    }

    private fun onViewState(viewState: CardsViewViewModel.State) {
        when (val type = viewState.type) {
            is CardsViewViewModel.State.Type.Card -> {
                switchScreenToCard(
                    qPackId = type.qPackId,
                    cardId = type.cardId,
                    cardIndex = type.cardIndex,
                    cardsCount = type.cardsCount,
                    viewMode = viewState.viewMode,
                    onAnswer = type.onAnswer
                )
                showProgress(
                    viewedCardCount = type.cardIndex,
                    totalCardCount = type.cardsCount,
                    onAnswer = type.onAnswer
                )
                showRevertButton(type.hasRevertButton)
            }
            is CardsViewViewModel.State.Type.Error,
            CardsViewViewModel.State.Type.Initialization -> Unit
            is CardsViewViewModel.State.Type.Results -> {
                switchScreenToResults(
                    viewItemId = type.viewItemId,
                    viewMode = viewState.viewMode
                )
            }
        }
    }

    private fun createCardFragment(
        qPackId: Long?,
        cardId: Long,
        cardIndex: Int,
        cardsCount: Int,
        viewMode: CardViewMode?,
        onAnswer: Boolean
    ): Fragment {
        if (onAnswer) {
            return CardAnswerFragment.newFragment(
                qPackId,
                cardId,
                cardIndex,
                cardsCount,
                viewMode
            )
        } else {
            return CardQuestionFragment.newFragment(
                qPackId,
                cardId,
                cardIndex,
                cardsCount,
                viewMode
            )
        }
    }

    private fun switchScreen(
        newFragment: Fragment,
        allowAnimations: Boolean,
        @AnimatorRes @AnimRes enter: Int,
        @AnimatorRes @AnimRes exit: Int
    ) {
        val fm = getChildFragmentManager()
        val firstTimeFragmentAdding = fm.findFragmentById(R.id.fragment_cont) == null

        if (firstTimeFragmentAdding) {
            fm.beginTransaction()
                .add(R.id.fragment_cont, newFragment)
                .commit()
        } else {
            if (allowAnimations) {
                fm.beginTransaction()
                    .setCustomAnimations(enter, exit)
                    .replace(R.id.fragment_cont, newFragment)
                    .commit()
            } else {
                fm.beginTransaction()
                    .replace(R.id.fragment_cont, newFragment)
                    .commit()
            }
        }
    }

    private fun switchScreenToCard(
        qPackId: Long,
        cardId: Long,
        cardIndex: Int,
        cardsCount: Int,
        viewMode: CardViewMode,
        onAnswer: Boolean
    ) {
        val currentFragment = getChildFragmentManager().findFragmentById(R.id.fragment_cont)

        if (currentFragment != null && currentFragment !is CardsViewResultFragment) {
            val currentIndex = if (currentFragment is CardQuestionFragment)
                CardQuestionFragment.getCardIndexArg(currentFragment)
            else
                CardAnswerFragment.getCardIndexArg(currentFragment)
            val currentOnAnswer = currentFragment is CardAnswerFragment
            if (currentIndex == cardIndex && currentOnAnswer == onAnswer) {
                // уже отображено
                return
            }
        }

        val newF = createCardFragment(
            qPackId,
            cardId,
            cardIndex,
            cardsCount,
            viewMode,
            onAnswer
        )

        val aType: AnimationType = resolveAnimationTypeForCard(
            currentFragment,
            cardIndex,
            onAnswer
        )

        when (aType) {
            AnimationType.FORWARD -> switchScreen(newF, true, R.animator.slide_in_left, R.animator.slide_out_right)
            AnimationType.BACK -> switchScreen(newF, true, R.animator.slide_in_right, R.animator.slide_out_left)
            AnimationType.OFF -> switchScreen(newF, false, 0, 0)
        }
    }

    private fun showProgress(viewedCardCount: Int, totalCardCount: Int, onAnswer: Boolean) {
        val addVirtualCards = if (onAnswer) 1 else 0
        val progress = (viewedCardCount * 2 + addVirtualCards) / (2 * totalCardCount.toFloat())
        val intProgress = min(floor((progress * 100).toDouble()), 100.0).toInt()

        showProgress(intProgress)

        if (!onAnswer) {
            Toast.makeText(getContext(), (viewedCardCount + 1).toString() + "/" + totalCardCount, Toast.LENGTH_SHORT)
                .show()
        }
    }

    private fun showProgress(intProgress: Int) {
        /* TODO
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.N){
            mProgressIndicator.setProgress(intProgress);
        } else{
            mProgressIndicator.setProgress(intProgress, true);
        }*/
    }

    fun switchScreenToResults(viewItemId: Long, viewMode: CardViewMode) {
        showProgress(100)

        val currentFragment = getChildFragmentManager().findFragmentById(R.id.fragment_cont)
        if (currentFragment is CardsViewResultFragment) {
            return
        }

        val newF: Fragment = CardsViewResultFragment.newFragment(viewItemId, viewMode)
        switchScreen(
            newF,
            true,
            R.animator.slide_in_down,
            R.animator.slide_out_right
        )
    }

    private fun showEditTextDialog(text: String?, question: Boolean) {
        showDialogFragment(
            EditTextBlockDialogFragment.newInstance(
                text,
                this,
                if (question) REQ_CODE_EDIT_Q_TEXT else REQ_CODE_EDIT_A_TEXT
            ),
            this, EditTextBlockDialogFragment.TAG
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            REQ_CODE_EDIT_Q_TEXT -> if (resultCode == Activity.RESULT_OK) {
                viewModel.onEvent(
                    Event.QuestionTextChanged(data!!.getExtras()!!.getString(EditTextBlockDialogFragment.RESULT_TEXT)!!)
                )
            }
            REQ_CODE_EDIT_A_TEXT -> if (resultCode == Activity.RESULT_OK) {
                viewModel.onEvent(
                    Event.AnswerTextChanged(data!!.getExtras()!!.getString(EditTextBlockDialogFragment.RESULT_TEXT)!!)
                )
            }
        }
    }

    // -----------------------------------------------------
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.log, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.getItemId()) {
            R.id.menu_item_log -> {
                startActivity(LogActivity.newActivityIntent(getContext()))
                return true
            }
        }

        return super.onOptionsItemSelected(item)
    }

    // -----------------------------------------------------

    override fun onPrevCard() {
        viewModel.onEvent(Event.PrevCardClick)
    }

    override fun onViewAnswer() {
        viewModel.onEvent(Event.ViewAnswerClick)
    }

    override fun onNextCard() {
        viewModel.onEvent(Event.NextCardClick)
    }

    override fun onEditQuestionText() {
        viewModel.onEvent(Event.QuestionEditTextClick)
    }

    override fun onBackToQuestion() {
        viewModel.onEvent(Event.BackToQuestionClick)
    }

    override fun onWrongAnswer() {
        viewModel.onEvent(Event.WrongAnswerClick)
    }

    override fun onEditAnswerText() {
        viewModel.onEvent(Event.AnswerEditTextClick)
    }

    override fun onResultOk(newSchedule: Schedule) {
        viewModel.onEvent(Event.ScheduleEditResultOk(newSchedule))
    }

    private fun resolveAnimationTypeForCard(
        currentFragment: Fragment?,
        newCardIndex: Int,
        newOnAnswer: Boolean
    ): AnimationType {
        if (currentFragment == null) {
            // первый фрагмент без анимации
            return AnimationType.OFF
        }
        if (currentFragment is CardsViewResultFragment) {
            // возврат на карточку с результатов
            return AnimationType.BACK
        }

        val currentIndex = if (currentFragment is CardQuestionFragment)
            CardQuestionFragment.getCardIndexArg(currentFragment)
        else
            CardAnswerFragment.getCardIndexArg(currentFragment)

        val currentOnAnswer = currentFragment is CardAnswerFragment

        if (newCardIndex > currentIndex) {
            // движение вперед
            return AnimationType.FORWARD
        }
        if (newCardIndex < currentIndex) {
            // движение назад
            return AnimationType.BACK
        }
        // та же карточка
        if (currentOnAnswer == newOnAnswer) {
            // тот же тип
            return AnimationType.OFF
        }
        if (newOnAnswer) {
            // переход с вопроса на ответ
            return AnimationType.FORWARD
        } else {
            // возврат с ответа на вопрос
            return AnimationType.BACK
        }
    }

    // -----------------------------------------------------

    private fun showError(text: String) {
        Toast.makeText(getContext(), text, Toast.LENGTH_SHORT).show()
    }

    private fun showRevertButton(visibility: Boolean) {
        mFab!!.setVisibility(
            if (visibility) View.VISIBLE else View.INVISIBLE
        )
    }

    companion object {
        private const val ARG_VIEW_HISTORY_ITEM_ID = "ARG_VIEW_HISTORY_ITEM_ID"
        private const val ARG_VIEW_MODE = "ARG_VIEW_MODE"
        const val ARG_CARD_INDEX: String = "ARG_CARD_INDEX"
        const val ARG_CARDS_COUNT: String = "ARG_CARDS_COUNT"

        private const val REQ_CODE_EDIT_Q_TEXT = 1
        private const val REQ_CODE_EDIT_A_TEXT = 2

        @JvmStatic fun buildBundle(
            viewHistoryItemId: Long,
            viewMode: CardViewMode
        ): Bundle {
            val args = Bundle()
            args.putLong(ARG_VIEW_HISTORY_ITEM_ID, viewHistoryItemId)
            args.putInt(ARG_VIEW_MODE, viewMode.ordinal)
            return args
        }
    }
}
