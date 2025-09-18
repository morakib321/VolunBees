package com.example.projectattempt1;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

public class Community extends AppCompatActivity {

    EditText postText;
    ImageView postImage;
    Button chooseImageBtn, postBtn;
    LinearLayout postsLayout;
    byte[] imageBytes;
    DatabaseManager dbManager;
    String username;
    int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //the database
        dbManager = new DatabaseManager(this);
        username = getIntent().getStringExtra("username");
        userId = dbManager.getUserId(username);


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_community);

        postText = findViewById(R.id.postText);
        postImage = findViewById(R.id.postImage);
        chooseImageBtn = findViewById(R.id.chooseImageBtn);
        postBtn = findViewById(R.id.postBtn);
        postsLayout = findViewById(R.id.postsLayout);

        chooseImageBtn.setOnClickListener(v -> pickImage());
        postBtn.setOnClickListener(v -> postSomething());

        loadPosts();
    }

    private void pickImage() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, 123);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 123 && resultCode == RESULT_OK) {
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), data.getData());
                postImage.setImageBitmap(bitmap);

                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                imageBytes = stream.toByteArray();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void postSomething() {
        String content = postText.getText().toString();
        if (!content.isEmpty()) {
            dbManager.insertPost(userId, content, imageBytes);

            postText.setText("");
            postImage.setImageResource(R.drawable.ic_launcher_background);
            imageBytes = null;

            loadPosts();
        }
    }


    private void loadPosts() {
        postsLayout.removeAllViews();
        List<Post> allPosts = dbManager.getAllPosts();
        for (Post post : allPosts) {
            String posterUsername = dbManager.getUsernameById(post.userId);

            TextView contentView = new TextView(this);
            contentView.setText("@" + posterUsername + ": " + post.content + "\n🕒 " + post.timestamp);

            ImageView imageView = new ImageView(this);
            if (post.image != null) {
                Bitmap bmp = BitmapFactory.decodeByteArray(post.image, 0, post.image.length);
                imageView.setImageBitmap(bmp);
                imageView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 300));
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            }

            postsLayout.addView(contentView);
            postsLayout.addView(imageView);
        }
    }


    //this method will go to the homepage
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
