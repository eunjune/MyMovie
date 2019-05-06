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
import com.example.mymovie.MyFunction;
import com.example.mymovie.NetworkManager;
import com.example.mymovie.R;
import com.example.mymovie.data.MovieDetailInfo;
import com.example.mymovie.data.ProtocolObj;
import com.example.mymovie.data.ResponseInfo;

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

                final ProtocolObj protocolObj = new ProtocolObj();
                protocolObj.setUrl("createComment");
                protocolObj.setRequestType(Request.Method.GET);
                protocolObj.setParam("id",String.valueOf(movieDetailInfo.getId()));
                protocolObj.setParam("writer","kym7112");
                protocolObj.setParam("time",movieDetailInfo.getDate());
                protocolObj.setParam("rating",String.valueOf(ratingBar.getRating()));
                protocolObj.setParam("contents",etContents.getText().toString());

                MyFunction myFunction = new MyFunction() {
                    @Override
                    public void myMethod(String response) {

                        ResponseInfo responseInfo = protocolObj.getResponseInfo(response);

                        if(responseInfo.code == 200) {
                            finish();
                        } else if(responseInfo.code == 400) {
                            String message = "작성 실패";
                            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                        } else {
                            String message = "알 수 없는 오류";
                            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                        }
                    }
                };

                networkManager.request(protocolObj,getApplicationContext(),myFunction);
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
