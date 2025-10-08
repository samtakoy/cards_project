package ru.samtakoy.core.presentation.log

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ru.samtakoy.R
import ru.samtakoy.core.presentation.log.MyLog.strings

class LogFragment : Fragment() {
    private var mRecyclerView: RecyclerView? = null
    private var mLog: MutableList<String>? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(R.layout.fragment_log, container, false)

        mLog = strings

        mRecyclerView = v.findViewById<RecyclerView>(R.id.recycler_view)
        mRecyclerView!!.setLayoutManager(LinearLayoutManager(getContext()))
        mRecyclerView!!.setAdapter(RecyclerViewAdapter())

        // items divider
        val divider = DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL)
        //divider.setDrawable(...);
        mRecyclerView!!.addItemDecoration(divider)

        return v
    }

    private class RecyclerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val mText: TextView

        init {
            mText = itemView.findViewById<TextView>(R.id.list_item_text)
        }

        fun setText(s: String?) {
            mText.setText(s)
        }
    }

    private inner class RecyclerViewAdapter : RecyclerView.Adapter<RecyclerViewHolder?>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerViewHolder {
            return RecyclerViewHolder(
                LayoutInflater.from(getContext()).inflate(R.layout.log_list_item, parent, false)
            )
        }

        override fun onBindViewHolder(holder: RecyclerViewHolder, position: Int) {
            holder.setText(mLog!!.get(position))
        }

        override fun getItemCount(): Int {
            return mLog?.size ?: 0
        }
    }

    companion object {
        @JvmStatic fun newFragment(): Fragment {
            val result = LogFragment()
            return result
        }
    }
}




