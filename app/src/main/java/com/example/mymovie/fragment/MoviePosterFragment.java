package com.example.mymovie.fragment;

import android.content.Context;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
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
import com.example.mymovie.MyFunction;
import com.example.mymovie.R;
import com.example.mymovie.data.MovieDetailInfo;
import com.example.mymovie.data.MovieDetailList;
import com.example.mymovie.data.MovieInfo;
import com.example.mymovie.data.ProtocolObj;
import com.example.mymovie.data.ResponseInfo;
import com.example.mymovie.database.DBHelper;
import com.example.mymovie.network.ImageLoadTask;
import com.example.mymovie.network.NetworkManager;

public class MoviePosterFragment extends Fragment {

    private NetworkManager networkManager;

    private MovieDetailFragment movieDetailFragment;
    private ImageView ivPoster;
    private TextView tvTitle, tvReservationRate, tvGrade;

    private MovieInfo movieInfo;
    private int index;

    public MovieDetailFragment getMovieDetailFragment() {
        return movieDetailFragment;
    }

    @Override
    public void setArguments(@Nullable Bundle args) {
        super.setArguments(args);
        index = args.getInt("index");
        movieInfo = (MovieInfo) args.getSerializable("movieItem");
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        movieDetailFragment = new MovieDetailFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_movie_poster, container, false);


        networkManager = new NetworkManager(getContext());

        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        actionBar.setTitle("영화 목록");

        // 뷰 세팅
        ivPoster = (ImageView) rootView.findViewById(R.id.iv_poster);

        int networkState = NetworkManager.getConnectivityStatus(getContext());

        if(networkState == ConnectivityManager.TYPE_MOBILE || networkState == ConnectivityManager.TYPE_WIFI) {
            ImageLoadTask imageLoadTask = new ImageLoadTask(movieInfo.getImage(),ivPoster);
            imageLoadTask.execute();
        }


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

                int networkState = NetworkManager.getConnectivityStatus(getContext());

                if(networkState == ConnectivityManager.TYPE_MOBILE || networkState == ConnectivityManager.TYPE_WIFI) {
                    final ProtocolObj protocolObj = new ProtocolObj();
                    protocolObj.setUrl("readMovie");
                    protocolObj.setRequestType(Request.Method.GET);
                    protocolObj.setParam("name",movieInfo.getTitle());
                    protocolObj.setResponseClass(MovieDetailList.class);

                    MyFunction myFunction = new MyFunction() {
                        @Override
                        public void callback(String response) {

                            ResponseInfo responseInfo = protocolObj.getResponseInfo(response);

                            if(responseInfo.code == 200) {
                                MovieDetailList movieDetailList = (MovieDetailList) protocolObj
                                        .getResponseClass(response);
                                MovieDetailInfo movieDetailInfo = movieDetailList.getMovieDetailInfo(0);

                                if(DBHelper.selectMovieCount(movieDetailInfo.getId()) == 0) {
                                    DBHelper.insertMovie(movieDetailInfo);
                                } else {
                                    DBHelper.updateMovie(movieDetailInfo);
                                }

                                if(movieDetailInfo != null) {
                                    Bundle bundle = new Bundle();
                                    bundle.putSerializable("movieDetailInfo", movieDetailInfo);
                                    bundle.putInt("index",index);
                                    showMovieDetailFragment(bundle);
                                }
                            } else if(responseInfo.code == 400) {
                                String message = "데이터 불러오기 실패";
                                Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
                            } else {
                                String message = "알 수 없는 오류";
                                Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
                            }
                        }
                    };

                    networkManager.request(protocolObj,getContext(), myFunction);
                } else {
                    MovieDetailInfo movieDetailInfo = DBHelper.selectMovie(movieInfo.getId());
                    if(movieDetailInfo != null) {
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("movieDetailInfo", movieDetailInfo);
                        bundle.putInt("index",index);
                        showMovieDetailFragment(bundle);
                    }
                }


            }
        });

        return rootView;
    }


    // 영화 상세 정보
    public void showMovieDetailFragment(Bundle bundle) {

        movieDetailFragment.setArguments(bundle);

        FragmentManager manager = getActivity().getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();

        transaction.addToBackStack(null);
        transaction.add(R.id.container, movieDetailFragment);

        transaction.commit();
    }

}
