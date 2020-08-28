package ru.samtakoy.core.presentation.export_cards;

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
import ru.samtakoy.core.presentation.log.MyLog;
import ru.samtakoy.features.import_export.CoursesExporter;

@InjectViewState
public class BatchExportCoursesPresenter extends MvpPresenter<BatchExportDialogView> {


    CoursesExporter mCoursesExporter;

    public static class Factory {

        @Inject
        CoursesExporter mCoursesExporter;

        @Inject
        Factory() {
        }

        public BatchExportCoursesPresenter create(String exportDirPath) {
            return new BatchExportCoursesPresenter(mCoursesExporter, exportDirPath);
        }
    }

    private CompositeDisposable mDisposable;

    public BatchExportCoursesPresenter(
            CoursesExporter coursesExporter,
            String exportDirPath
    ) {

        mCoursesExporter = coursesExporter;

        mDisposable = new CompositeDisposable();

        //mCoursesExporter.exportAllToFolder(exportDirPath)
        mCoursesExporter.exportAllToEmail()
                .observeOn(Schedulers.io())
                .subscribeOn(AndroidSchedulers.mainThread())
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

    @Override
    public void onDestroy() {

        mDisposable.dispose();
        mDisposable = null;

        super.onDestroy();
    }

}
