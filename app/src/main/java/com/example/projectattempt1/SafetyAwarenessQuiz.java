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

public class SafetyAwarenessQuiz extends AppCompatActivity {
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
        setContentView(R.layout.activity_safety_awareness_quiz);
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

    private void endQuiz() {
        questionText.setBackgroundResource(R.drawable.ligh_green_square);
        questionText.setTextColor(getResources().getColor(R.color.darkGreen));
        optionsGroup.setVisibility(View.GONE);
        nextBtn.setVisibility(View.GONE);
        timerText.setVisibility(View.GONE);
        scoreText.setVisibility(View.VISIBLE);

        Achievement achievement = new Achievement("Safety Legend", "Awareness Mastery");

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
                "Raise the alarm and evacuate immediately",
                "Call a friend",
                "Hide under a table",
                "Keep calm and wait",

                "911",
                "123",
                "999",
                "112",

                "Look both ways before crossing",
                "Run quickly across",
                "Close your eyes and walk",
                "Cross only on red light",

                "Bend your knees and keep your back straight",
                "Lift with your back",
                "Ask someone else to lift",
                "Hold your breath",

                "To protect your head in case of an accident",
                "To look cool",
                "To keep warm",
                "To block the sun",

                "Do not touch it and notify authorities",
                "Open it immediately",
                "Move it to another room",
                "Throw it outside",

                "At least once a month",
                "Once a year",
                "Every day",
                "Never",

                "Stop moving, drop to the ground, and roll to smother flames",
                "Run outside",
                "Call for help",
                "Try to put fire out",

                "Standing near windows or heavy objects",
                "Standing in a doorway",
                "Under a sturdy table",
                "Outside in the open",

                "To ensure quick and safe evacuation",
                "To keep it clean",
                "For decoration",
                "To block people"
        ));
    }


    private void loadQuestions() {
        try {
            InputStream is = getAssets().open("safetyQuestions.json");
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
