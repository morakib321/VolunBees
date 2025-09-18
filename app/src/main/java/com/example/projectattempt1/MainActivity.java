package com.example.projectattempt1;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.*;

public class MainActivity extends Activity {

    EditText usernameLogin, passwordLogin;
    Button loginButton, registerBtn;
    DatabaseManager db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = new DatabaseManager(this);

        usernameLogin = findViewById(R.id.usernameLogin);
        passwordLogin = findViewById(R.id.passwordLogin);
        loginButton = findViewById(R.id.loginButton);
        registerBtn = findViewById(R.id.registerBtn);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginUser();
            }
        });


    }

    public void goToRegistrationPage(View v) {
        //Go to a register screen
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }
    private void loginUser() {
        String username = usernameLogin.getText().toString().trim();
        String password = passwordLogin.getText().toString().trim();

        if (username.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please enter both fields", Toast.LENGTH_SHORT).show();
            return;
        }

        //if the password and username were found in the database, then login successful
        if (db.validateLogin(username, password)) {
            Toast.makeText(this, "Login successful!", Toast.LENGTH_SHORT).show();

            // Optional: Go to homepage
            Intent intent = new Intent(this, HomePage.class);
            intent.putExtra("username", username); // pass data if needed
            startActivity(intent);
            finish(); // prevent returning to login
        } else {
            Toast.makeText(this, "Invalid username or password.", Toast.LENGTH_SHORT).show();
        }
    }
}
