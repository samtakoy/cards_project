package ru.samtakoy.core.presentation.qpack.list;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import ru.samtakoy.R;
import ru.samtakoy.core.presentation.qpack.list.model.QPackListItemUiModel;

public class QPacksListAdapter extends RecyclerView.Adapter<QPacksListAdapter.QPacksListViewHolder>{

    public interface ItemClickListener{
        void onClick(QPackListItemUiModel item);
    }

    class QPacksListViewHolder extends RecyclerView.ViewHolder{

        private TextView mTitle;
        private TextView mLastViewText;
        private TextView mViewCountText;
        private View mItemView;

        QPacksListViewHolder(@NonNull View itemView){
            super(itemView);

            mItemView = itemView;
            mTitle = itemView.findViewById(R.id.list_item_text);
            mLastViewText = itemView.findViewById(R.id.last_view_text);
            mViewCountText = itemView.findViewById(R.id.view_count);
        }

        public void bindData(QPackListItemUiModel item, ItemClickListener listener) {

            mTitle.setText(item.getTitle());
            mLastViewText.setText(item.getDateText());
            mViewCountText.setText(item.getViewCountText());

            mItemView.setOnClickListener(view -> listener.onClick(item));
        }

    }


    private List<QPackListItemUiModel> mItems;
    private ItemClickListener mItemClickListener;

    public QPacksListAdapter(ItemClickListener itemClickListener){

        mItemClickListener = itemClickListener;
        mItems = new ArrayList<>();
    }

    public void setItems(List<QPackListItemUiModel> items) {
        mItems = items;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public QPacksListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.qpacks_list_item, parent, false);
        return new QPacksListViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull QPacksListViewHolder holder, int position) {
        holder.bindData(mItems.get(position), mItemClickListener);
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }
}
