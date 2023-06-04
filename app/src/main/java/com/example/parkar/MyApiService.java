package com.example.parkar;

import com.firebase.ui.auth.data.model.User;
import okhttp3.RequestBody;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface MyApiService {


//    @Multipart
//    @POST("api/helper/help/")
//    Call<UploadResponse> uploadFile(
//            @Part("description") RequestBody description,
//            @Part MultipartBody.Part file);

    @POST("api/helper/help/")
    Call<FileUploadResponse> uploadFile(@Body FileUploadRequest request);


}


