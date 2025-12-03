package ru.samtakoy.oldlegacy.core.presentation.qpack.list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ru.samtakoy.R
import ru.samtakoy.oldlegacy.core.presentation.qpack.list.QPacksListAdapter.QPacksListViewHolder
import ru.samtakoy.oldlegacy.core.presentation.qpack.list.model.QPackListItemUiModel

internal class QPacksListAdapter(
    private val mItemClickListener: ItemClickListener
) : RecyclerView.Adapter<QPacksListViewHolder>() {
    interface ItemClickListener {
        fun onClick(item: QPackListItemUiModel)
    }

    internal inner class QPacksListViewHolder(private val mItemView: View) : RecyclerView.ViewHolder(
        mItemView
    ) {
        private val mTitle: TextView
        private val mLastViewText: TextView
        private val mViewCountText: TextView

        init {
            mTitle = mItemView.findViewById<TextView>(R.id.list_item_text)
            mLastViewText = mItemView.findViewById<TextView>(R.id.last_view_text)
            mViewCountText = mItemView.findViewById<TextView>(R.id.view_count)
        }

        fun bindData(item: QPackListItemUiModel, listener: ItemClickListener) {
            mTitle.setText(item.title)
            mLastViewText.setText(item.dateText)
            mViewCountText.setText(item.viewCountText)

            mItemView.setOnClickListener(View.OnClickListener { view: View? -> listener.onClick(item) })
        }
    }

    private var mItems: List<QPackListItemUiModel?>

    init {
        mItems = ArrayList<QPackListItemUiModel?>()
    }

    fun setItems(items: List<QPackListItemUiModel?>) {
        mItems = items
        notifyDataSetChanged()
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QPacksListViewHolder {
        val v = LayoutInflater.from(parent.getContext()).inflate(R.layout.qpacks_list_item, parent, false)
        return QPacksListViewHolder(v)
    }

    override fun onBindViewHolder(holder: QPacksListViewHolder, position: Int) {
        holder.bindData(mItems.get(position)!!, mItemClickListener)
    }

    override fun getItemCount(): Int {
        return mItems.size
    }
}
