package com.example.projectattempt1;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class HealthcareQuizzes extends AppCompatActivity {
    String loggedInUsername;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_healthcare_quizzes);
        //getting the user name
        loggedInUsername = getIntent().getStringExtra("username");
    }

    public void goToCPRforAdults(View v){
        Intent intent = new Intent(this, CPR_Quiz.class);
        intent.putExtra("username", loggedInUsername);
        startActivity(intent);

    }


    public void goToHomePage(View v){
        Intent intent = new Intent(this, HomePage.class);
        intent.putExtra("username", loggedInUsername);
        startActivity(intent);
    }

    public void goToQuizzes(View v){
        Intent intent = new Intent(this, QuizzesActivity.class);
        intent.putExtra("username", loggedInUsername);
        startActivity(intent);
    }

    public void goToMessages(View v){
//        Intent intent = new Intent(this, HomePage.class);
//        intent.putExtra("username", loggedInUsername);
//        startActivity(intent);
    }

    public void goToProfile(View v){
//        Intent intent = new Intent(this, QuizzesActivity.class);
//        intent.putExtra("username", loggedInUsername);
//        startActivity(intent);
    }
}