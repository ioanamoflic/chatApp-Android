package com.mip.chatapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity {

    private TextInputEditText email, password;
    private Button login_button;

    FirebaseAuth firebaseAuth;
    TextView forgot_password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Intra in cont");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        firebaseAuth = FirebaseAuth.getInstance();

        email = (TextInputEditText) findViewById(R.id.email);
        password =  (TextInputEditText) findViewById(R.id.password);
        login_button =  findViewById(R.id.login_button);
        forgot_password =  findViewById(R.id.forgot_password);

        forgot_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, ResetPasswordActivity.class));
            }
        });

        login_button.setOnClickListener(l -> {
            String strEmail = Objects.requireNonNull(email.getText()).toString(),
                    strPassword = Objects.requireNonNull(password.getText()).toString();

            if (strEmail.isEmpty() || strPassword.isEmpty())
                Toast.makeText(LoginActivity.this, "Toate casutele trebuie sa fie completate!", Toast.LENGTH_LONG).show();
            else {
                firebaseAuth.signInWithEmailAndPassword(strEmail, strPassword).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();
                    } else
                        Toast.makeText(LoginActivity.this, "Conectare esuata!", Toast.LENGTH_SHORT).show();
                });

            }
        });
    }
}