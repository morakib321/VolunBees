package com.example.projectattempt1;

import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
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

public class TeachingQuiz extends AppCompatActivity {
    private Button startBtn;
    private DatabaseManager db;
    private int userId;
    private String username;


    private TextView questionText;
    private RadioGroup optionsGroup;
    private RadioButton option1, option2, option3, option4;
    private Button nextBtn;
    private TextView timerText, scoreText;
    private CountDownTimer countDownTimer;


    private List<Question> allQuestions = new ArrayList<>();
    private List<String> globalOptionsPool = new ArrayList<>();
    private List<Integer> questionInADice = new ArrayList<>();

    private int currentIndex = 0;
    private int score = 0;
    private List<String> currentOptions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teaching_quiz);
        setupViews();
        db = new DatabaseManager(this);
        username = getIntent().getStringExtra("username");
        userId = db.getUserId(username);


        loadGlobalOptions();
        loadQuestionsFromJson();


        for (int i = 0; i < allQuestions.size(); i++) {
            questionInADice.add(i);
        }
        Collections.shuffle(questionInADice);
        questionInADice = questionInADice.subList(0, 6);

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
            if (selectedAnswer.equals(currentQ.getCorrectAnswer())) {
                score += 5;
            }

            goToNextQuestion();
        });

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
        questionText.setText("Quiz Complete!");
        questionText.setBackgroundResource(R.drawable.ligh_green_square);
        questionText.setTextColor(getResources().getColor(R.color.darkGreen));
        optionsGroup.setVisibility(View.GONE);
        nextBtn.setVisibility(View.GONE);
        timerText.setVisibility(View.GONE);
        scoreText.setVisibility(View.VISIBLE);

        int maxScore = questionInADice.size() * 5;
        scoreText.setText("Your final score: " + score + "/" + maxScore);

        Achievement achievement = new Achievement("Teacher Level Genius", "Teaching Quiz Master");

        if (score == maxScore) {
            if (!db.userHasAchievement(userId, achievement.getName())) {
                questionText.setText("Perfect Score!\n" + achievement.toString());
                //the user will get their score updated only if they got the perfect score
                db.updateUserScore(userId, score);
                //insert the user achievment
                db.insertAchievementForUser(achievement, userId);
                questionText.setBackgroundColor(Color.GREEN);
                questionText.setTextColor(Color.BLACK);
            } else {
                questionText.setText("Perfect Score!\nAchievement already unlocked!");
            }
        } else if (score >= maxScore * 0.8) {
            questionText.setText("Great job! You're almost a teacher!");
        } else {
            questionText.setText("Not bad! Study more to reach teacher level!");
        }

        new android.os.Handler(android.os.Looper.getMainLooper()).postDelayed(() -> {
            this.finish();
        }, 4000);
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

        //Initially hide everything except the Start button
        questionText.setVisibility(View.GONE);
        optionsGroup.setVisibility(View.GONE);
        nextBtn.setVisibility(View.GONE);
        timerText.setVisibility(View.GONE);
        scoreText.setVisibility(View.GONE);

    }


    private void loadGlobalOptions() {
        // Mix of plausible options to draw from
        globalOptionsPool.addAll(Arrays.asList(
                "Sydney", "Melbourne", "Canberra", "Perth",
                "Emoji", "Nothing", "A again", "Omega",
                "Jupiter", "Mars", "Venus", "Mercury",
                "15", "21", "29", "1",
                "Jane Austen", "Mark Twain", "William Shakespeare", "Charles Dickens",
                "Python", "Java", "Cobra", "HTML",
                "5", "6", "7", "8",
                "Finger", "Toe", "Ear", "Nose",
                "Salt", "Water", "Hydrogen", "Oxygen",
                "50°C", "100°C", "150°C", "200°C",
                "Elephant", "Hyena", "Dolphin", "Parrot",
                "Calf", "Kid", "Cub", "Pup",
                "6", "8", "10", "12",
                "10", "11", "12", "13",
                "France", "USA", "Italy", "Mexico",
                "30", "60", "90", "100",
                "Cheetah", "Horse", "Gazelle", "Tiger",
                "Hydrogen", "Carbon Dioxide", "Oxygen", "Nitrogen",
                "January", "April", "February", "June",
                "Sparkle", "Glimmer", "Tinker Bell", "Bellatrix"
        ));
    }

    private void loadQuestionsFromJson() {
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
    private void startTimer() {
        if (countDownTimer != null) countDownTimer.cancel();

        countDownTimer = new CountDownTimer(6000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timerText.setText("Time left: " + millisUntilFinished / 1000 + "s");
            }

            @Override
            public void onFinish() {
                timerText.setText("Time's up!");

                if (optionsGroup.getCheckedRadioButtonId() == -1) {
                    //No answer selected, skipppppp
                    goToNextQuestion();
                } else {
                    nextBtn.performClick();
                }
            }
        }.start();
    }

}
