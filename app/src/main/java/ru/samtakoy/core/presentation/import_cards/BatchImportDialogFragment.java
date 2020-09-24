package ru.samtakoy.core.presentation.import_cards;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import org.jetbrains.annotations.NotNull;

import javax.inject.Inject;

import io.reactivex.Completable;
import ru.samtakoy.R;
import ru.samtakoy.core.app.di.Di;
import ru.samtakoy.core.presentation.progress_dialog.ProgressDialogFragment;
import ru.samtakoy.core.presentation.progress_dialog.ProgressDialogPresenter;
import ru.samtakoy.features.import_export.ImportApi;
import ru.samtakoy.features.import_export.utils.ImportCardsOpts;

public class BatchImportDialogFragment extends ProgressDialogFragment {


    public static final String TAG = "BatchImportDialogFragment";
    private static final String ARG_DIR_PATH = "ARG_DIR_PATH";
    private static final String ARG_OPTS = "ARG_OPTS";
    private static final String ARG_THEME_ID = "ARG_THEME_ID";


    public static BatchImportDialogFragment newFragment(
            String dirPath,
            Long targetThemeId,
            ImportCardsOpts opts,
            Fragment targetFragmet,
            int targetReqCode
    ){
        BatchImportDialogFragment result = new BatchImportDialogFragment();
        Bundle args = new Bundle();
        args.putString(ARG_DIR_PATH, dirPath);
        args.putLong(ARG_THEME_ID, targetThemeId);
        args.putSerializable(ARG_OPTS, opts);

        result.setArguments(args);
        result.setTargetFragment(targetFragmet, targetReqCode);
        return result;
    }

    @Inject
    ImportApi mImportApi;

    // TODO во все прогресс диалоги делаем инжект, ради инжекта presenter родительского класса, что неочевидно
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        Di.appComponent.inject(this);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected ProgressDialogPresenter.WorkerImpl createWorkerImpl() {

        Bundle args = getArguments();
        String dirPath = args.getString(ARG_DIR_PATH);
        Long targetThemeId = args.getLong(ARG_THEME_ID, -1);
        ImportCardsOpts opts = (ImportCardsOpts) (args.getSerializable(ARG_OPTS));

        return new ProgressDialogPresenter.WorkerImpl() {
            @NotNull
            @Override
            public Completable createObservable() {
                return mImportApi.batchLoadFromFolder(dirPath, targetThemeId, opts);
            }

            @Override
            public int getTitleTextId() {
                return R.string.fragment_dialog_batch_import_title;
            }

            @Override
            public int getErrorTextId() {
                return R.string.fragment_dialog_batch_import_error_msg;
            }

            @Override
            public void onComplete() {
            }
        };
    }
}
