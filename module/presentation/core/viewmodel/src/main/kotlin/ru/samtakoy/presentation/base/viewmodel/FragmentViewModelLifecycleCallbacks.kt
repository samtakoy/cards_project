package ru.samtakoy.presentation.base.viewmodel

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import timber.log.Timber

class FragmentViewModelLifecycleCallbacks : FragmentManager.FragmentLifecycleCallbacks() {

    override fun onFragmentViewCreated(fm: FragmentManager, f: Fragment, v: View, savedInstanceState: Bundle?) {
        Timber.tag("mytest").e("onFragmentViewCreated: ${f.javaClass.name}")
        when (f) {
            is ViewModelOwner -> {
                f.onObserveViewModel()
                f.getViewModel().onViewCreated()
            }
        }
    }
}