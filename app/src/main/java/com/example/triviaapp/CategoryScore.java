package com.example.triviaapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.triviaapp.ViewHolder.ScoreCategoryViewHolder;
import com.example.triviaapp.model.QuestionScore;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class CategoryScore extends AppCompatActivity {

    FirebaseDatabase database;
    DatabaseReference question_score;

    String viewUser = "";
    RecyclerView scoreList;
    RecyclerView.LayoutManager layoutManager;
    FirebaseRecyclerAdapter<QuestionScore, ScoreCategoryViewHolder> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.category_score);

        database = FirebaseDatabase.getInstance();
        question_score = database.getReference("Question_Score");

        scoreList = findViewById(R.id.scoreList);
        scoreList.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        scoreList.setLayoutManager(layoutManager);

        if (getIntent() != null) {
            viewUser = getIntent().getStringExtra("viewUser");
        }
        assert viewUser != null;
        if (!viewUser.isEmpty()) {
            loadScoreCategory(viewUser);
        }

    }

    private void loadScoreCategory(String viewUser) {
        adapter = new FirebaseRecyclerAdapter<QuestionScore, ScoreCategoryViewHolder>(
                QuestionScore.class, R.layout.score_category, ScoreCategoryViewHolder.class, question_score.orderByChild("user").equalTo(viewUser)) {
            @Override
            protected void populateViewHolder(ScoreCategoryViewHolder scoreCategoryViewHolder, QuestionScore questionScore, int i) {
                scoreCategoryViewHolder.cardCategory.setText(questionScore.getCategoryName());
                scoreCategoryViewHolder.cardScore.setText(String.valueOf(questionScore.getScore()));
            }
        };
        adapter.notifyDataSetChanged();
        scoreList.setAdapter(adapter);
    }
}

