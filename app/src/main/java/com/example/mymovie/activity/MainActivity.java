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
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.example.mymovie.MovieRepo;
import com.example.mymovie.R;
import com.example.mymovie.UiResponseCallback;
import com.example.mymovie.data.MovieDetailInfo;
import com.example.mymovie.data.MovieInfo;
import com.example.mymovie.data.ResponseMovieList;
import com.example.mymovie.database.DBHelper;
import com.example.mymovie.fragment.MovieDetailFragment;
import com.example.mymovie.fragment.MoviePosterFragment;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements ActivityActionListener,NavigationView.OnNavigationItemSelectedListener {

    private MovieRepo movieRepo;
    private MoviePagerAdapter moviePagerAdapter;
    private ActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initFields();
        initDB();
        initView();
        movieRepo.requestMovieList("readMovieList", String.valueOf(1), ResponseMovieList.class
                , new UiResponseCallback() {
            @Override
            public void callback(Object... args) {
                renderViewPager((ArrayList<MovieInfo>)args[0]);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        // 한줄평 모두 보기에서 한줄평 작성 버튼 클릭

        if (resultCode == RESULT_OK) {
            MovieDetailInfo movieDetailInfo = (MovieDetailInfo)intent.getSerializableExtra("movieDetailInfo");
            int index = intent.getIntExtra("index",-1);
            moviePagerAdapter.moviePosterItems.get(index).getMovieDetailFragment()
                                                        .showCommentWriteActivity(movieDetailInfo);
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

    @Override
    public void setTitle(String title) {
        actionBar.setTitle(title);
    }

    private void initFields() {
        movieRepo = new MovieRepo(getApplicationContext());

    }


    private void initDB() {
        DBHelper.openDatabase(getApplicationContext(),"MyMovie");
        DBHelper.createTable("movieList");
        DBHelper.createTable("movie");
        DBHelper.createTable("comment");
    }

    private void initView() {
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

        actionBar = getSupportActionBar();
    }



    public void renderViewPager(List<MovieInfo> movieList) {
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

        }

        pager.setAdapter(moviePagerAdapter);
    }

    private class MoviePagerAdapter extends FragmentStatePagerAdapter {
        ArrayList<MoviePosterFragment> moviePosterItems = new ArrayList<>();

        public MoviePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        public void addMoviePosterItem(MoviePosterFragment item) {
            moviePosterItems.add(item);
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
