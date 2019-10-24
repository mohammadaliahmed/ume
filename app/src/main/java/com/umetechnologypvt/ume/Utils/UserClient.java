package com.umetechnologypvt.ume.Utils;

import com.umetechnologypvt.ume.Example;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface UserClient {


    @POST("/video.php")
    @Multipart
    Call<Example> uploadFile(
            @Part MultipartBody.Part file, @Part("file") RequestBody name

    );


}
