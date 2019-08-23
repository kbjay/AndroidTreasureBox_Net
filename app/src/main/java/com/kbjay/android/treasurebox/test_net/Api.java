package com.kbjay.android.treasurebox.test_net;


import io.reactivex.Flowable;
import retrofit2.http.GET;

//https://wanandroid.com/wxarticle/chapters/json
public interface Api {
    @GET("/wxarticle/chapters/json")
    Flowable<String> test();
}
