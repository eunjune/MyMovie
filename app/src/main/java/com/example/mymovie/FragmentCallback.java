package com.example.mymovie;

import android.os.Bundle;

import java.util.ArrayList;

public interface FragmentCallback {
    void showMovieDetailFragment(int position, Bundle bundle);
    void showCommentWriteActivity(String title,int index);
    void showAllCommentActivity(ArrayList<CommentItem> list, String title
            , float rating, String strRating, int index);
}
