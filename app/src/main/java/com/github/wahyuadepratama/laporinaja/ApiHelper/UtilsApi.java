package com.github.wahyuadepratama.laporinaja.ApiHelper;

/**
 * Created by wahyu on 01/12/18.
 */

public class UtilsApi {
    public static final String BASE_URL_API = "http://laporinaja.wapydesign.com/api/";
    public static final String BASE_URL_IMAGE = "http://laporinaja.wapydesign.com/storage/report/";

    // Mendeklarasikan Interface BaseApiService
    public static BaseApiService getAPIService(){
        return RetrofitClient.getClient(BASE_URL_API).create(BaseApiService.class);
    }
}
