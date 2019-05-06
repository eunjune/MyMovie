package com.example.mymovie;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class NetwokrStatus {
    public static int getConnectivityStatus(Context context) {
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        if(networkInfo != null) {
            int type = networkInfo.getType();

            // 모바일 데이터로 연결된 상태.
            if(type == ConnectivityManager.TYPE_MOBILE) {

            }

            // 와이 파이로 연결된 상태
            else if(type == ConnectivityManager.TYPE_WIFI) {

            }
        }

        return 0;

    }

}
