package ru.samtakoy.core.presentation.import_cards;

import android.net.Uri;
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

public class ImportZipDialogFragment extends ProgressDialogFragment {


    public static final String TAG = "ImportZipDialogFragment";

    private static final String ARG_FILE_URI = "ARG_FILE_URI";
    private static final String ARG_OPTS = "ARG_OPTS";



    public static ImportZipDialogFragment newFragment(
            Uri selectedFileUri,
            ImportCardsOpts opts,
            Fragment targetFragmet,
            int targetReqCode
    ){
        ImportZipDialogFragment result = new ImportZipDialogFragment();

        result.setTargetFragment(targetFragmet, targetReqCode);

        Bundle args = new Bundle();
        args.putParcelable(ARG_FILE_URI, selectedFileUri);
        args.putSerializable(ARG_OPTS, opts);
        result.setArguments(args);
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

    // --

    @Override
    protected ProgressDialogPresenter.WorkerImpl createWorkerImpl() {

        Bundle args = getArguments();
        Uri selectedFileUri = args.getParcelable(ARG_FILE_URI);
        ImportCardsOpts opts = (ImportCardsOpts) (args.getSerializable(ARG_OPTS));

        return new ProgressDialogPresenter.WorkerImpl() {
            @NotNull
            @Override
            public Completable createObservable() {
                return mImportApi.batchUpdateFromZip(selectedFileUri, opts);
            }

            @Override
            public int getTitleTextId() {
                return R.string.fragment_dialog_pack_import_title;
            }

            @Override
            public int getErrorTextId() {
                return R.string.fragment_dialog_pack_import_error_msg;
            }

            @Override
            public void onComplete() {
            }
        };
    }

    // --
}
