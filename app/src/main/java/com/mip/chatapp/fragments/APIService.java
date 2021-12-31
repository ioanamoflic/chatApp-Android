package com.mip.chatapp.fragments;

import com.mip.chatapp.notifications.MyResponse;
import com.mip.chatapp.notifications.Sender;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface APIService {
    @Headers(
            {
                    "Content-Type:application/json",
                    "Authorization:key=AAAAdm5BI7Y:APA91bHEC-Wqeg-uE6Px6AdvzZGpL3BHO-mA6iej50Di18Gk72MjV5MH0roKXk9dNZbF4Olti-LA0KRa-U6WyB48LuY69Tphk_62h-8g5Pt3pdhCnFTo4TIa6ZvJB-gV8WxpJm48DpiH"
            }
    )

    @POST("fcm/send")
    Call<MyResponse> sendNotification(@Body Sender body);
}
