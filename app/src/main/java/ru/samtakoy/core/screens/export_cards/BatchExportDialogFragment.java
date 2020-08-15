package ru.samtakoy.core.screens.export_cards;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import moxy.MvpAppCompatDialogFragment;
import moxy.MvpPresenter;
import moxy.presenter.InjectPresenter;
import moxy.presenter.ProvidePresenter;
import ru.samtakoy.core.MyApp;
import ru.samtakoy.R;


public class BatchExportDialogFragment extends MvpAppCompatDialogFragment implements BatchExportDialogView{




    public static final String TAG = "BatchExportDialogFragment";

    private static final String ARG_EXPORT_TYPE = "ARG_EXPORT_TYPE";
    private static final String ARG_DIR_PATH = "ARG_DIR_PATH";

    private static final int EXPORT_TYPE_QPACKS = 1;
    private static final int EXPORT_TYPE_COURSES = 2;

    public static BatchExportDialogFragment newQPacksFragment(
            String dirPath, Fragment targetFragment, int targetReqCode
    ){
        BatchExportDialogFragment result = new BatchExportDialogFragment();
        Bundle args = new Bundle();
        args.putString(ARG_DIR_PATH, dirPath);
        args.putInt(ARG_EXPORT_TYPE, EXPORT_TYPE_QPACKS);

        result.setArguments(args);
        result.setTargetFragment(targetFragment, targetReqCode);
        return result;
    }

    // TODO пока экспорт только по Email, dirPath не используется
    public static BatchExportDialogFragment newCoursesFragment(
            String dirPath, Fragment targetFragment, int targetReqCode
    ){
        BatchExportDialogFragment result = new BatchExportDialogFragment();
        Bundle args = new Bundle();
        args.putString(ARG_DIR_PATH, dirPath);
        args.putInt(ARG_EXPORT_TYPE, EXPORT_TYPE_COURSES);

        result.setArguments(args);
        result.setTargetFragment(targetFragment, targetReqCode);
        return result;
    }


    @InjectPresenter
    MvpPresenter<BatchExportDialogView> mPresenter;

    @ProvidePresenter
    MvpPresenter<BatchExportDialogView> providePresenter(){
        Bundle args = getArguments();

        if(args.getInt(ARG_EXPORT_TYPE) == EXPORT_TYPE_QPACKS){
            return  new BatchExportQPacksPresenter(
                    MyApp.getInstance().getAppComponent(), args.getString(ARG_DIR_PATH)
            );
        }

        return  new BatchExportCoursesPresenter(
                MyApp.getInstance().getAppComponent(), args.getString(ARG_DIR_PATH)
        );
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        View v = LayoutInflater.from(getContext()).inflate(R.layout.fragment_dialog_batch_export, null);
        return new AlertDialog.Builder(getContext())
                .setView(v)
                .setCancelable(false)
                .create();
    }



    @Override
    public void exitWithError(int errorTextId) {
        Toast.makeText(getContext(), errorTextId, Toast.LENGTH_SHORT).show();
        getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_CANCELED, null);
        dismiss();
    }

    @Override
    public void exitOk() {
        if(getTargetFragment() == null){
            return;
        }
        getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, null);
        dismiss();
    }


}
