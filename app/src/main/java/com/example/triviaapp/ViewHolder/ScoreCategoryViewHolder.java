package com.example.triviaapp.ViewHolder;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.triviaapp.R;

public class ScoreCategoryViewHolder extends RecyclerView.ViewHolder {

    public TextView cardCategory, cardScore;

    public ScoreCategoryViewHolder(@NonNull View itemView) {
        super(itemView);

        cardCategory = itemView.findViewById(R.id.cardCategory);
        cardScore = itemView.findViewById(R.id.cardScore);


    }
}
