package com.github.wahyuadepratama.laporinaja.ApiHelper;

import android.graphics.Bitmap;

import com.github.wahyuadepratama.laporinaja.Model.ReportList;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
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

    @FormUrlEncoded
    @POST("report/store")
    Call<ResponseBody> reportStore(
            @Field("id_type") String id_type,
            @Field("id_user") String id_user,
            @Field("address") String address,
            @Field("photo") String photo,
            @Field("description") String description,
            @Field("lat") Double lat,
            @Field("lang") Double lang);

    @FormUrlEncoded
    @POST("report/favorite/store")
    Call<ResponseBody> addFavorite(@Field("id") Integer id);

    @GET("report/favorite")
    Call<ReportList> getFavoriteReport();
}
