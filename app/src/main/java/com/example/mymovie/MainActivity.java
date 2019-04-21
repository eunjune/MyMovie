package com.example.mymovie;

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

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, FragmentCallback {
    private final int MAIN_TO_COMMENT_WRITE = 101;
    private final int MAIN_TO_ALL_COMMENT = 102;

    private MoviePagerAdapter moviePagerAdapter;

    private int[] posterDrawbles = {R.drawable.image1, R.drawable.image2, R.drawable.image3
            , R.drawable.image4, R.drawable.image5, R.drawable.image6};

    private int[] ageDrawbles = {R.drawable.ic_15, R.drawable.ic_12, R.drawable.ic_15
            , R.drawable.ic_12, R.drawable.ic_15, R.drawable.ic_19};


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


        // 뷰 페이저
        ViewPager pager = (ViewPager) findViewById(R.id.pager);
        pager.setOffscreenPageLimit(5);
        moviePagerAdapter = new MoviePagerAdapter(getSupportFragmentManager());

        for (int i = 0; i < 6; i++) {
            MovieInformItem movieInformItem = new MovieInformItem();

            movieInformItem.setAgeId(ageDrawbles[i]);
            movieInformItem.setImageId(posterDrawbles[i]);
            movieInformItem.setOpening(getResources().getStringArray(R.array.movie_opening)[i]);
            movieInformItem.setAge(getResources().getStringArray(R.array.movie_audience_rating)[i]);
            movieInformItem.setReserveRating(getResources().getStringArray(R.array.movie_reserve_rate)[i]);
            movieInformItem.setSummary(getResources().getStringArray(R.array.movie_summary)[i]);
            movieInformItem.setTitle(getResources().getStringArray(R.array.movie_title)[i]);

            MoviePosterFragment moviePosterFragment = new MoviePosterFragment();
            Bundle bundle = new Bundle();
            bundle.putInt("index", i);
            bundle.putSerializable("movieItem", movieInformItem);
            moviePosterFragment.setArguments(bundle);
            moviePagerAdapter.addMoviePosterItem(moviePosterFragment);

            MovieDetailFragment movieDetailFragment = new MovieDetailFragment();
            moviePagerAdapter.addMovieDetailItem(movieDetailFragment);
        }

        pager.setAdapter(moviePagerAdapter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        if (intent != null) {
            // 한줄평에서 작성 버튼 클릭
            if (requestCode == MAIN_TO_COMMENT_WRITE) {
                String contents = intent.getStringExtra("contents");
                float rating = intent.getFloatExtra("rating", 0f);
                int index = intent.getIntExtra("index", -1);

                MovieDetailFragment fragment = moviePagerAdapter.getMovieDetailItem(index);

                fragment.getCommentAdapter().getItems().add(new CommentItem("iws**"
                        , contents
                        , 10, rating, 0));
            }

            //한줄평에서 취소 버튼 클릭
            if (requestCode == MAIN_TO_ALL_COMMENT && resultCode == RESULT_OK) {
                String title = intent.getStringExtra("title");
                int index = intent.getIntExtra("index", -1);

                showCommentWriteActivity(title, index);
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
    public void showCommentWriteActivity(String title, int index) {
        Intent intent = new Intent(getApplicationContext(), CommentWriteActivity.class);
        intent.putExtra("title", title);
        intent.putExtra("index", index);
        startActivityForResult(intent, MAIN_TO_COMMENT_WRITE);
    }

    // 한줄평 모두 보기
    public void showAllCommentActivity(ArrayList<CommentItem> list, String title
            , float rating, String strRating, int index) {
        Intent intent = new Intent(getApplicationContext(), AllCommentActivity.class);
        intent.putExtra("commentsList", list);
        intent.putExtra("title", title);
        intent.putExtra("rating", rating);
        intent.putExtra("strRating", strRating);
        intent.putExtra("index", index);

        startActivityForResult(intent, MAIN_TO_ALL_COMMENT);
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
