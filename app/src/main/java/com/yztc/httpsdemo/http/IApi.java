package com.yztc.httpsdemo.http;


import okhttp3.ResponseBody;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.Url;
import rx.Observable;

/**
 * Created by wanggang on 2016/11/2.
 *
 * 定义接口
 *
 */

public interface IApi {

    @FormUrlEncoded
    @Headers({"Cache-Control : public,max-age=3600"})
    @POST("RegServlet")
    Observable<BaseResponse<String>> register(@Field("username") String username, @Field("password") String password);

    @GET("RegServlet")
    Observable<BaseResponse<String>> registerGet(@Query("username") String username, @Query("password") String password);


    @GET
    Observable<ResponseBody> getUrl(@Url String url);
}
