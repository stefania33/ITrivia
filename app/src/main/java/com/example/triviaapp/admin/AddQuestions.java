package com.example.triviaapp.admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.triviaapp.R;
import com.example.triviaapp.model.Category;
import com.example.triviaapp.model.Question;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class AddQuestions extends AppCompatActivity {
    EditText question, answer1, answer2, answer3, answer4;
    RadioGroup radioGroup;
    RadioButton radioButton, radio1, radio2, radio3, radio4;
    String correctAnswer;
    Spinner spinnerCategory;
    ImageButton prev;

    DatabaseReference dbQuestion, dbCategory;
    Question newQuestion;
    String size, names, ids;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_questions);

        dbQuestion = FirebaseDatabase.getInstance().getReference("Questions");
        dbCategory = FirebaseDatabase.getInstance().getReference("Category");

        question = findViewById(R.id.question);
        answer1 = findViewById(R.id.answer1);
        answer2 = findViewById(R.id.answer2);
        answer3 = findViewById(R.id.answer3);
        answer4 = findViewById(R.id.answer4);

        radioGroup = findViewById(R.id.radioGroup);
        radio1 = findViewById(R.id.radio_answer1);
        radio2 = findViewById(R.id.radio_answer2);
        radio3 = findViewById(R.id.radio_answer3);
        radio4 = findViewById(R.id.radio_answer4);


        Button add = findViewById(R.id.add_button);
        prev = findViewById(R.id.prev);

        spinnerCategory = findViewById(R.id.spinnerCategory);
        final List<String> categories = new ArrayList<>();
        categories.add(0, "Set Category");

        final ArrayAdapter dataAdapter = new ArrayAdapter<>(getApplicationContext(), R.layout.support_simple_spinner_dropdown_item, categories);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinnerCategory.setAdapter(dataAdapter);
        dbCategory.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dsp : dataSnapshot.getChildren()) {
                    names = String.valueOf(Objects.requireNonNull(dsp.getValue(Category.class)).getName());
                    categories.add(names);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });


        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newQuestion = new Question(question.getText().toString(), answer1.getText().toString(), answer2.getText().toString(),
                        answer3.getText().toString(), answer4.getText().toString(), checkButton(v),
                        String.valueOf(spinnerCategory.getSelectedItemPosition()), false);
                dbQuestion.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot dsp : dataSnapshot.getChildren()) {
                            size = dsp.getKey();
                        }
                        if (question.getText().toString().isEmpty() || answer1.getText().toString().isEmpty()
                                || answer2.getText().toString().isEmpty() || answer3.getText().toString().isEmpty()
                                || answer4.getText().toString().isEmpty() || spinnerCategory.getSelectedItem().toString().equals("Set Category")) {
                            Toast.makeText(AddQuestions.this, "Please fill all the fields", Toast.LENGTH_SHORT).show();
                        } else if (dataSnapshot.child(newQuestion.getQuestion()).exists()) {
                            Toast.makeText(AddQuestions.this, "Question already exists", Toast.LENGTH_SHORT).show();
                        } else {
                            int childSize = Integer.parseInt(size);
                            childSize++;
                            dbQuestion.child(String.valueOf(childSize)).setValue(newQuestion);
                            Toast.makeText(AddQuestions.this, "The question was added successfully", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });

        prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddQuestions.this, AdminPage.class);
                startActivity(intent);
                finish();
            }
        });

    }
    private String checkButton(View v) {
        int radioId = radioGroup.getCheckedRadioButtonId();
        radioButton = findViewById(radioId);
        if (radioButton == radio1) {
            correctAnswer = answer1.getText().toString();
        } else if (radioButton == radio2) {
            correctAnswer = answer2.getText().toString();
        } else if (radioButton == radio3) {
            correctAnswer = answer3.getText().toString();
        } else {
            correctAnswer = answer4.getText().toString();
        }
        return correctAnswer;
    }






}


