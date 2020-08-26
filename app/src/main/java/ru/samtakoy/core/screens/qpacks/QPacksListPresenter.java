package ru.samtakoy.core.screens.qpacks;

import org.apache.commons.lang3.exception.ExceptionUtils;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import moxy.InjectViewState;
import moxy.MvpPresenter;
import ru.samtakoy.R;
import ru.samtakoy.core.business.CardsInteractor;
import ru.samtakoy.core.database.room.entities.QPackEntity;
import ru.samtakoy.core.screens.log.MyLog;

@InjectViewState
public class QPacksListPresenter extends MvpPresenter<QPacksListView> {

    @Inject
    CardsInteractor mCardsInteractor;

    private CompositeDisposable mCompositeDisposable;

    private QPackSortType mSortType;

    @Inject
    public QPacksListPresenter() {

        mSortType = QPackSortType.LAST_VIEW_DATE_ASC;

        mCompositeDisposable = new CompositeDisposable();
    }

    @Inject
    public void onAfterConstruct() {
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
                        .onBackpressureLatest()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                qPacks -> getViewState().setQPacks(qPacks, sortType),
                                throwable -> onGetError(throwable)
                        )
        );
    }

    private void onGetError(Throwable t) {
        MyLog.add(ExceptionUtils.getMessage(t));
        getViewState().showError(R.string.qpacks_request_error);
    }

    private Flowable<List<QPackEntity>> getAllQPacksSorted(QPackSortType sortType) {
        switch (sortType) {

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

    public void onUiPackClick(QPackEntity qPack) {
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
