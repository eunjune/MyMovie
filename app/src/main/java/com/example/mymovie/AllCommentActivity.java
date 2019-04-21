package com.example.mymovie;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.ArrayList;

public class AllCommentActivity extends AppCompatActivity {
    private TextView tvTitle;

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
            ArrayList<CommentItem> list = (ArrayList<CommentItem>) intent.getSerializableExtra("commentsList");
            String title = intent.getStringExtra("title");
            String strRating = intent.getStringExtra("strRating");
            float rating = intent.getFloatExtra("rating", 0f);

            tvTitle = (TextView) findViewById(R.id.tv_title);
            TextView tvRating = (TextView) findViewById(R.id.tv_rating);
            RatingBar ratingBar = (RatingBar) findViewById(R.id.ratingBar);
            ListView listView = (ListView) findViewById(R.id.listView);
            CommentAdapter adapter = new CommentAdapter(list, getApplicationContext());

            tvTitle.setText(title);
            tvRating.setText(strRating + getString(R.string.all_comment_rating));
            ratingBar.setRating(rating);
            listView.setAdapter(adapter);

            index = intent.getIntExtra("index",-1);
        }

    }

    private void showCommentWriteActivity() {
        Intent intent = new Intent();
        intent.putExtra("title",tvTitle.getText().toString());
        intent.putExtra("index",index);
        setResult(RESULT_OK, intent);
        finish();
    }
}
