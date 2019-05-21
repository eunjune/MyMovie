package com.example.mymovie.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.mymovie.data.CommentInfo;
import com.example.mymovie.data.MovieDetailInfo;
import com.example.mymovie.data.MovieInfo;

import java.util.ArrayList;
import java.util.List;

public class DBHelper {

    private static final String TAG = "DBHelper";

    private static SQLiteDatabase database;

    // 메인액티비티에서 호출
    public static void openDatabase(Context context, String databaseName) {

        println("openDatabase 호출.");

        try{
            database = context.openOrCreateDatabase(databaseName, Context.MODE_PRIVATE, null);
            if(database != null) {
                println("데이터베이스 " + databaseName + " 오픈됨.");

            }
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    // 메인에서 호출
    public static void createTable(String tableName) {
        println("createTable 호출 : " + tableName);

        if(database != null) {
            if(tableName.equals("movieList")) {
                database.execSQL(DBsql.createTableMovieListSql);
                println("movieList 테이블 생성 요청.");
            }

            if(tableName.equals("movie")) {
                database.execSQL(DBsql.createTableMovieSql);
                println("movie 테이블 생성 요청.");
            }

            if(tableName.equals("comment")) {
                database.execSQL(DBsql.createTableCommentSql);
                println("comment 테이블 생성 요청.");
            }
        } else {
            println("데이터베이스를 먼저 오픈하세요.");
        }
    }

    public static List<MovieInfo> selectMovieList() {

        ArrayList<MovieInfo> list = new ArrayList<>();

        if(database != null) {
            Cursor cursor = database.rawQuery(DBsql.selectMovieListSql,null);
            while(cursor.moveToNext()) {
                MovieInfo movieInfo = new MovieInfo();

                movieInfo.setId(cursor.getInt(1));
                movieInfo.setTitle(cursor.getString(2));
                movieInfo.setTitle_eng(cursor.getString(3));
                movieInfo.setDate(cursor.getString(4));
                movieInfo.setUser_rating(cursor.getFloat(5));
                movieInfo.setAudience_rating(cursor.getFloat(6));
                movieInfo.setReviewer_rating(cursor.getFloat(7));
                movieInfo.setReservation_rate(cursor.getFloat(8));
                movieInfo.setReservation_grade(cursor.getInt(9));
                movieInfo.setGrade(cursor.getInt(10));
                movieInfo.setThumb(cursor.getString(11));
                movieInfo.setImage(cursor.getString(12));

                list.add(movieInfo);
            }

            cursor.close();
        } else {
            println("데이터베이스를 먼저 오픈하세요.");
        }

        return list;

    }

    public static int selectMovieListCount(int id) {
        int result = 0;

        if(database != null) {
            Cursor cursor = database.rawQuery(DBsql.selectMovieListIdSql, new String[]{String.valueOf(id)});
            result = cursor.getCount();
        } else {
            println("데이터베이스를 먼저 오픈하세요.");
        }

        return result;
    }

    public static MovieDetailInfo selectMovie(int id) {

        MovieDetailInfo movieDetailInfo = new MovieDetailInfo();

        if(database != null) {
            Cursor cursor = database.rawQuery(DBsql.selectMovieSql,new String[]{String.valueOf(id)});
            while(cursor.moveToNext()) {
                movieDetailInfo.setId(cursor.getInt(1));
                movieDetailInfo.setTitle(cursor.getString(2));
                movieDetailInfo.setDate(cursor.getString(3));
                movieDetailInfo.setUser_rating(cursor.getFloat(4));
                movieDetailInfo.setAudience_rating(cursor.getFloat(5));
                movieDetailInfo.setReviewer_rating(cursor.getFloat(6));
                movieDetailInfo.setReservation_rate(cursor.getFloat(7));
                movieDetailInfo.setReservation_grade(cursor.getInt(8));
                movieDetailInfo.setGrade(cursor.getInt(9));
                movieDetailInfo.setThumb(cursor.getString(10));
                movieDetailInfo.setImage(cursor.getString(11));
                movieDetailInfo.setPhotos(cursor.getString(12));
                movieDetailInfo.setVideos(cursor.getString(13));
                movieDetailInfo.setOutlinks(cursor.getString(14));
                movieDetailInfo.setGenre(cursor.getString(15));
                movieDetailInfo.setDuration(cursor.getInt(16));
                movieDetailInfo.setAudience(cursor.getInt(17));
                movieDetailInfo.setSynopsis(cursor.getString(18));
                movieDetailInfo.setDirector(cursor.getString(19));
                movieDetailInfo.setActor(cursor.getString(20));
                movieDetailInfo.setLike(cursor.getInt(21));
                movieDetailInfo.setDislike(cursor.getInt(22));
            }

            cursor.close();
        } else {
            println("데이터베이스를 먼저 오픈하세요.");
        }

        return movieDetailInfo;
    }

    public static int selectMovieCount(int id) {
        int result = 0;

        if(database != null) {
            Cursor cursor = database.rawQuery(DBsql.selectMovieSql, new String[]{String.valueOf(id)});
            result = cursor.getCount();
        } else {
            println("데이터베이스를 먼저 오픈하세요.");
        }

        return result;
    }

    public static List<CommentInfo> selectCommentList(int movieId) {

        ArrayList<CommentInfo> list = new ArrayList<>();

        if(database != null) {
            Cursor cursor = database.rawQuery(DBsql.selectCommentSql,new String[]{String.valueOf(movieId)});
            while(cursor.moveToNext()) {
                CommentInfo commentInfo = new CommentInfo();

                commentInfo.setId(cursor.getInt(1));
                commentInfo.setWriter(cursor.getString(2));
                commentInfo.setMovieId(cursor.getInt(3));
                commentInfo.setWriter_image(cursor.getString(4));
                commentInfo.setTime(cursor.getString(5));
                commentInfo.setTimestamp(cursor.getInt(6));
                commentInfo.setRating(cursor.getInt(7));
                commentInfo.setContents(cursor.getString(8));
                commentInfo.setRecommend(cursor.getInt(9));

                list.add(commentInfo);
            }

            cursor.close();
        } else {
            println("데이터베이스를 먼저 오픈하세요.");
        }

        return list;
    }

    public static int selectCommentCount(int id) {
        int result = 0;

        if(database != null) {
            Cursor cursor = database.rawQuery(DBsql.selectCommentIdSql, new String[]{String.valueOf(id)});
            result = cursor.getCount();
        } else {
            println("데이터베이스를 먼저 오픈하세요.");
        }

        return result;
    }

    public static void insertMovieList(MovieInfo movieInfo) {
        if(database != null) {

            Object[] params = {
                    movieInfo.getId(),
                    movieInfo.getTitle(),
                    movieInfo.getTitle_eng(),
                    movieInfo.getDate(),
                    movieInfo.getUser_rating(),
                    movieInfo.getAudience_rating(),
                    movieInfo.getReviewer_rating(),
                    movieInfo.getReservation_rate(),
                    movieInfo.getReservation_grade(),
                    movieInfo.getGrade(),
                    movieInfo.getThumb(),
                    movieInfo.getImage()
            };
            database.execSQL(DBsql.insertMovieListSql,params);

        } else {
            println("데이터베이스를 먼저 오픈하세요.");
        }
    }

    public static void insertMovie(MovieDetailInfo movieDetailInfo) {
        if(database != null) {

            Object[] params = {
                    movieDetailInfo.getId(),
                    movieDetailInfo.getTitle(),
                    movieDetailInfo.getDate(),
                    movieDetailInfo.getUser_rating(),
                    movieDetailInfo.getAudience_rating(),
                    movieDetailInfo.getReviewer_rating(),
                    movieDetailInfo.getReservation_rate(),
                    movieDetailInfo.getReservation_grade(),
                    movieDetailInfo.getGrade(),
                    movieDetailInfo.getThumb(),
                    movieDetailInfo.getImage(),
                    movieDetailInfo.getPhotos(),
                    movieDetailInfo.getVideos(),
                    movieDetailInfo.getOutlinks(),
                    movieDetailInfo.getGenre(),
                    movieDetailInfo.getDuration(),
                    movieDetailInfo.getAudience(),
                    movieDetailInfo.getSynopsis(),
                    movieDetailInfo.getDirector(),
                    movieDetailInfo.getActor(),
                    movieDetailInfo.getLike(),
                    movieDetailInfo.getDislike()
            };
            database.execSQL(DBsql.insertMovieSql,params);

        } else {
            println("데이터베이스를 먼저 오픈하세요.");
        }
    }

    public static void insertComment(CommentInfo commentInfo) {
        if(database != null) {

            Object[] params = {
                    commentInfo.getId(),
                    commentInfo.getWriter(),
                    commentInfo.getMovieId(),
                    commentInfo.getWriter_image(),
                    commentInfo.getTime(),
                    commentInfo.getTimestamp(),
                    commentInfo.getRating(),
                    commentInfo.getContents(),
                    commentInfo.getRecommend()
            };
            database.execSQL(DBsql.insertCommentSql,params);

        } else {
            println("데이터베이스를 먼저 오픈하세요.");
        }
    }

    public static void updateMovieList(MovieInfo movieInfo) {
        if(database != null) {

            Object[] params = {
                    movieInfo.getTitle(),
                    movieInfo.getTitle_eng(),
                    movieInfo.getDate(),
                    movieInfo.getUser_rating(),
                    movieInfo.getAudience_rating(),
                    movieInfo.getReviewer_rating(),
                    movieInfo.getReservation_rate(),
                    movieInfo.getReservation_grade(),
                    movieInfo.getGrade(),
                    movieInfo.getThumb(),
                    movieInfo.getImage(),
                    movieInfo.getId()

            };
            database.execSQL(DBsql.updateMovieListSql,params);

        } else {
            println("데이터베이스를 먼저 오픈하세요.");
        }
    }

    public static void updateMovie(MovieDetailInfo movieDetailInfo) {
        if(database != null) {

            Object[] params = {
                    movieDetailInfo.getTitle(),
                    movieDetailInfo.getDate(),
                    movieDetailInfo.getUser_rating(),
                    movieDetailInfo.getAudience_rating(),
                    movieDetailInfo.getReviewer_rating(),
                    movieDetailInfo.getReservation_rate(),
                    movieDetailInfo.getReservation_grade(),
                    movieDetailInfo.getGrade(),
                    movieDetailInfo.getThumb(),
                    movieDetailInfo.getImage(),
                    movieDetailInfo.getPhotos(),
                    movieDetailInfo.getVideos(),
                    movieDetailInfo.getOutlinks(),
                    movieDetailInfo.getGenre(),
                    movieDetailInfo.getDuration(),
                    movieDetailInfo.getAudience(),
                    movieDetailInfo.getSynopsis(),
                    movieDetailInfo.getDirector(),
                    movieDetailInfo.getActor(),
                    movieDetailInfo.getLike(),
                    movieDetailInfo.getDislike(),
                    movieDetailInfo.getId()
            };
            database.execSQL(DBsql.updateMovieSql,params);

        } else {
            println("데이터베이스를 먼저 오픈하세요.");
        }
    }

    public static void updateComment(CommentInfo commentInfo) {
        if(database != null) {

            Object[] params = {
                    commentInfo.getWriter(),
                    commentInfo.getMovieId(),
                    commentInfo.getWriter_image(),
                    commentInfo.getTime(),
                    commentInfo.getTimestamp(),
                    commentInfo.getRating(),
                    commentInfo.getContents(),
                    commentInfo.getRecommend(),
                    commentInfo.getId()
            };
            database.execSQL(DBsql.updateCommentSql,params);

        } else {
            println("데이터베이스를 먼저 오픈하세요.");
        }
    }

    // 연결되어 있으면 네트ㅝ크로 가져와서 데이터베이스에 저장.
    // 안되어 있으면 데이터베이스에서 가져옴.
    public static void println(String data) {
        Log.d(TAG, data);
    }

}
