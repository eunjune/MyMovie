package com.example.mymovie;

import android.content.Context;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.example.mymovie.data.CommentInfo;
import com.example.mymovie.data.MovieDetailInfo;
import com.example.mymovie.data.ResponseCommentList;
import com.example.mymovie.data.ResponseInfo;
import com.example.mymovie.database.DBHelper;
import com.example.mymovie.network.NetworkManager;
import com.example.mymovie.network.ProtocolObj;

import java.util.ArrayList;
import java.util.List;

public class CommentRepo {

    private NetworkManager networkManager;
    private Context context;

    public CommentRepo(Context context) {
        this.networkManager = new NetworkManager(context);
        this.context = context;
    }

    public void requestCommentWrite(String url, MovieDetailInfo movieDetailInfo, String rating
            , String contents, final UiResponseCallback uiResponseCallback) {

        if(!networkManager.isNetworkAvailable()) {
            String message = context.getResources().getString(R.string.all_connection_error);
            Toast.makeText(context, message, Toast.LENGTH_LONG).show();
            return;
        }

        final ProtocolObj protocolObj = new ProtocolObj();
        protocolObj.setUrl(url);
        protocolObj.setRequestType(Request.Method.GET);
        protocolObj.setParam("id",String.valueOf(movieDetailInfo.getId()));
        protocolObj.setParam("writer","kym7112");
        protocolObj.setParam("time",movieDetailInfo.getDate());
        protocolObj.setParam("rating",rating);
        protocolObj.setParam("contents",contents);

        /*protocolObj.setUrl("createComment");
        protocolObj.setRequestType(Request.Method.GET);
        protocolObj.setParam("id",String.valueOf(movieDetailInfo.getId()));
        protocolObj.setParam("writer","kym7112");
        protocolObj.setParam("time",movieDetailInfo.getDate());
        protocolObj.setParam("rating",String.valueOf(ratingBar.getRating()));
        protocolObj.setParam("contents",etContents.getText().toString());*/

        NetworkResponseCallback networkResponseCallback = new NetworkResponseCallback() {
            @Override
            public void callback(String response) {

                ResponseInfo responseInfo = protocolObj.getResponseInfo(response);

                if(responseInfo.code == 200) {
                    uiResponseCallback.callback();
                    //finish()
                } else {
                    errorMessage(responseInfo);
                }
            }
        };

        networkManager.request(protocolObj,context, networkResponseCallback);

    }

    private void requestCommentList() {

        if(networkManager.isNetworkAvailable()) {

            // 한줄평 리스트 불러오기
            final ProtocolObj protocolObj = new ProtocolObj();
            protocolObj.setUrl("readCommentList");
            protocolObj.setRequestType(Request.Method.GET);
            protocolObj.setParam("id",String.valueOf(movieDetailInfo.getId()));
            protocolObj.setResponseClass(ResponseCommentList.class);

            NetworkResponseCallback networkResponseCallback = new NetworkResponseCallback() {
                @Override
                public void callback(String response) {

                    ResponseInfo responseInfo = protocolObj.getResponseInfo(response);

                    if(responseInfo.code == 200) {
                        ResponseCommentList list = (ResponseCommentList) protocolObj.getResponseClass(response);
                        ListView listView = (ListView) rootView.findViewById(R.id.listView);
                        ArrayList<CommentInfo> items = list.getResult();

                        for(CommentInfo item : items) {
                            if(DBHelper.selectCommentCount(item.getId()) == 0) {
                                DBHelper.insertComment(item);
                            } else {
                                DBHelper.updateComment(item);
                            }
                        }

                        commentAdapter = new CommentAdapter(items, getContext());
                        commentAdapter.setTotal(responseInfo.totalCount);
                        listView.setAdapter(commentAdapter);
                    } else if(responseInfo.code == 400) {
                        String message = getResources().getString(R.string.all_load_error);
                        Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
                    } else {
                        String message = getResources().getString(R.string.all_unknown_error);
                        Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
                    }
                }
            };

            networkManager.request(protocolObj, getContext(), networkResponseCallback);

        } else {
            List<CommentInfo> items = DBHelper.selectCommentList(movieDetailInfo.getId());
            ListView listView = (ListView) rootView.findViewById(R.id.listView);

            commentAdapter = new CommentAdapter(items, getContext());
            commentAdapter.setTotal(items.size());
            listView.setAdapter(commentAdapter);
        }
    }

    public void requestRecommend() {
        final ProtocolObj protocolObj = new ProtocolObj();
        protocolObj.setUrl("increaseRecommend");
        protocolObj.setRequestType(Request.Method.GET);
        protocolObj.setParam("review_id",String.valueOf(reviewId));
        protocolObj.setParam("writer",tvWriter.getText().toString());

        NetworkResponseCallback networkResponseCallback = new NetworkResponseCallback() {
            @Override
            public void callback(String response) {
                ResponseInfo responseInfo = protocolObj.getResponseInfo(response);

                if(responseInfo.code == 200) {
                    int recommendation = Integer.valueOf(tvRecommendation.getText().toString());
                    recommendation++;

                    String strRecommendation = String.valueOf(recommendation);
                    tvRecommendation.setText(strRecommendation);
                } else if(responseInfo.code == 400) {
                    String message = getResources().getString(R.string.all_write_error);
                    Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
                } else {
                    String message = getResources().getString(R.string.all_unknown_error);
                    Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
                }
            }
        };

        networkManager.request(protocolObj,getContext(), networkResponseCallback);

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
