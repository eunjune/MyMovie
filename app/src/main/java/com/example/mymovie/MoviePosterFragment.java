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
import android.widget.ImageView;
import android.widget.TextView;

public class MoviePosterFragment extends Fragment {

    private FragmentCallback callback;
    private Context context;

    private ImageView ivPoster;
    private TextView tvTitle, tvReserveRating, tvAudienceRating, tvOpening;


    private MovieInformItem movieInformItem;
    private int index;

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

        this.context = context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_movie_poster, container, false);

        ActionBar actionBar = ((AppCompatActivity)getActivity()).getSupportActionBar();
        actionBar.setTitle("영화 목록");

        ivPoster = (ImageView) rootView.findViewById(R.id.iv_poster);
        tvTitle = (TextView) rootView.findViewById(R.id.tv_title);
        tvReserveRating = (TextView) rootView.findViewById(R.id.tv_reserve_rating);
        tvAudienceRating = (TextView)rootView.findViewById(R.id.tv_audience_rating);
        tvOpening = (TextView) rootView.findViewById(R.id.tv_opening);

        ivPoster.setImageResource(movieInformItem.getImageId());
        tvTitle.setText(String.valueOf(index+1) + "." + movieInformItem.getTitle());
        tvReserveRating.setText(movieInformItem.getReserveRating());
        tvAudienceRating.setText(movieInformItem.getAge());
        tvOpening.setText(movieInformItem.getOpening());


        Button btnDetail = (Button)rootView.findViewById(R.id.btn_detail);
        btnDetail.setOnClickListener(new View.OnClickListener() {

            // MoviePoster -> Main
            @Override
            public void onClick(View v) {

                Bundle bundle = new Bundle();
                bundle.putInt("index",index);
                bundle.putSerializable("movieItem", movieInformItem);
                callback.onFragmentSelected(index,bundle);
            }
        });

        return rootView;
    }


    @Override
    public void onDetach() {
        super.onDetach();

        if(callback != null) {
            callback = null;
        }
    }
}
