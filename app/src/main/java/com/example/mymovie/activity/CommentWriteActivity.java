package com.example.mymovie.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.mymovie.R;
import com.example.mymovie.data.MovieDetailInfo;
import com.example.mymovie.network.NetworkManager;

public class CommentWriteActivity extends AppCompatActivity{

    private NetworkManager networkManager;

    private TextView tvTitle;
    private ImageView ivAge;
    private RatingBar ratingBar;
    private EditText etContents;
    private Button btnSave, btnCancel;

    private MovieDetailInfo movieDetailInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment_write);

        networkManager = new NetworkManager(getApplicationContext());

        Intent intent = getIntent();
        movieDetailInfo = (MovieDetailInfo) intent.getSerializableExtra("movieDetailInfo");

        tvTitle = (TextView) findViewById(R.id.tv_title);
        tvTitle.setText(movieDetailInfo.getTitle());

        ratingBar = (RatingBar) findViewById(R.id.ratingBar);

        etContents = (EditText) findViewById(R.id.et_contents);
        
        btnSave = (Button) findViewById(R.id.btn_save);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



            }
        });

        btnCancel = (Button) findViewById(R.id.btn_cancel);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
