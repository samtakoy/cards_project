package ru.samtakoy.core.presentation.qpack.list

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import android.widget.Toast
import androidx.appcompat.widget.AppCompatCheckBox
import androidx.appcompat.widget.SearchView
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.samtakoy.R
import ru.samtakoy.core.presentation.RouterHolder
import ru.samtakoy.core.presentation.qpack.list.QPacksListAdapter.ItemClickListener
import ru.samtakoy.core.presentation.qpack.list.model.QPackListItemUiModel
import ru.samtakoy.core.presentation.qpack.list.model.QPackSortType
import ru.samtakoy.core.presentation.qpack.list.vm.QPacksListViewModel
import ru.samtakoy.core.presentation.qpack.list.vm.QPacksListViewModel.Action
import ru.samtakoy.core.presentation.qpack.list.vm.QPacksListViewModel.Event
import ru.samtakoy.core.presentation.qpack.list.vm.QPacksListViewModel.NavAction
import ru.samtakoy.core.presentation.qpack.list.vm.QPacksListViewModel.State
import ru.samtakoy.core.presentation.qpack.list.vm.QPacksListViewModelImpl
import ru.samtakoy.presentation.base.observe
import ru.samtakoy.presentation.base.viewmodel.AbstractViewModel
import ru.samtakoy.presentation.base.viewmodel.ViewModelOwner

open class QPacksListFragment : Fragment(), ViewModelOwner {

    private var mSearchText: SearchView? = null
    private var mFavoritesCheckBox: AppCompatCheckBox? = null
    private var mAdapter: QPacksListAdapter? = null
    private var mRecyclerView: RecyclerView? = null

    private var mTabLayout: TabLayout? = null
    private var mTabByLastViewDate: TabLayout.Tab? = null
    private var mTabByCreationDate: TabLayout.Tab? = null

    private var mRouterHolder: RouterHolder? = null

    private val viewModel: QPacksListViewModel by viewModel<QPacksListViewModelImpl>()
    override fun getViewModel(): AbstractViewModel = viewModel

    private val mTextListener: SearchView.OnQueryTextListener = object : SearchView.OnQueryTextListener {
        override fun onQueryTextSubmit(query: String): Boolean {
            viewModel.onEvent(Event.SearchTextChange(query))
            return false
        }

        override fun onQueryTextChange(newText: String): Boolean {
            viewModel.onEvent(Event.SearchTextChange(newText))
            return false
        }
    }

    private val mCheckBoxListener =
        CompoundButton.OnCheckedChangeListener { compoundButton: CompoundButton?, b: Boolean ->
            viewModel.onEvent(Event.FavoritesCheckBoxChange(b))
        }

    private val mTabSelectedListener: OnTabSelectedListener = object : OnTabSelectedListener {
        override fun onTabSelected(tab: TabLayout.Tab?) {
            if (tab === mTabByLastViewDate) {
                viewModel.onEvent(Event.SortByLastViewDate)
            } else if (tab === mTabByCreationDate) {
                viewModel.onEvent(Event.SortByCreationDate)
            }
        }

        override fun onTabUnselected(tab: TabLayout.Tab?) {}
        override fun onTabReselected(tab: TabLayout.Tab?) {}
    }

    private val menuProvider = object : MenuProvider {

        private var sortType: QPackSortType? = null

        override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
            menuInflater.inflate(R.menu.fragment_qpacks, menu)
        }

        override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
            when (menuItem.getItemId()) {
                R.id.menu_item_by_last_view_date -> {
                    viewModel.onEvent(Event.SortByLastViewDate)
                    return true
                }
                R.id.menu_item_by_creation_date -> {
                    viewModel.onEvent(Event.SortByCreationDate)
                    return true
                }
            }
            return false
        }

        override fun onPrepareMenu(menu: Menu) {
            super.onPrepareMenu(menu)
            var item: MenuItem? = null

            item = when (sortType) {
                QPackSortType.LAST_VIEW_DATE_ASC -> menu.findItem(R.id.menu_item_by_last_view_date)
                QPackSortType.CREATION_DATE_DESC -> menu.findItem(R.id.menu_item_by_creation_date)
                else -> null
            }
            if (item != null) {
                item.setChecked(true)
            }
        }

        fun setSortType(value: QPackSortType, activity: FragmentActivity) {
            sortType = value
            activity.invalidateMenu()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(R.layout.fragment_qpacks_list, container, false)

        mSearchText = v.findViewById<SearchView>(R.id.search_view)
        mSearchText!!.setOnQueryTextListener(mTextListener)

        mFavoritesCheckBox = v.findViewById<AppCompatCheckBox>(R.id.favorite_check_box)
        mFavoritesCheckBox!!.setOnCheckedChangeListener(mCheckBoxListener)

        mRecyclerView = v.findViewById<RecyclerView>(R.id.recycler_view)
        mRecyclerView!!.addItemDecoration(
            DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL)
        )

        mAdapter = QPacksListAdapter(
            object: ItemClickListener {
                override fun onClick(item: QPackListItemUiModel) {
                    viewModel.onEvent(Event.PackClick(item))
                }
            }
        )
        mRecyclerView!!.setAdapter(mAdapter)
        mRecyclerView!!.setLayoutManager(LinearLayoutManager(getContext()))

        mTabLayout = v.findViewById<TabLayout>(R.id.tab_layout)
        mTabLayout!!.addOnTabSelectedListener(mTabSelectedListener)
        mTabByLastViewDate = mTabLayout!!.getTabAt(0)
        mTabByCreationDate = mTabLayout!!.getTabAt(1)

        return v
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().addMenuProvider(menuProvider, viewLifecycleOwner)
    }

    override fun onDestroyView() {
        mSearchText!!.setOnQueryTextListener(null)
        mTabLayout!!.removeOnTabSelectedListener(mTabSelectedListener)
        mFavoritesCheckBox!!.setOnCheckedChangeListener(null)

        super.onDestroyView()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mRouterHolder = context as RouterHolder
    }

    override fun onDetach() {
        mRouterHolder = null
        super.onDetach()
    }

    override fun onObserveViewModel() {
        super.onObserveViewModel()
        viewModel.getViewActionsAsFlow().observe(viewLifecycleOwner, ::onAction)
        viewModel.getViewStateAsFlow().observe(viewLifecycleOwner, ::onViewState)
    }

    private fun onAction(action: Action) {
        when (action) {
            is Action.ShowErrorMessage -> showError(action.message)
            is NavAction.ShowPackInfo -> showPackInfo(action.qPackId)
            is Action.SearchText -> setSearchText(action.text)
        }
    }

    private fun onViewState(state: State) {
        mAdapter!!.setItems(state.items)
        setFavoritesChecked(state.isFavoritesChecked)
        updateTabsState(state.sortType)
        menuProvider.setSortType(state.sortType, requireActivity())
    }

    private fun setSearchText(text: String) {
        mSearchText!!.setOnQueryTextListener(null)
        mSearchText!!.setQuery(text, false)
        mSearchText!!.setOnQueryTextListener(mTextListener)
    }

    private fun setFavoritesChecked(isChecked: Boolean) {
        mFavoritesCheckBox!!.setOnCheckedChangeListener(null)
        mFavoritesCheckBox!!.setChecked(isChecked)
        mFavoritesCheckBox!!.setOnCheckedChangeListener(mCheckBoxListener)
    }

    private fun showPackInfo(qPackId: Long) {
        /*
        mRouterHolder!!.navController.navigate(
            R.id.action_qPacksListFragment_to_qPackInfoFragment,
            buildBundle(qPackId)
        )*/
    }

    private fun showError(message: String) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show()
    }

    private fun updateTabsState(sortType: QPackSortType) {
        var tab: TabLayout.Tab? = null

        when (sortType) {
            QPackSortType.LAST_VIEW_DATE_ASC -> tab = mTabByLastViewDate
            QPackSortType.CREATION_DATE_DESC -> tab = mTabByCreationDate
        }

        if (tab != null) {
            mTabLayout!!.selectTab(tab)
        }
    }

    companion object {
        fun newInstance(): QPacksListFragment {
            val fragment = QPacksListFragment()
            return fragment
        }
    }
}
