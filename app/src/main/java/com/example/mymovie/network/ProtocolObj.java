package com.example.mymovie.network;

import com.example.mymovie.data.ResponseInfo;
import com.google.gson.Gson;

import java.util.HashMap;

public class ProtocolObj {
    private String url;
    private int requestType;
    private HashMap<String,String> params = new HashMap<String,String>();
    private Class<?> responseClass;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getRequestType() {
        return requestType;
    }

    public void setRequestType(int requestType) {
        this.requestType = requestType;
    }

    public HashMap<String, String> getParams() {
        return params;
    }

    public void setParams(HashMap<String, String> params) {
        this.params = params;
    }

    public void setParam(String key, String value) {
        if(params.size() == 0) {
            url += "?";
        } else {
            url += "&";
        }
        url += key + "=" + value;

        params.put(key,value);
    }

    public String getParam(String key) {
        return params.get(key);
    }

    public void setResponseClass(Class<?> responseClass) {
        this.responseClass = responseClass;
    }

    public Object getResponseClass(String response) {
        Gson gson = new Gson();

        return (Object) gson.fromJson(response, responseClass);
    }

    public ResponseInfo getResponseInfo(String response) {
        Gson gson = new Gson();

        return gson.fromJson(response, ResponseInfo.class);
    }


}
