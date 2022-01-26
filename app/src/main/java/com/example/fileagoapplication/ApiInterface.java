package com.example.fileagoapplication;

import android.widget.EditText;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ApiInterface {

    //Login Authentication Api Call
    @Headers({
            "Accept: application/json",
    })
    @POST("auth/")
    Call<User> userlogin(@Body LoginRequest loginRequest);



    // Gettting all the folders from home
    @Headers({
            "Accept: application/json",
    })
    @GET("api/nodes/home/dirlist/")
    Call<PersonalWorkspace> getdirectory(@Header("Authorization") String token);



    //Getting Folders and Files inside folder by uuids
    @Headers({
            "Accept: application/json",
    })
    @GET("api/nodes/{uuid}/dirlist/")
    Call<PersonalWorkspace> getalldata(@Header("Authorization")String token,@Path("uuid")String uuid);



    //Getting all the Groups
    @Headers({
            "Accept: application/json",
    })
    @GET("api/groups/")
    Call<GroupWorkspace> getgroups(@Header("Authorization") String token);




    //Getting Folders of particular Group
    @Headers({
            "Accept: application/json",
    })
    @GET("api/nodes/home%3A{uuid}/dirlist/")
    Call<PersonalWorkspace> getallgroupsdata(@Header("Authorization")String token,@Path("uuid")String uuid);



    //Create FOlder Api call
    @Headers({
            "content-type: application/json",
    })
    @POST("api/nodes/{uuid}")
    Call<Void> createfolder( @Header("Authorization") String token,@Path("uuid") String uuid,@Body DIRNAME dirname);
}
