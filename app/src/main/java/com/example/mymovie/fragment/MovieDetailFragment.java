package com.example.mymovie.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.mymovie.AppHelper;
import com.example.mymovie.CommentAdapter;
import com.example.mymovie.ImageLoadTask;
import com.example.mymovie.R;
import com.example.mymovie.activity.AllCommentActivity;
import com.example.mymovie.activity.CommentWriteActivity;
import com.example.mymovie.data.CommentInfo;
import com.example.mymovie.data.CommentList;
import com.example.mymovie.data.MovieDetailInfo;
import com.example.mymovie.data.ResponseInfo;
import com.google.gson.Gson;

import java.util.ArrayList;

public class MovieDetailFragment extends Fragment{

    private ActionBar actionBar;
    private ViewGroup rootView;
    private TextView tvLikeCount, tvDislikeCount, tvTitle, tvRating,
                        tvDate,tvGenre,tvDuration,tvReservationGrade,tvReservationRate,
                        tvAudience, tvDirector, tvActor;
    private ImageView ivLikeButton, ivDislikeButton, ivPoster, ivAge;
    private RatingBar ratingBar;
    private Button btnWrite, btnAllComments;
    private EditText etSynopsis;

    private int likeCount;
    private int dislikeCount;
    private boolean likeState = false;
    private boolean dislikeState = false;
    private int index;


    private CommentAdapter commentAdapter;

    private MovieDetailInfo movieDetailInfo;

    @Override
    public void setArguments(@Nullable Bundle args) {
        super.setArguments(args);

        movieDetailInfo = (MovieDetailInfo) args.getSerializable("movieDetailInfo");
        index = args.getInt("index");
}

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = (ViewGroup) inflater.inflate(R.layout.fragment_movie_detail, container, false);

        actionBar.setTitle(getString(R.string.action_bar_movie_detail));

        // 뷰 세팅
        ivPoster = (ImageView) rootView.findViewById(R.id.iv_poster);
        ImageLoadTask imageLoadTask = new ImageLoadTask(movieDetailInfo.getThumb(),ivPoster);
        imageLoadTask.execute();

        tvTitle = (TextView) rootView.findViewById(R.id.tv_title);
        tvTitle.setText(movieDetailInfo.getTitle());

        ivAge = (ImageView) rootView.findViewById(R.id.iv_age);
        ivAge.setImageResource(movieDetailInfo.getGradeImageId());

        tvDate = (TextView) rootView.findViewById(R.id.tv_date);
        tvDate.setText(movieDetailInfo.getDate());

        tvGenre = (TextView) rootView.findViewById(R.id.tv_genre);
        tvGenre.setText(movieDetailInfo.getGenre());

        tvDuration = (TextView) rootView.findViewById(R.id.tv_duration);
        tvDuration.setText(movieDetailInfo.getDuration() + getString(R.string.detail_duration));

        // 좋아요 클릭시
        likeCount = movieDetailInfo.getLike();
        tvLikeCount = (TextView) rootView.findViewById(R.id.tv_likeCount);
        tvLikeCount.setText(String.valueOf(likeCount));

        ivLikeButton = (ImageView) rootView.findViewById(R.id.btn_like);
        ivLikeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickLikeButton(movieDetailInfo.getId());
            }
        });

        // 싫어요 클릭시
        dislikeCount = movieDetailInfo.getDislike();
        tvDislikeCount = (TextView) rootView.findViewById(R.id.tv_dislikeCount);
        tvDislikeCount.setText(String.valueOf(dislikeCount));

        ivDislikeButton = (ImageView) rootView.findViewById(R.id.btn_dislike);
        ivDislikeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickDislikeButton(movieDetailInfo.getId());
            }
        });

        tvReservationGrade = (TextView)rootView.findViewById(R.id.tv_reservation_grade);
        tvReservationGrade.setText(String.valueOf(movieDetailInfo.getReservation_grade()) + "위 ");

        tvReservationRate = (TextView) rootView.findViewById(R.id.tv_reservation_rate);
        tvReservationRate.setText(String.valueOf(movieDetailInfo.getReservation_rate()));

        ratingBar = (RatingBar) rootView.findViewById(R.id.ratingBar);
        ratingBar.setRating(movieDetailInfo.getAvgRating()/2f);

        tvRating = (TextView) rootView.findViewById(R.id.tv_rating);
        tvRating.setText(String.valueOf(movieDetailInfo.getAvgRating()));

        tvAudience = (TextView) rootView.findViewById(R.id.tv_audience);
        tvAudience.setText(movieDetailInfo.getAudience());

        etSynopsis = (EditText) rootView.findViewById(R.id.et_synopsis);
        etSynopsis.setText(movieDetailInfo.getSynopsis());

        tvDirector = (TextView) rootView.findViewById(R.id.tv_director);
        tvDirector.setText(movieDetailInfo.getDirector());

        tvActor = (TextView) rootView.findViewById(R.id.tv_actor);
        tvActor.setText(movieDetailInfo.getActor());

        btnWrite = (Button) rootView.findViewById(R.id.write_btn);
        btnWrite.setOnClickListener(new View.OnClickListener() {

            // 작성하기 클릭시
            @Override
            public void onClick(View v) {

                showCommentWriteActivity(movieDetailInfo);
            }
        });

        btnAllComments = (Button) rootView.findViewById(R.id.btn_allComments);
        btnAllComments.setOnClickListener(new View.OnClickListener() {

            // 모두보기 클릭시
            @Override
            public void onClick(View v) {
                showAllCommentActivity();
            }
        });

        // 한줄평 리스트 불러오기
        if(AppHelper.requestQueue == null) {
            AppHelper.requestQueue = Volley.newRequestQueue(getContext());
        }

        readCommentList(movieDetailInfo.getId());

        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        actionBar.setTitle(getString(R.string.action_bar_movie_list));
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    private void clickLikeButton(int movieId) {
        String likeyn = null;
        String dislikeyn = null;

        if (likeState) {
            likeCount--;
            likeyn = "N";
        } else {
            if (dislikeState) {
                dislikeCount--;
                dislikeyn = "N";
            }
            likeCount++;
            likeyn = "Y";
        }

        increaseLikeDislike(movieId, likeyn, null);
        increaseLikeDislike(movieId, null, dislikeyn);
    }

    private void clickDislikeButton(int movieId) {
        String likeyn = null;
        String dislikeyn = null;

        if (dislikeState) {
            dislikeCount--;
            dislikeyn = "N";
        } else {
            if (likeState) {
                likeCount--;
                likeyn = "N";
            }
            dislikeCount++;
            dislikeyn="Y";
        }
        increaseLikeDislike(movieId, likeyn, null);
        increaseLikeDislike(movieId, null, dislikeyn);
    }

    private  void increaseLikeDislike(int movieId, final String likeyn, final String dislikeyn) {

        String url = "http://" + AppHelper.host + ":" + AppHelper.port + "/movie/increaseLikeDisLike";
        url += "?" + "id=" + movieId;

        if(likeyn == null & dislikeyn == null) {
            return;
        }

        if(likeyn != null) {
            url += "&likeyn=" + likeyn;
        }

        else if(dislikeyn != null) {
            url += "&dislikeyn=" + dislikeyn;
        }


        StringRequest request = new StringRequest(
                Request.Method.GET,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Gson gson = new Gson();

                        int status = gson.fromJson(response, ResponseInfo.class).code;

                        if(status == 200) {
                            changeLikeDislikeView(likeyn, dislikeyn);
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

    private void changeLikeDislikeView(String likeyn, String dislikeyn) {

        if(likeyn != null) {
            tvLikeCount.setText(String.valueOf(likeCount));
            likeState = !likeState;
            ivLikeButton.setSelected(likeState);
        }

        else if(dislikeyn != null) {
            tvDislikeCount.setText(String.valueOf(dislikeCount));
            dislikeState = !dislikeState;
            ivDislikeButton.setSelected(dislikeState);
        }
    }



    private void readCommentList(int id) {
        String strId = String.valueOf(id);

        String url = "http://" + AppHelper.host + ":" + AppHelper.port + "/movie/readCommentList";
        url += "?" + "id=" + strId;

        StringRequest request = new StringRequest(
                Request.Method.GET,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        renderListView(response);
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

    private void renderListView(String response) {
        Gson gson = new Gson();

        ResponseInfo responseInfo = gson.fromJson(response, ResponseInfo.class);


        if(responseInfo.code == 200) {
            ArrayList<CommentInfo> items = gson.fromJson(response, CommentList.class).getResult();
            ListView listView = (ListView) rootView.findViewById(R.id.listView);

            commentAdapter = new CommentAdapter(items, getContext());
            commentAdapter.setTotal(responseInfo.totalCount);
            listView.setAdapter(commentAdapter);
        }
    }

    // 한줄평 작성
    public void showCommentWriteActivity(MovieDetailInfo movieDetailInfo) {
        Intent intent = new Intent(getActivity().getApplicationContext(), CommentWriteActivity.class);
        intent.putExtra("movieDetailInfo",movieDetailInfo);
        startActivity(intent);
    }

    // 한줄평 모두 보기
    public void showAllCommentActivity() {

        Intent intent = new Intent(getActivity(), AllCommentActivity.class);
        intent.putExtra("commentList", commentAdapter.getItems());
        intent.putExtra("movieDetailInfo", movieDetailInfo);
        intent.putExtra("total",commentAdapter.getTotal());
        intent.putExtra("index",index);

        startActivityForResult(intent,101);
    }
}
