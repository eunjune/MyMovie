package com.example.mymovie;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class MovieFragment extends Fragment {

    private FragmentCallback callback;

    private ImageView ivPoster;
    private TextView tvTitle, tvReserveRating, tvAudienceRating, tvOpening;

    private int[] drawbles = {R.drawable.image1, R.drawable.image2, R.drawable.image3
            , R.drawable.image4, R.drawable.image5, R.drawable.image6};
    private String movieTitle, movieSummary, movieReserveRate, movieAudienceRating, movieOpening;
    private int index;

    @Override
    public void setArguments(@Nullable Bundle args) {
        super.setArguments(args);
        index = args.getInt("index");
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if(context instanceof FragmentCallback) {
            callback = (FragmentCallback) context;
        }

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_movie, container, false);

        movieTitle = getResources().getStringArray(R.array.movie_title)[index];
        movieSummary = getResources().getStringArray(R.array.movie_summary)[index];
        movieReserveRate = getResources().getStringArray(R.array.movie_reserve_rate)[index];
        movieAudienceRating = getResources().getStringArray(R.array.movie_audience_rating)[index];
        movieOpening = getResources().getStringArray(R.array.movie_opening)[index];

        ivPoster = (ImageView) rootView.findViewById(R.id.iv_poster);
        tvTitle = (TextView) rootView.findViewById(R.id.tv_title);
        tvReserveRating = (TextView) rootView.findViewById(R.id.tv_reserve_rating);
        tvAudienceRating = (TextView)rootView.findViewById(R.id.tv_audience_rating);
        tvOpening = (TextView) rootView.findViewById(R.id.tv_opening);

        ivPoster.setImageResource(drawbles[index]);
        tvTitle.setText(movieTitle);
        tvReserveRating.setText(movieReserveRate);
        tvAudienceRating.setText(movieAudienceRating);
        tvOpening.setText(movieOpening);


        Button btnDetail = (Button)rootView.findViewById(R.id.btn_detail);
        btnDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("title", movieTitle);
                bundle.putString("summary",movieSummary);
                bundle.putString("reserveRating",movieReserveRate);
                bundle.putString("audienceRating",movieAudienceRating);
                bundle.putString("opening",movieOpening);

                callback.onFragmentSelected(bundle);
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
