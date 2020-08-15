package ru.samtakoy.core.screens.import_cards;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import io.reactivex.Completable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import ru.samtakoy.R;
import ru.samtakoy.core.screens.ProgressDialogFragment;
import ru.samtakoy.core.services.import_utils.ImportCardsHelper;
import ru.samtakoy.core.services.import_utils.ImportCardsOpts;

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

    @Override
    protected Completable createObservable() {
        Bundle args = getArguments();
        String dirPath = args.getString(ARG_DIR_PATH);
        Long targetThemeId = args.getLong(ARG_THEME_ID, -1);
        ImportCardsOpts opts = (ImportCardsOpts)(args.getSerializable(ARG_OPTS));

        return ImportCardsHelper.batchLoadFromFolder(getContext().getContentResolver(), dirPath, targetThemeId, opts );
    }

    @Override
    protected int getTitleTextId() {
        return R.string.fragment_dialog_batch_import_title;
    }

    @Override
    protected int getErrorTextId() {
        return R.string.fragment_dialog_batch_import_error_msg;
    }
}
