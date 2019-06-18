package com.example.mymovie.data;

import java.util.ArrayList;

public class ResponseMovieDetailList {
    private ArrayList<MovieDetailInfo> result = new ArrayList<>();

    public MovieDetailInfo getMovieDetailInfo(int index) {
        return result.get(index);
    }

    public int getSize() {
        return result.size();
    }

    public ArrayList<MovieDetailInfo> getResult() {
        return result;
    }
}
