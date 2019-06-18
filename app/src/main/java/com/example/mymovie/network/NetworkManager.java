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
import com.example.mymovie.NetworkResponseCallback;


public class NetworkManager {

    private static RequestQueue requestQueue;
    private static String url = "http://boostcourse-appapi.connect.or.kr:10000/movie/";
    private Context context;

    public NetworkManager(Context context) {
        if(requestQueue == null) {
            requestQueue = Volley.newRequestQueue(context);
        }
        this.context = context;
    }

    public void request(final ProtocolObj protocolParam, final Context context, final NetworkResponseCallback func) {
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

    public static boolean isNetworkAvailable() {
        ConnectivityManager manager = (ConnectivityManager) this.context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        int networkState = -1;

        if(networkInfo != null) {
            networkState = networkInfo.getType();
        }

        return networkState == ConnectivityManager.TYPE_MOBILE || networkState == ConnectivityManager.TYPE_WIFI;

    }

}
