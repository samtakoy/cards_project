package ru.samtakoy.core.presentation;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import ru.samtakoy.R;
import ru.samtakoy.core.database.room.entities.elements.Schedule;
import ru.samtakoy.core.database.room.entities.elements.ScheduleItem;
import ru.samtakoy.core.database.room.entities.elements.ScheduleTimeUnit;

public class ScheduleEditFragment extends DialogFragment {

    public static final String TAG = "ScheduleEditFragment";

    private static final String ARG_SCHEDULE_STRING = "ARG_SCHEDULE_STRING";

    private static final String SAVED_SCHEDULE_CUR_ITEM_TO_ADD = "SAVED_SCHEDULE_CUR_ITEM_TO_ADD";

    public static final String RESULT_SCHEDULE_STRING = "RESULT_SCHEDULE_STRING";

    public static ScheduleEditFragment newFragment(Schedule schedule){
        ScheduleEditFragment result = new ScheduleEditFragment();
        Bundle args = new Bundle();
        args.putString(ARG_SCHEDULE_STRING, schedule == null ? null : schedule.toString());
        result.setArguments(args);
        return result;
    }

    private Schedule mSchedule_;
    private ScheduleItem mScheduleCurItemToAdd;

    private TextView mScheduleView;
    private Button mClearScheduleButton;

    private Button mScheduleCurItemButton;
    private Button mScheduleCurItemClearButton;
    private Button mScheduleCurItemAddButton;
    private ViewGroup mScheduleItemButtons;

    /*public interface Callbacks{
        void onScheduleSelect(Schedule schedule);
    }/**/

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        readArgs(getArguments());
        if(savedInstanceState != null){
            String strScheduleItem = savedInstanceState.getString(SAVED_SCHEDULE_CUR_ITEM_TO_ADD, null);
            //if(strScheduleItem != null) {
                mScheduleCurItemToAdd = ScheduleItem.fromString(strScheduleItem);
            //}
        }
        if(mScheduleCurItemToAdd == null){
            mScheduleCurItemToAdd = new ScheduleItem(1, ScheduleTimeUnit.MINUTE);
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        //outState.putString(SAVED_SCHEDULE_CUR_ITEM_TO_ADD, mScheduleCurItemToAdd == null ? null : mScheduleCurItemToAdd.toString());
        outState.putString(SAVED_SCHEDULE_CUR_ITEM_TO_ADD, mScheduleCurItemToAdd.toString());
    }

    @Override
    public void onDestroy() {
        mSchedule_ = null;
        super.onDestroy();
    }

    private void readArgs(Bundle arguments) {
        mSchedule_ = new Schedule();
        String strSchedule = arguments.getString(ARG_SCHEDULE_STRING);
        if(strSchedule != null){
            Log.e(TAG, "readArgs:"+strSchedule);
            mSchedule_.initFromString(strSchedule);
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View v = inflater.inflate(R.layout.fragment_schedule_edit, null);

        initView(v);

        return new AlertDialog.Builder(getActivity())
                .setView(v)
                .setCancelable(true)
                .setPositiveButton(R.string.btn_ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        trySendResult();
                    }
                })
                .create();
    }

    private void initView(View v) {
        mScheduleView = v.findViewById(R.id.schedule_text);

        mClearScheduleButton = v.findViewById(R.id.clear_btn);
        mClearScheduleButton.setOnClickListener((view)->clearSchedule());

        mScheduleCurItemButton = v.findViewById(R.id.item_btn);

        mScheduleCurItemClearButton = v.findViewById(R.id.item_clear_btn);
        mScheduleCurItemClearButton.setOnClickListener((view)->clearScheduleCurItem());

        mScheduleCurItemAddButton = v.findViewById(R.id.item_add_btn);
        mScheduleCurItemAddButton.setOnClickListener((view)->addScheduleCurItem());

        mScheduleItemButtons = v.findViewById(R.id.schedule_item_btns);

        createScheduleItemButtons(v.findViewById(R.id.schedule_item_btns));
        updateScheduleView();
        updateScheduleItemView();
    }


    @Override
    public void onDestroyView() {

        mScheduleView = null;
        mClearScheduleButton = null;
        mScheduleCurItemButton = null;
        mScheduleCurItemClearButton = null;
        mScheduleCurItemAddButton = null;
        mScheduleItemButtons = null;

        super.onDestroyView();
    }

    private void updateScheduleView(){
        if(mSchedule_.isEmpty()){
            mScheduleView.setText(R.string.schedule_none);
        } else {
            mScheduleView.setText(mSchedule_.toStringView(getResources()));
        }
    }

    private void updateScheduleItemView(){
        mScheduleCurItemButton.setText(
                mScheduleCurItemToAdd.toStringView(getResources())
        );
    }

    private void createScheduleItemButtons(ViewGroup container) {

        Context ctx = getActivity();
        for(ScheduleTimeUnit unit:ScheduleTimeUnit.values()) {
            Button unitBtn = new Button(ctx);
            unitBtn.setText("+1"+getResources().getString(unit.getStringsId()));
            unitBtn.setOnClickListener((View view)->onScheduleTimeUnitSelect(unit));
            container.addView(unitBtn);
        }
    }

    private void clearSchedule() {
        mSchedule_.clear();
        updateScheduleView();
    }

    private void clearScheduleCurItem() {
        mScheduleCurItemToAdd.setValue(1, ScheduleTimeUnit.MINUTE);
        updateScheduleItemView();
    }

    private void addScheduleCurItem() {
        mSchedule_.addItem(mScheduleCurItemToAdd.copy());
        updateScheduleView();
    }

    private void onScheduleTimeUnitSelect(ScheduleTimeUnit unit) {
        if(unit == mScheduleCurItemToAdd.getTimeUnit()){
            mScheduleCurItemToAdd.setValue(1+mScheduleCurItemToAdd.getDimension(), unit);
        } else{
            mScheduleCurItemToAdd.setValue(1, unit);
        }
        updateScheduleItemView();
    }

    private void trySendResult() {

        if(getTargetFragment() == null){
            Log.e(TAG, "Target fragment is missing");
            return;
        }

        Intent result = new Intent();
        result.putExtra(RESULT_SCHEDULE_STRING, mSchedule_.toString());
        getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, result);
    }

}
