package com.example.triviaapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.triviaapp.Common.Common;
import com.example.triviaapp.admin.AdminPage;
import com.example.triviaapp.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import org.jetbrains.annotations.NotNull;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {

    private EditText newPassword, newEmail, newUserName;
    private EditText editUser, editPassword;

    private FirebaseAuth auth;
    private DatabaseReference users;

    private static final Pattern USERNAME_PATTERN = Pattern.compile("^[a-z0-9_-]{3,15}$");
    private static final Pattern PASSWORD_PATTERN = Pattern.compile("^[a-z0-9_-]{6,20}$");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        auth = FirebaseAuth.getInstance();

        users = FirebaseDatabase.getInstance().getReference("Users");

        editPassword = findViewById(R.id.editPassword);
        editUser = findViewById(R.id.editUser);

        Button btnSignIn = findViewById(R.id.signIn_button);
        Button btnSignUp = findViewById(R.id.signUp_button);


        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSignUpDialog();
            }
        });

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn(editUser.getText().toString().trim(), editPassword.getText().toString().trim());
            }
        });
    }

    private void showSignUpDialog() {
        final AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
        alert.setTitle("Sign Up");
        alert.setMessage("Please fill all the fields");


        LayoutInflater inflater = this.getLayoutInflater();
        View sign_up = inflater.inflate(R.layout.sign_up, null);
        newPassword = sign_up.findViewById(R.id.newPassword);
        newEmail = sign_up.findViewById(R.id.newEmail);
        newUserName = sign_up.findViewById(R.id.newUserName);

        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void afterTextChanged(Editable s) {
                validateUserName(newUserName);
                validatePassword(newPassword);
                validateEmail(newEmail);
            }
        };

        newUserName.addTextChangedListener(textWatcher);
        newPassword.addTextChangedListener(textWatcher);
        newEmail.addTextChangedListener(textWatcher);


        alert.setView(sign_up);
        alert.setIcon(R.drawable.ic_account_circle_black_24dp);


        alert.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });


        alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (validateUserName(newUserName) && validatePassword(newPassword) && validateEmail(newEmail)) {
                    final User user = new User(newEmail.getText().toString().trim(),
                            newUserName.getText().toString().trim(), getMd5(newPassword.getText().toString().trim()));
                    users.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.child(user.getUser()).exists()) {
                                Toast.makeText(MainActivity.this, "User already exists", Toast.LENGTH_SHORT).show();
                            } else {
                                auth.createUserWithEmailAndPassword(newEmail.getText().toString(), newPassword.getText().toString())
                                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                            @Override
                                            public void onComplete(@NonNull Task<AuthResult> task) {
                                                if (task.isSuccessful()) {
                                                    users.child(user.getUser()).setValue(user);
                                                    Toast.makeText(MainActivity.this, "Registration successful", Toast.LENGTH_SHORT).show();
                                                } else {
                                                    Toast.makeText(MainActivity.this, "Email is already used", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                } else {
                    Toast.makeText(MainActivity.this, "Registration doesn't work. Please enter valid data.", Toast.LENGTH_SHORT).show();
                }

                dialog.dismiss();
            }

        });
        alert.show();
    }


    private void signIn(final String user, final String password) {
        users.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child(user).exists()) {
                    if (!user.isEmpty() && !password.isEmpty()) {
                        User login = dataSnapshot.child(user).getValue(User.class);
                        if (login.getPassword().equals(getMd5(password))) {
                            if (user.equals("admin")) {
                                Intent adminActivity = new Intent(MainActivity.this, AdminPage.class);
                                Common.currentUser = login;
                                startActivity(adminActivity);
                                finish();
                            } else {
                                Intent homeActivity = new Intent(MainActivity.this, HomeScreen.class);
                                Common.currentUser = login;
                                startActivity(homeActivity);
                                finish();
                            }
                        } else {
                            Toast.makeText(MainActivity.this, "Wrong Password", Toast.LENGTH_LONG).show(); }
                    } else {
                        Toast.makeText(MainActivity.this, "Please enter your username and your password", Toast.LENGTH_LONG).show(); }

                } else {
                    Toast.makeText(MainActivity.this, "User doesn't exist", Toast.LENGTH_LONG).show(); }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }


    private boolean validateEmail(@NotNull EditText email) {
        String emailInput = email.getText().toString().trim();
        if (emailInput.isEmpty()) {
            email.setError("Field can't be empty");
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(emailInput).matches()) {
            email.setError("Please enter a valid email address");
            return false;
        }
        return true;
    }

    private boolean validatePassword(@NotNull EditText password) {
        String passwordInput = password.getText().toString().trim();
        if (passwordInput.isEmpty()) {
            password.setError("Field can't be empty");
            return false;
        } else if (!PASSWORD_PATTERN.matcher(passwordInput).matches()) {
            password.setError("Password too weak");
            return false;
        }
        return true;
    }

    private boolean validateUserName(@NotNull EditText userName) {
        String userInput = userName.getText().toString().trim();
        if (userInput.isEmpty()) {
            userName.setError("Field can't be empty");
            return false;
        } else if (!USERNAME_PATTERN.matcher(userInput).matches()) {
            userName.setError("Please enter a valid username");
            return false;
        }
        return true;
    }

    public static String getMd5(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] messageDigest = md.digest(input.getBytes());
            BigInteger bigInteger= new BigInteger(1, messageDigest);
            String hashtext = bigInteger.toString(16);
            while (hashtext.length() < 32) {
                hashtext = "0" + hashtext;
            }
            return hashtext;
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }






}









