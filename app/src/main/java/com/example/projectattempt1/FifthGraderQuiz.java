package com.example.projectattempt1;

import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.View;
import android.widget.*;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class FifthGraderQuiz extends AppCompatActivity {
    private Button startBtn, nextBtn;
    private DatabaseManager db;
    private int userId;
    private String username;

    private TextView questionText, timerText, scoreText;
    private RadioGroup optionsGroup;
    private RadioButton option1, option2, option3, option4;
    private CountDownTimer countDownTimer;

    private List<Question> allQuestions = new ArrayList<>();
    private List<String> globalOptionsPool = new ArrayList<>();
    private List<Integer> questionInADice = new ArrayList<>();
    private List<String> currentOptions;
    private int currentIndex = 0;
    private int score = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fifth_grader_quiz);
        setupViews();

        db = new DatabaseManager(this);
        username = getIntent().getStringExtra("username");
        userId = db.getUserId(username);


        loadGlobalOptions();
        loadQuestions();

        for (int i = 0; i < allQuestions.size(); i++) {
            questionInADice.add(i);
        }
        Collections.shuffle(questionInADice);
        questionInADice = questionInADice.subList(0, 10);

        startBtn.setOnClickListener(v -> {
            startBtn.setVisibility(View.GONE);
            questionText.setVisibility(View.VISIBLE);
            optionsGroup.setVisibility(View.VISIBLE);
            nextBtn.setVisibility(View.VISIBLE);
            timerText.setVisibility(View.VISIBLE);
            showQuestion();
        });

        nextBtn.setOnClickListener(v -> {
            countDownTimer.cancel();

            int selectedId = optionsGroup.getCheckedRadioButtonId();
            if (selectedId == -1) {
                Toast.makeText(this, "Please select an answer", Toast.LENGTH_SHORT).show();
                return;
            }

            int selectedIndex = optionsGroup.indexOfChild(findViewById(selectedId));
            String selectedAnswer = currentOptions.get(selectedIndex);
            Question currentQ = allQuestions.get(questionInADice.get(currentIndex));

            boolean correct = selectedAnswer.equals(currentQ.getCorrectAnswer());
            if (correct) score += 5;
            goToNextQuestion();
        });
    }

    private void setupViews() {
        questionText = findViewById(R.id.questionText);
        optionsGroup = findViewById(R.id.optionsGroup);
        option1 = findViewById(R.id.option1);
        option2 = findViewById(R.id.option2);
        option3 = findViewById(R.id.option3);
        option4 = findViewById(R.id.option4);
        nextBtn = findViewById(R.id.nextBtn);
        timerText = findViewById(R.id.timerText);
        scoreText = findViewById(R.id.scoreText);
        startBtn = findViewById(R.id.startBtn);

        questionText.setVisibility(View.GONE);
        optionsGroup.setVisibility(View.GONE);
        nextBtn.setVisibility(View.GONE);
        timerText.setVisibility(View.GONE);
        scoreText.setVisibility(View.GONE);
    }

    private void goToNextQuestion() {
        currentIndex++;
        if (currentIndex < questionInADice.size()) {
            showQuestion();
        } else {
            endQuiz();
        }
    }
    private void unlockAchievementIfNeeded(int userId, String achName, String genre) {
        if (!db.userHasAchievement(userId, achName)) {
            Achievement achievement = new Achievement(achName, genre);
            db.insertAchievementForUser(achievement, userId);
            Toast.makeText(this, "Achievement unlocked: " + achName, Toast.LENGTH_LONG).show();
        }
    }

    private void endQuiz() {
        questionText.setBackgroundResource(R.drawable.ligh_green_square);
        questionText.setTextColor(getResources().getColor(R.color.darkGreen));
        optionsGroup.setVisibility(View.GONE);
        nextBtn.setVisibility(View.GONE);
        timerText.setVisibility(View.GONE);
        scoreText.setVisibility(View.VISIBLE);

        Achievement achievement = new Achievement("Fifth Grade Wizard", "Education Mastery");
        // Save best score for Game 2
        db.setUserBestScore(userId, 2, score);
        int totalScore = db.getUserScore(userId) + score;
        db.updateUserScore(userId, totalScore);



        if (score >= 40) {
            unlockAchievementIfNeeded(userId, "Wizard", "Education Mastery");
        }
        // Update user score in DB here
        if (score == 50) {
            if (!db.userHasAchievement(userId, achievement.getName())) {
                questionText.setText("Perfect Score!\n" + achievement.toString());
                //the user will only get their score updated if they got the perfect score
                db.updateUserScore(userId, score + db.getUserScore(userId));
                //insert the user achievement
                db.insertAchievementForUser(achievement, userId);

                questionText.setBackgroundColor(Color.GREEN);
                questionText.setTextColor(Color.BLACK);
            } else {
                questionText.setText("Perfect Score!\nAchievement already unlocked!");
            }
        } else if (score >= 40) {
            questionText.setText("WOW! You're smart!");
        } else {
            questionText.setText("Try again!");
        }

        scoreText.setText("Your final score: " + score + "/50");

        new Handler(getMainLooper()).postDelayed(this::finish, 5000);
    }


    private void startTimer() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }

        countDownTimer = new CountDownTimer(6000, 1000) {
            public void onTick(long millisUntilFinished) {
                timerText.setText("Time left: " + millisUntilFinished / 1000 + "s");
            }

            public void onFinish() {
                timerText.setText("Time's up!");
                if (optionsGroup.getCheckedRadioButtonId() == -1) {
                    goToNextQuestion();
                } else {
                    nextBtn.performClick();
                }
            }
        }.start();
    }

    private void loadGlobalOptions() {
        globalOptionsPool.addAll(Arrays.asList(
                "Einstein", "Newton", "Tesla", "Edison",
                "3", "4", "5", "6",
                "Mercury", "Earth", "Mars", "Jupiter",
                "George Washington", "Lincoln", "Obama", "Jefferson",
                "Amazon", "Mississippi", "Nile", "Yangtze",
                "Shakespeare", "Dickens", "Rowling", "Twain",
                "Bacteria", "Viruses", "Fungi", "Protozoa",
                "299,792 km/s", "150,000 km/s", "1,000 km/s", "3,000 km/s",
                "Triangle", "Square", "Hexagon", "Pentagon",
                "Oxygen", "Hydrogen", "Carbon", "Helium"
        ));
    }

    private void loadQuestions() {
        try {
            InputStream is = getAssets().open("fifthGraderQuestions.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();

            String json = new String(buffer, StandardCharsets.UTF_8);
            JSONArray jsonArray = new JSONArray(json);

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject obj = jsonArray.getJSONObject(i);
                String questionText = obj.getString("questionText");
                String correctAnswer = obj.getString("correctAnswer");
                allQuestions.add(new Question(questionText, correctAnswer));
            }

            questionInADice.clear(); // Just in case
            for (int i = 0; i < allQuestions.size(); i++) {
                questionInADice.add(i);
            }
            Collections.shuffle(questionInADice);
            questionInADice = questionInADice.subList(0, Math.min(10, questionInADice.size()));

        } catch (IOException | JSONException e) {
            e.printStackTrace();
            Toast.makeText(this, "Failed to load questions", Toast.LENGTH_LONG).show();
        }
    }


    private void showQuestion() {
        optionsGroup.clearCheck();
        Question currentQ = allQuestions.get(questionInADice.get(currentIndex));
        questionText.setText(currentQ.getQuestionText());

        currentOptions = currentQ.getShuffledOptions(globalOptionsPool);
        option1.setText(currentOptions.get(0));
        option2.setText(currentOptions.get(1));
        option3.setText(currentOptions.get(2));
        option4.setText(currentOptions.get(3));

        startTimer();
    }
}
