package ru.samtakoy.core.screens;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import org.apache.commons.lang3.exception.ExceptionUtils;

import io.reactivex.Completable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import ru.samtakoy.R;
import ru.samtakoy.core.screens.log.MyLog;
import ru.samtakoy.core.services.import_utils.ImportCardsException;

public abstract class ProgressDialogFragment extends DialogFragment {

    protected Disposable mSubscriber;

    @Override
    public void onDestroy() {

        if(mSubscriber != null && !mSubscriber.isDisposed()){
            mSubscriber.dispose();
            mSubscriber = null;
        }

        super.onDestroy();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        MyLog.add("ImportPackDialogFragment::onCreateView");

        if(mSubscriber == null) {
            mSubscriber = createObservable()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                            () -> trySendResult(),
                            (throwable)->onError(throwable)
                    );
        }


        return super.onCreateView(inflater, container, savedInstanceState);
    }

    protected abstract Completable createObservable();

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
        TextView tv = view.findViewById(R.id.title);
        tv.setText(getTitleTextId());
    }

    protected void onError(Throwable err){

        if(err instanceof ImportCardsException){
            ImportCardsException impError = (ImportCardsException)err;
            MyLog.add("import pack error: "+err.toString()+", id:"+impError.getErrorId());
        }else{
            MyLog.add("batch import error: "+err.getMessage() +",\n "+ ExceptionUtils.getStackTrace(err));
        }

        Toast.makeText(getContext(), getErrorTextId(), Toast.LENGTH_SHORT).show();

        getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_CANCELED, null);
        dismiss();
    }

    protected abstract int getTitleTextId();
    protected abstract int getErrorTextId();

    protected void onComplete(){}

    protected void trySendResult(){

        if(getTargetFragment() != null){
            getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, null);
        }
        onComplete();
        dismiss();
    }


}
