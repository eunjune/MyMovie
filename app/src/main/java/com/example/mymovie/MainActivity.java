package com.example.mymovie;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private TextView tvLikeCount, tvDislikeCount,tvTitle;
    private ImageView ivLikeButton, ivDislikeButton;
    private RatingBar ratingBar;
    private Button btnWrite,btnAllComments;

    private int likeCount;
    private int dislikeCount;
    private boolean likeState = false;
    private boolean dislikeState = false;

    private CommentAdapter adapter;
    //private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 좋아요 클릭시
        tvLikeCount = (TextView) findViewById(R.id.tv_likeCount);
        likeCount = Integer.valueOf(tvLikeCount.getText().toString());

        ivLikeButton = (ImageView) findViewById(R.id.btn_like);
        ivLikeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                likeClick();
            }
        });



        // 싫어요 클릭시
        tvDislikeCount = (TextView) findViewById(R.id.tv_dislikeCount);
        dislikeCount = Integer.valueOf(tvDislikeCount.getText().toString());

        ivDislikeButton = (ImageView) findViewById(R.id.btn_dislike);
        ivDislikeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dislikeClick();
            }
        });

        // 리스트 뷰
        renderListView();

        tvTitle = (TextView)findViewById(R.id.tv_title);
        ratingBar = (RatingBar)findViewById(R.id.ratingBar);
        btnWrite = (Button)findViewById(R.id.write_btn);
        btnWrite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCommentWriteActivity();
            }
        });
        btnAllComments = (Button)findViewById(R.id.btn_allComments);
        btnAllComments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAllCommentActivity();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        if(requestCode == 101) {
            String contents = intent.getStringExtra("contents");
            float rating = intent.getFloatExtra("rating",0f);

            adapter.getItems().add(new CommentItem("iws**"
                    ,contents
                    ,10,rating,0));
        }

        if(requestCode == 102 && resultCode == RESULT_OK) {
            showCommentWriteActivity();
        }
    }

    private void likeClick() {
        if(likeState) {
            likeCount--;
        } else {
            if(dislikeState) {
                dislikeCount--;
                tvDislikeCount.setText(String.valueOf(dislikeCount));
                dislikeState = !dislikeState;
                ivDislikeButton.setSelected(dislikeState);
            }
            likeCount++;
        }

        tvLikeCount.setText(String.valueOf(likeCount));
        likeState = !likeState;
        ivLikeButton.setSelected(likeState);
    }

    private void dislikeClick() {
        if(dislikeState) {
            dislikeCount--;
        } else {
            if(likeState) {
                likeCount--;
                tvLikeCount.setText(String.valueOf(likeCount));
                likeState = !likeState;
                ivLikeButton.setSelected(likeState);
            }
            dislikeCount++;
        }

        tvDislikeCount.setText(String.valueOf(dislikeCount));
        dislikeState = !dislikeState;
        ivDislikeButton.setSelected(dislikeState);
    }

    private void renderListView() {
        ArrayList<CommentItem> items = new ArrayList<>();
        ListView listView = (ListView)findViewById(R.id.listView);


        items.add(new CommentItem("kym71**"
        , "적당히 재밌다. 오랜만에 잠 안오는 영화봤네요."
        , 10, 5, 0));

        items.add(new CommentItem("angel**"
        , "웃긴 내용보다는 좀 더 진지한 영화."
        , 15, 4.5f, 1));

        items.add(new CommentItem("beaut**"
        , "연기가 부족한 느낌이 드는 배우도 있다. 그래도 전체적으로는 재밌다."
        , 16, 4.5f, 3));

        items.add(new CommentItem("pargo**"
        , "스트레스가 해소되는 영화네요."
        , 19, 5, 0));

        items.add(new CommentItem("memory**"
        , "아무 생각없이 보면 되는 영화. 하지만 생각하면 안되는 영화."
        , 22, 4.5f, 1));

        items.add(new CommentItem("code2**"
        , "적당히 웃기고 적당히 재밌었어요."
        , 23, 5, 0));

        items.add(new CommentItem("add11**"
        , "요즘 제대로 만든 영화 없더니 잘 만들었습니다."
        , 27, 3, 4));

        items.add(new CommentItem("code2**"
        , "즐길 수 있는 영화입니다."
        , 30, 5, 5));

        adapter = new CommentAdapter(items,getApplicationContext());
        listView.setAdapter(adapter);
    }

    private void showCommentWriteActivity() {
        String title = tvTitle.getText().toString();

        Intent intent = new Intent(getApplicationContext(),CommentWriteActivity.class);
        intent.putExtra("title",title);
        startActivityForResult(intent, 101);
    }

    private  void showAllCommentActivity() {
        ArrayList<CommentItem> list = adapter.getItems();

        TextView tvRating = (TextView)findViewById(R.id.tv_rating);

        String title = tvTitle.getText().toString();
        float rating = ratingBar.getRating();
        String strRating = tvRating.getText().toString();

        Intent intent = new Intent(getApplicationContext(),AllCommentActivity.class);
        intent.putExtra("commentsList",list);
        intent.putExtra("title",title);
        intent.putExtra("rating",rating);
        intent.putExtra("strRating",strRating);

        startActivityForResult(intent,102);

    }
}
