package com.example.triviaapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.triviaapp.Common.Common;
import com.example.triviaapp.model.QuestionScore;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Locale;
import java.util.Objects;

public class Done extends AppCompatActivity {

    Button tryAgain;
    TextView resultScore, passedScore;
    ProgressBar progressBar;

    FirebaseDatabase database;
    DatabaseReference question_score;


    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_done);

        database = FirebaseDatabase.getInstance();
        question_score = database.getReference("Question_Score");

        resultScore = findViewById(R.id.text_totalScore);
        passedScore = findViewById(R.id.text_totalQuestion);
        progressBar = findViewById(R.id.doneProgressBar);
        tryAgain = findViewById(R.id.tryAgain_button);

        tryAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Done.this, HomeScreen.class);
                intent.putExtra("alreadyRun", true);
                startActivity(intent);
                finish();
            }
        });

        Bundle extra = getIntent().getExtras();
        if (extra != null) {

            final int score = extra.getInt("SCORE");
            int totalQuestion = extra.getInt("TOTAL");
            int correctAnswer = extra.getInt("CORRECT");

            resultScore.setText(String.format(Locale.US, "SCORE : %d", score));
            passedScore.setText(String.format(Locale.US, "PASSED: %d / %d", correctAnswer, totalQuestion));


            progressBar.setMax(totalQuestion);
            progressBar.setProgress(correctAnswer);

            question_score.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    if( !dataSnapshot.child(String.format("%s_%s", Common.currentUser.getUser(), Common.categoryId)).exists()
                            || score > Objects.requireNonNull(dataSnapshot.child
                            (String.format("%s_%s", Common.currentUser.getUser(), Common.categoryId))
                            .getValue(QuestionScore.class)).getScore() )
                    {
                        question_score.child(String.format("%s_%s", Common.currentUser.getUser(), Common.categoryId))
                                .setValue(new QuestionScore (Common.currentUser.getUser(),score, Common.categoryId, Common.categoryName));
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }
}
