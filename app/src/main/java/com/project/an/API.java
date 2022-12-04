package com.project.an;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Streaming;

public interface API {

    // GET request to get all videos
    @GET("getVideos")
    Call<List<Video>> getVideos();

    // POST request to upload a video
    @Multipart
    @POST("upload")
    Call<ResponseBody> uploadVideo(@Part MultipartBody.Part video, @Part MultipartBody.Part title, @Part MultipartBody.Part description);
}