package com.example.mymovie;

import android.content.Context;
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

import java.util.ArrayList;

public class MovieDetailFragment extends Fragment {

    private FragmentCallback callback;
    private ActionBar actionBar;

    private ViewGroup rootView;

    private TextView tvLikeCount, tvDislikeCount, tvTitle;
    private ImageView ivLikeButton, ivDislikeButton, ivPoster, ivAge;
    private RatingBar ratingBar;
    private Button btnWrite, btnAllComments;
    private EditText etSummary;

    private int likeCount;
    private int dislikeCount;
    private boolean likeState = false;
    private boolean dislikeState = false;

    private CommentAdapter commentAdapter;

    private MovieInformItem movieInformItem;
    private int index;

    public CommentAdapter getCommentAdapter() {
        return commentAdapter;
    }

    @Override
    public void setArguments(@Nullable Bundle args) {
        super.setArguments(args);

        index = args.getInt("index");
        movieInformItem = (MovieInformItem) args.getSerializable("movieItem");
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof FragmentCallback) {
            callback = (FragmentCallback) context;
        }

        actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = (ViewGroup) inflater.inflate(R.layout.fragment_movie_detail, container, false);

        actionBar.setTitle(getString(R.string.action_bar_movie_detail));



        // 좋아요 클릭시
        tvLikeCount = (TextView) rootView.findViewById(R.id.tv_likeCount);
        likeCount = Integer.valueOf(tvLikeCount.getText().toString());

        ivLikeButton = (ImageView) rootView.findViewById(R.id.btn_like);
        ivLikeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickLikeButton();
            }
        });


        // 싫어요 클릭시
        tvDislikeCount = (TextView) rootView.findViewById(R.id.tv_dislikeCount);
        dislikeCount = Integer.valueOf(tvDislikeCount.getText().toString());

        ivDislikeButton = (ImageView) rootView.findViewById(R.id.btn_dislike);
        ivDislikeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickDislikeButton();
            }
        });

        // 리스트 뷰
        renderListView();


        // 뷰 세팅
        tvTitle = (TextView) rootView.findViewById(R.id.tv_title);
        tvTitle.setText(movieInformItem.getTitle());
        etSummary = (EditText) rootView.findViewById(R.id.et_summary);
        etSummary.setText(movieInformItem.getSummary());
        ivPoster = (ImageView) rootView.findViewById(R.id.iv_poster);
        ivPoster.setImageResource(movieInformItem.getImageId());
        ivAge = (ImageView) rootView.findViewById(R.id.iv_age);
        ivAge.setImageResource(movieInformItem.getAgeId());

        ratingBar = (RatingBar) rootView.findViewById(R.id.ratingBar);
        btnWrite = (Button) rootView.findViewById(R.id.write_btn);
        btnWrite.setOnClickListener(new View.OnClickListener() {

            // 작성하기 클릭시
            @Override
            public void onClick(View v) {

                callback.showCommentWriteActivity(tvTitle.getText().toString(), index);
            }
        });

        btnAllComments = (Button) rootView.findViewById(R.id.btn_allComments);
        btnAllComments.setOnClickListener(new View.OnClickListener() {

            // 모두보기 클릭시
            @Override
            public void onClick(View v) {

                ArrayList<CommentItem> list = commentAdapter.getItems();

                TextView tvRating = (TextView) rootView.findViewById(R.id.tv_rating);

                String title = tvTitle.getText().toString();
                float rating = ratingBar.getRating();
                String strRating = tvRating.getText().toString();

                callback.showAllCommentActivity(list, title, rating, strRating, index);
            }
        });

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

    private void clickLikeButton() {
        if (likeState) {
            likeCount--;
        } else {
            if (dislikeState) {
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

    private void clickDislikeButton() {
        if (dislikeState) {
            dislikeCount--;
        } else {
            if (likeState) {
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
        ListView listView = (ListView) rootView.findViewById(R.id.listView);


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

        commentAdapter = new CommentAdapter(items, (Context) callback);
        listView.setAdapter(commentAdapter);
    }


}
