package com.project.an;

import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Path;
import retrofit2.http.Streaming;

public interface API {


    @GET("getVideos")
    Call<List<Video>> getVideos();

    @Multipart                                                                                          // POST request to upload an image from storage
    @POST("upload")
    Call<ResponseBody> uploadVideo(@Part MultipartBody.Part video, @PartMap Map<String, RequestBody> requestBody);
}