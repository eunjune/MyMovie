package com.example.mymovie.fragment;

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
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.mymovie.AppHelper;
import com.example.mymovie.FragmentCallback;
import com.example.mymovie.ImageLoadTask;
import com.example.mymovie.R;
import com.example.mymovie.data.MovieDetailInfo;
import com.example.mymovie.data.MovieDetailList;
import com.example.mymovie.data.MovieInfo;
import com.example.mymovie.data.ResponseInfo;
import com.google.gson.Gson;

public class MoviePosterFragment extends Fragment {

    private FragmentCallback callback;

    private ImageView ivPoster;
    private TextView tvTitle, tvReservationRate, tvGrade, tvOpening;


    private MovieInfo movieInfo;
    private int index;

    @Override
    public void setArguments(@Nullable Bundle args) {
        super.setArguments(args);
        index = args.getInt("index");
        movieInfo = (MovieInfo) args.getSerializable("movieItem");
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof FragmentCallback) {
            callback = (FragmentCallback) context;
        }

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_movie_poster, container, false);

        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        actionBar.setTitle("영화 목록");

        // 뷰 세팅
        ivPoster = (ImageView) rootView.findViewById(R.id.iv_poster);
        ImageLoadTask imageLoadTask = new ImageLoadTask(movieInfo.getImage(),ivPoster);
        imageLoadTask.execute();

        tvTitle = (TextView) rootView.findViewById(R.id.tv_title);
        tvTitle.setText((index + 1) + "." + movieInfo.getTitle());

        tvReservationRate = (TextView) rootView.findViewById(R.id.tv_reservation_rate);
        String strReservationRate = getString(R.string.poster_reservation_rate) + movieInfo.getReservation_rate() + "%";
        tvReservationRate.setText(strReservationRate);

        tvGrade = (TextView) rootView.findViewById(R.id.tv_grade);
        String strGrade = String.valueOf(movieInfo.getGrade()) + getString(R.string.poster_grade);
        tvGrade.setText(strGrade);


        Button btnDetail = (Button) rootView.findViewById(R.id.btn_detail);
        btnDetail.setOnClickListener(new View.OnClickListener() {

            // 상세보기 클릭시
            @Override
            public void onClick(View v) {

                if(AppHelper.requestQueue == null) {
                    AppHelper.requestQueue = Volley.newRequestQueue(getContext());
                }

                readMovie(movieInfo.getTitle());
            }
        });

        return rootView;
    }

    @Override
    public void onDetach() {
        super.onDetach();

        if (callback != null) {
            callback = null;
        }
    }

    public void readMovie(String name) {
        String url = "http://" + AppHelper.host + ":" + AppHelper.port + "/movie/readMovie";
        url += "?" + "name=" + name;

        StringRequest request = new StringRequest(
                Request.Method.GET,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        MovieDetailInfo movieDetailInfo = processResponse(response);

                        if(movieDetailInfo != null) {
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("movieDetailInfo", movieDetailInfo);
                            callback.showMovieDetailFragment(index, bundle);
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

    private MovieDetailInfo processResponse(String response) {
        Gson gson = new Gson();

        ResponseInfo responseInfo = gson.fromJson(response, ResponseInfo.class);
        if(responseInfo.code == 200) {
            return gson.fromJson(response, MovieDetailList.class).result.get(0);
        }
        return null;
    }

}
