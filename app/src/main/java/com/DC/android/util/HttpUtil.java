package com.DC.android.util;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * Created by XZJ on 2017/10/18.
 */

public class HttpUtil {
    public static void  sendOkHttpRequest(String address,String token,okhttp3.Callback callback){
        OkHttpClient client = new OkHttpClient();
        RequestBody requestBody = new FormBody.Builder()
                .add("token",token )
                .build();
        Request request =new Request.Builder().url(address).post(requestBody).build();
        client.newCall(request).enqueue(callback);
    }
    public static void  sendDonateOkHttpRequest(String address,String token,int projectId, String numofMoney,okhttp3.Callback callback){
        OkHttpClient client = new OkHttpClient();
        RequestBody requestBody = new FormBody.Builder()
                .add("token",token ).add("projectId",Integer.toString(projectId)).add("numOfMoney",numofMoney)
                .build();
        Request request =new Request.Builder().url(address).post(requestBody).build();
        client.newCall(request).enqueue(callback);
    }
    public static void  sendPlanSignOkHttpRequest(String address,String token,int planId,okhttp3.Callback callback){
        OkHttpClient client = new OkHttpClient();
        RequestBody requestBody = new FormBody.Builder()
                .add("token",token ).add("planId",Integer.toString(planId))
                .build();
        Request request =new Request.Builder().url(address).post(requestBody).build();
        client.newCall(request).enqueue(callback);
    }
}
