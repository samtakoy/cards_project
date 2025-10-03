package ru.samtakoy.core.presentation.courses.list

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.apache.commons.lang3.ArrayUtils
import ru.samtakoy.R
import ru.samtakoy.core.app.di.Di
import ru.samtakoy.core.data.local.database.room.entities.types.LearnCourseMode
import ru.samtakoy.core.presentation.RouterHolder
import ru.samtakoy.core.presentation.base.observe
import ru.samtakoy.core.presentation.base.viewmodel.AbstractViewModel
import ru.samtakoy.core.presentation.base.viewmodel.ViewModelOwner
import ru.samtakoy.core.presentation.courses.CourseEditDialogFragment
import ru.samtakoy.core.presentation.courses.info.CourseInfoFragment.Companion.buildBundle
import ru.samtakoy.core.presentation.courses.list.vm.CoursesListViewModel
import ru.samtakoy.core.presentation.courses.list.vm.CoursesListViewModel.Action
import ru.samtakoy.core.presentation.courses.list.vm.CoursesListViewModel.Event
import ru.samtakoy.core.presentation.courses.list.vm.CoursesListViewModel.State
import ru.samtakoy.core.presentation.courses.list.vm.CoursesListViewModelFactory
import ru.samtakoy.core.presentation.courses.list.vm.CoursesListViewModelImpl
import ru.samtakoy.core.presentation.courses.model.CourseItemUiModel
import ru.samtakoy.core.presentation.courses.model.CoursesAdapter
import ru.samtakoy.core.presentation.courses.model.CoursesAdapter.CourseClickListener
import ru.samtakoy.core.presentation.export_cards.BatchExportDialogFragment
import ru.samtakoy.core.presentation.showDialogFragment
import javax.inject.Inject

class CoursesListFragment : Fragment(), CourseClickListener, ViewModelOwner {
    private var mCoursesIsEmptyLabel: TextView? = null
    private var mCoursesRecycler: RecyclerView? = null
    private var mCoursesAdapter: CoursesAdapter? = null

    private var mRouterHolder: RouterHolder? = null

    private val menuProvider = object : MenuProvider {

        private var isMenuItemAddVisible: Boolean = false

        override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
            menuInflater.inflate(R.menu.fragment_courses, menu)
        }

        override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
            when (menuItem.getItemId()) {
                R.id.menu_item_add -> {
                    viewModel.onEvent(Event.AddCourseRequestClick)
                    return true
                }
                R.id.menu_item_send_courses -> {
                    viewModel.onEvent(Event.BatchExportToEmailClick)
                    return true
                }
            }
            return false
        }

        override fun onPrepareMenu(menu: Menu) {
            menu.findItem(R.id.menu_item_add)?.let { menuItem ->
                menuItem.setVisible(isMenuItemAddVisible)
            }
        }

        fun setIsMenuItemAddVisible(value: Boolean, activity: FragmentActivity) {
            if (value != isMenuItemAddVisible) {
                isMenuItemAddVisible = value
                activity.invalidateMenu()
            }
        }
    }

    @Inject
    internal lateinit var viewModelFactory: CoursesListViewModelFactory.Factory
    private val viewModel: CoursesListViewModelImpl by viewModels {
        viewModelFactory.create(
            targetQPackId = readTargetPackId(),
            targetModes = readTargetModes(),
            targetCourseIds = readTargetCourseIds()
        )
    }
    override fun getViewModel(): AbstractViewModel = viewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        Di.appComponent.inject(this)
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v = inflater.inflate(R.layout.fragment_courses, container, false)
        initViews(v)
        return v
    }

    private fun initViews(v: View) {
        mCoursesIsEmptyLabel = v.findViewById<TextView>(R.id.courses_is_empty_label)

        mCoursesAdapter = CoursesAdapter(this)

        mCoursesRecycler = v.findViewById<RecyclerView>(R.id.courses_list_recycler)
        mCoursesRecycler!!.setLayoutManager(LinearLayoutManager(getActivity()))
        mCoursesRecycler!!.setAdapter(mCoursesAdapter)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mRouterHolder = context as RouterHolder
    }

    override fun onDetach() {
        super.onDetach()
        mRouterHolder = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().addMenuProvider(menuProvider, viewLifecycleOwner)
    }

    override fun onDestroyView() {
        requireActivity().removeMenuProvider(menuProvider)
        super.onDestroyView()
    }

    override fun onObserveViewModel() {
        super.onObserveViewModel()
        viewModel.getViewActionsAsFlow().observe(viewLifecycleOwner, ::onAction)
        viewModel.getViewStateAsFlow().observe(viewLifecycleOwner, ::onViewState)
    }

    private fun onAction(action: Action) {
        when (action) {
            is Action.ShowAddCourseDialog -> showAddCourseDialog(action.defaultTitle)
            Action.ShowBatchExportToEmailDialog -> showBatchExportToEmailDialog()
            is Action.ShowErrorMessage -> showError(action.message)
            is CoursesListViewModel.NavigationAction.NavigateToCourseInfo -> navigateToCourseInfo(action.courseId)
        }
    }

    private fun onViewState(state: State) {
        menuProvider.setIsMenuItemAddVisible(state.isMenuItemAddVisible, requireActivity())
        mCoursesAdapter!!.setCurCourses(state.curCourses)
        updateListVisibility(state.curCourses.isNotEmpty())
    }

    private fun showBatchExportToEmailDialog() {
        val dialog = BatchExportDialogFragment.newCoursesFragment(
            targetFragment = this,
            targetReqCode = REQ_CODE_EXPORT_COURSES
        )
        showDialogFragment(dialog, this, TAG_DIALOG_EXPORT_COURSES)
    }

    private fun showAddCourseDialog(defaultTitle: String) {
        val dialog = CourseEditDialogFragment.newDialog(defaultTitle)
        dialog.setTargetFragment(this, REQ_CODE_ADD_COURSE)
        showDialogFragment(dialog, this, TAG_DIALOG_ADD_COURSE)
    }

    private fun showError(message: String) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show()
    }

    private fun updateListVisibility(coursesIsVisible: Boolean) {
        if (coursesIsVisible) {
            mCoursesRecycler!!.setVisibility(View.VISIBLE)
            mCoursesIsEmptyLabel!!.setVisibility(View.GONE)
        } else {
            mCoursesRecycler!!.setVisibility(View.GONE)
            mCoursesIsEmptyLabel!!.setVisibility(View.VISIBLE)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            REQ_CODE_ADD_COURSE -> {
                val courseTitle = data?.getStringExtra(CourseEditDialogFragment.RESULT_EXTRA_TEXT)
                viewModel.onEvent(Event.AddNewCourseConfirm(courseTitle))
            }
        }
    }

    private fun navigateToCourseInfo(courseId: Long) {
        mRouterHolder!!.getNavController().navigate(
            R.id.action_coursesListFragment_to_courseInfoFragment,
            buildBundle(courseId)
        )
    }

    override fun onCourseClick(course: CourseItemUiModel) {
        viewModel.onEvent(Event.CourseClick(course))
    }

    private fun readTargetPackId(): Long? {
        val result = requireArguments().getSerializable(ARG_TARGET_QPACK_ID) as Long?
        return if (result == 0L) null else result
    }

    private fun readTargetModes(): List<LearnCourseMode>? {
        val result = requireArguments().getIntArray(ARG_TARGET_MODES)
        return LearnCourseMode.primitiveArrayToList(result)
    }

    private fun readTargetCourseIds(): Array<Long>? {
        return ArrayUtils.toObject(requireArguments().getLongArray(ARG_TARGET_COURSE_IDS))
    }

    // +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    // +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

    companion object {
        private const val ARG_TARGET_QPACK_ID = "ARG_TARGET_QPACK_ID"
        private const val ARG_TARGET_MODES = "ARG_TARGET_MODES"
        private const val ARG_TARGET_COURSE_IDS = "ARG_TARGET_COURSE_IDS"

        private const val REQ_CODE_ADD_COURSE = 1
        private const val REQ_CODE_EXPORT_COURSES = 2
        private const val TAG_DIALOG_ADD_COURSE = "TAG_DIALOG_ADD_COURSE"
        private const val TAG_DIALOG_EXPORT_COURSES = "TAG_DIALOG_EXPORT_COURSES"

        fun newFragment(
            targetQPackId: Long?,
            targetModes: List<LearnCourseMode>?,
            targetCourseIds: Array<Long>?
        ): CoursesListFragment {
            val result = CoursesListFragment()
            result.setArguments(buildBundle(targetQPackId!!, targetModes, targetCourseIds))
            return result
        }

        @JvmStatic
        fun buildBundle(
            targetQPackId: Long?,
            targetModes: List<LearnCourseMode>?,
            targetCourseIds: Array<Long>?
        ): Bundle {
            val args = Bundle()
            args.putSerializable(ARG_TARGET_QPACK_ID, targetQPackId)
            args.putIntArray(ARG_TARGET_MODES, LearnCourseMode.listToPrimitiveArray(targetModes))
            args.putLongArray(ARG_TARGET_COURSE_IDS, ArrayUtils.toPrimitive(targetCourseIds))
            return args
        }
    }
}
