package com.example.projectattempt1;

import java.util.*;

public class Question {
    private final String questionText;
    private final String correctAnswer;

    public Question(String questionText, String correctAnswer) {
        this.questionText = questionText;
        this.correctAnswer = correctAnswer;
    }

    public String getQuestionText() {
        return questionText;
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }

    // This returns 4 shuffled options, always including the correct answer
    public List<String> getShuffledOptions(List<String> globalOptionsPool) {
        Set<String> optionsSet = new HashSet<>(globalOptionsPool);
        optionsSet.remove(correctAnswer); // Avoid duplicate correct answers

        List<String> options = new ArrayList<>(optionsSet);
        Collections.shuffle(options);
        options = options.subList(0, 3); // Pick 3 random wrong answers

        options.add(correctAnswer); // Add correct answer
        Collections.shuffle(options); // Shuffle final options list

        return options;
    }
}
