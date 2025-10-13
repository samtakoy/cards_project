package ru.samtakoy.core.presentation.qpack.info

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import ru.samtakoy.R
import ru.samtakoy.common.utils.MyLog.add

class CardViewingTypeSelector : DialogFragment() {
    enum class CardViewingType {
        RANDOM,
        SIMPLE,
        LIST
    }

    internal interface CardViewingTypeSelectorListener {
        fun OnCardViewingTypeSelect(type: CardViewingType?)
    }

    private var mPositionIndex = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (getTargetFragment() !is CardViewingTypeSelectorListener) {
            add("CardViewingTypeSelectorListener must be implemented in target fragment")
            //throw new ClassCastException("CardViewingTypeSelectorListener must be implemented in target fragment");
            dismiss()
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val items = requireContext().getResources().getStringArray(R.array.qpack_viewing_types)

        val builder = AlertDialog.Builder(requireContext())
            .setTitle(R.string.qpack_viewing_types_title)
            .setSingleChoiceItems(items, mPositionIndex, object : DialogInterface.OnClickListener {
                override fun onClick(dialogInterface: DialogInterface?, i: Int) {
                    mPositionIndex = i
                }
            })
            .setPositiveButton(
                R.string.btn_ok,
                DialogInterface.OnClickListener { dialogInterface: DialogInterface?, i: Int -> onOk() })
            .setNegativeButton(
                R.string.btn_cancel,
                DialogInterface.OnClickListener { dialogInterface: DialogInterface?, i: Int -> dismiss() })

        return builder.create()
    }

    private fun onOk() {
        val listener = getTargetFragment() as CardViewingTypeSelectorListener?
        listener!!.OnCardViewingTypeSelect(CardViewingType.entries[mPositionIndex])
        dismiss()
    }

    companion object {
        fun newFragment(targetFragment: QPackInfoFragment?, requestCode: Int): DialogFragment {
            val result = CardViewingTypeSelector()
            result.setCancelable(true)
            result.setTargetFragment(targetFragment, requestCode)
            return result
        }
    }
}
