package com.example.projectattempt1;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class QuizzesActivity extends AppCompatActivity {
    DatabaseManager dbManager;
    String username;
    @Override
   protected void onCreate(Bundle savedInstanceState) {
       super.onCreate(savedInstanceState);
       setContentView(R.layout.activity_quizzes);
       displayScore();

   }

   public void displayScore(){
       dbManager = new DatabaseManager(this);
       TextView scoreTextView = findViewById(R.id.score);

       //Get the username passed from the previous activity
       username = getIntent().getStringExtra("username");

       //if the username is not null (found)
       if (username != null && !username.isEmpty()) {
           //we will get the user ID from the database
           int userId = dbManager.getUserId(username);
           //if the ID exists
           if (userId != -1) {
               //we get the score of the user
               int score = dbManager.getUserScore(userId);
               //And then set it and show it ont the screen
               scoreTextView.setText(String.valueOf(score));
           } else {
               //User ID not found
               scoreTextView.setText("0");
           }
       } else {
           //Username not passed or empty
           scoreTextView.setText("0");
       }
   }


    public void goToHealthcareQuizPage(View v){
        Intent intent = new Intent(this, HealthcareQuizzes.class);
        intent.putExtra("username", getIntent().getStringExtra("username")); // pass username along!
        startActivity(intent);

    }

    public void goToIntelligenceQuizPage(View v){
        Intent intent = new Intent(this, EducationQuizzes.class);
        intent.putExtra("username", getIntent().getStringExtra("username")); // pass username along!
        this.startActivity(intent);
    }

    public void goToSafetyQuiz(View v){
        Intent intent = new Intent(this, SafetyAwarenessQuiz.class);
        intent.putExtra("username", getIntent().getStringExtra("username")); // pass username along!
        this.startActivity(intent);
    }





    @Override
    protected void onStart() {
        super.onStart();
        displayScore();

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
        Intent intent = new Intent(this, Community.class);
        intent.putExtra("username", username);
        startActivity(intent);
    }

    public void goToProfile(View v){
        Intent intent = new Intent(this, ProfilePage.class);
        intent.putExtra("username", username);
        startActivity(intent);
    }
}