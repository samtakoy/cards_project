package ru.samtakoy.presentation.base.viewmodel

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import io.github.aakira.napier.Napier

class FragmentViewModelLifecycleCallbacks : FragmentManager.FragmentLifecycleCallbacks() {
    override fun onFragmentViewCreated(fm: FragmentManager, f: Fragment, v: View, savedInstanceState: Bundle?) {
        Napier.e { "onFragmentViewCreated: ${f.javaClass.name}" }
        tryObserveFragmentViewModel(f)
    }

    private fun tryObserveFragmentViewModel(f: Fragment) {
        when (f) {
            is ViewModelOwner -> {
                f.onObserveViewModel()
                f.getViewModel().onViewCreated()
            }
        }
    }
}