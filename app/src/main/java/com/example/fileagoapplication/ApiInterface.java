package com.example.fileagoapplication;
import android.widget.EditText;

import java.io.File;
import java.net.URL;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Url;
public interface ApiInterface {
    //Login Authentication Api Call
    @Headers({
            "Accept: application/json",
    })
    @POST("auth/")
    Call<User> userlogin(@Body LoginRequest loginRequest);

    //Gettting all the folders from home
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



    //UploadToken for 5 Secs wala call
    @Headers({
            "content-type: application/json",
    })
    @GET("api/nodes/{uuid}/upload")
    Call<UploadToken> gettoken(@Header("Authorization") String token,@Path("uuid") String uuid);


    //Upload a file using token
    @Multipart
    @POST("upload/{token}")
    Call<FilesUploadMsg> uploadfile(@Path("token")String uploadtoken,@Part MultipartBody.Part file);
   /*
Request URL: https://ocean.fileago.com/upload/761c4fa9-01c5-4004-bd16-1342d03e7b10-5f36abff-c76c-415c-af46-7d2123a22775
Request Method: POST
Status Code: 200 OK
Remote Address: 5.9.105.84:443
Referrer Policy: strict-origin-when-cross-origin
Access-Control-Allow-Origin: *
Connection: keep-alive
Content-Length: 58
Content-Type: application/json
Date: Sat, 29 Jan 2022 16:58:23 GMT
Server: nginx/1.16.1
//Accept: */
//    Accept-Encoding: gzip, deflate, br
//    Accept-Language: en-US,en;q=0.9
//    Connection: keep-alive
//    Content-Length: 602378
//    Content-Type: multipart/form-data; boundary=----WebKitFormBoundaryYrJcR8i6gVKP1B0y
//    Cookie: _ga=GA1.2.481965995.1637757587; __tawkuuid=e::fileago.com::ohjWtj8VTLXOWgk4qF3w7rVLGEYRqaSR1ikkEAzcFqsR1m/mARxcKKwyDPaeEy2m::2; _hjSessionUser_1061851=eyJpZCI6ImE1YzRmYTliLTVmNWEtNTkxNy05ZmQyLTUwNjQ1ZDljYmE1YyIsImNyZWF0ZWQiOjE2Mzc3NTc1ODYwMDgsImV4aXN0aW5nIjp0cnVlfQ==
//    Host: ocean.fileago.com
//    Origin: https://ocean.fileago.com
//    Referer: https://ocean.fileago.com/
//    sec-ch-ua: " Not;A Brand";v="99", "Microsoft Edge";v="97", "Chromium";v="97"
//    sec-ch-ua-mobile: ?0
//    sec-ch-ua-platform: "Windows"
//    Sec-Fetch-Dest: empty
//    Sec-Fetch-Mode: cors
//    Sec-Fetch-Site: same-origin
//    User-Agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/97.0.4692.99 Safari/537.36 Edg/97.0.1072.76
//
//
//
//
//            ------WebKitFormBoundaryYrJcR8i6gVKP1B0y
//    Content-Disposition: form-data; name="file"; filename="SPOORTHI 2022.png"
//    Content-Type: image/png
//
//
//------WebKitFormBoundaryYrJcR8i6gVKP1B0y--
//

        //    */

    //Delete a node
    @DELETE("api/nodes/{uuid}")
    Call<Void> deletenode(@Header("Authorization") String token,@Path("uuid") String uuid);

    //Rename a File
    @Headers({
            "Content-Type: application/json",
    })
    @POST("api/nodes/{uuid}")
    Call<Void> renamefile(@Header("Authorization") String token,@Path("uuid") String uuid,@Body RenameFile renameFile);

    @Headers({
            "Content-Type: application/json",

    })
    @POST("api/nodes/{uuid}")
    Call<Void> movenode(@Header("Authorization") String token,@Path("uuid") String uuid,@Body MoveNode moveNode);


    //Add to Favorites
    @Headers({
            "Content-Type: application/json",
    })
    @POST("api/nodes/{uuid}/")
    Call<Void> addtofavorite(@Header("Authorization") String token,@Path("uuid") String uuid,@Body Fav fav);


    //Download file from Server
    //"/resources/auth/download/" & filetoken & "/" & remotenodeuuid &
    //                    "/999999999999999/" & encodeUrl(filename)

//  https://ocean.fileago.com/resources/auth/download/b7a5d70d-c52a-4988-ae3f-1af59fe02340-2b9d303d-a7f7-426d-bc7e-5e5ce0ed233f/9e48b061-be47-4312-bcfb-3e585f910b4c/999999999999999/ToDo.txt

    @GET("resources/auth/download/{filetoken}/{uuid}/999999999999999/{filename}")
    Call<ResponseBody> downloadfile(@Path("filetoken") String filetoken, @Path("uuid") String uuid, @Path("filename") String filename);
    //999999999999999

    @Headers({
            "Accept: application/json",
    })
    @GET("api/nodes/{uuid}/myaccess")
    Call<MyAccessWorkspace> myaccess(@Header("Authorization")String token,@Path("uuid")String uuid);
}
//https://ocean.fileago.com/resources/auth/download/b7a5d70d-c52a-4988-ae3f-1af59fe02340-2b9d303d-a7f7-426d-bc7e-5e5ce0ed233f/9e48b061-be47-4312-bcfb-3e585f910b4c/999999999999999/ToDo.txt