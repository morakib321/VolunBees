package com.example.projectattempt1;

public class Achievement {
    private String achievementName;
    private String achievementGenre;

    public Achievement(String achievementName, String achievementGenre) {
        this.achievementName = achievementName;
        this.achievementGenre = achievementGenre;
    }

    public String getGenre() {
        return achievementGenre;
    }

    public String getName() {
        return achievementName;
    }

    public void setName(String achievementName) {
        this.achievementName = achievementName;
    }

    public void setGenre(String achievementGenre) {
        this.achievementGenre = achievementGenre;
    }

    @Override
    public String toString() {
        return "Achievement: " + achievementName + "\nGenre: " + achievementGenre;
    }
}
