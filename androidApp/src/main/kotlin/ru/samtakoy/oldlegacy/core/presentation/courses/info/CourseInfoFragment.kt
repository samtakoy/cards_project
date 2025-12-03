package ru.samtakoy.oldlegacy.core.presentation.courses.info

import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import ru.samtakoy.R
import ru.samtakoy.oldlegacy.core.presentation.DialogHelper
import ru.samtakoy.oldlegacy.core.presentation.RouterHolder
import ru.samtakoy.presentation.cards.view.model.CardViewMode
import ru.samtakoy.oldlegacy.core.presentation.courses.info.vm.CourseInfoViewModel
import ru.samtakoy.oldlegacy.core.presentation.courses.info.vm.CourseInfoViewModel.Event
import ru.samtakoy.oldlegacy.core.presentation.courses.info.vm.CourseInfoViewModelImpl
import ru.samtakoy.oldlegacy.core.presentation.log.LogActivity
import ru.samtakoy.oldlegacy.core.oldutils.observe
import ru.samtakoy.presentation.base.viewmodel.AbstractViewModel
import ru.samtakoy.presentation.base.viewmodel.ViewModelOwner

class CourseInfoFragment : Fragment(), ViewModelOwner {
    // ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    // ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    private var mTitleText: TextView? = null
    private var mCardCountText: TextView? = null
    private var mStatusContentText: TextView? = null
    private var mScheduleButton: Button? = null
    private var mActionButton: Button? = null

    private var mRouterHolder: RouterHolder? = null

    private val viewModel: CourseInfoViewModel by viewModel<CourseInfoViewModelImpl> {
        parametersOf(readLearnCourseId())
    }
    override fun getViewModel(): AbstractViewModel = viewModel

    // ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    // ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

    override fun onObserveViewModel() {
        super.onObserveViewModel()
        viewModel.getViewActionsAsFlow().observe(viewLifecycleOwner, ::onAction)
        viewModel.getViewStateAsFlow().observe(viewLifecycleOwner, ::onViewState)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    private fun readLearnCourseId(): Long {
        return requireArguments().getLong(ARG_LEARN_COURSE_ID, -1)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        mRouterHolder = context as RouterHolder
    }

    override fun onDetach() {
        mRouterHolder = null

        super.onDetach()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)

        inflater.inflate(R.menu.course_info, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.getItemId()) {
            R.id.menu_item_delete -> {
                viewModel.onEvent(CourseInfoViewModel.Event.DeleteCourseClick)
                return true
            }
            R.id.menu_item_log -> {
                startActivity(LogActivity.Companion.newActivityIntent(requireContext()))
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun onAction(action: CourseInfoViewModel.Action) {
        when (action) {
            CourseInfoViewModel.Action.RequestExtraordinaryRepeating -> requestExtraordinaryRepeating()
            is CourseInfoViewModel.Action.ShowErrorMessage -> showError(action.message)
            CourseInfoViewModel.NavigationAction.Exit -> exit()
            is CourseInfoViewModel.NavigationAction.NavigateToCardsViewScreen -> navigateToCardsViewScreen(
                viewHistoryItemId = action.viewHistoryItemId,
                viewMode = action.viewMode
            )
        }
    }

    private fun onViewState(viewState: CourseInfoViewModel.State) {
        when (val type = viewState.type) {
            is CourseInfoViewModel.State.Type.Data -> showLearnCourseInfo(type)
            CourseInfoViewModel.State.Type.Initialization -> Unit
        }
    }

    private fun exit() {
        mRouterHolder!!.navController.navigateUp()
        //mRouterHolder.getRouter().exit();
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(R.layout.fragment_course_info, container, false)

        // android:id="@+id/title"
        mTitleText = v.findViewById<TextView>(R.id.title)
        // android:id="@+id/cards_count"
        mCardCountText = v.findViewById<TextView>(R.id.cards_count)
        // android:id="@+id/status_content"
        mStatusContentText = v.findViewById<TextView>(R.id.status_content)
        // android:id="@+id/schedule"
        mScheduleButton = v.findViewById<Button>(R.id.schedule)
        mScheduleButton!!.setOnClickListener(View.OnClickListener { view: View? -> })
        // android:id="@+id/action_button"
        mActionButton = v.findViewById<Button>(R.id.action_button)
        mActionButton!!.setOnClickListener(
            View.OnClickListener { view: View? ->
                viewModel.onEvent(CourseInfoViewModel.Event.ActionButtonClick)
            }
        )

        return v
    }

    private fun navigateToCardsViewScreen(
        viewHistoryItemId: Long,
        viewMode: CardViewMode
    ) {
        /*
        mRouterHolder!!.navController.navigate(
            R.id.action_courseInfoFragment_to_cardsViewFragment,
            buildBundle(viewHistoryItemId, viewMode)
        )*/
    }

    private fun requestExtraordinaryRepeating() {
        DialogHelper.showYesNoDialog(
            requireContext(),
            getResources().getString(R.string.confirm_dialog_title),
            getResources().getString(R.string.course_info_extra_repeating_confirm),
            DialogInterface.OnClickListener { dialogInterface: DialogInterface?, i: Int ->
                viewModel.onEvent(Event.StartRepeatingExtraConfirm)
            },
            null
        )
    }

    private fun showLearnCourseInfo(dataState: CourseInfoViewModel.State.Type.Data) {
        mTitleText!!.setText(dataState.titleText)
        mCardCountText!!.setText(dataState.cardsCountText)
        mStatusContentText!!.setText(dataState.statusString)
        mScheduleButton!!.setText(dataState.scheduleButtonText)
        mActionButton!!.setText(dataState.actionButtonText)
    }

    private fun showError(string: String) {
        Toast.makeText(getContext(), string, Toast.LENGTH_SHORT).show()
    }

    companion object {
        private const val ARG_LEARN_COURSE_ID = "ARG_LEARN_COURSE_ID"

        fun newFragment(learnCourseId: Long): CourseInfoFragment {
            val result = CourseInfoFragment()
            val args: Bundle = buildBundle(learnCourseId)
            result.setArguments(args)
            return result
        }

        @JvmStatic fun buildBundle(learnCourseId: Long): Bundle {
            val args = Bundle()
            args.putLong(ARG_LEARN_COURSE_ID, learnCourseId)
            return args
        }
    }
}
