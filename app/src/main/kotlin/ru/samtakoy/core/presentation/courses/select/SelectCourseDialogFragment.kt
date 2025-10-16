package ru.samtakoy.core.presentation.courses.select

import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatDialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import ru.samtakoy.R
import ru.samtakoy.core.presentation.courses.model.CoursesAdapter
import ru.samtakoy.core.presentation.courses.model.CoursesAdapter.CourseClickListener
import ru.samtakoy.core.presentation.courses.select.vm.SelectCourseViewModel
import ru.samtakoy.core.presentation.courses.select.vm.SelectCourseViewModel.Action
import ru.samtakoy.core.presentation.courses.select.vm.SelectCourseViewModel.State
import ru.samtakoy.core.presentation.courses.select.vm.SelectCourseViewModelImpl
import ru.samtakoy.core.presentation.qpack.info.QPackInfoFragment
import ru.samtakoy.presentation.base.observe
import ru.samtakoy.presentation.base.viewmodel.AbstractViewModel
import ru.samtakoy.presentation.base.viewmodel.ViewModelOwner

/**
 * TODO это все устарело и будет переписано (удалено)
 * */
class SelectCourseDialogFragment : AppCompatDialogFragment(), ViewModelOwner {

    private val viewModel: SelectCourseViewModel by viewModel<SelectCourseViewModelImpl> {
        parametersOf(
            // targetQPackId
            requireArguments().getSerializable(ARG_TARGET_QPACK_ID) as? Long
        )
    }
    override fun getViewModel(): AbstractViewModel = viewModel

    private var mCoursesIsEmptyLabel: TextView? = null
    private var mCoursesRecycler: RecyclerView? = null
    private var mCoursesAdapter: CoursesAdapter? = null

    private fun initView(v: View) {
        mCoursesIsEmptyLabel = v.findViewById<TextView>(R.id.courses_is_empty_label)

        mCoursesAdapter = CoursesAdapter(
            clickListener = CourseClickListener {
                viewModel.onEvent(SelectCourseViewModel.Event.CourseClick(it))
            }
        )

        mCoursesRecycler = v.findViewById<RecyclerView>(R.id.courses_list_recycler)
        mCoursesRecycler!!.setLayoutManager(LinearLayoutManager(getActivity()))
        mCoursesRecycler!!.setAdapter(mCoursesAdapter)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialogView = LayoutInflater.from(getContext()).inflate(R.layout.fragment_courses, null)
        initView(dialogView)

        return AlertDialog.Builder(getContext())
            .setTitle(R.string.courses_list_select_title)
            .setView(dialogView)
            .setNegativeButton(
                R.string.btn_cancel,
                DialogInterface.OnClickListener { dialogInterface: DialogInterface?, i: Int -> exitCanceled() })
            .create()
    }

    override fun onObserveViewModel() {
        super.onObserveViewModel()
        viewModel.getViewActionsAsFlow().observe(viewLifecycleOwner, ::onAction)
        viewModel.getViewStateAsFlow().observe(viewLifecycleOwner, ::onViewState)
    }

    private fun onAction(action: Action) {
        when (action) {
            Action.ExitCanceled -> exitCanceled()
            is Action.ExitOk -> exitOk(action.courseId)
            is Action.ShowErrorMessage -> showError(action.message)
        }
    }

    private fun onViewState(state: State) {
        mCoursesAdapter!!.setCurCourses(state.curCourses)
        updateListVisibility(state.curCourses.isNotEmpty())
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

    private fun exitCanceled() {
        if (getTargetFragment() != null) {
            val result = Intent()
            getTargetFragment()!!.onActivityResult(getTargetRequestCode(), Activity.RESULT_CANCELED, result)
        }
        dismiss()
    }

    private fun exitOk(courseId: Long) {
        if (getTargetFragment() != null) {
            val result = Intent()
            result.putExtra(RESULT_EXTRA_COURSE_ID, courseId)
            getTargetFragment()!!.onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, result)
        }

        dismiss()
    }

    companion object {
        private const val ARG_TARGET_QPACK_ID = "ARG_TARGET_QPACK_ID"

        const val RESULT_EXTRA_COURSE_ID: String = "RESULT_EXTRA_COURSE_ID"

        @JvmStatic fun newFragment(
            targetQPackId: Long?,
            targetFragment: QPackInfoFragment,
            requestCode: Int
        ): SelectCourseDialogFragment {
            val result = SelectCourseDialogFragment()
            val args = Bundle()
            args.putSerializable(ARG_TARGET_QPACK_ID, targetQPackId)
            result.setArguments(args)
            result.setTargetFragment(targetFragment, requestCode)
            return result
        }
    }
}
