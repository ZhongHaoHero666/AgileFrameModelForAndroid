package com.android.szh.common.http.api;

import java.util.Map;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.QueryMap;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

/**
 * ApiService(请求网络的API接口类)
 */
public interface BaseApiService {

    @GET()
    Observable<ResponseBody> executeGet(@Url String url);

    @GET()
    Observable<ResponseBody> executeGet(@Url String url, @QueryMap Map<String, String> map);

    @POST()
    Observable<ResponseBody> executePost(@Url() String url);

    @POST()
    @FormUrlEncoded
    Observable<ResponseBody> executePost(@Url() String url, @FieldMap Map<String, String> map);

    @POST()
    Observable<ResponseBody> executePost(@Url String url, @Body RequestBody Body);

    @DELETE()
    Observable<ResponseBody> executeDelete(@Url String url, @QueryMap Map<String, String> map);

    @PUT()
    @FormUrlEncoded
    Observable<ResponseBody> executePut(@Url String url, @FieldMap Map<String, String> map);

    @POST()
    @Multipart
    Observable<ResponseBody> uploadFile(@Url String url, @Part MultipartBody.Part file);

    @POST()
    Observable<ResponseBody> uploadFile(@Url() String url, @Body RequestBody body);

    @POST()
    @Multipart
    Observable<ResponseBody> uploadFile(@Url() String url, @PartMap Map<String, RequestBody> map);

    @POST
    @Multipart
    Observable<ResponseBody> uploadFile(@Url() String url, @PartMap() Map<String, RequestBody> partMap, @Part MultipartBody.Part part);

    @GET
    @Streaming
    Observable<ResponseBody> downloadFile(@Url String url);

}
