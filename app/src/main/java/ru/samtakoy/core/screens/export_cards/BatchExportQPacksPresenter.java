package ru.samtakoy.core.screens.export_cards;


import org.apache.commons.lang3.exception.ExceptionUtils;

import javax.inject.Inject;

import io.reactivex.CompletableObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import moxy.InjectViewState;
import moxy.MvpPresenter;
import ru.samtakoy.R;
import ru.samtakoy.core.business.QPacksExporter;
import ru.samtakoy.core.di.components.AppComponent;
import ru.samtakoy.core.screens.log.MyLog;

@InjectViewState
public class BatchExportQPacksPresenter extends MvpPresenter<BatchExportDialogView> {

    @Inject
    QPacksExporter mQPacksExporter;

    //private Disposable mSubscriber;
    private CompositeDisposable mDisposable;

    public BatchExportQPacksPresenter(AppComponent appComponent, String exportDirPath){

        appComponent.inject(this);

        mDisposable = new CompositeDisposable();

        if(exportDirPath == null || exportDirPath.length()==0){
            exportToEmail();
        } else{
            exportToDir(exportDirPath);
        }


    }

    private void exportToDir(String exportDirPath) {
        // to dir
        mQPacksExporter.exportAllToFolder(exportDirPath)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        new CompletableObserver() {
                            @Override
                            public void onSubscribe(Disposable d) { mDisposable.add(d); }

                            @Override
                            public void onComplete() { getViewState().exitOk(); }

                            @Override
                            public void onError(Throwable e) {
                                MyLog.add("batch export error: "+ ExceptionUtils.getStackTrace(e));
                                getViewState().exitWithError(R.string.fragment_dialog_batch_export_error_msg);
                            }
                        }
                );
    }

    private void exportToEmail() {
        mQPacksExporter.exportAllToEmail()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        new CompletableObserver() {
                            @Override
                            public void onSubscribe(Disposable d) { mDisposable.add(d); }

                            @Override
                            public void onComplete() { getViewState().exitOk(); }

                            @Override
                            public void onError(Throwable e) {
                                MyLog.add("batch export error: "+ExceptionUtils.getStackTrace(e));
                                getViewState().exitWithError(R.string.fragment_dialog_batch_export_error_msg);
                            }
                        }
                );
    }

    @Override
    public void onDestroy() {

        mDisposable.dispose();
        mDisposable = null;

        super.onDestroy();
    }


}
