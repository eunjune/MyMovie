package com.example.mymovie;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;

public class PosterActivity extends AppCompatActivity implements FragmentCallback{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_poster);

        ViewPager pager = (ViewPager)findViewById(R.id.pager);
        pager.setOffscreenPageLimit(5);
        MoviePagerAdapter adapter = new MoviePagerAdapter(getSupportFragmentManager());

        for(int i=0;i<6;i++) {
            Fragment fragment = new MovieFragment();
            Bundle bundle = new Bundle();
            bundle.putInt("index",i);
            fragment.setArguments(bundle);
            adapter.addItem(fragment);
        }

        pager.setAdapter(adapter);
    }

    @Override
    public void onFragmentSelected(Bundle bundle) {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.putExtra("title", bundle.getString("title"));
        intent.putExtra("summary", bundle.getString("summary"));
        intent.putExtra("reserveRating", bundle.getString("reserveRating"));
        intent.putExtra("audienceRating", bundle.getString("audienceRating"));
        intent.putExtra("opening", bundle.getString("opening"));

        startActivity(intent);
    }

    class MoviePagerAdapter extends FragmentStatePagerAdapter {
        ArrayList<Fragment> items = new ArrayList<>();

        public MoviePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        public void addItem(Fragment item) {
            items.add(item);
        }

        @Override
        public Fragment getItem(int position) {
            return items.get(position);
        }

        @Override
        public int getCount() {
            return items.size();
        }
    }
}
