package ru.samtakoy.core.screens;

import android.os.Bundle;

import androidx.annotation.LayoutRes;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import ru.samtakoy.R;

public abstract class SingleFragmentActivity extends AppCompatActivity {

    protected abstract Fragment createFragment();

    @LayoutRes
    protected int getLayoutResId() {
        return R.layout.activity_single_fragment;
    }

    protected int getFragmentContainerId() {
        return R.id.fragment_cont;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(getLayoutResId());

        FragmentManager fm = getSupportFragmentManager();
        Fragment f = fm.findFragmentById(getFragmentContainerId());
        if(f == null){
            f = createFragment();
            fm.beginTransaction()
                    .add(getFragmentContainerId(), f)
                    .commit();
        }
    }


}
