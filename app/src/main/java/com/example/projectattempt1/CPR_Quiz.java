package com.example.projectattempt1;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class CPR_Quiz extends AppCompatActivity implements View.OnTouchListener {

    TextView questionTxt, countChestCompressionTxt;
    DatabaseManager dbManager;
    String currentUsername;
    int currentUserId;

    RadioButton option1, option2, option3, option4;
    ImageView guyOnTheFloor;
    Button startButton, submitBtn;
    View radioGroup;
    int countChestCompression = 0;
    //this boolean prevents multiple touches from jumping ahead
    boolean isTouchHandled = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cpr);
        countChestCompression = 0;


        dbManager = new DatabaseManager(this);

        // Example: get username from intent extras (adjust as per your app)
        currentUsername = getIntent().getStringExtra("username");
        currentUserId = dbManager.getUserId(currentUsername);

        countChestCompressionTxt = findViewById(R.id.countChestCompressionTxt);
        startButton = findViewById(R.id.startButton);
        submitBtn = findViewById(R.id.submitBtn);
        questionTxt = findViewById(R.id.questionTxt);
        option1 = findViewById(R.id.option1);
        option2 = findViewById(R.id.option2);
        option3 = findViewById(R.id.option3);
        option4 = findViewById(R.id.option4);
        guyOnTheFloor = findViewById(R.id.guyOnFloorImg);

        radioGroup = findViewById(R.id.radioGroup);
    }

    //Initialize a variable to follow-up on the questions step by step
    int currentQuestion = 0;
    //the score will get it from the user account
    int score = 0;

    public void cprQuiz(View view) {
        //make the start button invisible
        startButton.setVisibility(View.GONE);
        submitBtn.setVisibility(View.VISIBLE);
        //make the image, question text, and buttons visible
        guyOnTheFloor.setVisibility(View.VISIBLE);
        questionTxt.setVisibility(View.VISIBLE);
        radioGroup.setVisibility(View.VISIBLE);
        countChestCompression = 0;
        currentQuestion = 0;
        //Call the method to show the next question
        showNextQuestion();
    }

    private void showNextQuestion() {
        // Reset visuals
        questionTxt.setBackgroundResource(R.drawable.ligh_green_square);
        clearRadioButtons(); // uncheck all

        //there will be fixed cases
        switch (currentQuestion) {
            //the first case will ask the user what to do
            //when we see someone uncincious
            case 0:
                guyOnTheFloor.setOnTouchListener(null);
                submitBtn.setVisibility(View.VISIBLE);
                questionTxt.setText("You stumbled on an unconsious person... What's the first thing you do?");
                //The user will pick a choice
                option1.setText("Start CPR");
                option2.setText("Check if the scene is safe"); //this is correct
                option3.setText("Shout to wake them up");
                option4.setText("Call for help");
                break;

            case 1:
                guyOnTheFloor.setOnTouchListener(null);
                submitBtn.setVisibility(View.VISIBLE);
                questionTxt.setText("The surroundings are safe, what’s next?");
                option1.setText("Check pulse");
                option2.setText("Start CPR");
                option3.setText("Shout & tap to wake them up");//this is correct
                option4.setText("Call 999");
                break;
            case 2:
                guyOnTheFloor.setOnTouchListener(null);
                submitBtn.setVisibility(View.VISIBLE);
                questionTxt.setText("There is no response from them... What to do?!");
                option1.setText("Call 999"); //this is the answer
                option2.setText("Start CPR");
                option3.setText("Check breathing");
                option4.setText("Tap and shout again");
                break;

            case 3:
                guyOnTheFloor.setOnTouchListener(null);
                submitBtn.setVisibility(View.VISIBLE);
                questionTxt.setText("You cheked their breathing... it is abnormal. What's next?");
                option1.setText("Wait for them to wake up");
                option2.setText("Put them in recovery position");
                option3.setText("Give them water");
                option4.setText("Start CPR"); //this is correct
                break;

            case 4:

                questionTxt.setText("Where should you place your hands on the patient?\n(Use on the image!)");
                radioGroup.setVisibility(View.GONE);
                submitBtn.setVisibility(View.GONE);
                guyOnTheFloor.setOnTouchListener(this);

                //i want to check if the user clicked on the right dimension in terms of X and Y
                //so it should be between from X: 1375 to X: 1411  and between  Y: 293 to Y: 315
                break;
            case 5:
                questionTxt.setText("Do chest compressions with the same hand placement!\nHINT: More than 14 compressions!");
                radioGroup.setVisibility(View.GONE);
                submitBtn.setVisibility(View.GONE);
                guyOnTheFloor.setOnTouchListener(this);
                break;
            case 6:
                guyOnTheFloor.setOnTouchListener(null);
                questionTxt.setText("After chest compression...");
                option1.setText("Wait and see if they wake up");
                option2.setText("Chest compressions again");
                option3.setText("Give 2 rescue breaths "); //this is correct
                option4.setText("Shout & tap to wake them up");
                radioGroup.setVisibility(View.VISIBLE);
                submitBtn.setVisibility(View.VISIBLE);
                break;
            default:
                questionTxt.setBackgroundResource(R.drawable.green_square);
                questionTxt.setTextSize(30);
                questionTxt.setText("Quiz complete! Your score: " + score);
                radioGroup.setVisibility(View.GONE);
                submitBtn.setVisibility(View.GONE);

                // Insert the new score and award achievement for perfect score if applicable
                handleQuizCompletion();

                new android.os.Handler().postDelayed(this::finish, 4000);
                break;
        }
    }

    // This should be called when the user presses a "Next" or "Submit" button:
    public void checkAnswer(View view) {
        boolean correct = false;

        switch (currentQuestion) {
            case 0:
                if (option2.isChecked()) correct = true;
                break;
            case 1:
            case 6:
                if (option3.isChecked()) correct = true;
                break;
            case 2:
                if (option1.isChecked()) correct = true;
                break;
            case 3:
                if (option4.isChecked()) correct = true;
                break;


        }

        if (correct) {
            score += 10;
            questionTxt.setBackgroundResource(R.drawable.green_square);
            questionTxt.setText("CORRECT!");
        } else {
            questionTxt.setBackgroundResource(R.drawable.red_square);
            questionTxt.setText("Oops! That’s not quite right.");
        }
        submitBtn.setVisibility(View.GONE);
        //wait before showing next
        new android.os.Handler().postDelayed(() -> {
            currentQuestion++;
            clearRadioButtons();
            showNextQuestion();
        }, 2000);
    }



    private void clearRadioButtons() {
        ((RadioGroup) radioGroup).clearCheck();
    }


    @Override
    public boolean onTouch(View v, MotionEvent event) {
        //this condition checks if the touched item is the image of the guy on the floor
        //AND
        //if the event that is happening is a DOWN or the item being presed on
        if (v.getId() == R.id.guyOnFloorImg && event.getAction() == MotionEvent.ACTION_DOWN) {

            //if the touch handle is true, then it will ignore it
            /*it means that if there was any action on the image during the cases
             * that does not need any action on the image (like in questions of cases 0, 1, 2...)
             * then we ignore these touches*/
            if (isTouchHandled) {
                return true; //ignore extra taps
            }
            //other wise, if the touching was necessary, then we continue
            isTouchHandled = true;

            //if the current question is not of cases 4 or 5 where the touch action is necessary
            if (currentQuestion != 4 && currentQuestion != 5) {
                //then we skip
                return false;
            }

            //this will return the X value where the ACTION_DOWN happened
            int touchX = (int) event.getX();
            //this will return the Y value where the ACTION_DOWN happened
            int touchY = (int) event.getY();

            //This boolean checks if the specified X and Y access when ACTION_DOWN where pressed on the right coordinations
            boolean correctPlacement = touchX >= 1205 && touchX <= 1270 && touchY >= 275 && touchY <= 330;
            //this is just to check on the value
            Log.w("Touch", "X: " + touchX + " Y: " + touchY);

            //if the question is at case 4, which is about placing the hand on the right placement
            if (currentQuestion == 4) {
                //if it was the correct placement
                if (correctPlacement) {
                    //then we give the user 20 more scores
                    score += 20;
                    //and we print a message to them with a green background
                    questionTxt.setBackgroundResource(R.drawable.green_square);
                    questionTxt.setText("CORRECT hand placement!");
//*********************************************************************************************************************************
                    //LET ME ASK THE DR IF ANIMATION IS FINE OR NOT
                    //this is for animation!
                    //the scale of the image would increase id the handplacement is correct
                    Animation scaleAnimation = AnimationUtils.loadAnimation(this, R.anim.pulse);
                    guyOnTheFloor.startAnimation(scaleAnimation);

                } else {
                    //if it was the wrong placement, then we give the user the message with red background
                    questionTxt.setBackgroundResource(R.drawable.red_square);
                    questionTxt.setText("Oops! Wrong hand placement");
                }

                //make the image untouchable again
                guyOnTheFloor.setOnTouchListener(null);
                //make the next question appear after 2 seconds
                new android.os.Handler().postDelayed(() -> {
                    currentQuestion++;
                    showNextQuestion();
                    //reset touch handle for the next questions
                    isTouchHandled = false;
                }, 2000);

                return true;
            }

            if (currentQuestion == 5) {
                //if the question is to do chest compressions
                //the user has to tap on the guy on the floor more than 14 times
                countChestCompression++;
                countChestCompressionTxt.setText(String.valueOf(countChestCompression));
                if (countChestCompression > 29) {
                    score += 30;
                    questionTxt.setBackgroundResource(R.drawable.green_square);
                    questionTxt.setText("Great job! Chest compressions count: " + countChestCompression);

                    new android.os.Handler().postDelayed(() -> {
                        currentQuestion++;
                        showNextQuestion();
                        countChestCompression = 0;
                        countChestCompressionTxt.setText("0");
                        //make the image untouchable again
                        guyOnTheFloor.setOnTouchListener(null);
                        //reset touch handle for the next questions
                        isTouchHandled = false;
                    }, 2000);

                    return true;
                }

                //if it is not more than 14 taps, then just give feedback with the current count
                questionTxt.setBackgroundResource(R.drawable.ligh_green_square);
                questionTxt.setText("Chest compressions count: " + countChestCompression);

                //allow further taps
                isTouchHandled = false;
                return true;
            }

        }
        return false;
    }

    // Insert the new score and award achievement for perfect score if applicable
    private void handleQuizCompletion() {

        // Save score as best for Game 1 (CPR)
        dbManager.setUserBestScore(currentUserId, 1, score);
        int totalScore = dbManager.getUserScore(currentUserId) + score;
        dbManager.updateUserScore(currentUserId, totalScore);

        // Award achievement for Game 1 if score >= 80
        if (score >= 80) {
            unlockAchievementIfNeeded(currentUserId, "First Aid Master", "First Aid");
        }

        Toast.makeText(this, "Your score: " + score, Toast.LENGTH_SHORT).show();
    }

    private void unlockAchievementIfNeeded(int userId, String achName, String genre) {
        if (!dbManager.userHasAchievement(userId, achName)) {
            Achievement achievement = new Achievement(achName, genre);
            dbManager.insertAchievementForUser(achievement, userId);
            Toast.makeText(this, "Achievement unlocked: " + achName, Toast.LENGTH_LONG).show();
        }
    }


    private void awardAchievementForPerfectScore() {
        if (currentUserId == -1) {
            Toast.makeText(this, "User not found. Cannot award achievement.", Toast.LENGTH_SHORT).show();
            return;
        }

        String achievementName = "CPR Master";

        if (!dbManager.userHasAchievement(currentUserId, achievementName)) {
            Achievement achievement = new Achievement(achievementName, "CPR");
            //insert the new achievement
            dbManager.insertAchievementForUser(achievement, currentUserId);
            //update the score of the user
            int updatedScore = score + dbManager.getUserScore(currentUserId);
            //insert it into the database
            dbManager.updateUserScore(currentUserId, updatedScore);

            Toast.makeText(this, "🎉 Achievement Unlocked: CPR Master! Perfect Score! 🎉", Toast.LENGTH_LONG).show();
        }
    }

}
