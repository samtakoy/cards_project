package ru.samtakoy.core.presentation.schedule

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import ru.samtakoy.R
import ru.samtakoy.core.app.di.Di
import ru.samtakoy.core.presentation.base.observe
import ru.samtakoy.core.presentation.base.viewmodel.AbstractViewModel
import ru.samtakoy.core.presentation.base.viewmodel.ViewModelOwner
import ru.samtakoy.core.presentation.schedule.vm.ScheduleEditViewModel
import ru.samtakoy.core.presentation.schedule.vm.ScheduleEditViewModel.Event
import ru.samtakoy.core.presentation.schedule.vm.ScheduleEditViewModelFactory
import ru.samtakoy.core.presentation.schedule.vm.ScheduleEditViewModelImpl
import ru.samtakoy.features.learncourse.domain.model.schedule.ScheduleTimeUnit
import ru.samtakoy.features.learncourse.domain.model.schedule.serialize.ParcelableSchedule
import ru.samtakoy.features.learncourse.domain.model.schedule.serialize.toDomainOrEmpty
import javax.inject.Inject

class ScheduleEditFragment : DialogFragment(), ViewModelOwner {
    private var mScheduleView: TextView? = null
    private var mClearScheduleButton: Button? = null

    private var mScheduleCurItemButton: Button? = null
    private var mScheduleCurItemClearButton: Button? = null
    private var mScheduleCurItemAddButton: Button? = null
    private var mScheduleItemButtons: ViewGroup? = null

    @Inject
    internal lateinit var viewModelFactory: ScheduleEditViewModelFactory.Factory
    private val viewModel by viewModels<ScheduleEditViewModelImpl> {
        viewModelFactory.create(
            schedule = (requireArguments().getParcelable(ARG_SCHEDULE_STRING) as? ParcelableSchedule).toDomainOrEmpty()
        )
    }
    override fun getViewModel(): AbstractViewModel = viewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        Di.appComponent.inject(this)
        super.onCreate(savedInstanceState)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val v = layoutInflater.inflate(R.layout.fragment_schedule_edit, null)

        initView(v)

        return AlertDialog.Builder(requireActivity())
            .setView(v)
            .setCancelable(true)
            .setPositiveButton(R.string.btn_ok, object : DialogInterface.OnClickListener {
                override fun onClick(dialogInterface: DialogInterface?, i: Int) {
                    viewModel.onEvent(Event.ConfirmResultClick)
                }
            })
            .create()
    }

    override fun onObserveViewModel() {
        super.onObserveViewModel()
        viewModel.getViewActionsAsFlow().observe(viewLifecycleOwner, ::onAction)
        viewModel.getViewStateAsFlow().observe(viewLifecycleOwner, ::onState)
    }

    private fun onState(state: ScheduleEditViewModel.State) {
        mScheduleView!!.setText(state.schedule)
        mScheduleCurItemButton!!.setText(state.scheduleCurItemButtonText)
    }

    private fun onAction(action: ScheduleEditViewModel.Action) {
        when (action) {
            is ScheduleEditViewModel.Action.CloseWithResult -> {
                trySendResult(action.serializedSchedule)
            }
        }
    }

    private fun initView(v: View) {
        mScheduleView = v.findViewById<TextView?>(R.id.schedule_text)

        mClearScheduleButton = v.findViewById<Button?>(R.id.clear_btn)
        mClearScheduleButton!!.setOnClickListener(
            View.OnClickListener { view: View? ->
                viewModel.onEvent(Event.ClearScheduleClick)
            }
        )

        mScheduleCurItemButton = v.findViewById<Button?>(R.id.item_btn)

        mScheduleCurItemClearButton = v.findViewById<Button?>(R.id.item_clear_btn)
        mScheduleCurItemClearButton!!.setOnClickListener(
            View.OnClickListener { view: View? ->
                viewModel.onEvent(Event.ClearScheduleCurItemClick)
            }
        )

        mScheduleCurItemAddButton = v.findViewById<Button?>(R.id.item_add_btn)
        mScheduleCurItemAddButton!!.setOnClickListener(
            View.OnClickListener { view: View? ->
                viewModel.onEvent(Event.AddScheduleCurItem)
            }
        )

        mScheduleItemButtons = v.findViewById<ViewGroup?>(R.id.schedule_item_btns)

        createScheduleItemButtons(v.findViewById<ViewGroup?>(R.id.schedule_item_btns))
    }

    override fun onDestroyView() {
        mScheduleView = null
        mClearScheduleButton = null
        mScheduleCurItemButton = null
        mScheduleCurItemClearButton = null
        mScheduleCurItemAddButton = null
        mScheduleItemButtons = null

        super.onDestroyView()
    }

    private fun createScheduleItemButtons(container: ViewGroup) {
        val ctx: Context? = getActivity()
        if (container.childCount < ScheduleTimeUnit.entries.size) {
            for (unit in ScheduleTimeUnit.entries) {
                val unitBtn = Button(ctx)
                unitBtn.setText("+1" + getResources().getString(unit.textStringId))
                unitBtn.setOnClickListener(
                    View.OnClickListener { view: View? ->
                        viewModel.onEvent(Event.ScheduleTimeUnitSelect(unit))
                    }
                )
                container.addView(unitBtn)
            }
        }
    }

    private fun trySendResult(serializedSchedule: ParcelableSchedule) {
        if (getTargetFragment() == null) {
            Log.e(TAG, "Target fragment is missing")
            return
        }

        val result = Intent()
        result.putExtra(RESULT_SCHEDULE_STRING, serializedSchedule)
        getTargetFragment()!!.onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, result)
    }

    companion object {
        const val TAG: String = "ScheduleEditFragment"

        private const val ARG_SCHEDULE_STRING = "ARG_SCHEDULE_STRING"
        const val RESULT_SCHEDULE_STRING: String = "RESULT_SCHEDULE_STRING"

        @JvmStatic fun newFragment(schedule: ParcelableSchedule?): ScheduleEditFragment {
            return ScheduleEditFragment().apply {
                arguments = bundleOf(
                    ARG_SCHEDULE_STRING to if (schedule == null) {
                        null
                    } else {
                        schedule
                    }
                )
            }
        }
    }
}
