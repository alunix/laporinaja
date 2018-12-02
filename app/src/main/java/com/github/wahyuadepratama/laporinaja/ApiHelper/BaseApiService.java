package com.github.wahyuadepratama.laporinaja.ApiHelper;

import com.github.wahyuadepratama.laporinaja.Model.ReportList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by wahyu on 01/12/18.
 */

public interface BaseApiService {
    // Fungsi ini untuk memanggil API http://domain/api/login
    @FormUrlEncoded
    @POST("login")
    Call<ResponseBody> login(@Field("identity") String identity,
                             @Field("password") String password);

    @FormUrlEncoded
    @POST("register")
    Call<ResponseBody> register(@Field("name") String name,
                                  @Field("email") String email,
                                  @Field("phone") String phone,
                                  @Field("password") String password);
    @GET("report")
    Call<ReportList> getReport();
}
