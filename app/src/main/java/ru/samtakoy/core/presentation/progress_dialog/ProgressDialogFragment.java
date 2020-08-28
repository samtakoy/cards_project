package ru.samtakoy.core.presentation.progress_dialog;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import javax.inject.Inject;
import javax.inject.Provider;

import moxy.MvpAppCompatDialogFragment;
import moxy.presenter.InjectPresenter;
import moxy.presenter.ProvidePresenter;
import ru.samtakoy.R;

public abstract class ProgressDialogFragment extends MvpAppCompatDialogFragment implements ProgressDialogView {

    protected abstract ProgressDialogPresenter.WorkerImpl createWorkerImpl();

    private TextView titleTextView;

    @InjectPresenter
    public ProgressDialogPresenter mPresenter;
    @Inject
    Provider<ProgressDialogPresenter.Factory> mFactoryProvider;

    @ProvidePresenter
    public ProgressDialogPresenter providePresenter() {
        //return new ProgressDialogPresenter.Factory().create(createWorkerImpl());
        return mFactoryProvider.get().create(createWorkerImpl());
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        View v = LayoutInflater.from(getContext()).inflate(R.layout.fragment_dialog_progress, null);
        initView(v);
        return new AlertDialog.Builder(getContext())
                .setView(v)
                .setCancelable(false)
                .create();
    }

    protected void initView(View view) {
        titleTextView = view.findViewById(R.id.title);
    }

    @Override
    public void showTitle(int titleResId) {
        titleTextView.setText(titleResId);
    }

    @Override
    public void showError(int stringId) {
        Toast.makeText(getContext(), stringId, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void exitCanceled() {
        getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_CANCELED, null);
        dismiss();
    }

    @Override
    public final void exitOk() {
        if (getTargetFragment() != null) {
            getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, null);
        }
        dismiss();
    }


}
