package com.example.mymovie;

import android.os.Bundle;

import com.example.mymovie.data.MovieDetailInfo;

public interface FragmentCallback {
    void showMovieDetailFragment(int position, Bundle bundle);
    void showCommentWriteActivity(MovieDetailInfo movieDetailInfo);
}
