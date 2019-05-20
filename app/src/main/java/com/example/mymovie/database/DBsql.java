package com.example.mymovie.database;

public class DBsql {

    public static final String createTableMovieListSql = "create table if not exists movie_list" +
            "(" +
            "_id integer PRIMARY KEY autoincrement, " +
            "id integer, " +
            "title text, " +
            "title_eng, " +
            "dateValue text, " +
            "user_rating float, " +
            "audience_rating float, " +
            "reviewer_rating float, " +
            "reservation_rate float, " +
            "reservation_grade integer, " +
            "grade integer, " +
            "thumb text, " +
            "image text" +
            ")";

    public static final String createTableMovieSql = "create table if not exists movie" +
            "(" +
            "_id integer PRIMARY KEY autoincrement, " +
            "id integer," +
            "title text," +
            "dateValue text," +
            "user_rating float," +
            "audience_rating float," +
            "reviewer_rating float," +
            "reservation_rate float," +
            "reservation_grade integer," +
            "grade integer," +
            "thumb text," +
            "image text," +
            "photos text," +
            "videos text," +
            "outlinks text," +
            "genre text," +
            "duration integer," +
            "audience integer," +
            "synopsis text," +
            "director text," +
            "actor text," +
            "like integer," +
            "dislike integer" +
            ")";


    public static final String createTableCommentSql = "create table if not exists comment" +
            "(" +
            "_id integer PRIMARY KEY autoincrement," +
            "id integer," +
            "writer text," +
            "movieId integer," +
            "writer_image text," +
            "time text," +
            "timestamp integer," +
            "rating integer," +
            "contents text," +
            "recommend integer" +
            ")";

    public static final String selectMovieListSql = "select * from movie_list";

    public static final String selectMovieListIdSql = "select * from movie_list where id = ?";

    public static final String selectMovieSql = "select * from movie where id=?";

    public static final String selectCommentSql = "select * from comment where movieId=?";

    public static final String selectCommentIdSql = "select * from comment where id=?";

    public static final String insertMovieListSql = "insert into " +
            "movie_list(id,title, title_eng, dateValue, user_rating, audience_rating," +
            " reviewer_rating, reservation_rate, reservation_grade, grade, thumb, image) " +
            "values(?,?,?,?,?,?,?,?,?,?,?,?)";

    public static final String insertMovieSql = "insert into " +
            "movie(id,title, dateValue,  user_rating, audience_rating, reviewer_rating, reservation_rate, " +
            "reservation_grade, grade, thumb, image, photos, videos, outlinks, genre, duration, audience," +
            "synopsis,director,actor,like,dislike) " +
            "values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

    public static final String insertCommentSql = "insert into " +
            "comment(id,writer, movieId, writer_image, time, timestamp, rating, contents, recommend) " +
            "values(?,?,?,?,?,?,?,?,?)";

    public static final String updateMovieListSql = "update movie_list " +
            "set title=?,title_eng=?,dateValue=?,user_rating=?,audience_rating=?,reviewer_rating=?," +
            "reservation_rate=?, reservation_grade=?, grade=?, thumb=?, image=? where id = ?";

    public static final String updateMovieSql = "update movie " +
            "set title=?, dateValue=?, user_rating=?, audience_rating=?, reviewer_rating=?, " +
            "reservation_rate=?,reservation_grade=?, grade=?, thumb=?, image=? photos=?, videos=?, " +
            "outlinks=?, genre=?, duration=?, audience=?,synopsis=?,director=?,actor=?,like=?,dislike=? where id = ?";

    public static final String updateCommentSql = "update comment " +
            "set writer=?, movieId=?, writer_image=?, time=?, timestamp=?, rating=?, contents=?, recommend=? where id = ?";

}
