package com.community.hundred.common.constant;

import android.os.Environment;

/**
 * 第三方KEY
 */
public class KeyConstant {

    public static final String TAG = "BaseApiRetrofit";

    public static final int DEBUGLEVEL = 1;
    /**
     * 微信AppKey
     **/
    public static String WEI_CHAT_APP_KEY = "";

    /**
     * QQ AppKey
     **/
    public static String QQ_APP_KEY = "";

    /**
     * 浏览器模式
     **/
    public static int BROWSER_PARAM_MODE = 1;

    public static String NEWS_KEY = "45b9e6a580d942b96e034bb207093193";

    public static String provider = "com.community.hundred.FileProvider";

    // 根据路径
    public static final String DCMI_PATH = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).getAbsolutePath();

    // 小说存储路径
    public static String NOVEL_SAVE_PATH = DCMI_PATH + "/hundred/txt/";

    // 位置
    public static String POSITION_KEY = "position";

    public static String HOME_TITLE = "home";

    public static String FORUM_TITLE = "forum";
}
