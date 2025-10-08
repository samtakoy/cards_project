package ru.samtakoy.core.presentation.themes

import android.view.*
import android.view.ContextMenu.ContextMenuInfo
import android.view.View.OnCreateContextMenuListener
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ru.samtakoy.R

private const val ITEM_TYPE_THEME = 1
private const val ITEM_TYPE_QPACK = 2


// +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
// +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
internal class ThemesItemHolder(
        itemView: View,
        private var callback: ThemesAdapter.Callback
) : RecyclerView.ViewHolder(itemView), View.OnClickListener, OnCreateContextMenuListener {

    private var mItem: ThemeUiItem? = null
    private var mText: TextView = itemView.findViewById(R.id.list_item_text)


    fun bindTheme(item: ThemeUiItem.Theme) {
        mItem = item
        mText.text = item.title
    }

    fun bindQPack(item: ThemeUiItem.QPack) {
        mItem = item
        mText.text = item.title
        val dateText = itemView.findViewById<TextView>(R.id.creation_date)
        dateText.text = item.creationDate
    }

    override fun onClick(view: View) {
        mItem?.let {
            callback.onListItemClick(it)
        }
    }

    override fun onCreateContextMenu(contextMenu: ContextMenu, view: View, contextMenuInfo: ContextMenuInfo?) {
        val mi: MenuInflater = callback.getMenuInflater()
        if (mItem is ThemeUiItem.Theme) {
            mi.inflate(R.menu.fragment_themes_tctx, contextMenu)
        } else {
            mi.inflate(R.menu.fragment_themes_ctx, contextMenu)
        }
    }

    init {
        itemView.setOnClickListener(this)
    }
}


// +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
// +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
internal class ThemesAdapter(private val callback: Callback) : RecyclerView.Adapter<ThemesItemHolder>() {

    interface Callback {
        fun getMenuInflater(): MenuInflater
        fun onListItemClick(item: ThemeUiItem)
        fun onListItemLongClick(item: ThemeUiItem)
    }

    private var mItems: List<ThemeUiItem> = emptyList()

    fun updateData(items: List<ThemeUiItem>) {
        mItems = items
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ThemesItemHolder {
        val li = LayoutInflater.from(parent.context)
        val layoutId = if (viewType == ITEM_TYPE_THEME) R.layout.themes_list_item else R.layout.themes_list_qpack_item
        return ThemesItemHolder(li.inflate(layoutId, parent, false), callback)
    }

    override fun onBindViewHolder(holder: ThemesItemHolder, position: Int) {

        if (getItemViewType(position) == ITEM_TYPE_THEME) {
            holder.bindTheme(mItems[position] as ThemeUiItem.Theme)
        } else if (getItemViewType(position) == ITEM_TYPE_QPACK) {
            holder.bindQPack(mItems[position] as ThemeUiItem.QPack)
        }
        holder.itemView.setOnCreateContextMenuListener(holder)
        holder.itemView.setOnLongClickListener { view: View? ->
            callback.onListItemLongClick(mItems[position])
            false
        }
    }

    override fun getItemCount(): Int {
        return mItems.size
    }

    override fun onViewRecycled(holder: ThemesItemHolder) {
        holder.itemView.setOnLongClickListener(null)
        holder.itemView.setOnCreateContextMenuListener(null)
        super.onViewRecycled(holder)
    }

    override fun getItemViewType(position: Int): Int {
        return if (mItems.getOrNull(position) is ThemeUiItem.QPack) ITEM_TYPE_QPACK else ITEM_TYPE_THEME
    }
}
