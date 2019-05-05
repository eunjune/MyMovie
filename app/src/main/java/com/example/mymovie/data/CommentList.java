package com.example.mymovie.data;

import java.util.ArrayList;

public class CommentList {
    private ArrayList<CommentInfo> result = new ArrayList<>();

    public CommentInfo getCommentInfo(int index) {
        return result.get(index);
    }

    public int getSize() {
        return result.size();
    }

    public ArrayList<CommentInfo> getResult() {
        return result;
    }

}
