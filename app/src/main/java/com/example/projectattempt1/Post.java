package com.example.projectattempt1;

public class Post {
    public int id, userId;
    public String content, timestamp;
    public byte[] image;

    public Post(int id, int userId, String content, byte[] image, String timestamp) {
        this.id = id;
        this.userId = userId;
        this.content = content;
        this.image = image;
        this.timestamp = timestamp;
    }
}
