package com.community.hundred.common.constant;

/**
 * 项目地址访问常量
 */
public class HttpConstant {

    public static String BASE_HOST = "http://yinshi.papang.top/";

    public static String PROJECT = "index/";

    //访问URL
    public static String BASE_URL = BASE_HOST + PROJECT;

    // 第二个网络地址
    public static String KIP = "http://yinshi.papang.top/api/public/?service=";

    public static String H5IP = "http://test.liclanch.com/wap";

    public static String VIDEO_URL = "http://yinshi.papang.top";

    // 直播地址
    public static String LIVE_BROAD_CAST_URL = "http://api.k6z49.cn/mf/";

    // 直播平台
    public static String LIVE_BROAD_CAST_PLATFORM = LIVE_BROAD_CAST_URL + "json.txt";

    public static final String SEND_MSG = "Gj/fayzm";

    // 注册
    public static final String REGISTER = "Gj/register";

    // 登录
    public static final String LOGIN = "Gj/doLogin";

    // 忘记密码修改密码
    public static final String FOR_GET_PWD = "Gj/forgotpassword";

    // 公告
    public static final String NOTICE = "Gj/notice";

    // 启动图
    public static final String STARTUP_IMG = "Gj/appStartAdvert";

    // banner 图
    public static final String BANNER = "Get/getFilmSlide";

    // 首页分类
    public static final String HOME_CLASSIFY = "get/cateList";


    /****************************************首页相关************************************/
    //搜索
    public static final String searchURL = BASE_URL + "Get/search";
    // 视频搜索
    public static final String homeVideoSearchURL = KIP + "Home.videoSearch";
    // 小说搜索
    public static final String homeNovSearchURL = KIP + "Home.novSearch";
    //视频历史搜索，热门搜索
    public static final String homeVideoMast = KIP + "Home.videoMast";
    //视频列表
    public static final String listURL = BASE_URL + "Gj/lists";
    //视频详情
    public static final String videoDetails = KIP + "Video.getVideo";
    // 小说列表
    public static final String novList = KIP + "Home.novList";


    /********************************************直播相关***************************************/
    //获取直播链接
    public static final String liveURL = "http://yinshi.papang.top/index/Live/getLive";

    /*****************************************专栏************************************************/
    //圈子发布
    public static final String saveCircleURL = BASE_URL + "Gj/saveCircle";
    //分类
    public static final String zlflURL = BASE_URL + "GJ/zlfl";
    //专栏列表
    public static final String zhuanlanURL = BASE_URL + "GJ/zhuanlan";
    // 女星详情
    public static final String nxDetailsURL = KIP + "Video.getPerVideoList";
    // 专栏搜索
    public static final String zlSearchURL = KIP + "Home.zhuanSearch";
    // 专栏历史搜索，热门搜索
    public static final String zlMast = KIP + "Home.zhuanMast";

    /*****************************************论坛************************************************/
    //圈子分类
    public static final String qzflURL = BASE_URL + "GJ/qzfl";
    //圈子列表
    public static final String tzlbURL = BASE_URL + "Gj/tzlb";
    //关注列表
    public static final String guanzhulistURL = BASE_URL + "Gj/guanzhulist";
    //通过分类获取圈   子列表
    public static final String qztzURL = BASE_URL + "Gj/qztz";
    //最新圈子
    public static final String zxqzURL = BASE_URL + "Gj/zxqz";
    //推荐圈子
    public static final String tjtzURL = BASE_URL + "Gj/tjtz";
    //圈子点赞
    public static final String loveCircleURL = BASE_URL + "Gj/loveCircle";
    //圈子评论
    public static final String commentCircleURL = BASE_URL + "Gj/commentCircle";
    //关注
    public static final String guanzhuURL = BASE_URL + "Gj/guanzhu";
    //礼物列表
    public static final String liwuURL = BASE_URL + "lj/liwu";
    //礼物打赏
    public static final String dashangURL = BASE_URL + "lj/dashang";
    //帖子详情
    public static final String tiezifindURL = BASE_URL + "lj/tiezifind";
    // 获取评论
    public static final String getCommentCircleURL = KIP + "User.circlecomm";
    // 举报
    public static final String reportURL = BASE_URL + "Gj/jubao";
    // 评论点赞
    public static final String commentLoveURL = BASE_URL + "Gj/loveComment";
    // 删除评论
    public static final String commentDel = BASE_URL + "/Gj/scpl";


    /*****************************************个人中心************************************************/
    //个人中心
    public static final String userURL = BASE_URL + "lj/user";
    //用户协议
    public static final String userAgreementURL = BASE_URL + "GJ/GJ/userAgreement";
    //关于我们
    public static final String AboutURL = BASE_URL + "Gj/About";
    //我的粉丝
    public static final String guanzhumyURL = BASE_URL + "lj/guanzhumy";
    //我的关注
    public static final String myguanzhuURL = BASE_URL + "lj/myguanzhu";
    //收到的礼物
    public static final String mylwlistURL = BASE_URL + "lj/mylwlist";
    //关注对方
    public static final String ljGuanzhuURL = BASE_URL + "lj/guanzhu";
    //取消关注对方
    public static final String quxiaogzURL = BASE_URL + "lj/quxiaogz";
    //用户余额
    public static final String useryueURL = BASE_URL + "lj/useryue";
    //申请提现
    public static final String sqtxURL = BASE_URL + "lj/sqtx";
    //添加观看记录
    public static final String addGkjlURL = BASE_URL + "lj/addgkjl";
    //查询观看记录
    public static final String gkjlURL = BASE_URL + "lj/gkjl";
    //清空观看记录
    public static final String qingkongURL = BASE_URL + "lj/qingkong";
    //删除观看记录
    public static final String delgkjlURL = BASE_URL + "lj/delgkjl";
    //查询收藏
    public static final String myscjlURL = BASE_URL + "lj/myscjl";
    //取消收藏
    public static final String delmyscURL = BASE_URL + "lj/delmysc";
    //清空收藏
    public static final String delmyscqkURL = BASE_URL + "lj/delmyscqk";
    //提交意见
    public static final String yijianURL = BASE_URL + "lj/yijian";
    //安全码修改
    public static final String aqmeitURL = BASE_URL + "lj/aqmeit";
    //获取用户资料及相册
    public static final String getUserAllURL = KIP + "User.getUserAll";
    //激活码兑换
    public static final String jhmdhURL = KIP + "User.cardMake";
    // 头像上传
    public static final String txscURL = KIP + "User.go";
    // 修改资料
    public static final String xgdata = KIP + "User.updateUserinfo";
    // 相册上传
    public static final String scxcURL = KIP + "User.almgo";
    // 相册删除
    public static final String delUserImgURL = KIP + "User.delUserimg";
    // 我的发布
    public static final String mySendPostURL = BASE_URL + "lj/userteizilists";
    // 获取用户资料
    public static final String getUserDataURL = BASE_URL + "lj/xianhqing";

    /*****************************************视频详情************************************************/
    // 视频点赞
    public static final String addLikeURL = KIP + "Video.addLike";
    // 视频踩
    public static final String addStepURL = KIP + "Video.addStep";

}
