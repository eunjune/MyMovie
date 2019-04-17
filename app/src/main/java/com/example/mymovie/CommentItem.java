package com.example.mymovie;

import java.io.Serializable;

public class CommentItem implements Serializable {
    String id;
    String content;
    int createTime;
    float rating;
    int recommendation;

    public CommentItem(String id, String content, int createTime, float rating, int recommendation) {
        this.id = id;
        this.content = content;
        this.createTime = createTime;
        this.rating = rating;
        this.recommendation = recommendation;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getCreateTime() {
        return createTime;
    }

    public void setCreateTime(int createTime) {
        this.createTime = createTime;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public int getRecommendation() {
        return recommendation;
    }

    public void setRecommendation(int recommendation) {
        this.recommendation = recommendation;
    }
}
