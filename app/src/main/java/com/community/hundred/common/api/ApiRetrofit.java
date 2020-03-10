package com.community.hundred.common.api;

import com.community.hundred.common.base.BaseResponse;
import com.community.hundred.common.constant.HttpConstant;
import com.community.hundred.common.network.BaseApiRetrofit;
import com.community.hundred.modules.entry.LoginEntry;
import com.community.hundred.modules.ui.main.entry.NoticeEntry;
import com.community.hundred.modules.ui.startup.entry.StartUpEntry;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * @创建者 CSDN_LQR
 * @描述 使用Retrofit对网络请求进行配置
 */
public class ApiRetrofit extends BaseApiRetrofit {

    public MyApi mApi;
    public static ApiRetrofit mInstance;

    private ApiRetrofit() {
        super();
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        //在构造方法中完成对Retrofit接口的初始化
        mApi = new Retrofit.Builder()
                .baseUrl(HttpConstant.BASE_URL)
                .client(getClient())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
                .create(MyApi.class);
    }

    public static ApiRetrofit getInstance() {
        if (mInstance == null) {
            synchronized (ApiRetrofit.class) {
                if (mInstance == null) {
                    mInstance = new ApiRetrofit();
                }
            }
        }
        return mInstance;
    }

    private RequestBody getRequestBody(Object obj) {
        String route = new Gson().toJson(obj);
        RequestBody body=RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"),route);
        return body;
    }

    // 发送验证码
    public Observable<BaseResponse<String>> sendMsg(String phone) {
        return mApi.sendMsg(phone);
    }

    // 注册
    public Observable<BaseResponse<String>> register(String phone,String password,String code) {
        return mApi.register(phone,password,code);
    }

    // 登录
    public Observable<BaseResponse<LoginEntry>> login(String phone, String password) {
        return mApi.login(phone,password);
    }

    // 忘记密码
    public Observable<BaseResponse<String>> forgetPwd(String phone,String password,String code) {
        return mApi.forgetPwd(phone,password,code);
    }

    // 公告
    public Observable<BaseResponse<NoticeEntry>> notice() {
        return mApi.notice();
    }

    // 启动图
    public Observable<BaseResponse<List<StartUpEntry>>> startUp() {
        return mApi.startUp();
    }







}
