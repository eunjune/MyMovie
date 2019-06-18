package com.example.mymovie.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.mymovie.CommentAdapter;
import com.example.mymovie.R;
import com.example.mymovie.data.CommentInfo;
import com.example.mymovie.data.MovieDetailInfo;

import java.util.ArrayList;

public class AllCommentActivity extends AppCompatActivity {
    private TextView tvTitle, tvRating, tvTotal;
    private ImageView ivAge;
    private RatingBar ratingBar;

    private ArrayList<CommentInfo> commentList;
    private MovieDetailInfo movieDetailInfo;

    private int index;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_comment);

        tvTitle = (TextView) findViewById(R.id.tv_title);

        Intent intent = getIntent();
        processIntent(intent);


        Button btnWrite = (Button) findViewById(R.id.write_btn);
        btnWrite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCommentWriteActivity();
            }
        });
    }

    private void processIntent(Intent intent) {
        if (intent != null) {
            commentList = (ArrayList<CommentInfo>) intent.getSerializableExtra("commentList");
            movieDetailInfo = (MovieDetailInfo)intent.getSerializableExtra("movieDetailInfo");
            index = intent.getIntExtra("index",-1);

            tvTitle = (TextView) findViewById(R.id.tv_title);
            tvTitle.setText(movieDetailInfo.getTitle());

            ivAge = (ImageView) findViewById(R.id.iv_age);
            ivAge.setImageResource(movieDetailInfo.getGradeImageId());

            ratingBar = (RatingBar) findViewById(R.id.ratingBar);
            ratingBar.setRating(movieDetailInfo.getAvgRating()/2f);

            tvRating = (TextView) findViewById(R.id.tv_rating);
            tvRating.setText(String.valueOf(movieDetailInfo.getAvgRating()));

            tvTotal = (TextView)findViewById(R.id.tv_total);
            tvTotal.setText(getResources().getString(R.string.all_comment_left_bucket)
                + intent.getIntExtra("total",0)
                + getResources().getString(R.string.all_comment_rating)
                + getResources().getString(R.string.all_comment_right_bucket));
                               
            ListView listView = (ListView) findViewById(R.id.listView);
            CommentAdapter adapter = new CommentAdapter(commentList, getApplicationContext());

            listView.setAdapter(adapter);

        }

    }

    private void showCommentWriteActivity() {
        Intent intent = new Intent();
        intent.putExtra("movieDetailInfo",movieDetailInfo);
        intent.putExtra("index",index);
        setResult(RESULT_OK, intent);
        finish();
    }

}
