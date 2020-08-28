package ru.samtakoy.core.presentation.qpack;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ru.samtakoy.R;
import ru.samtakoy.core.database.room.entities.CardEntity;


public class CardsFastViewAdapter extends RecyclerView.Adapter<CardsFastViewAdapter.MyViewHolder> {


    private List<CardEntity> mCards;

    public CardsFastViewAdapter(List<CardEntity> cards) {

        mCards = cards;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.cards_fast_view_list_item, parent, false
        );
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.bind(mCards.get(position));
    }

    @Override
    public int getItemCount() {
        return mCards.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder{

        private TextView mTextQ;
        private TextView mTextA;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            mTextQ = itemView.findViewById(R.id.questionText);
            mTextA = itemView.findViewById(R.id.answerText);
        }

        public void bind(CardEntity card) {

            mTextQ.setText(card.getQuestion());
            mTextA.setText(card.getAnswer());
        }
    }

}




