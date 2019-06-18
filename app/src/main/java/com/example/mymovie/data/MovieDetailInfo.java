package com.example.mymovie.data;

import com.example.mymovie.R;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.HashMap;

public class MovieDetailInfo implements Serializable {
    private int id;
    private String title;
    private String date;
    private float user_rating;
    private float audience_rating;
    private float reviewer_rating;
    private float reservation_rate;
    private int reservation_grade;
    private int grade;
    private String thumb;
    private String image;
    private String photos;
    private String videos;
    private String outlinks;
    private String genre;
    private int duration;
    private int audience;
    private String synopsis;
    private String director;
    private String actor;
    private int like;
    private int dislike;

    private static HashMap<Integer,Integer> GRADE_IMAGE_ID = null;

    static {
        GRADE_IMAGE_ID = new HashMap<>();
        GRADE_IMAGE_ID.put(12, R.drawable.ic_12);
        GRADE_IMAGE_ID.put(15,R.drawable.ic_15);
        GRADE_IMAGE_ID.put(19,R.drawable.ic_19);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public float getUser_rating() {
        return user_rating;
    }

    public void setUser_rating(float user_rating) {
        this.user_rating = user_rating;
    }

    public float getAudience_rating() {
        return audience_rating;
    }

    public void setAudience_rating(float audience_rating) {
        this.audience_rating = audience_rating;
    }

    public float getReviewer_rating() {
        return reviewer_rating;
    }

    public void setReviewer_rating(float reviewer_rating) {
        this.reviewer_rating = reviewer_rating;
    }

    public float getReservation_rate() {
        return reservation_rate;
    }

    public void setReservation_rate(float reservation_rate) {
        this.reservation_rate = reservation_rate;
    }

    public int getReservation_grade() {
        return reservation_grade;
    }

    public void setReservation_grade(int reservation_grade) {
        this.reservation_grade = reservation_grade;
    }

    public int getGrade() {
        return grade;
    }

    public void setGrade(int grade) {
        this.grade = grade;
    }

    public String getThumb() {
        return thumb;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getPhotos() {
        return photos;
    }

    public void setPhotos(String photos) {
        this.photos = photos;
    }

    public String getVideos() {
        return videos;
    }

    public void setVideos(String videos) {
        this.videos = videos;
    }

    public String getOutlinks() {
        return outlinks;
    }

    public void setOutlinks(String outlinks) {
        this.outlinks = outlinks;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getAudience() {
        DecimalFormat decimalFormat = new DecimalFormat("###,###");

        return decimalFormat.format(audience);
    }

    public void setAudience(int audience) {
        this.audience = audience;
    }

    public String getSynopsis() {
        return synopsis;
    }

    public void setSynopsis(String synopsis) {
        this.synopsis = synopsis;
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public String getActor() {
        return actor;
    }

    public void setActor(String actor) {
        this.actor = actor;
    }

    public int getLike() {
        return like;
    }

    public void setLike(int like) {
        this.like = like;
    }

    public int getDislike() {
        return dislike;
    }

    public void setDislike(int dislike) {
        this.dislike = dislike;
    }

    public int getGradeImageId() {
        return GRADE_IMAGE_ID.get(grade);
    }

    public float getAvgRating() {
        float result = (user_rating + audience_rating + reviewer_rating) / 3f;

        return Math.round(result*100)/100.0f;
    }
}
