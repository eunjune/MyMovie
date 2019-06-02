package com.example.mymovie.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.example.mymovie.R;
import com.github.chrisbanes.photoview.PhotoView;

public class PhotoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);

        Intent intent = getIntent();
        String url = intent.getStringExtra("url");

        PhotoView photoView = (PhotoView)findViewById(R.id.photoView);
        photoView.setImageURI(Uri.parse(url));
    }
}
