package com.example.triviaapp.ViewHolder;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.triviaapp.Interface.ItemClickListener;
import com.example.triviaapp.R;

public class RankingViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

    public TextView cardName, cardScore;
    private ItemClickListener itemClickListener;


    public RankingViewHolder(@NonNull View itemView) {
        super(itemView);
        cardName = itemView.findViewById(R.id.cardName);
        cardScore= itemView.findViewById(R.id.cardScore);

        itemView.setOnClickListener(this);

    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public void onClick(View v) {
        itemClickListener.onClick(v, getAdapterPosition(), false);
    }


}
