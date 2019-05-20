package com.example.mymovie.network;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.mymovie.MyFunction;
import com.example.mymovie.data.ProtocolObj;


public class NetworkManager {

    public static RequestQueue requestQueue;
    private static String url = "http://boostcourse-appapi.connect.or.kr:10000/movie/";


    public NetworkManager(Context context) {
        if(requestQueue == null) {
            requestQueue = Volley.newRequestQueue(context);
        }
    }

    public void request(final ProtocolObj protocolParam, final Context context, final MyFunction func) {
        StringRequest request = new StringRequest(
                protocolParam.getRequestType(),
                url + protocolParam.getUrl(),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        func.callback(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(context, error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
        );

        request.setShouldCache(false);
        NetworkManager.requestQueue.add(request);
    }

    public static int getConnectivityStatus(Context context) {
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        if(networkInfo != null) {
            return networkInfo.getType();
        }

        return -1;

    }
}
