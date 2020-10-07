package com.example.triviaapp.admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.triviaapp.R;
import com.example.triviaapp.model.Category;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AddCategory extends AppCompatActivity {

    Button insert, addQuestions;
    ImageButton prev;
    EditText name, image;
    DatabaseReference dbCategory;
    Category category;
    String size;


    @Override
    protected
    void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_category);


        insert = findViewById(R.id.insert);
        addQuestions = findViewById(R.id.addQuestions);
        name = findViewById(R.id.name);
        image = findViewById(R.id.image);
        prev = findViewById(R.id.prev);

        dbCategory = FirebaseDatabase.getInstance().getReference("Category");

        insert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                category = new Category(name.getText().toString(), image.getText().toString());
                dbCategory.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot dsp : dataSnapshot.getChildren()) {
                            size = dsp.getKey();
                        }
                        if(name.getText().toString().isEmpty() || image.getText().toString().isEmpty()){
                            Toast.makeText(AddCategory.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                        }else if(dataSnapshot.child(category.getName()).exists()){
                            Toast.makeText(AddCategory.this, "Category already exists", Toast.LENGTH_SHORT).show();
                        }else{
                            int childSize = Integer.parseInt(size);
                            childSize++;
                            dbCategory.child(String.valueOf(childSize)).setValue(category);
                            Toast.makeText(AddCategory.this, "The category was added successfully", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });

        addQuestions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddCategory.this, AddQuestions.class);
                startActivity(intent);
                finish();
            }
        });

        prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddCategory.this, AdminPage.class);
                startActivity(intent);
                finish();
            }
        });

    }
}
