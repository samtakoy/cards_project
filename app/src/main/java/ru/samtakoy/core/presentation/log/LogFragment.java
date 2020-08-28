package ru.samtakoy.core.presentation.log;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ru.samtakoy.R;

public class LogFragment extends Fragment {

    private static final String TAG = "LogFragment";

    private RecyclerView mRecyclerView;
    private List<String> mLog;

    public static Fragment newFragment() {
        LogFragment result = new LogFragment();
        return result;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_log, container, false);

        mLog = MyLog.getStrings();

        mRecyclerView = v.findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(new RecyclerViewAdapter());

        // items divider
        DividerItemDecoration divider = new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL);
        //divider.setDrawable(...);
        mRecyclerView.addItemDecoration(divider);

        return v;
    }

    private static class RecyclerViewHolder extends RecyclerView.ViewHolder{

        private TextView mText;

        public RecyclerViewHolder(@NonNull View itemView) {
            super(itemView);

            mText = itemView.findViewById(R.id.list_item_text);
        }

        public void setText(String s) {
            mText.setText(s);
        }
    }

    private class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewHolder>{

        @NonNull
        @Override
        public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new RecyclerViewHolder(
                    LayoutInflater.from(getContext()).inflate(R.layout.log_list_item, parent, false)
            );
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerViewHolder holder, int position) {
            holder.setText(mLog.get(position));
        }

        @Override
        public int getItemCount() {
            return mLog.size();
        }
    }
}




