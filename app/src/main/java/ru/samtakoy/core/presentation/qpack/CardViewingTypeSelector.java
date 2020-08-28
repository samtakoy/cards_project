package ru.samtakoy.core.presentation.qpack;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import ru.samtakoy.R;
import ru.samtakoy.core.presentation.log.MyLog;

public class CardViewingTypeSelector extends DialogFragment {


    public static DialogFragment newFragment(QPackInfoFragment targetFragment, int requestCode) {
        CardViewingTypeSelector result = new CardViewingTypeSelector();
        result.setCancelable(true);
        result.setTargetFragment(targetFragment, requestCode);
        return result;
    }

    enum CardViewingType{
        RANDOM,
        SIMPLE,
        LIST
    };

    interface CardViewingTypeSelectorListener{
        void OnCardViewingTypeSelect(CardViewingType type);
    }

    private int mPositionIndex = 0;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(!(getTargetFragment() instanceof CardViewingTypeSelectorListener))
        {
            MyLog.add("CardViewingTypeSelectorListener must be implemented in target fragment");
            //throw new ClassCastException("CardViewingTypeSelectorListener must be implemented in target fragment");
            dismiss();
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        String[] items = getContext().getResources().getStringArray(R.array.qpack_viewing_types);

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext())
                .setTitle(R.string.qpack_viewing_types_title)
                .setSingleChoiceItems(items, mPositionIndex, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        mPositionIndex = i;
                    }
                })
                .setPositiveButton(R.string.btn_ok, (dialogInterface, i) -> onOk())
                .setNegativeButton(R.string.btn_cancel, (dialogInterface, i) -> dismiss())
                ;
        return builder.create();
    }

    private void onOk(){
        CardViewingTypeSelectorListener listener = (CardViewingTypeSelectorListener)getTargetFragment();
        listener.OnCardViewingTypeSelect(CardViewingType.values()[mPositionIndex]);
        dismiss();
    }

}
