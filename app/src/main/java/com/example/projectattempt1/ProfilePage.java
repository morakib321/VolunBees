package com.example.projectattempt1;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class ProfilePage extends AppCompatActivity {

    TextView nameView;
    TextView hoursView;
    TextView scoreView;

    DatabaseManager db;
    String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_page);

        nameView = findViewById(R.id.textViewName);
        hoursView = findViewById(R.id.textViewHours);
        scoreView = findViewById(R.id.textViewScore);

        username = getIntent().getStringExtra("username");

        db = new DatabaseManager(this);

        String fullName = db.getFullNameByUsername(username);
        int volunteeringHours = db.getUserHoursByUsername(username);
        int score = db.getUserScore(db.getUserId(username));

        nameView.setText("Name: " + fullName);
        hoursView.setText(""+volunteeringHours+" Hours");
        scoreView.setText(""+ score + " Score");
    }

    // Navigation methods
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