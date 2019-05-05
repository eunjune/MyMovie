package com.example.mymovie.data;

import java.util.ArrayList;

public class MovieList {

    private ArrayList<MovieInfo> result = new ArrayList<>();

    public MovieInfo getMovieInfo(int index) {
        return result.get(index);
    }

    public int getSize() {
        return result.size();
    }

    public ArrayList<MovieInfo> getResult() {
        return result;
    }

}
