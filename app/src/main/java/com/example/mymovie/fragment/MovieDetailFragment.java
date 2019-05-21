package com.example.mymovie.fragment;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
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
import com.example.mymovie.CommentAdapter;
import com.example.mymovie.MyFunction;
import com.example.mymovie.R;
import com.example.mymovie.activity.AllCommentActivity;
import com.example.mymovie.activity.CommentWriteActivity;
import com.example.mymovie.data.CommentInfo;
import com.example.mymovie.data.CommentList;
import com.example.mymovie.data.MovieDetailInfo;
import com.example.mymovie.data.ProtocolObj;
import com.example.mymovie.data.ResponseInfo;
import com.example.mymovie.database.DBHelper;
import com.example.mymovie.network.ImageLoadTask;
import com.example.mymovie.network.NetworkManager;

import java.util.ArrayList;
import java.util.List;

public class MovieDetailFragment extends Fragment{

    private NetworkManager networkManager;

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

        networkManager = new NetworkManager(getContext());

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
        ivLikeButton.setSelected(likeState);


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

        int networkState = NetworkManager.getConnectivityStatus(getContext());

        if(networkState == ConnectivityManager.TYPE_MOBILE || networkState == ConnectivityManager.TYPE_WIFI) {

            // 한줄평 리스트 불러오기
            final ProtocolObj protocolObj = new ProtocolObj();
            protocolObj.setUrl("readCommentList");
            protocolObj.setRequestType(Request.Method.GET);
            protocolObj.setParam("id",String.valueOf(movieDetailInfo.getId()));
            protocolObj.setResponseClass(CommentList.class);

            MyFunction myFunction = new MyFunction() {
                @Override
                public void callback(String response) {

                    ResponseInfo responseInfo = protocolObj.getResponseInfo(response);

                    if(responseInfo.code == 200) {
                        CommentList list = (CommentList) protocolObj.getResponseClass(response);
                        ListView listView = (ListView) rootView.findViewById(R.id.listView);
                        ArrayList<CommentInfo> items = list.getResult();

                        for(CommentInfo item : items) {
                            if(DBHelper.selectCommentCount(item.getId()) == 0) {
                                DBHelper.insertComment(item);
                            } else {
                                DBHelper.updateComment(item);
                            }
                        }

                        commentAdapter = new CommentAdapter(items, getContext());
                        commentAdapter.setTotal(responseInfo.totalCount);
                        listView.setAdapter(commentAdapter);
                    } else if(responseInfo.code == 400) {
                        String message = "데이터 불러오기 실패";
                        Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
                    } else {
                        String message = "알 수 없는 오류";
                        Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
                    }
                }
            };

            networkManager.request(protocolObj, getContext(), myFunction);

        } else {
            List<CommentInfo> items = DBHelper.selectCommentList(movieDetailInfo.getId());
            ListView listView = (ListView) rootView.findViewById(R.id.listView);

            commentAdapter = new CommentAdapter(items, getContext());
            commentAdapter.setTotal(items.size());
            listView.setAdapter(commentAdapter);
        }


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
        int networkState = NetworkManager.getConnectivityStatus(getContext());

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

        if(networkState == ConnectivityManager.TYPE_MOBILE || networkState == ConnectivityManager.TYPE_WIFI) {
            increaseLikeDislike(movieId, likeyn, null);
            increaseLikeDislike(movieId, null, dislikeyn);
        } else {
            String message = "네트워크가 연결되어 있지 않습니다.";
            Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
        }


    }

    private void clickDislikeButton(int movieId) {
        String likeyn = null;
        String dislikeyn = null;
        int networkState = NetworkManager.getConnectivityStatus(getContext());

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

        if(networkState == ConnectivityManager.TYPE_MOBILE || networkState == ConnectivityManager.TYPE_WIFI) {
            increaseLikeDislike(movieId, likeyn, null);
            increaseLikeDislike(movieId, null, dislikeyn);
        } else {
            String message = "네트워크가 연결되어 있지 않습니다.";
            Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
        }
    }

    private  void increaseLikeDislike(int movieId, final String likeyn, final String dislikeyn) {

        final ProtocolObj protocolObj = new ProtocolObj();
        protocolObj.setUrl("increaseLikeDisLike");
        protocolObj.setRequestType(Request.Method.GET);
        protocolObj.setParam("id",String.valueOf(movieId));

        if(likeyn == null && dislikeyn == null) {
            return;
        }

        if(likeyn != null) {
            protocolObj.setParam("likeyn",likeyn);
        }

        else if(dislikeyn != null) {
            protocolObj.setParam("dislikeyn",dislikeyn);
        }

        MyFunction myFunction = new MyFunction() {
            @Override
            public void callback(String response) {
                ResponseInfo responseInfo = protocolObj.getResponseInfo(response);

                if(responseInfo.code == 200) {
                    changeLikeDislikeView(likeyn, dislikeyn);
                } else if(responseInfo.code == 400) {
                    String message = "작성 실패";
                    Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
                } else {
                    String message = "알 수 없는 오류";
                    Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
                }
            }
        };
        networkManager.request(protocolObj, getContext(), myFunction);

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
    public void showAllCommentActivity() {
        Intent intent = new Intent(getActivity(), AllCommentActivity.class);
        intent.putExtra("commentList", (ArrayList)commentAdapter.getItems());
        intent.putExtra("movieDetailInfo", movieDetailInfo);
        intent.putExtra("total",commentAdapter.getTotal());
        intent.putExtra("index",index);

        startActivityForResult(intent,101);
    }
}
