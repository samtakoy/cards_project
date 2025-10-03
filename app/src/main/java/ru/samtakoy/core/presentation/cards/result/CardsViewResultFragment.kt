package ru.samtakoy.core.presentation.cards.result

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import ru.samtakoy.R
import ru.samtakoy.core.app.di.Di
import ru.samtakoy.core.data.local.database.room.entities.elements.Schedule
import ru.samtakoy.core.presentation.base.observe
import ru.samtakoy.core.presentation.base.viewmodel.AbstractViewModel
import ru.samtakoy.core.presentation.base.viewmodel.ViewModelOwner
import ru.samtakoy.core.presentation.cards.result.vm.CardsViewResultViewModel.Action
import ru.samtakoy.core.presentation.cards.result.vm.CardsViewResultViewModel.Event
import ru.samtakoy.core.presentation.cards.result.vm.CardsViewResultViewModel.State
import ru.samtakoy.core.presentation.cards.result.vm.CardsViewResultViewModelFactory
import ru.samtakoy.core.presentation.cards.result.vm.CardsViewResultViewModelImpl
import ru.samtakoy.core.presentation.cards.types.CardViewMode
import ru.samtakoy.core.presentation.schedule.ScheduleEditFragment
import ru.samtakoy.core.presentation.schedule.ScheduleEditFragment.Companion.newFragment
import ru.samtakoy.core.presentation.showDialogFragment
import java.text.MessageFormat
import javax.inject.Inject

class CardsViewResultFragment : Fragment(), ViewModelOwner {
    private var mViewedCardsText: TextView? = null
    private var mErrorCardsText: TextView? = null
    private var mScheduleLabelText: TextView? = null
    private var mScheduleBtn: Button? = null
    private var mOkBtn: Button? = null

    @Inject
    internal lateinit var viewModelFactory: CardsViewResultViewModelFactory.Factory
    private val viewModel: CardsViewResultViewModelImpl by viewModels {
        viewModelFactory.create(
            viewItemId = requireArguments().getLong(ARG_VIEW_ITEM_ID, -1),
            cardViewMode = requireArguments().getSerializable(ARG_VIEW_MODE) as CardViewMode
        )
    }
    override fun getViewModel(): AbstractViewModel = viewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        Di.appComponent.inject(this)

        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(R.layout.fragment_cards_view_result, container, false)

        mViewedCardsText = v.findViewById<TextView>(R.id.viewed_cards_text)
        mErrorCardsText = v.findViewById<TextView>(R.id.err_cards_text)

        mScheduleLabelText = v.findViewById<TextView>(R.id.schedule_label)
        mScheduleBtn = v.findViewById<Button>(R.id.schedule_btn)
        mScheduleBtn!!.setOnClickListener(
            View.OnClickListener { view: View? ->
                viewModel.onEvent(Event.ScheduleBtnClick)
            }
        )

        mOkBtn = v.findViewById<Button>(R.id.ok_btn)
        mOkBtn!!.setOnClickListener(
            View.OnClickListener { view: View? ->
                viewModel.onEvent(Event.OkBtnClick)
            }
        )

        return v
    }

    override fun onObserveViewModel() {
        super.onObserveViewModel()
        viewModel.getViewActionsAsFlow().observe(viewLifecycleOwner, ::onAction)
        viewModel.getViewStateAsFlow().observe(viewLifecycleOwner, ::onViewState)
    }

    private fun onAction(action: Action) {
        when (action) {
            is Action.ResultOk -> {
                (getParentFragment() as CardsViewResultPresenter.Callbacks?)
                    ?.onResultOk(action.newSchedule)
            }
            is Action.ShowErrorMessage -> showError(action.message)
            is Action.ShowScheduleEditDialog -> showScheduleEditDialog(action.schedule)
        }
    }

    private fun onViewState(state: State) {
        setLearnView(state.isLearnView)
        setViewedCardsCount(state.viewedCardsCount)
        setErrorCardsCount(state.errorCardsCount)
        mScheduleBtn!!.setText(state.newSchedule)
    }

    private fun setLearnView(value: Boolean) {
        val visible = if (value) View.INVISIBLE else View.VISIBLE
        mErrorCardsText!!.setVisibility(visible)
        mScheduleLabelText!!.setVisibility(visible)
        mScheduleBtn!!.setVisibility(visible)
    }

    private fun setViewedCardsCount(count: Int) {
        mViewedCardsText!!.setText(
            MessageFormat.format(
                getResources().getString(R.string.cards_view_res_viewed_cards),
                count
            )
        )
    }

    private fun setErrorCardsCount(count: Int) {
        mErrorCardsText!!.setText(
            MessageFormat.format(getResources().getString(R.string.cards_view_res_err_cards), count)
        )
    }

    private fun showError(string: String) {
        Toast.makeText(getContext(), string, Toast.LENGTH_SHORT).show()
    }

    private fun showScheduleEditDialog(schedule: Schedule?) {
        val dialog = newFragment(schedule)
        dialog.setTargetFragment(this, REQ_SCHEDULE_EDIT)
        showDialogFragment(dialog, this, ScheduleEditFragment.TAG)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQ_SCHEDULE_EDIT && resultCode == Activity.RESULT_OK) {
            viewModel.onEvent(
                Event.NewScheduleDialogResult(
                    serializedSchedule = data?.getStringExtra(
                        ScheduleEditFragment.RESULT_SCHEDULE_STRING
                    )
                )
            )
        }
    }

    companion object {
        private const val ARG_VIEW_ITEM_ID = "ARG_VIEW_ITEM_ID"
        private const val ARG_VIEW_MODE = "ARG_VIEW_MODE"

        private const val REQ_SCHEDULE_EDIT = 1

        fun newFragment(viewItemId: Long, viewMode: CardViewMode?): CardsViewResultFragment {
            val result = CardsViewResultFragment()
            val args = Bundle()
            args.putLong(ARG_VIEW_ITEM_ID, viewItemId)
            args.putSerializable(ARG_VIEW_MODE, viewMode)
            result.setArguments(args)
            return result
        }
    }
}
