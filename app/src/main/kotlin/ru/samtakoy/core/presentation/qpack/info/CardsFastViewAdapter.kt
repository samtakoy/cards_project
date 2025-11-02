package ru.samtakoy.core.presentation.qpack.info

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ru.samtakoy.R
import ru.samtakoy.presentation.qpacks.screens.fastlist.model.FastCardUiModel

// TODO remove
internal class CardsFastViewAdapter : RecyclerView.Adapter<CardsFastViewAdapter.MyViewHolder>() {
    private var mCards: List<FastCardUiModel>

    init {
        mCards = ArrayList<FastCardUiModel>()
    }

    fun setCards(cards: List<FastCardUiModel>) {
        mCards = cards
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val v = LayoutInflater.from(parent.getContext()).inflate(
            R.layout.cards_fast_view_list_item, parent, false
        )
        return MyViewHolder(v)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(mCards.get(position)!!)
    }

    override fun getItemCount(): Int {
        return mCards.size
    }

    internal class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val mTextQ: TextView
        private val mTextA: TextView

        init {
            mTextQ = itemView.findViewById<TextView>(R.id.questionText)
            mTextA = itemView.findViewById<TextView>(R.id.answerText)
        }

        fun bind(card: FastCardUiModel) {
            mTextQ.setText(card.question)
            mTextA.setText(card.answer)
        }
    }
}