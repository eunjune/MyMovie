package com.example.mymovie;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.mymovie.data.ResponseInfo;
import com.google.gson.Gson;

import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.Hours;
import org.joda.time.Minutes;
import org.joda.time.Months;
import org.joda.time.Seconds;
import org.joda.time.Years;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;


public class CommentItemView extends LinearLayout {

    private int reviewId;

    private TextView tvWriter;
    private TextView tvCreateTime;
    private TextView tvContent;
    private TextView tvRecommendation;
    private RatingBar rbComment;
    private LinearLayout layoutRecommendation;

    public CommentItemView(Context context) {
        super(context);

        init(context);
    }

    public CommentItemView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        init(context);
    }

    public int getMovieId() {
        return reviewId;
    }

    public void setReviewId(int reviewId) {
        this.reviewId = reviewId;
    }

    private void init(Context context) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.comment_item_view, this, true);

        tvWriter = (TextView) findViewById(R.id.tv_writer);

        tvCreateTime = (TextView) findViewById(R.id.tv_create_time);

        tvContent = (TextView) findViewById(R.id.tv_content);

        tvRecommendation = (TextView) findViewById(R.id.tv_recommendation);

        layoutRecommendation = (LinearLayout)findViewById(R.id.layout_recommendation);
        layoutRecommendation.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(AppHelper.requestQueue == null) {
                    AppHelper.requestQueue = Volley.newRequestQueue(getContext());
                }

                increaseRecommand();
            }
        });

        rbComment = (RatingBar) findViewById(R.id.rb_comment);

    }

    public void addWriter(String writer) {
        tvWriter.setText(writer);
    }

    public void addCreateTime(String createTime, Context context) {

        String beforeDateTime = "";

        DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
        DateTime ldCreateDateTime =formatter.parseDateTime(createTime);
        DateTime currentDatetime = DateTime.now();

        Years years = Years.yearsBetween(ldCreateDateTime, currentDatetime);
        Months months = Months.monthsBetween(ldCreateDateTime, currentDatetime);
        Days days = Days.daysBetween(ldCreateDateTime, currentDatetime);
        Hours hours = Hours.hoursBetween(ldCreateDateTime, currentDatetime);
        Minutes minutes = Minutes.minutesBetween(ldCreateDateTime, currentDatetime);
        Seconds seconds = Seconds.secondsBetween(ldCreateDateTime, currentDatetime);

        if(years.getYears() != 0) {
            beforeDateTime = String.valueOf(years.getYears()) + "년 전";
        } else if(months.getMonths() != 0 ) {
            beforeDateTime = String.valueOf(months.getMonths()) + "개월 전";
        } else if(days.getDays() != 0) {
            beforeDateTime = String.valueOf(days.getDays()) + "일 전";
        } else if(hours.getHours() != 0) {
            beforeDateTime = String.valueOf(hours.getHours()) + "시간 전";
        } else if(minutes.getMinutes() != 0) {
            beforeDateTime = String.valueOf(minutes.getMinutes()) + "분 전";
        } else if(seconds.getSeconds() != 0) {
            beforeDateTime = String.valueOf(seconds.getSeconds()) + "초 전";
        }

        tvCreateTime.setText(beforeDateTime);
    }

    public void addContent(String contents) {
        tvContent.setText(contents);
    }

    public void addRecommendation(int recommendation, Context context) {
        tvRecommendation.setText(String.valueOf(recommendation));
    }

    public void addCommentRating(float rating) {
        rbComment.setRating(rating);
    }

    private void increaseRecommand() {
        String url = "http://" + AppHelper.host + ":" + AppHelper.port + "/movie/increaseRecommend";
        url += "?" + "review_id=" + reviewId
                + "&writer=" + tvWriter.getText().toString();

        StringRequest request = new StringRequest(
                Request.Method.GET,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Gson gson = new Gson();

                        int status = gson.fromJson(response, ResponseInfo.class).code;

                        if(status == 200) {
                            int recommendation = Integer.valueOf(tvRecommendation.getText().toString());
                            recommendation++;

                            String strRecommendation = String.valueOf(recommendation);
                            tvRecommendation.setText(strRecommendation);
                        } else if(status == 400) {
                            String message = "작성 실패";
                            Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
                        } else {
                            String message = "알 수 없는 오류";
                            Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
        );

        request.setShouldCache(false);
        AppHelper.requestQueue.add(request);
    }

}
