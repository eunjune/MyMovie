package com.example.mymovie;

import android.content.Context;
import android.widget.Toast;

import com.android.volley.Request;
import com.example.mymovie.data.MovieDetailInfo;
import com.example.mymovie.data.MovieInfo;
import com.example.mymovie.data.ResponseInfo;
import com.example.mymovie.data.ResponseMovieDetailList;
import com.example.mymovie.data.ResponseMovieList;
import com.example.mymovie.database.DBHelper;
import com.example.mymovie.network.NetworkManager;
import com.example.mymovie.network.ProtocolObj;

import java.util.List;

public class MovieRepo {

    private NetworkManager networkManager;
    private Context context;

    public MovieRepo(Context context) {
        this.networkManager = new NetworkManager(context);
        this.context = context;
    }

    public void requestMovieList(String url, String type, Class<?> obj, final UiResponseCallback uiResponseCallback) {

        if(!NetworkManager.isNetworkAvailable()) {
            List<MovieInfo> movieList = DBHelper.selectMovieList();
            uiResponseCallback.callback(movieList);
            return;
        }

        // 영화 목록 요청
        final ProtocolObj protocolObj = new ProtocolObj();
        protocolObj.setRequestType(Request.Method.GET);
        protocolObj.setUrl(url);
        protocolObj.setParam("type",type);
        protocolObj.setResponseClass(obj);

        NetworkResponseCallback networkResponseCallback = new NetworkResponseCallback() {
            @Override
            public void callback(String response) {
                ResponseInfo responseInfo = protocolObj.getResponseInfo(response);
                if(responseInfo.code == 200) {
                    ResponseMovieList responseMovieList = (ResponseMovieList)protocolObj.getResponseClass(response);
                    List<MovieInfo> movieList = responseMovieList.getResult();

                    for(MovieInfo item : movieList) {
                        if(DBHelper.selectMovieListCount(item.getId()) == 0) {
                            DBHelper.insertMovieList(item);
                        } else {
                            DBHelper.updateMovieList(item);
                        }

                    }
                    uiResponseCallback.callback(movieList);
                } else {
                    errorMessage(responseInfo);
                }
            }
        };

        networkManager.request(protocolObj,context, networkResponseCallback);

    }

    public  void increaseLikeDislike(int movieId, final String likeyn, final String dislikeyn,final UiResponseCallback uiResponseCallback) {

        final ProtocolObj protocolObj = new ProtocolObj();
        protocolObj.setUrl("increaseLikeDisLike");
        protocolObj.setRequestType(Request.Method.GET);
        protocolObj.setParam("id",String.valueOf(movieId));

        if(likeyn == null && dislikeyn == null) {
            return;
        }

        if(likeyn != null) {
            protocolObj.setParam("likeyn",likeyn);
        }

        else if(dislikeyn != null) {
            protocolObj.setParam("dislikeyn",dislikeyn);
        }

        NetworkResponseCallback networkResponseCallback = new NetworkResponseCallback() {
            @Override
            public void callback(String response) {
                ResponseInfo responseInfo = protocolObj.getResponseInfo(response);

                if(responseInfo.code == 200) {
                    uiResponseCallback.callback(likeyn,dislikeyn);

                } else if(responseInfo.code == 400) {
                    errorMessage(responseInfo);
                }
            }
        };
        networkManager.request(protocolObj, context, networkResponseCallback);
    }

    public void requestMovie(String url, MovieInfo movieInfo, Class<?> obj, final UiResponseCallback uiResponseCallback) {

        if(!NetworkManager.isNetworkAvailable()) {
            MovieDetailInfo movieDetailInfo = DBHelper.selectMovie(movieInfo.getId());
            uiResponseCallback.callback(movieDetailInfo);
            return;

        }

        final ProtocolObj protocolObj = new ProtocolObj();
        protocolObj.setUrl(url);
        protocolObj.setRequestType(Request.Method.GET);
        protocolObj.setParam("name",movieInfo.getTitle());
        protocolObj.setResponseClass(obj);

        NetworkResponseCallback networkResponseCallback = new NetworkResponseCallback() {
            @Override
            public void callback(String response) {

                ResponseInfo responseInfo = protocolObj.getResponseInfo(response);

                if(responseInfo.code == 200) {
                    ResponseMovieDetailList responseMovieDetailList = (ResponseMovieDetailList) protocolObj
                            .getResponseClass(response);
                    MovieDetailInfo movieDetailInfo = responseMovieDetailList.getMovieDetailInfo(0);

                    if(DBHelper.selectMovieCount(movieDetailInfo.getId()) == 0) {
                        DBHelper.insertMovie(movieDetailInfo);
                    } else {
                        DBHelper.updateMovie(movieDetailInfo);
                    }
                    uiResponseCallback.callback(movieDetailInfo);

                } else {
                    errorMessage(responseInfo);
                }
            }
        };

        networkManager.request(protocolObj,context, networkResponseCallback);
    }

    private void errorMessage(ResponseInfo responseInfo) {
        if(responseInfo.code == 400) {
            String message = context.getResources().getString(R.string.all_load_error);
            Toast.makeText(context, message, Toast.LENGTH_LONG).show();
        } else {
            String message = context.getString(R.string.all_unknown_error);
            Toast.makeText(context, message, Toast.LENGTH_LONG).show();
        }
    }

}
