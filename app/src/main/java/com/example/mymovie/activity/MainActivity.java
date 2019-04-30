package com.example.mymovie.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.mymovie.AppHelper;
import com.example.mymovie.FragmentCallback;
import com.example.mymovie.fragment.MovieDetailFragment;
import com.example.mymovie.fragment.MoviePosterFragment;
import com.example.mymovie.R;
import com.example.mymovie.data.MovieDetailInfo;
import com.example.mymovie.data.MovieInfo;
import com.example.mymovie.data.MovieList;
import com.example.mymovie.data.ResponseInfo;
import com.google.gson.Gson;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, FragmentCallback {
    private final int MAIN_TO_COMMENT_WRITE = 101;
    private final int MAIN_TO_ALL_COMMENT = 102;

    private MoviePagerAdapter moviePagerAdapter;
    private ArrayList<MovieInfo> movieList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 바로 가기 메뉴
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        // 영화 목록 요청
        if(AppHelper.requestQueue == null) {
            AppHelper.requestQueue = Volley.newRequestQueue(getApplicationContext());
        }

        readMovieList();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        if (intent != null) {
            // 한줄평 모두 보기에서 한줄평 작성 버튼 클릭
            if (resultCode == RESULT_OK) {
                showCommentWriteActivity((MovieDetailInfo) intent.getSerializableExtra("movieDetailInfo"));
            }
        }

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        // 바로가기 메뉴에서 영화 목록 클릭
        if (id == R.id.nav_movie_list) {
            FragmentManager manager = getSupportFragmentManager();
            FragmentTransaction transaction = manager.beginTransaction();

            Fragment fragment = manager.findFragmentById(R.id.container);

            if (fragment instanceof MovieDetailFragment) {
                transaction.remove(fragment);
                transaction.commit();
            }
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    // 영화 상세 정보
    @Override
    public void showMovieDetailFragment(int position, Bundle bundle) {
        MovieDetailFragment movieDetailFragment = moviePagerAdapter.getMovieDetailItem(position);
        movieDetailFragment.setArguments(bundle);

        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();

        transaction.addToBackStack(null);
        transaction.add(R.id.container, movieDetailFragment);

        transaction.commit();
    }

    // 한줄평 작성
    public void showCommentWriteActivity(MovieDetailInfo movieDetailInfo) {
        Intent intent = new Intent(getApplicationContext(), CommentWriteActivity.class);
        intent.putExtra("movieDetailInfo",movieDetailInfo);
        startActivityForResult(intent, MAIN_TO_COMMENT_WRITE);
    }



    public void readMovieList() {
        String url = "http://" + AppHelper.host + ":" + AppHelper.port + "/movie/readMovieList";
        url += "?" + "type=1";

        StringRequest request = new StringRequest(
                Request.Method.GET,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        processResponse(response);
                        renderViewPager();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
        );

        request.setShouldCache(false);
        AppHelper.requestQueue.add(request);

    }

    private void processResponse(String response) {
        Gson gson = new Gson();

        ResponseInfo responseInfo = gson.fromJson(response, ResponseInfo.class);
        if(responseInfo.code == 200) {
            movieList = gson.fromJson(response,MovieList.class).result;
        }

    }

    public void renderViewPager() {
        // 뷰 페이저
        ViewPager pager = (ViewPager) findViewById(R.id.pager);
        pager.setOffscreenPageLimit(movieList.size());
        moviePagerAdapter = new MoviePagerAdapter(getSupportFragmentManager());

        for (int i=0; i<movieList.size(); i++) {
            Bundle bundle = new Bundle();
            bundle.putSerializable("movieItem", movieList.get(i));
            bundle.putInt("index",i);

            MoviePosterFragment moviePosterFragment = new MoviePosterFragment();
            moviePosterFragment.setArguments(bundle);
            moviePagerAdapter.addMoviePosterItem(moviePosterFragment);

            MovieDetailFragment movieDetailFragment = new MovieDetailFragment();
            moviePagerAdapter.addMovieDetailItem(movieDetailFragment);
        }

        pager.setAdapter(moviePagerAdapter);
    }


    class MoviePagerAdapter extends FragmentStatePagerAdapter {
        ArrayList<MoviePosterFragment> moviePosterItems = new ArrayList<>();
        ArrayList<MovieDetailFragment> movieDetailItems = new ArrayList<>();

        public MoviePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        public void addMoviePosterItem(MoviePosterFragment item) {
            moviePosterItems.add(item);
        }

        public void addMovieDetailItem(MovieDetailFragment detailFragment) {
            movieDetailItems.add(detailFragment);
        }

        public MovieDetailFragment getMovieDetailItem(int position) {
            return movieDetailItems.get(position);
        }

        @Override
        public MoviePosterFragment getItem(int position) {
            return moviePosterItems.get(position);
        }

        @Override
        public int getCount() {
            return moviePosterItems.size();
        }

    }
}
