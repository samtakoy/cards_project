package ru.samtakoy.core.screens.qpacks;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import moxy.InjectViewState;
import moxy.MvpPresenter;
import ru.samtakoy.core.business.CardsInteractor;
import ru.samtakoy.core.di.components.AppComponent;
import ru.samtakoy.core.model.QPack;

@InjectViewState
public class QPacksListPresenter extends MvpPresenter<QPacksListView> {


    @Inject
    CardsInteractor mCardsInteractor;

    private CompositeDisposable mCompositeDisposable;

    private QPackSortType mSortType;

    public QPacksListPresenter(AppComponent appComponent){

        appComponent.inject(this);

        mSortType = QPackSortType.LAST_VIEW_DATE_ASC;

        mCompositeDisposable = new CompositeDisposable();
        updateData();
    }

    public QPackSortType getSortType() {
        return mSortType;
    }

    private void updateData() {
        final QPackSortType sortType = mSortType;
        mCompositeDisposable.clear();
        mCompositeDisposable.add(
                getAllQPacksSorted(sortType)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(qPacks -> getViewState().setQPacks(qPacks, sortType) )
        );
    }

    private Single<List<QPack>> getAllQPacksSorted(QPackSortType sortType){
        switch (sortType){

            case LAST_VIEW_DATE_ASC:
                return mCardsInteractor.getAllQPacksByLastViewDateAsc();
            case CREATION_DATE_DESC:
                return mCardsInteractor.getAllQPacksByCreationDateDesc();
        }
        return null;
    }

    @Override
    public void onDestroy() {

        mCompositeDisposable.dispose();
        super.onDestroy();
    }

    public void onUiPackClick(QPack qPack) {
        getViewState().showPackInfo(qPack);
    }

    public void onUiSortByCreationDate() {
        mSortType = QPackSortType.CREATION_DATE_DESC;

        getViewState().updateMenuState(mSortType);
        updateData();
    }

    public void onUiSortByLastViewDate() {
        mSortType = QPackSortType.LAST_VIEW_DATE_ASC;

        getViewState().updateMenuState(mSortType);
        updateData();
    }

}
