package ru.samtakoy.oldlegacy.core.presentation.qpack.info

/*
class QPackInfoFragment : Fragment() {

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
            override fun onCheckedChanged(p0: CompoundButton, isChecked: Boolean) {
                // viewModel.onEvent(Event.FavoriteChange(isChecked))
            }
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
                // viewModel.onEvent(Event.ViewPackCards)
            }
        )

        mUncompletedViewBtn = v.findViewById<Button>(R.id.qpack_btn_view_uncompleted)
        mUncompletedViewBtn!!.setOnClickListener(
            View.OnClickListener { view: View? ->
                // viewModel.onEvent(Event.ViewUncompletedClick)
            }
        )

        mAddToNewCourseBtn = v.findViewById<Button>(R.id.qpack_btn_add_to_new_course)
        mAddToNewCourseBtn!!.setOnClickListener(
            View.OnClickListener { view: View? ->
                // viewModel.onEvent(Event.AddToNewCourse)
            }
        )

        mAddToCourseBtn = v.findViewById<Button>(R.id.qpack_btn_add_to_course)
        mAddToCourseBtn!!.setOnClickListener(
            View.OnClickListener { view: View? ->
                // viewModel.onEvent(Event.AddToExistsCourse)
            }
        )

        mViewCoursesBtn = v.findViewById<Button>(R.id.qpack_btn_view_courses)
        mViewCoursesBtn!!.setOnClickListener(
            View.OnClickListener { view: View? ->
                // viewModel.onEvent(Event.ShowPackCourses)
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
        // mConstraintLayout = v.findViewById<ConstraintLayout>(R.id.innerContainer)
    }

    override fun onDestroyView() {

        if (mBottomSheetCallback != null) {
            mBottomSheetBehavior!!.removeBottomSheetCallback(mBottomSheetCallback!!)
            mBottomSheetCallback = null
        }

        super.onDestroyView()

    }

    private fun onAction(action : Action) {
        when (action) {
            Action.OpenCardsInBottomList -> openCardsInBottomList()
            is Action.RequestNewCourseCreation -> requestNewCourseCreation(action.title)
            is Action.RequestsSelectCourseToAdd -> requestsSelectCourseToAdd(action.qPackId)
            is Action.ShowErrorMessage -> showMessage(action.message)
            is Action.ShowLearnCourseCardsViewingType -> showLearnCourseCardsViewingType()
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
        // if (state.uncompletedButton == null || state.uncompletedButton.isEmpty()) {
        if (true) {
            mUncompletedViewBtn!!.setVisibility(View.GONE)
        } else {
            mUncompletedViewBtn!!.setVisibility(View.VISIBLE)
            // mUncompletedViewBtn!!.setText(state.uncompletedButton)
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
                // viewModel.onEvent(Event.CardsFastView)
            }
        }.also { mBottomSheetCallback = it })
    }

    private fun setInnerContainerPercent(percent: Float) {
        val invisiblePercent = 0.1f
        val alpha = max(((percent - invisiblePercent) / (1 - invisiblePercent)).toDouble(), 0.0).toFloat()
        mQPackTitle!!.setAlpha(alpha)
        mQPackCardsCount!!.setAlpha(alpha)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            AREQUEST_NEW_COURSE -> if (resultCode == Activity.RESULT_OK) {
                val courseName = data?.getStringExtra(CourseEditDialogFragment.RESULT_EXTRA_TEXT)!!
                // viewModel.onEvent(Event.NewCourseCommit(courseName))
            }
            AREQUEST_SELECT_COURSE_TO_ADD_TO -> if (resultCode == Activity.RESULT_OK) {
                val courseId = data?.getLongExtra(SelectCourseDialogFragment.RESULT_EXTRA_COURSE_ID, -1)!!
                // viewModel.onEvent(Event.AddCardsToCourseCommit(courseId))
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

        private const val ARG_QPACK_ID = "ARG_QPACK_ID"

        @JvmStatic fun buildBundle(qPackId: Long?): Bundle {
            val args = Bundle()
            args.putSerializable(ARG_QPACK_ID, qPackId)
            return args
        }
    }
}
*/