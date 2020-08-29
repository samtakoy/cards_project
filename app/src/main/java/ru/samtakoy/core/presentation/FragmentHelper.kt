package ru.samtakoy.core.presentation

import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import javax.annotation.Nullable


fun showDialogFragment(
        dialog: DialogFragment,
        fragment: Fragment,
        @Nullable tag: String) {

    // TODO
    // getSupportFragmentManager causes exception:
    // Fragment declared target fragment   that does not belong to this FragmentManager!
    //        at androidx.fragment.app.FragmentManager.moveToState(FragmentManager.java:1148)
    // because of new Navigation Component??? v.2.3.0


    val fm: androidx.fragment.app.FragmentManager? = fragment.fragmentManager
    dialog.show(fm!!, tag);
}



