package com.mip.chatapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.annotations.NotNull;

import java.util.HashMap;
import java.util.Objects;

public class RegisterActivity extends AppCompatActivity {

    TextInputEditText username, email, password;
    Button register_button;

    FirebaseAuth firebaseAuth;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Înregistrare");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        username = findViewById(R.id.username);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        register_button = findViewById(R.id.register_button);

        firebaseAuth = FirebaseAuth.getInstance();

        register_button.setOnClickListener((View v) -> {
            String strUsername = Objects.requireNonNull(username.getText()).toString(),
                    strEmail = Objects.requireNonNull(email.getText()).toString(),
                    strPassword = Objects.requireNonNull(password.getText()).toString();

            if (strUsername.isEmpty() || strEmail.isEmpty() || strPassword.isEmpty())
                Toast.makeText(RegisterActivity.this, "Toate casutele trebuie completate!", Toast.LENGTH_SHORT).show();
            else if (strPassword.length() < 6)
                Toast.makeText(RegisterActivity.this, "Parola trebuie sa contina minim 6 caractere", Toast.LENGTH_SHORT).show();
            else {
                Toast.makeText(RegisterActivity.this, "Inregistrare reusita!", Toast.LENGTH_SHORT).show();
                registerUser(strUsername, strEmail, strPassword);
            }
        });
    }

    private void registerUser(String username, String email, String password) {
        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener((Task<AuthResult> task) -> {
            if (task.isSuccessful()) {
                FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                String userID = firebaseUser.getUid();

                reference = FirebaseDatabase.getInstance().getReference("Users").child(userID);

                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("id", userID);
                hashMap.put("username", username);
                hashMap.put("imageURL", "default");
                hashMap.put("status", "offline");
                hashMap.put("search", username.toLowerCase());

                reference.setValue(hashMap).addOnCompleteListener((@NotNull Task<Void> task1) -> {
                    if (task1.isSuccessful()) {
                        Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();
                    }
                });
            } else {
                Toast.makeText(RegisterActivity.this, "Nu te poti inregistra cu aceasta adresa de e-mail!", Toast.LENGTH_LONG).show();
            }
        });
    }
}

//DUMMY COMMENT