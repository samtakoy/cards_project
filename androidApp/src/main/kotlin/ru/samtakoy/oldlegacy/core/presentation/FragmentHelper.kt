package ru.samtakoy.oldlegacy.core.presentation

import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import javax.annotation.Nullable


fun showDialogFragment(
        dialog: DialogFragment,
        fragment: Fragment,
        @Nullable tag: String) {

    val fm: FragmentManager? = fragment.fragmentManager
    dialog.show(fm!!, tag);
}



