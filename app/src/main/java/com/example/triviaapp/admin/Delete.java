package com.example.triviaapp.admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
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

public class Delete extends AppCompatActivity {

    private Spinner categorySpinner, questionSpinner;
    private DatabaseReference categoryDb, questionDb;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete);

        categorySpinner = findViewById(R.id.categorySpinner);
        questionSpinner = findViewById(R.id.questionSpinner);
        Button delete = findViewById(R.id.delete);
        ImageButton prev = findViewById(R.id.prev);



        categoryDb = FirebaseDatabase.getInstance().getReference("Category");
        questionDb = FirebaseDatabase.getInstance().getReference("Questions");


        database(categoryDb,spinner(categorySpinner," category"));

        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                database(questionDb, spinner(questionSpinner, " question"));
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Toast.makeText(Delete.this, "Please choose a category", Toast.LENGTH_SHORT).show();
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (questionSpinner.getSelectedItem() != "Set question"){
                    questionDb.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for (DataSnapshot dsp : dataSnapshot.getChildren()) {
                                String deleteQuestion = String.valueOf(Objects.requireNonNull(dsp.getValue(Question.class)).getQuestion());
                                if (deleteQuestion.equals(questionSpinner.getSelectedItem())) {
                                    dsp.getRef().removeValue();
                                    Toast.makeText(Delete.this, "The question has been deleted", Toast.LENGTH_SHORT).show();
                                }

                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
            }
        });

        prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Delete.this, AdminPage.class);
                startActivity(intent);
                finish();
            }
        });

    }

    public List<String> spinner(Spinner spinner, String string){
        final List<String> list = new ArrayList<>();
        list.add(0, "Set" + string);

        ArrayAdapter<String> dataAdapter;
        dataAdapter = new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(dataAdapter);
        return list;
    }

    public void database(final DatabaseReference database, final List<String> list){
        database.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dsp : dataSnapshot.getChildren()) {
                    if(database.equals(categoryDb)) {
                        list.add(String.valueOf(Objects.requireNonNull(dsp.getValue(Category.class)).getName()));
                    }else{
                        if(String.valueOf(categorySpinner.getSelectedItemPosition()).equals(dsp.getValue(Question.class).getCategoryId())) {
                           if (!dsp.getValue(Question.class).getIsImageQuestion()) {
                               list.add(String.valueOf(Objects.requireNonNull(dsp.getValue(Question.class)).getQuestion()));
                           }
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }


}
