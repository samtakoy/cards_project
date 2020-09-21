package ru.samtakoy.core.presentation.themes

import android.view.*
import android.view.ContextMenu.ContextMenuInfo
import android.view.View.OnCreateContextMenuListener
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ru.samtakoy.R
import ru.samtakoy.core.data.local.database.room.entities.QPackEntity
import ru.samtakoy.core.data.local.database.room.entities.ThemeEntity


private const val ITEM_TYPE_THEME = 1
private const val ITEM_TYPE_QPACK = 2


// +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
// +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
internal class ThemesItemHolder(
        itemView: View,
        private val mViewType: Int,
        private var callback: ThemesAdapter.Callback
) : RecyclerView.ViewHolder(itemView), View.OnClickListener, OnCreateContextMenuListener {

    private var mTheme: ThemeEntity? = null
    private var mQPack: QPackEntity? = null
    private var mText: TextView = itemView.findViewById(R.id.list_item_text)


    fun bindTheme(t: ThemeEntity) {
        mTheme = t
        mQPack = null
        mText.text = t.title
    }

    fun bindQPack(qPack: QPackEntity) {
        mTheme = null
        mQPack = qPack
        mText.text = qPack.title
        val dateText = itemView.findViewById<TextView>(R.id.creation_date)
        dateText.text = qPack.getCreationDateAsString()
    }

    override fun onClick(view: View) {
        if (mTheme != null) {
            callback.navigateToTheme(mTheme!!)
        } else {
            callback.navigateToQPack(mQPack!!)
        }
    }

    override fun onCreateContextMenu(contextMenu: ContextMenu, view: View, contextMenuInfo: ContextMenuInfo?) {
        val mi: MenuInflater = callback.getMenuInflater()
        if (mTheme != null) {
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
        fun navigateToTheme(theme: ThemeEntity)
        fun navigateToQPack(qPack: QPackEntity)
    }

    private var mLongClickPosition = 0
    private var mCurThemes: List<ThemeEntity>
    private var mCurQPacks: List<QPackEntity>
    fun updateData(themes: List<ThemeEntity>, qPacks: List<QPackEntity>) {
        mCurThemes = themes
        mCurQPacks = qPacks
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ThemesItemHolder {
        val li = LayoutInflater.from(parent.context)
        val layoutId = if (viewType == ITEM_TYPE_THEME) R.layout.themes_list_item else R.layout.themes_list_qpack_item
        return ThemesItemHolder(li.inflate(layoutId, parent, false), viewType, callback)
    }

    override fun onBindViewHolder(holder: ThemesItemHolder, position: Int) {
        if (getItemViewType(position) == ITEM_TYPE_THEME) {
            holder.bindTheme(mCurThemes[position]!!)
        } else if (getItemViewType(position) == ITEM_TYPE_QPACK) {
            holder.bindQPack(mCurQPacks[position - mCurThemes.size]!!)
        }
        holder.itemView.setOnCreateContextMenuListener(holder)
        holder.itemView.setOnLongClickListener { view: View? ->
            mLongClickPosition = position
            false
        }
    }

    override fun getItemCount(): Int {
        return mCurThemes.size + mCurQPacks.size
    }

    override fun onViewRecycled(holder: ThemesItemHolder) {
        holder.itemView.setOnLongClickListener(null)
        holder.itemView.setOnCreateContextMenuListener(null)
        super.onViewRecycled(holder)
    }

    override fun getItemViewType(position: Int): Int {
        return if (position >= mCurThemes.size) ITEM_TYPE_QPACK else ITEM_TYPE_THEME
    }

    val isQPackLongClicked: Boolean
        get() = mLongClickPosition - mCurThemes.size >= 0
    val longClickedQPack: QPackEntity?
        get() = mCurQPacks[mLongClickPosition - mCurThemes.size]
    val longClickedTheme: ThemeEntity?
        get() = mCurThemes[mLongClickPosition]

    init {
        mCurThemes = emptyList<ThemeEntity>()
        mCurQPacks = emptyList<QPackEntity>()
    }
}
