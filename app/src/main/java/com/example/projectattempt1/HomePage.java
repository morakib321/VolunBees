package com.example.projectattempt1;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import java.util.List;

// Move your Event class outside or in its own file if you want reusability
class Event {
    public String name;
    public String location;
    public String date;
    public String requiredNationality;
    public int requiredAge;
    public boolean achievementRequired;

    public Event(String name, String location, String date, String requiredNationality, int requiredAge, boolean achievementRequired) {
        this.name = name;
        this.location = location;
        this.date = date;
        this.requiredNationality = requiredNationality;
        this.requiredAge = requiredAge;
        this.achievementRequired = achievementRequired;
    }
}

public class HomePage extends AppCompatActivity {

    DatabaseManager dbManager;

    // ---------- Double Linked List for Events ----------
    class EventNode {
        String title, location, date, requiredNationality;
        int requiredAge;
        boolean achievementRequired;
        EventNode next, prev;

        EventNode(String title, String location, String date, String requiredNationality, int requiredAge, boolean achievementRequired) {
            this.title = title;
            this.location = location;
            this.date = date;
            this.requiredNationality = requiredNationality;
            this.requiredAge = requiredAge;
            this.achievementRequired = achievementRequired;
            this.next = this.prev = null;
        }
    }
    EventNode headEvent = null, currentEvent = null;

    // ---------- Double Linked List for Achievements ----------
    class AchNode {
        String text;
        AchNode next, prev;
        AchNode(String text) {
            this.text = text;
            next = prev = null;
        }
    }
    AchNode headAch = null, currentAch = null;

    // UI elements
    TextView eventTitle, achText, welcomeText;
    ImageButton addEventBtn;
    LinearLayout leaderboardLayout;
    ImageView firstAidIcon, wizardIcon, safetyIcon;
    ImageView firstAidLock, wizardLock, safetyLock;

    private String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        // Receive username passed from login page
        Intent intent = getIntent();
        username = intent.getStringExtra("username");

        // Find views
        eventTitle = findViewById(R.id.eventTitle);
        addEventBtn = findViewById(R.id.addEventBtn);
        welcomeText = findViewById(R.id.welcomeText);
        leaderboardLayout = findViewById(R.id.leaderboardLayout);
        firstAidIcon = findViewById(R.id.first_aid_icon);
        wizardIcon = findViewById(R.id.wizard_icon);
        safetyIcon = findViewById(R.id.safety_icon);

        firstAidLock = findViewById(R.id.first_aid_lock);
        wizardLock = findViewById(R.id.wizard_lock);
        safetyLock = findViewById(R.id.safety_lock);

        welcomeText.setText("Welcome, " + username + "!");

        // Initialize dbManager
        dbManager = new DatabaseManager(this);

        // Get role for the current user
        String role = dbManager.getRoleByUsername(username);

        // Only seekers can see the plus button
        boolean isSeeker = "Seeker".equalsIgnoreCase(role);
        addEventBtn.setVisibility(isSeeker ? View.VISIBLE : View.GONE);

        // Build lists
        buildEventList();



        // Show first event and achievement
        showEvent();

        RelativeLayout eventCard = findViewById(R.id.eventCard);
        eventCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkVolunteerEligibility();
            }
        });
        fillLeaderboard();
        updateAchievements();
    }

    // --------- Event Linked List Logic ----------
    private void buildEventList() {
        dbManager = new DatabaseManager(this);
        List<Event> eventList = dbManager.getAllEvents();
        if (eventList.size() > 0) {
            for (int i = eventList.size() - 1; i >= 0; i--) {
                Event data = eventList.get(i);
                EventNode node = new EventNode(
                        data.name,
                        data.location,
                        data.date,
                        data.requiredNationality,
                        data.requiredAge,
                        data.achievementRequired
                );
                if (headEvent == null) {
                    headEvent = node;
                    currentEvent = node;
                } else {
                    node.next = headEvent;
                    headEvent.prev = node;
                    headEvent = node;
                }
            }
        } else {
            headEvent = new EventNode("No Events", "", "", "", 0, false);
            currentEvent = headEvent;
        }
    }

    private void showEvent() {
        // Display event info
        if (currentEvent != null) {
            eventTitle.setText(
                    currentEvent.title + "\n" +
                            currentEvent.location + "\n" +
                            currentEvent.date
                    // You can also display requiredNationality, requiredAge, etc if you want
            );
        } else {
            eventTitle.setText("No Events");
        }
    }

    public void onLeftEvent(View v) {
        if (currentEvent != null && currentEvent.prev != null) {
            currentEvent = currentEvent.prev;
            showEvent();
        }
    }

    public void onRightEvent(View v) {
        if (currentEvent != null && currentEvent.next != null) {
            currentEvent = currentEvent.next;
            showEvent();
        }
    }

    public void onLeftAchievement(View v) {
        if (currentAch != null && currentAch.prev != null) {
            currentAch = currentAch.prev;
            showAchievement();
        }
    }

    public void onRightAchievement(View v) {
        if (currentAch != null && currentAch.next != null) {
            currentAch = currentAch.next;
            showAchievement();
        }
    }

    private void showAchievement() {
        if (currentAch != null) {
            achText.setText(currentAch.text);
        }
    }



    //Leaderboard ----------
    private void fillLeaderboard() {
        TableLayout leaderboardLayout = findViewById(R.id.leaderboardLayout);
        leaderboardLayout.removeAllViews(); // Clear previous data

        //Table Header ---
        TableRow headerRow = new TableRow(this);

        TextView nameHeader = new TextView(this);
        nameHeader.setText("Name");
        nameHeader.setTextSize(17f);
        nameHeader.setTypeface(null, android.graphics.Typeface.BOLD);
        nameHeader.setPadding(20, 16, 20, 16);

        TextView pointsHeader = new TextView(this);
        pointsHeader.setText("Points");
        pointsHeader.setTextSize(17f);
        pointsHeader.setTypeface(null, android.graphics.Typeface.BOLD);
        pointsHeader.setPadding(40, 16, 20, 16);

        headerRow.addView(nameHeader);
        headerRow.addView(pointsHeader);
        leaderboardLayout.addView(headerRow);

        //Table Data Rows ---
        List<DatabaseManager.UserPoints> leaderboard = dbManager.getLeaderboard();

        for (DatabaseManager.UserPoints user : leaderboard) {
            TableRow row = new TableRow(this);

            TextView nameCell = new TextView(this);
            nameCell.setText(user.username != null ? user.username : "No Name");
            nameCell.setTextSize(15f);
            nameCell.setPadding(20, 8, 20, 8);

            TextView pointsCell = new TextView(this);
            pointsCell.setText(String.valueOf(user.points));
            pointsCell.setTextSize(15f);
            pointsCell.setPadding(40, 8, 20, 8);
            pointsCell.setGravity(android.view.Gravity.END); // right-align points
            pointsCell.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1f));

            row.addView(nameCell);
            row.addView(pointsCell);

            leaderboardLayout.addView(row);
        }
    }

    private void checkVolunteerEligibility() {
        // Only volunteers can participate
        String role = dbManager.getRoleByUsername(username);
        if (!"Volunteer".equalsIgnoreCase(role)) {
            Toast.makeText(this, "Only volunteers can participate.", Toast.LENGTH_SHORT).show();
            return;
        }
        // Get current event's details
        if (currentEvent == null || "No Events".equals(currentEvent.title)) {
            Toast.makeText(this, "No event selected.", Toast.LENGTH_SHORT).show();
            return;
        }
        // Get volunteer details
        int userId = dbManager.getUserId(username);
        String userCountry = dbManager.getUserNationality(userId);
        int userAge = dbManager.getUserAge(userId);
        boolean achievementRequired = currentEvent.achievementRequired;
        int minAge = currentEvent.requiredAge;
        String eventRequiredNationality = currentEvent.requiredNationality;

        // Get user's achievements
        List<String> userAchievements = dbManager.getUserAchievements(userId);
        // Nationality logic
        boolean nationalityEligible;
        if ("UAE".equalsIgnoreCase(eventRequiredNationality)) {
            nationalityEligible = "United Arab Emirates".equalsIgnoreCase(userCountry);
        } else if ("Non-UAE".equalsIgnoreCase(eventRequiredNationality)) {
            nationalityEligible = !"United Arab Emirates".equalsIgnoreCase(userCountry);
        } else {
            nationalityEligible = true; // For "Any"
        }

        // Age logic (assuming max age is 65)
        boolean ageEligible = (userAge >= minAge && userAge <= 65);

        // Achievement logic
        boolean achievementEligible = !achievementRequired || (userAchievements != null && !userAchievements.isEmpty());

        // Final eligibility
        if (nationalityEligible && ageEligible && achievementEligible) {
            Toast.makeText(this, "You are eligible for this event!", Toast.LENGTH_LONG).show();
            Toast.makeText(this, "Booking confirmed", Toast.LENGTH_LONG).show();
        } else {
            StringBuilder reason = new StringBuilder();
            if (!nationalityEligible) reason.append("Nationality does not match. ");
            if (!ageEligible) reason.append("Age not in required range. ");
            if (!achievementEligible) reason.append("Achievement required. ");
            Toast.makeText(this, "Not eligible: " + reason, Toast.LENGTH_LONG).show();
        }
    }

    // --------- Add Event (+) Button ----------
    public void onAddEvent(View v) {
        Intent intent = new Intent(HomePage.this, AddEventActivity.class);
        intent.putExtra("username", username);
        startActivity(intent);
    }

    // --------- Navigation Methods ----------
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
    private void updateAchievements() {
        int userId = dbManager.getUserId(username);

        // Get best scores from your new UserGameScores table
        int scoreGame1 = dbManager.getUserBestScore(userId, 1); // Game 1
        int scoreGame2 = dbManager.getUserBestScore(userId, 2); // Game 2
        int scoreGame3 = dbManager.getUserBestScore(userId, 3); // Game 3
        int scoreGame4 = dbManager.getUserBestScore(userId, 4); // Game 4

        boolean hasFirstAid = scoreGame1 >= 80;
        boolean hasWizard = (scoreGame2 >= 80) && (scoreGame3 >= 80);
        boolean hasSafety = scoreGame4 >= 80;

        // Update UI for First Aid Master
        firstAidIcon.setAlpha(hasFirstAid ? 1.0f : 0.4f);
        firstAidLock.setVisibility(hasFirstAid ? View.GONE : View.VISIBLE);

        // Update UI for Wizard
        wizardIcon.setAlpha(hasWizard ? 1.0f : 0.4f);
        wizardLock.setVisibility(hasWizard ? View.GONE : View.VISIBLE);

        // Update UI for Safety Expert
        safetyIcon.setAlpha(hasSafety ? 1.0f : 0.4f);
        safetyLock.setVisibility(hasSafety ? View.GONE : View.VISIBLE);
    }

}