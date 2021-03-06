package com.example.mymovie.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mymovie.CommentAdapter;
import com.example.mymovie.MovieRepo;
import com.example.mymovie.R;
import com.example.mymovie.UiResponseCallback;
import com.example.mymovie.activity.ActivityActionListener;
import com.example.mymovie.activity.AllCommentActivity;
import com.example.mymovie.activity.CommentWriteActivity;
import com.example.mymovie.data.MovieDetailInfo;
import com.example.mymovie.network.ImageLoadTask;
import com.example.mymovie.network.NetworkManager;

import java.util.ArrayList;

public class MovieDetailFragment extends Fragment{

    public static final int REQUEST_CODE_ALL_COMMENT = 101;

    private MovieRepo movieRepo;

    private ActivityActionListener activityActionListener;
    private ViewGroup rootView;
    private TextView tvLikeCount, tvDislikeCount, tvTitle, tvRating,
                        tvDate,tvGenre,tvDuration,tvReservationGrade,tvReservationRate,
                        tvAudience, tvDirector, tvActor,tvSynopsis;
    private ImageView ivLikeButton, ivDislikeButton, ivPoster, ivAge;
    private RatingBar ratingBar;
    private Button btnWrite, btnAllComments;

    private int likeCount;
    private int dislikeCount;
    private boolean likeState = false;
    private boolean dislikeState = false;
    private int index;


    private CommentAdapter commentAdapter;

    private MovieDetailInfo movieDetailInfo;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Activity activity = getActivity();
        if(activity instanceof  ActivityActionListener) {
            activityActionListener = (ActivityActionListener)activity;
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = (ViewGroup) inflater.inflate(R.layout.fragment_movie_detail, container, false);
      
        initFields();
        initView();
        requestCommentList();

        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        activityActionListener.setTitle(getString(R.string.all_movie_list));
    }

    private void requestIncrease(int movieId, String likeyn, String dislikeyn) {
        if(NetworkManager.isNetworkAvailable()) {

            movieRepo.increaseLikeDislike(movieId, likeyn, null, new UiResponseCallback() {
                @Override
                public void callback(Object... args) {
                    changeLikeDislikeView((String)args[0], (String)args[1]);
                }
            });

            movieRepo.increaseLikeDislike(movieId, null, dislikeyn, new UiResponseCallback() {
                @Override
                public void callback(Object... args) {
                    changeLikeDislikeView((String)args[0], (String)args[1]);
                }
            });

        } else {
            String message = getResources().getString(R.string.all_connection_error);
            Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
        }
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

        requestIncrease(movieId,likeyn,dislikeyn);
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

        requestIncrease(movieId,likeyn,dislikeyn);
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

    // 한줄평 작성
    public void showCommentWriteActivity(MovieDetailInfo movieDetailInfo) {
        Intent intent = new Intent(getActivity().getApplicationContext(), CommentWriteActivity.class);
        intent.putExtra("movieDetailInfo",movieDetailInfo);
        startActivity(intent);
    }

    // 한줄평 모두 보기
    private void showAllCommentActivity() {
        Intent intent = new Intent(getActivity(), AllCommentActivity.class);
        intent.putExtra("commentList", (ArrayList)commentAdapter.getItems());
        intent.putExtra("movieDetailInfo", movieDetailInfo);
        intent.putExtra("total",commentAdapter.getTotal());
        intent.putExtra("index",index);

        startActivityForResult(intent,REQUEST_CODE_ALL_COMMENT);
    }

    private void initFields() {
        movieDetailInfo = (MovieDetailInfo) getArguments().getSerializable("movieDetailInfo");
        index = getArguments().getInt("index");
        movieRepo = new MovieRepo(getContext());
        likeCount = movieDetailInfo.getLike();
        dislikeCount = movieDetailInfo.getDislike();
    }

    private void initView() {
        activityActionListener.setTitle(getString(R.string.all_movie_detail));

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
        tvDuration.setText(movieDetailInfo.getDuration() + getString(R.string.movie_detail_duration));

        // 좋아요 클릭시
        
        tvLikeCount = (TextView) rootView.findViewById(R.id.tv_likeCount);
        tvLikeCount.setText(String.valueOf(likeCount));

        ivLikeButton = (ImageView) rootView.findViewById(R.id.btn_like);
        ivLikeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickLikeButton(movieDetailInfo.getId());
            }
        });
        ivLikeButton.setSelected(likeState);


        // 싫어요 클릭시
        
        tvDislikeCount = (TextView) rootView.findViewById(R.id.tv_dislikeCount);
        tvDislikeCount.setText(String.valueOf(dislikeCount));

        ivDislikeButton = (ImageView) rootView.findViewById(R.id.btn_dislike);
        ivDislikeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickDislikeButton(movieDetailInfo.getId());
            }
        });
        ivDislikeButton.setSelected(dislikeState);

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

        tvSynopsis = (TextView) rootView.findViewById(R.id.tv_synopsis);
        tvSynopsis.setText(movieDetailInfo.getSynopsis());

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

       /* RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.rv_list);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL,false);
        recyclerView.setLayoutManager(layoutManager);

        PhotoAdapter photoAdapter = new PhotoAdapter(getContext());

        String photoAndVideo = movieDetailInfo.getPhotos() +"," + movieDetailInfo.getVideos();
        String[] photoUrlList = movieDetailInfo.getPhotos().split(",");
        String[] urlList = photoAndVideo.split(",");

        photoAdapter.setVideoStartIndex(photoUrlList.length);
        photoAdapter.addItems(urlList);

        recyclerView.setAdapter(photoAdapter);

        photoAdapter.setOnItemClickListener(new PhotoAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(PhotoAdapter.ViewHolder holder, String url, int position) {

                if(holder.getVideoStartIndex() > position) {
                    Intent intent = new Intent(getContext(), PhotoActivity.class);
                    intent.putExtra("url",url);
                    startActivity(intent);

                } else {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    startActivity(intent);
                }

            }
        });*/
    }


}
