package ru.samtakoy.core.presentation.online_import

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ru.samtakoy.R
import ru.samtakoy.core.data.network.pojo.RemoteFile

internal class RecyclerAdapter : RecyclerView.Adapter<RecyclerAdapter.MyViewHolder?>() {
    private var mFiles: MutableList<RemoteFile> = ArrayList<RemoteFile>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val v = LayoutInflater.from(parent.getContext()).inflate(R.layout.remote_file_list_item, parent, false)
        return MyViewHolder(v)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(mFiles.get(position))
    }

    override fun getItemCount(): Int {
        return mFiles.size
    }

    fun setFiles(files: MutableList<RemoteFile>) {
        mFiles = files
        notifyDataSetChanged()
    }

    fun updateFile(file: RemoteFile) {
        // ?
        mFiles.set(mFiles.indexOf(file), file)
        notifyItemChanged(mFiles.indexOf(file))
    }

    internal class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val mText: TextView

        init {
            mText = itemView.findViewById<TextView>(R.id.title)
        }

        fun bind(remoteFile: RemoteFile) {
            mText.setText(remoteFile.path)
        }
    }
}
