package com.community.hundred.common.api;


import com.community.hundred.common.base.BaseResponse;
import com.community.hundred.common.constant.HttpConstant;
import com.community.hundred.modules.entry.LoginEntry;
import com.community.hundred.modules.ui.main.entry.NoticeEntry;
import com.community.hundred.modules.ui.startup.entry.StartUpEntry;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface MyApi {


    /**
     * 发送验证码
     *
     * @param phone
     * @return
     */
    @POST(HttpConstant.SEND_MSG)
    @FormUrlEncoded
    Observable<BaseResponse<String>> sendMsg(@Field("phone") String phone);

    /**
     * 注册
     *
     * @param phone
     * @param password
     * @param code
     * @return
     */
    @GET(HttpConstant.REGISTER)
    Observable<BaseResponse<String>> register(@Query("phone") String phone,
                                              @Query("password") String password,
                                              @Query("code") String code);

    // 登录
    @GET(HttpConstant.LOGIN)
    Observable<BaseResponse<LoginEntry>> login(@Query("phone") String phone,
                                               @Query("password") String password);

    /**
     * 注册
     *
     * @param phone
     * @param password
     * @param code
     * @return
     */
    @GET(HttpConstant.FOR_GET_PWD)
    Observable<BaseResponse<String>> forgetPwd(@Query("phone") String phone,
                                               @Query("password") String password,
                                               @Query("code") String code);

    // 公告
    @POST(HttpConstant.NOTICE)
    Observable<BaseResponse<NoticeEntry>> notice();

    // 公告
    @POST(HttpConstant.STARTUP_IMG)
    Observable<BaseResponse<List<StartUpEntry>>> startUp();

}
