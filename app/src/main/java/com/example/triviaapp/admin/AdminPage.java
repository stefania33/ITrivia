package com.example.triviaapp.admin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.example.triviaapp.MainActivity;
import com.example.triviaapp.R;

public class AdminPage extends AppCompatActivity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_page);

        Button users = findViewById(R.id.users);
        Button add = findViewById(R.id.add);
        Button delete = findViewById(R.id.delete);
        Button addCategory = findViewById(R.id.addCategory);
        ImageButton prev = findViewById(R.id.prev);


        users.setOnClickListener(this);
        add.setOnClickListener(this);
        delete.setOnClickListener(this);
        addCategory.setOnClickListener(this);
        prev.setOnClickListener(this);


    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.prev:
                Intent intent = new Intent(AdminPage.this, MainActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.users:
                Intent mapIntent = new Intent(AdminPage.this, CurrentLocation.class);
                startActivity(mapIntent);
                finish();
                break;
            case R.id.addCategory:
                Intent addcategories = new Intent(AdminPage.this, AddCategory.class);
                startActivity(addcategories);
                finish();
                break;
            case R.id.add:
                Intent addQuestions = new Intent(AdminPage.this, AddQuestions.class);
                startActivity(addQuestions);
                finish();
                break;
            case R.id.delete:
                Intent deleteQuestion = new Intent(AdminPage.this, Delete.class);
                startActivity(deleteQuestion);
                finish();
                break;
        }
    }
}



