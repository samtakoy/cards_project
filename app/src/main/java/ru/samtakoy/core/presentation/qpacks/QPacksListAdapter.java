package ru.samtakoy.core.presentation.qpacks;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import ru.samtakoy.R;
import ru.samtakoy.core.data.local.database.room.entities.QPackEntity;

public class QPacksListAdapter extends RecyclerView.Adapter<QPacksListAdapter.QPacksListViewHolder>{



    public interface ItemClickListener{
        void onClick(QPackEntity qPack);
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

//            mItemView.setClickable(true);
        }

        public void bindData(QPackEntity qPack, QPackSortType itemType, ItemClickListener listener) {

            mTitle.setText(qPack.getTitle());
            switch (itemType) {
                case LAST_VIEW_DATE_ASC:
                    mLastViewText.setText(qPack.getLastViewDateAsString());
                    break;
                case CREATION_DATE_DESC:
                    mLastViewText.setText(qPack.getCreationDateAsString());
                    break;
            }
            mViewCountText.setText(String.valueOf(qPack.getViewCount()));

            mItemView.setOnClickListener(view -> listener.onClick(qPack));
        }

    }


    private QPackSortType mItemType;
    private List<QPackEntity> mItems;
    private ItemClickListener mItemClickListener;

    public QPacksListAdapter(ItemClickListener itemClickListener){

        mItemClickListener = itemClickListener;
        mItems = new ArrayList<>();
        mItemType = null;
    }

    public void setItems(List<QPackEntity> items, QPackSortType itemType) {
        mItems = items;
        mItemType = itemType;
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        return mItemType.ordinal();
    }

    @NonNull
    @Override
    public QPacksListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.qpacks_list_item, parent, false);
        return new QPacksListViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull QPacksListViewHolder holder, int position) {
        holder.bindData(mItems.get(position), mItemType, mItemClickListener);
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }



}
