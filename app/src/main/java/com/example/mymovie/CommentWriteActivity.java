package com.example.mymovie;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;

public class CommentWriteActivity extends AppCompatActivity {

    private TextView tvTitle;
    private RatingBar ratingBar;
    private EditText etContents;
    private Button btnSave, btnCancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment_write);

        tvTitle = (TextView) findViewById(R.id.tv_title);
        ratingBar = (RatingBar) findViewById(R.id.ratingBar);
        etContents = (EditText) findViewById(R.id.et_contents);
        btnSave = (Button) findViewById(R.id.btn_save);
        btnCancel = (Button) findViewById(R.id.btn_cancel);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                returnToMain();
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Intent intent = getIntent();
        processIntent(intent);
    }

    private void processIntent(Intent intent) {
        if (intent != null) {
            String title = intent.getStringExtra("title");
            tvTitle.setText(title);
        }

    }

    private void returnToMain() {
        String contents = etContents.getText().toString();
        float rating = ratingBar.getRating();

        Intent intent = new Intent();
        intent.putExtra("contents", contents);
        intent.putExtra("rating", rating);
        setResult(RESULT_OK, intent);
        finish();
    }

}
