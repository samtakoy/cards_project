package ru.samtakoy.core.presentation.online_import;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import ru.samtakoy.R;
import ru.samtakoy.core.data.network.pojo.RemoteFile;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.MyViewHolder>{



    private List<RemoteFile> mFiles = new ArrayList<>();

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.remote_file_list_item, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.bind(mFiles.get(position));
    }

    @Override
    public int getItemCount() {
        return mFiles.size();
    }

    public void setFiles(List<RemoteFile> files) {
        mFiles = files;
        notifyDataSetChanged();
    }

    public void updateFile(RemoteFile file){
        // ?
        mFiles.set(mFiles.indexOf(file), file);
        notifyItemChanged(mFiles.indexOf(file));
    }



    static class MyViewHolder extends RecyclerView.ViewHolder{

        private TextView mText;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            mText = itemView.findViewById(R.id.title);
        }

        public void bind(RemoteFile remoteFile) {

            mText.setText(remoteFile.getPath());
        }
    }

}
