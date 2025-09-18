package com.example.projectattempt1;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseManager extends SQLiteOpenHelper {

    //the name of the databse
    public static final String DATABASE_NAME = "VolunteerApp.db";
    //the version
    public static final int DATABASE_VERSION = 4;

    //constructor
    public DatabaseManager(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    //onCreate method
    @Override
    public void onCreate(SQLiteDatabase db) {
        //creating a user table
        String usersTable = "CREATE TABLE Users (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "name TEXT, " +
                "age INTEGER, " +
                "gender TEXT, " +
                "phone TEXT, " +
                "username TEXT, " +
                "password TEXT, " +
                "hours INTEGER, " +
                "nationality TEXT, " +
                "role TEXT, " +
                "score INTEGER DEFAULT 0)";

        db.execSQL(usersTable);

        String achievementsTable = "CREATE TABLE Achievements (" +
                "achievement_name TEXT PRIMARY KEY," +
                "genre TEXT)";
        db.execSQL(achievementsTable);

        String userAchievementsTable = "CREATE TABLE UserAchievements (" +
                "user_id INTEGER, " +
                "achievement_name TEXT, " +
                "PRIMARY KEY(user_id, achievement_name), " +
                "FOREIGN KEY(user_id) REFERENCES Users(id), " +
                "FOREIGN KEY(achievement_name) REFERENCES Achievements(achievement_name))";
        db.execSQL(userAchievementsTable);


        String postsTable = "CREATE TABLE Posts (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "user_id INTEGER, " +
                "content TEXT, " +
                "image BLOB, " +
                "timestamp DATETIME DEFAULT CURRENT_TIMESTAMP, " +
                "FOREIGN KEY(user_id) REFERENCES Users(id))";

        db.execSQL(postsTable);
        String eventsTable = "CREATE TABLE Events (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "name TEXT, " +
                "location TEXT, " +
                "date TEXT, " +
                "required_nationality TEXT, " +
                "required_age INTEGER, " +
                "achievement_required INTEGER DEFAULT 0)";
        db.execSQL(eventsTable);
        String userGameScoresTable = "CREATE TABLE UserGameScores (" +
                "user_id INTEGER, " +
                "game_number INTEGER, " + // 1 to 4
                "score INTEGER, " +
                "PRIMARY KEY(user_id, game_number), " +
                "FOREIGN KEY(user_id) REFERENCES Users(id))";
        db.execSQL(userGameScoresTable);


        // Insert 4 dummy records (the id is AUTOINCREMENT)
        db.execSQL("INSERT INTO Users (name, username, score) VALUES ('Ali Ahmed', 'ali', 120)");
        db.execSQL("INSERT INTO Users (name, username, score) VALUES ('Sara Fadel', 'sara', 80)");
        db.execSQL("INSERT INTO Users (name, username, score) VALUES ('Noor Saleh', 'noor', 150)");
        db.execSQL("INSERT INTO Users (name, username, score) VALUES ('Yousef Samir', 'yousef', 50)");

    }

    @Override

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS UserAchievements");
        db.execSQL("DROP TABLE IF EXISTS Achievements");
        db.execSQL("DROP TABLE IF EXISTS Users");
        db.execSQL("DROP TABLE IF EXISTS Posts");
        db.execSQL("DROP TABLE IF EXISTS Events");
        db.execSQL("DROP TABLE IF EXISTS UserGameScores");
        onCreate(db);
    }

    public void insertUser(String name, int age, String gender, String phone, String username, String password, int hours, String nationality, String role, int score) {
        SQLiteDatabase db = this.getWritableDatabase();

        String insert = "INSERT INTO Users (name, age, gender, phone, username, password, hours, nationality, role, score) " +
                "VALUES('" + name + "', " + age + ", '" + gender + "', '" + phone + "', '" + username + "', '"
                + password + "', " + hours + ", '" + nationality + "', '" + role + "', "
                + score + ")";
        db.execSQL(insert);
        db.close();
    }




    public boolean checkUsernameExists(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM Users WHERE username = '" + username + "'";
        Cursor cursor = db.rawQuery(query, null);
        boolean exists = cursor.moveToFirst();
        cursor.close();
        return exists;
    }

    public int getUserId(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT id FROM Users WHERE username = '" + username + "'";
        Cursor cursor = db.rawQuery(query, null);

        int userId = -1;
        if (cursor.moveToFirst()) {
            userId = cursor.getInt(0);
        }
        cursor.close();
        return userId;
    }



    public boolean validateLogin(String username, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM Users WHERE username = '" + username + "' AND password = '" + password + "'";
        Cursor cursor = db.rawQuery(query, null);
        boolean valid = cursor.moveToFirst();
        cursor.close();
        return valid;
    }

    public void insertAchievementForUser(Achievement ach, int userId) {
        SQLiteDatabase db = this.getWritableDatabase();

        String insert = "INSERT OR IGNORE INTO UserAchievements(user_id, achievement_name) " +
                "VALUES(" + userId + ", '" + ach.getName() + "')";
        db.execSQL(insert);

        db.close();
    }

    public boolean userHasAchievement(int userId, String achievementName) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM UserAchievements WHERE user_id = " + userId + " AND achievement_name = '" + achievementName + "'";
        Cursor cursor = db.rawQuery(query, null);
        boolean hasAchievement = cursor.moveToFirst();
        cursor.close();
        return hasAchievement;
    }

    public void updateUserScore(int userId, int newScore) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "UPDATE Users SET score = " + newScore + " WHERE id = " + userId;
        db.execSQL(query);
        db.close();
    }

    public int getUserScore(int userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT score FROM Users WHERE id = " + userId, null);
        int score = -1;
        if (cursor != null && cursor.moveToFirst()) {
            score = cursor.getInt(0);
            cursor.close();
        }
        return score;
    }

    public String getFullNameByUsername(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT name FROM Users WHERE username = '" + username + "'";
        Cursor cursor = db.rawQuery(query, null);

        String name = "";
        if (cursor.moveToFirst()) {
            name = cursor.getString(0);
        }
        cursor.close();
        return name;
    }

    // Save or update a user's best score for a game
    public void setUserBestScore(int userId, int gameNumber, int score) {
        SQLiteDatabase db = this.getWritableDatabase();
        // Only update if the new score is higher
        int currentBest = getUserBestScore(userId, gameNumber);
        if (score > currentBest) {
            ContentValues values = new ContentValues();
            values.put("user_id", userId);
            values.put("game_number", gameNumber);
            values.put("score", score);

            // Try update first, if not exists insert
            int rows = db.update("UserGameScores", values,
                    "user_id=? AND game_number=?",
                    new String[]{String.valueOf(userId), String.valueOf(gameNumber)});
            if (rows == 0) {
                db.insert("UserGameScores", null, values);
            }

        }
        db.close();

    }


    // Get user's best score for a game
    public int getUserBestScore(int userId, int gameNumber) {
        SQLiteDatabase db = this.getReadableDatabase();
        int score = 0; // default
        Cursor cursor = db.rawQuery("SELECT score FROM UserGameScores WHERE user_id=? AND game_number=?",
                new String[]{String.valueOf(userId), String.valueOf(gameNumber)});
        if (cursor.moveToFirst()) {
            score = cursor.getInt(0);
        }
        cursor.close();
        return score;
    }



    public int getUserAge(int userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        int age = -1;
        Cursor cursor = db.rawQuery("SELECT age FROM Users WHERE id = " + userId, null);
        if (cursor.moveToFirst()) {
            age = cursor.getInt(0);
        }
        cursor.close();
        return age;
    }
    public List<String> getUserAchievements(int userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        List<String> achievements = new ArrayList<>();

        String query = "SELECT achievement_name FROM UserAchievements WHERE user_id = " + userId;
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                achievements.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return achievements;
    }

    public String getUserNationality(int userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        String nationality = "";
        Cursor cursor = db.rawQuery("SELECT nationality FROM Users WHERE id = " + userId, null);
        if (cursor.moveToFirst()) {
            nationality = cursor.getString(0);
        }
        cursor.close();
        return nationality;
    }

    // Helper class
    public static class UserPoints {
        public String username;
        public int points;

        public UserPoints(int id, String username, int points) {
            this.username = username;
            this.points = points;
        }
    }

    public void insertPost(int userId, String content, byte[] imageBytes) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("user_id", userId);
        values.put("content", content);
        values.put("image", imageBytes);
        db.insert("Posts", null, values);
        db.close();
    }

    public List<Post> getAllPosts() {
        List<Post> posts = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM Posts ORDER BY timestamp DESC", null);

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
                int userId = cursor.getInt(cursor.getColumnIndexOrThrow("user_id"));
                String content = cursor.getString(cursor.getColumnIndexOrThrow("content"));
                byte[] image = cursor.getBlob(cursor.getColumnIndexOrThrow("image"));
                String timestamp = cursor.getString(cursor.getColumnIndexOrThrow("timestamp"));

                posts.add(new Post(id, userId, content, image, timestamp));
            } while (cursor.moveToNext());
        }

        cursor.close();
        return posts;
    }

    public List<Event> getAllEvents() {
        List<Event> eventList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT name, location, date, required_nationality, required_age, achievement_required FROM Events", null);
        if (cursor.moveToFirst()) {
            do {
                String name = cursor.getString(0);
                String location = cursor.getString(1);
                String date = cursor.getString(2);
                String requiredNationality = cursor.getString(3);
                int requiredAge = cursor.getInt(4);
                boolean achievementRequired = cursor.getInt(5) == 1;
                eventList.add(new Event(name, location, date, requiredNationality, requiredAge, achievementRequired));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return eventList;
    }

    public String getUsernameById(int userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT username FROM Users WHERE id = ?", new String[]{String.valueOf(userId)});

        String username = "Unknown";
        if (cursor.moveToFirst()) {
            username = cursor.getString(cursor.getColumnIndexOrThrow("username"));
        }

        cursor.close();
        return username;
    }

    public int getUserHoursByUsername(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        int hours = 0;
        String query = "SELECT hours FROM Users WHERE username = '" + username + "'";
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            hours = cursor.getInt(0);
        }
        cursor.close();
        return hours;
    }

    public String getRoleByUsername(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        String role = "";
        String query = "SELECT role FROM Users WHERE username = '" + username + "'";
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            role = cursor.getString(0);
        }
        cursor.close();
        return role;
    }

    public String getGenderByUsername(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        String gender = "";
        String query = "SELECT gender FROM Users WHERE username = '" + username + "'";
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            gender = cursor.getString(0);
        }
        cursor.close();
        return gender;
    }


    public void updateUsernameAndGender(int userId, String newUsername, String newGender) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "UPDATE Users SET username = '" + newUsername + "', gender = '" + newGender + "' WHERE id = " + userId;
        db.execSQL(query);
        db.close();
    }
    public void insertEvent(String name, String location, String date, String requiredNationality, int requiredAge, boolean achievementRequired) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("location", location);
        values.put("date", date);
        values.put("required_nationality", requiredNationality);
        values.put("required_age", requiredAge);
        values.put("achievement_required", achievementRequired ? 1 : 0);
        db.insert("Events", null, values);
        db.close();
    }

    public List<UserPoints> getLeaderboard() {
        List<UserPoints> leaderboard = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        // Select id, name, score from Users, ordered by score (highest first)
        String query = "SELECT id, name, score FROM Users ORDER BY score DESC";

        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(0);
                String name = cursor.getString(1);  // Get the name (not username)
                int score = cursor.getInt(2);
                leaderboard.add(new UserPoints(id, name, score));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return leaderboard;
    }




}
