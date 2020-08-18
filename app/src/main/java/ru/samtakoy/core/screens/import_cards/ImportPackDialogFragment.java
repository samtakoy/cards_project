package ru.samtakoy.core.screens.import_cards;

import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import io.reactivex.Completable;
import ru.samtakoy.R;
import ru.samtakoy.core.business.impl.ImportCardsHelper;
import ru.samtakoy.core.screens.ProgressDialogFragment;
import ru.samtakoy.core.services.import_utils.ImportCardsOpts;

public class ImportPackDialogFragment extends ProgressDialogFragment {

    public static final String TAG = "ImportPackDialogFragment";

    private static final String ARG_FILE_URI = "ARG_FILE_URI";
    private static final String ARG_THEME_ID = "ARG_THEME_ID";
    private static final String ARG_OPTS = "ARG_OPTS";


    public static ImportPackDialogFragment newFragment(
            Uri selectedFileUri,
            Long targetThemeId,
            ImportCardsOpts opts,
            Fragment targetFragmet,
            int targetReqCode
    ){
        ImportPackDialogFragment result = new ImportPackDialogFragment();

        result.setTargetFragment(targetFragmet, targetReqCode);

        Bundle args = new Bundle();
        args.putLong(ARG_THEME_ID, targetThemeId);
        args.putParcelable(ARG_FILE_URI, selectedFileUri);
        args.putSerializable(ARG_OPTS, opts);
        result.setArguments(args);
        return result;
    }

    @Override
    protected Completable createObservable() {
        Bundle args = getArguments();
        Uri selectedFileUri = args.getParcelable(ARG_FILE_URI);
        Long targetThemeId = args.getLong(ARG_THEME_ID, -1);
        ImportCardsOpts opts = (ImportCardsOpts)(args.getSerializable(ARG_OPTS));

        return ImportCardsHelper.loadCardsFromFile(getContext().getContentResolver(), selectedFileUri, targetThemeId, opts );
    }

    @Override
    protected int getTitleTextId() {
        return R.string.fragment_dialog_pack_import_title;
    }

    @Override
    protected int getErrorTextId() {
        return R.string.fragment_dialog_pack_import_error_msg;
    }


}
