package com.example.triviaapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;


import com.example.triviaapp.Common.Common;

import com.squareup.picasso.Picasso;


public class Playing extends AppCompatActivity implements View.OnClickListener {


    private int index = 0, score = 0, thisQuestion = 0, totalQuestion, correctAnswer = 0;
    private String totalNumber;
    private Button button1, button2, button3, button4;
    private ImageView question_image;
    private TextView txtscore, txtQuestionNum, question_text;
    private CardView cardView;
    CountDownTimer countDownTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playing);


        txtscore = findViewById(R.id.text_score);
        txtQuestionNum = findViewById(R.id.total_question);
        question_text = findViewById(R.id.question_text);
        question_image = findViewById(R.id.question_image);


        button1 = findViewById(R.id.first_answer);
        button2 = findViewById(R.id.second_answer);
        button3 = findViewById(R.id.third_answer);
        button4 = findViewById(R.id.fourth_answer);

        button1.setOnClickListener(this);
        button2.setOnClickListener(this);
        button3.setOnClickListener(this);
        button4.setOnClickListener(this);



    }


    @Override
    protected void onResume() {
        super.onResume();
        totalQuestion =  Common.questionList.size();
        showQuestion(index);
    }


    @SuppressLint("DefaultLocale")
    @Override
    public void onClick(View v) {
        if (index < totalQuestion) {
            Button clickedButton = (Button) v;
            if (clickedButton.getText().equals(Common.questionList.get(index).getCorrectAnswer())) {
                fadeView();
                score += 10;
                correctAnswer++;
                showQuestion(++index);
            } else {
                shakeAnimation();
                showQuestion(++index);

            }
            if (Common.questionList.isEmpty()) {
                Intent intent = new Intent(this, Done.class);
                Bundle dataSend = new Bundle();

                dataSend.putInt("SCORE", score);
                dataSend.putInt("TOTAL", totalQuestion);
                dataSend.putInt("CORRECT", correctAnswer);
                intent.putExtras(dataSend);
                startActivity(intent);
                finish();
            }

            txtscore.setText(String.format("%d", score));
        }


    }



    @SuppressLint({"DefaultLocale", "SetTextI18n"})
    private void showQuestion(int index) {
        if (index < totalQuestion) {
            thisQuestion++;
            txtQuestionNum.setText(thisQuestion + "/" + totalQuestion);
            if (Common.questionList.get(index).getIsImageQuestion()) {
                Picasso.get().load(Common.questionList.get(index).getQuestion()).into(question_image);
                question_image.setVisibility(View.VISIBLE);
                question_text.setVisibility(View.INVISIBLE);
            } else {
                question_text.setVisibility(View.VISIBLE);
                question_image.setVisibility(View.INVISIBLE);
            }
            question_text.setText(Common.questionList.get(index).getQuestion());
            button1.setText(Common.questionList.get(index).getAnswer1());
            button2.setText(Common.questionList.get(index).getAnswer2());
            button3.setText(Common.questionList.get(index).getAnswer3());
            button4.setText(Common.questionList.get(index).getAnswer4());

        } else {
            Intent intent = new Intent(this, Done.class);
            Bundle dataSend = new Bundle();

            dataSend.putInt("SCORE", score);
            dataSend.putInt("TOTAL", totalQuestion);
            dataSend.putInt("CORRECT", correctAnswer);
            intent.putExtras(dataSend);
            startActivity(intent);
            finish();

        }
    }

    private void fadeView() {
        cardView = findViewById(R.id.cardview);
        txtscore = findViewById(R.id.text_score);
        AlphaAnimation alphaAnimation = new AlphaAnimation(1.0f, 0.0f);
        alphaAnimation.setDuration(350);
        alphaAnimation.setRepeatCount(1);
        alphaAnimation.setRepeatMode(Animation.REVERSE);
        cardView.clearAnimation();
        txtscore.clearAnimation();
        cardView.setAnimation(alphaAnimation);
        txtscore.setAnimation(alphaAnimation);
        alphaAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                cardView.setCardBackgroundColor(Color.GREEN);
              txtscore.setTextColor(Color.GREEN);
            }
            @Override
            public void onAnimationEnd(Animation animation) {
                cardView.setCardBackgroundColor(Color.WHITE);
                txtscore.setTextColor(Color.WHITE);
            }
            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });

    }

    private void shakeAnimation() {
        Animation shake = AnimationUtils.loadAnimation(Playing.this, R.anim.shake_animation);
        cardView = findViewById(R.id.cardview);
        txtscore = findViewById(R.id.text_score);
        cardView.clearAnimation();
        txtscore.clearAnimation();
        cardView.setAnimation(shake);
        txtscore.setAnimation(shake);

        shake.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                cardView.setCardBackgroundColor(Color.RED);
                txtscore.setTextColor(Color.RED);
            }
            @Override
            public void onAnimationEnd(Animation animation) {
                 cardView.setCardBackgroundColor(Color.WHITE);
                txtscore.setTextColor(Color.WHITE);
            }
            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

    }
}




