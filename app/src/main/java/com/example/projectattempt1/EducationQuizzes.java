package com.example.projectattempt1;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class EducationQuizzes extends AppCompatActivity {

    String username;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_education_quizzes);
        username = getIntent().getStringExtra("username");
    }


    public void goToTeacherQuiz(View v){

        Intent intent = new Intent(this, TeachingQuiz.class);
        intent.putExtra("username", username);
        this.startActivity(intent);
    }

    public void goTo5thGraderQuiz(View v){
        String username = getIntent().getStringExtra("username");
        Intent intent = new Intent(this, FifthGraderQuiz.class);
        intent.putExtra("username", username);
        this.startActivity(intent);
    }


    public void goToHomePage(View v){
        Intent intent = new Intent(this, HomePage.class);
        intent.putExtra("username", username);
        startActivity(intent);
    }

    public void goToQuizzes(View v){
        Intent intent = new Intent(this, QuizzesActivity.class);
        intent.putExtra("username", username);
        startActivity(intent);
    }

    public void goToMessages(View v){
//        Intent intent = new Intent(this, HomePage.class);
//        intent.putExtra("username", username);
//        startActivity(intent);
    }

    public void goToProfile(View v){
//        Intent intent = new Intent(this, QuizzesActivity.class);
//        intent.putExtra("username", username);
//        startActivity(intent);
    }
}