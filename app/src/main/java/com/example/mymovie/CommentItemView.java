package com.example.mymovie;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

public class CommentItemView extends LinearLayout {

    private TextView idTxt;
    private TextView createTimeTxt;
    private TextView contentTxt;
    private TextView recommendationTxt;
    private RatingBar commentRating;

    public CommentItemView(Context context) {
        super(context);

        init(context);
    }

    public CommentItemView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        init(context);
    }

    private void init(Context context) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.comment_item_view,this,true);

        idTxt = (TextView)findViewById(R.id.id_txt);
        createTimeTxt = (TextView)findViewById(R.id.create_time_txt);
        contentTxt = (TextView)findViewById(R.id.content_txt);
        recommendationTxt = (TextView)findViewById(R.id.recommendation_txt);

        commentRating = (RatingBar)findViewById(R.id.comment_rating);

    }

    public void addId(String id) {
        idTxt.setText(id);
    }

    public void addCreateTime(int createTime) {
        createTimeTxt.setText(createTime + "분전전");    }

    public void addContent(String content) {
        contentTxt.setText(content);
    }

    public void addRecommendation(int recommendation) {
        recommendationTxt.setText("추천 "+recommendation);
    }

    public void addCommentRating(int rating) {
        commentRating.setRating(rating);
    }

}
