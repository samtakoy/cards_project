package ru.samtakoy.core.screens.qpacks;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.tabs.TabLayout;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Provider;

import moxy.MvpAppCompatFragment;
import moxy.presenter.InjectPresenter;
import moxy.presenter.ProvidePresenter;
import ru.samtakoy.R;
import ru.samtakoy.core.MyApp;
import ru.samtakoy.core.database.room.entities.QPackEntity;
import ru.samtakoy.core.navigation.RouterHolder;
import ru.samtakoy.core.screens.qpack.QPackInfoFragment;

public class QPacksListFragment extends MvpAppCompatFragment implements QPacksListView{


    public static QPacksListFragment newInstance() {
        QPacksListFragment fragment = new QPacksListFragment();
        //Bundle args = new Bundle();
        //fragment.setArguments(args);
        return fragment;
    }


    private QPacksListAdapter mAdapter;
    private RecyclerView mRecyclerView;

    private TabLayout mTabLayout;
    private TabLayout.Tab mTabByLastViewDate;
    private TabLayout.Tab mTabByCreationDate;


    private RouterHolder mRouterHolder;

    @InjectPresenter
    QPacksListPresenter mPresenter;
    @Inject
    Provider<QPacksListPresenter> mPresenterProvider;

    @ProvidePresenter
    QPacksListPresenter providePresenter() {
        return mPresenterProvider.get();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        MyApp.getInstance().getAppComponent().inject(this);

        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_qpacks_list, container, false);

        mRecyclerView = v.findViewById(R.id.recycler_view);
        mRecyclerView.addItemDecoration(
                new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL)
        );

        mAdapter = new QPacksListAdapter(qPack -> mPresenter.onUiPackClick(qPack));
        mRecyclerView.setAdapter(mAdapter);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        mTabLayout = v.findViewById(R.id.tab_layout);
        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if(tab == mTabByLastViewDate){
                    mPresenter.onUiSortByLastViewDate();
                } else if(tab == mTabByCreationDate){
                    mPresenter.onUiSortByCreationDate();
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}
            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });
        mTabByLastViewDate = mTabLayout.getTabAt(0);
        mTabByCreationDate = mTabLayout.getTabAt(1);


        return v;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mRouterHolder = (RouterHolder) context;
    }

    @Override
    public void onDetach() {
        mRouterHolder = null;
        super.onDetach();
    }

    @Override
    public void setQPacks(List<QPackEntity> items, QPackSortType sortType) {
        mAdapter.setItems(items, sortType);
    }

    @Override
    public void showPackInfo(QPackEntity qPack) {

        mRouterHolder.getNavController().navigate(
                R.id.action_qPacksListFragment_to_qPackInfoFragment,
                QPackInfoFragment.buildBundle(qPack.getId())
        );

        //mRouterHolder.getRouter().navigateTo(new Screens.QPackInfoScreen(qPack.getId()));
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.fragment_qpacks, menu);
    }

    @Override
    public void onPrepareOptionsMenu(@NonNull Menu menu) {

        MenuItem item = null;

        switch (mPresenter.getSortType()){
            case LAST_VIEW_DATE_ASC:
                item = menu.findItem(R.id.menu_item_by_last_view_date);
                break;
            case CREATION_DATE_DESC:
                item = menu.findItem(R.id.menu_item_by_creation_date);
                break;
        }
        if(item != null) {
            item.setChecked(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_item_by_last_view_date:
                mPresenter.onUiSortByLastViewDate();
                return true;
            case R.id.menu_item_by_creation_date:
                mPresenter.onUiSortByCreationDate();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void updateMenuState(QPackSortType sortType) {
        getActivity().invalidateOptionsMenu();

        updateTabsState(sortType);
    }

    @Override
    public void showError(int codeResId) {
        Toast.makeText(getContext(), codeResId, Toast.LENGTH_SHORT).show();
    }

    private void updateTabsState(QPackSortType sortType) {
        TabLayout.Tab tab = null;

        switch (sortType) {

            case LAST_VIEW_DATE_ASC:
                tab = mTabByLastViewDate;
                break;
            case CREATION_DATE_DESC:
                tab = mTabByCreationDate;
                break;
        }

        if(tab != null){
            mTabLayout.selectTab(tab);
        }
    }
}
