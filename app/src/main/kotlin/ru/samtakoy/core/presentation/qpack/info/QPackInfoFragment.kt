package ru.samtakoy.core.presentation.qpack.info

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.view.animation.DecelerateInterpolator
import android.widget.Button
import android.widget.CheckBox
import android.widget.CompoundButton
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import android.widget.ToggleButton
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.transition.AutoTransition
import androidx.transition.TransitionManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetBehavior.BottomSheetCallback
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import ru.samtakoy.R
import ru.samtakoy.core.presentation.RouterHolder
import ru.samtakoy.core.presentation.cards.CardsViewFragment.Companion.buildBundle
import ru.samtakoy.core.presentation.cards.types.CardViewMode
import ru.samtakoy.core.presentation.courses.CourseEditDialogFragment
import ru.samtakoy.core.presentation.courses.info.CourseInfoFragment
import ru.samtakoy.core.presentation.courses.list.CoursesListFragment.Companion.buildBundle
import ru.samtakoy.core.presentation.courses.select.SelectCourseDialogFragment
import ru.samtakoy.core.presentation.courses.select.SelectCourseDialogFragment.Companion.newFragment
import ru.samtakoy.core.presentation.qpack.info.CardViewingTypeSelector.CardViewingType
import ru.samtakoy.core.presentation.qpack.info.CardViewingTypeSelector.CardViewingTypeSelectorListener
import ru.samtakoy.core.presentation.qpack.info.vm.QPackInfoViewModel
import ru.samtakoy.core.presentation.qpack.info.vm.QPackInfoViewModel.Action
import ru.samtakoy.core.presentation.qpack.info.vm.QPackInfoViewModel.Event
import ru.samtakoy.core.presentation.qpack.info.vm.QPackInfoViewModel.State
import ru.samtakoy.core.presentation.qpack.info.vm.QPackInfoViewModelImpl
import ru.samtakoy.core.presentation.showDialogFragment
import ru.samtakoy.presentation.base.observe
import ru.samtakoy.presentation.base.viewmodel.AbstractViewModel
import ru.samtakoy.presentation.base.viewmodel.ViewModelOwner
import kotlin.math.max

class QPackInfoFragment : Fragment(), CardViewingTypeSelectorListener, ViewModelOwner {

    private val viewModel: QPackInfoViewModel by viewModel<QPackInfoViewModelImpl> {
        parametersOf(requireArguments().getLong(ARG_QPACK_ID, -1))
    }
    override fun getViewModel(): AbstractViewModel = viewModel

    private var mQPackTitle: TextView? = null
    private var mQPackCardsCount: TextView? = null

    private var mViewCardsBtn: Button? = null
    private var mUncompletedViewBtn: Button? = null
    private var mAddToNewCourseBtn: Button? = null
    private var mAddToCourseBtn: Button? = null
    private var mViewCoursesBtn: Button? = null
    private var mFavoriteCheckBox: CheckBox? = null

    private var mConstraintLayout: ConstraintLayout? = null

    private var mBottomSheetBehavior: BottomSheetBehavior<*>? = null
    private var mLinearLayoutBSheet: LinearLayout? = null
    private var mTbUpDown: ToggleButton? = null
    private var mCardsFastViewList: RecyclerView? = null
    private var mCardsFastViewAdapter: CardsFastViewAdapter? = null

    private var mBottomSheetCallback: BottomSheetCallback? = null

    private var mRouterHolder: RouterHolder? = null

    private val mCheckListener: CompoundButton.OnCheckedChangeListener =
        object : CompoundButton.OnCheckedChangeListener {
            override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
                viewModel.onEvent(Event.FavoriteChange(isChecked))
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        mRouterHolder = context as RouterHolder
    }

    override fun onDetach() {
        mRouterHolder = null

        super.onDetach()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(R.layout.fragment_qpack_info, container, false)

        initBottomSheet(v)
        initAnimationContainers(v)

        mQPackTitle = v.findViewById<TextView>(R.id.qpack_title)
        mQPackCardsCount = v.findViewById<TextView>(R.id.qpack_cards_count)

        mViewCardsBtn = v.findViewById<Button>(R.id.qpack_btn_view_cards)
        mViewCardsBtn!!.setOnClickListener(
            View.OnClickListener { view: View? ->
                viewModel.onEvent(Event.ViewPackCards)
            }
        )

        mUncompletedViewBtn = v.findViewById<Button>(R.id.qpack_btn_view_uncompleted)
        mUncompletedViewBtn!!.setOnClickListener(
            View.OnClickListener { view: View? ->
                viewModel.onEvent(Event.ViewUncompletedClick)
            }
        )

        mAddToNewCourseBtn = v.findViewById<Button>(R.id.qpack_btn_add_to_new_course)
        mAddToNewCourseBtn!!.setOnClickListener(
            View.OnClickListener { view: View? ->
                viewModel.onEvent(Event.AddToNewCourse)
            }
        )

        mAddToCourseBtn = v.findViewById<Button>(R.id.qpack_btn_add_to_course)
        mAddToCourseBtn!!.setOnClickListener(
            View.OnClickListener { view: View? ->
                viewModel.onEvent(Event.AddToExistsCourse)
            }
        )

        mViewCoursesBtn = v.findViewById<Button>(R.id.qpack_btn_view_courses)
        mViewCoursesBtn!!.setOnClickListener(
            View.OnClickListener { view: View? ->
                viewModel.onEvent(Event.ShowPackCourses)
            }
        )

        mFavoriteCheckBox = v.findViewById<CheckBox>(R.id.qpack_favorite_check_box)
        mFavoriteCheckBox!!.setOnCheckedChangeListener(mCheckListener)

        if (savedInstanceState == null) {
            prepareAnim()
            Handler().post(Runnable { startAnim() })
        }

        return v
    }

    private fun prepareAnim() {
        val cs = ConstraintSet()
        cs.clone(getContext(), R.layout.fragment_qpack_info_frame1)
        cs.applyTo(mConstraintLayout)
    }

    private fun startAnim() {
        val cs = ConstraintSet()
        cs.clone(getContext(), R.layout.fragment_qpack_info_frame2)

        val transition = AutoTransition()
        transition.setDuration(300)
        transition.setInterpolator(DecelerateInterpolator())

        TransitionManager.beginDelayedTransition(mConstraintLayout!!, transition)
        cs.applyTo(mConstraintLayout)
    }

    private fun initAnimationContainers(v: View) {
        //mExternalContainer = v.findViewById(R.id.externalContainer);
        mConstraintLayout = v.findViewById<ConstraintLayout>(R.id.innerContainer)
    }

    override fun onDestroyView() {
        if (mBottomSheetCallback != null) {
            mBottomSheetBehavior!!.removeBottomSheetCallback(mBottomSheetCallback!!)
            mBottomSheetCallback = null
        }

        super.onDestroyView()
    }

    override fun onObserveViewModel() {
        super.onObserveViewModel()
        viewModel.getViewActionsAsFlow().observe(viewLifecycleOwner, ::onAction)
        viewModel.getViewStateAsFlow().observe(viewLifecycleOwner, ::onViewState)
    }

    private fun onAction(action : Action) {
        when (action) {
            Action.OpenCardsInBottomList -> openCardsInBottomList()
            is Action.RequestNewCourseCreation -> requestNewCourseCreation(action.title)
            is Action.RequestsSelectCourseToAdd -> requestsSelectCourseToAdd(action.qPackId)
            is Action.ShowErrorMessage -> showMessage(action.message)
            Action.ShowLearnCourseCardsViewingType -> showLearnCourseCardsViewingType()
            QPackInfoViewModel.NavigationAction.CloseScreen -> closeScreen()
            is QPackInfoViewModel.NavigationAction.NavigateToCardsView -> navigateToCardsView(action.viewItemId)
            is QPackInfoViewModel.NavigationAction.NavigateToPackCourses -> navigateToPackCourses(action.qPackId)
            is QPackInfoViewModel.NavigationAction.ShowCourseScreen -> showCourseScreen(action.courseId)
        }
    }

    private fun onViewState(state: State) {
        mQPackTitle!!.setText(state.title)
        mQPackCardsCount!!.setText(state.cardsCountText)
        mFavoriteCheckBox!!.setOnCheckedChangeListener(null)
        mFavoriteCheckBox!!.setChecked(state.isFavoriteChecked)
        mFavoriteCheckBox!!.setOnCheckedChangeListener(mCheckListener)
        if (state.uncompletedButton == null || state.uncompletedButton.isEmpty()) {
            mUncompletedViewBtn!!.setVisibility(View.GONE)
        } else {
            mUncompletedViewBtn!!.setVisibility(View.VISIBLE)
            mUncompletedViewBtn!!.setText(state.uncompletedButton)
        }
        setFastCardsViewAdapter()
        when (val cardsState = state.fastCards) {
            is State.CardsState.Data -> {
                mCardsFastViewAdapter!!.setCards(cardsState.cards)
            }
            State.CardsState.NotInit -> {
                mCardsFastViewAdapter!!.setCards(emptyList())
            }
        }
    }

    private fun initBottomSheet(v: View) {
        mLinearLayoutBSheet = v.findViewById<LinearLayout>(R.id.bottomSheet)
        mBottomSheetBehavior = BottomSheetBehavior.from<LinearLayout>(mLinearLayoutBSheet!!)
        mTbUpDown = v.findViewById<ToggleButton>(R.id.toggleButton)
        mCardsFastViewList = v.findViewById<RecyclerView>(R.id.list)

        mTbUpDown!!.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { compoundButton: CompoundButton?, isChecked: Boolean ->
            if (isChecked) {
                mBottomSheetBehavior!!.setState(BottomSheetBehavior.STATE_EXPANDED)
            } else {
                mBottomSheetBehavior!!.setState(BottomSheetBehavior.STATE_COLLAPSED)
            }
        })

        mBottomSheetBehavior!!.addBottomSheetCallback(object : BottomSheetCallback() {
            private var mFirstTimeOpened = false

            override fun onStateChanged(bottomSheet: View, newState: Int) {
                if (newState == BottomSheetBehavior.STATE_EXPANDED) {
                    mTbUpDown!!.setChecked(true)
                } else {
                    mTbUpDown!!.setChecked(false)
                }

                if (newState == BottomSheetBehavior.STATE_HIDDEN || newState == BottomSheetBehavior.STATE_COLLAPSED) {
                    setInnerContainerPercent(1f)
                } else if (!mFirstTimeOpened) {
                    mFirstTimeOpened = true
                    onFirstTimeOpen()
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                setInnerContainerPercent(1 - slideOffset)
            }

            fun onFirstTimeOpen() {
                viewModel.onEvent(Event.CardsFastView)
            }
        }.also { mBottomSheetCallback = it })
    }

    private fun setInnerContainerPercent(percent: Float) {
        val invisiblePercent = 0.1f
        val alpha = max(((percent - invisiblePercent) / (1 - invisiblePercent)).toDouble(), 0.0).toFloat()
        mQPackTitle!!.setAlpha(alpha)
        mQPackCardsCount!!.setAlpha(alpha)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.qpack_info, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.getItemId()) {
            R.id.menu_item_delete -> {
                viewModel.onEvent(Event.DeletePack)
                return true
            }
            R.id.menu_item_add_fake_card -> {
                viewModel.onEvent(Event.AddFakeCard)
                return true
            }
        }
        return false
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            AREQUEST_NEW_COURSE -> if (resultCode == Activity.RESULT_OK) {
                val courseName = data?.getStringExtra(CourseEditDialogFragment.RESULT_EXTRA_TEXT)!!
                viewModel.onEvent(Event.NewCourseCommit(courseName))
            }
            AREQUEST_SELECT_COURSE_TO_ADD_TO -> if (resultCode == Activity.RESULT_OK) {
                val courseId = data?.getLongExtra(SelectCourseDialogFragment.RESULT_EXTRA_COURSE_ID, -1)!!
                viewModel.onEvent(Event.AddCardsToCourseCommit(courseId))
            }
        }
    }

    private fun showCourseScreen(courseId: Long) {
        mRouterHolder!!.navController.navigate(R.id.courseInfoFragment, CourseInfoFragment.buildBundle(courseId))
    }

    private fun showMessage(message: String) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show()
    }

    private fun closeScreen() {
        mRouterHolder!!.navController.navigateUp()
    }

    private fun navigateToPackCourses(qPackId: Long) {
        mRouterHolder!!.navController.navigate(
            R.id.coursesListFragment,
            buildBundle(qPackId, null, null)
        )
    }

    private fun requestNewCourseCreation(title: String) {
        val dialog = CourseEditDialogFragment.newDialog(title)
        dialog.setTargetFragment(this, AREQUEST_NEW_COURSE)
        showDialogFragment(dialog, this, TAG)
    }

    private fun requestsSelectCourseToAdd(qPackId: Long) {
        val dialog: DialogFragment = newFragment(
            qPackId, this, AREQUEST_SELECT_COURSE_TO_ADD_TO
        )
        showDialogFragment(dialog, this, "SelectCourseDialogFragment")
    }

    private fun showLearnCourseCardsViewingType() {
        val dialogFragment = CardViewingTypeSelector.newFragment(this, AREQUEST_SELECT_VIEWING_TYPE)
        showDialogFragment(dialogFragment, this, "CardViewingTypeSelector")
    }

    private fun navigateToCardsView(viewItemId: Long) {
        mRouterHolder!!.navController.navigate(
            R.id.action_qPackInfoFragment_to_cardsViewFragment,
            buildBundle(
                viewItemId,
                CardViewMode.LEARNING
            )
        )
    }

    private fun openCardsInBottomList() {
        mBottomSheetBehavior!!.setState(BottomSheetBehavior.STATE_EXPANDED)
    }

    override fun OnCardViewingTypeSelect(type: CardViewingType?) {
        when (type) {
            CardViewingType.RANDOM -> {
                viewModel.onEvent(Event.ViewPackCardsRandomly)
            }
            CardViewingType.SIMPLE -> {
                viewModel.onEvent(Event.ViewPackCardsOrdered)
            }
            CardViewingType.LIST -> {
                viewModel.onEvent(Event.ViewPackCardsInList)
            }
            else -> Unit
        }
    }

    private fun setFastCardsViewAdapter() {
        if (mCardsFastViewAdapter == null) {
            mCardsFastViewAdapter = CardsFastViewAdapter()
            mCardsFastViewList!!.setHasFixedSize(false)
            mCardsFastViewList!!.setAdapter(mCardsFastViewAdapter)
            mCardsFastViewList!!.setLayoutManager(LinearLayoutManager(getContext()))
        }
    }

    companion object {
        private const val TAG = "QPackInfoFragment"

        private const val AREQUEST_NEW_COURSE = 1
        private const val AREQUEST_SELECT_COURSE_TO_ADD_TO = 2
        private const val AREQUEST_SELECT_VIEWING_TYPE = 3

        private const val ARG_QPACK_ID = "ARG_QPACK_ID"

        fun createFragment(qPackId: Long?): QPackInfoFragment {
            val result = QPackInfoFragment()
            result.setArguments(buildBundle(qPackId))
            return result
        }

        @JvmStatic fun buildBundle(qPackId: Long?): Bundle {
            val args = Bundle()
            args.putSerializable(ARG_QPACK_ID, qPackId)
            return args
        }
    }
}
