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
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.mymovie.AppHelper;
import com.example.mymovie.R;
import com.example.mymovie.data.MovieDetailInfo;
import com.example.mymovie.data.ResponseInfo;
import com.google.gson.Gson;

public class CommentWriteActivity extends AppCompatActivity{

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
                if(AppHelper.requestQueue == null) {
                    AppHelper.requestQueue = Volley.newRequestQueue(getApplicationContext());
                }

                createComment();
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

    private void createComment() {
        String url = "http://" + AppHelper.host + ":" + AppHelper.port + "/movie/createComment";
        url += "?" + "id=" + movieDetailInfo.getId()
        + "&writer=kym7112"
        + "&time=" + movieDetailInfo.getDate()
        + "&rating=" + ratingBar.getRating()
        + "&contents=" + etContents.getText();

        StringRequest request = new StringRequest(
                Request.Method.GET,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Gson gson = new Gson();

                        int status = gson.fromJson(response, ResponseInfo.class).code;

                        if(status == 200) {
                            finish();
                        } else if(status == 400) {
                            String message = "작성 실패";
                            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                        } else {
                            String message = "알 수 없는 오류";
                            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
        );

        request.setShouldCache(false);
        AppHelper.requestQueue.add(request);

    }


}
