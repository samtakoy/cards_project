package ru.samtakoy.core.screens.online_import;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import ru.samtakoy.R;
import ru.samtakoy.core.services.api.ServiceGenerator;
import ru.samtakoy.core.services.api.pojo.RemoteFilesInfo;

public class OnlineImportFragment extends Fragment {

    public static OnlineImportFragment createInstance(){
        return new OnlineImportFragment();
    }

    private RecyclerAdapter mAdapter;
    private RecyclerView mRecyclerView;
    private CompositeDisposable mDisposables = new CompositeDisposable();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_remote_update, container, false);


        mRecyclerView = view.findViewById(R.id.recycler_view);
        initRecycler();

        return view;
    }

    @Override
    public void onDestroy() {
        mDisposables.clear();

        super.onDestroy();
    }

    private void initRecycler() {
        mAdapter = new RecyclerAdapter();
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(mAdapter);


        Observer<RemoteFilesInfo> observer =
                new Observer<RemoteFilesInfo>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        mDisposables.add(d);
                    }

                    @Override
                    public void onNext(RemoteFilesInfo remoteFilesInfo) {
                        mAdapter.setFiles(remoteFilesInfo.getFiles());
                    }

                    @Override
                    public void onError(Throwable e) {
                        showMessage(e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        showMessage("ok!");
                    }
                };

        getRemoteFileObservable().subscribe(
                    observer
        );

    }

    private void showMessage(String text){
        Toast.makeText(getContext(), text, Toast.LENGTH_SHORT).show();
    }

    private Observable<RemoteFilesInfo> getRemoteFileObservable(){
        return ServiceGenerator.getApi().getFilesInfo()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                /*.flatMap(remoteFilesInfo ->
                        Observable.fromIterable(remoteFilesInfo.getFiles())
                        .subscribeOn(Schedulers.io())
                )*/;
    }

}
