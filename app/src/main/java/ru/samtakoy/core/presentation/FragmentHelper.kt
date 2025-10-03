package ru.samtakoy.core.presentation

import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import javax.annotation.Nullable


fun showDialogFragment(
        dialog: DialogFragment,
        fragment: Fragment,
        @Nullable tag: String) {

    val fm: androidx.fragment.app.FragmentManager? = fragment.fragmentManager
    dialog.show(fm!!, tag);
}



