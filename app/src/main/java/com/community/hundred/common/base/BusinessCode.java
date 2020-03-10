package com.community.hundred.common.base;

/**
 * @author wangxiangyi
 * @名称 返回状态码和对应的信息
 * @Create 2018/8/3.
 */
public enum BusinessCode {

    //暂无数据
    NO_DATA(100, "暂无数据"),
    /**
     * 请求成功
     */
    SUCCESS(200, "请求成功"),
    /**
     * 访问设备和登录设备不一致
     */
    INCONSISTENT(302, "该账号已在其它设备登录！"),
    /**
     * 请先登录
     */
    PLEASE_LOG_FIRST(303, "请先登录"),
    /**
     * 返回错误码<p>
     * 超时请求
     */
    TIMEOUT_REQUEST(201, "超时请求"),
    /**
     * 返回错误码<p>
     * 服务器异常
     */
    SERVER_EXCEPTION(300, "服务器异常"),
    /**
     * 返回错误码<p>
     * 参数传送出错
     */
    PARAMETER_ERROR(400, "参数传送出错"),
    /*对应HTTP的状态码*/
    /**
     * 未经授权
     */
    UNAUTHORIZED(401, "未经授权"),
    /**
     * 请求被拒绝
     */
    FORBIDDEN(403, "请求被拒绝"),
    /**
     * 未找到此网页
     */
    NOT_FOUND(404, "未找到此网页"),
    /**
     * 请求超时
     */
    REQUEST_TIMEOUT(408, "请求超时"),
    /**
     * 返回错误码<p>
     * 表示不确定是那方原因 看具体备注信息
     */
    INTERNAL_SERVER_ERROR(500, "内部服务器错误"),
    /**
     * 内部服务器错误
     */
    BAD_GATEWAY(502, "错误的网关"),
    /**
     * 暂停服务
     */
    SERVICE_UNAVAILABLE(503, "暂停服务"),
    /**
     * 网关超时
     */
    GATEWAY_TIMEOUT(504, "网关超时"),
    /**
     * 返回错误码<p>
     * 服务器错误
     */
    SERVER_ERROR(600, "服务器错误"),
    /*错误码*/
    /**
     * 未知错误
     */
    UNKNOWN(1000, "未知错误"),
    /**
     * 网络链接超时，请检查网络
     */
    PLEASE_CHECK_NETWORK(1005, "网络连接超时，请检查网络"),
    /**
     * 解析错误
     */
    PARSE_ERROR(1001, "解析错误"),
    /**
     * 网络错误
     */
    NETWORD_ERROR(1002, "网络连接失败、请检查网络"),
    /**
     * 协议错误
     */
    HTTP_ERROR(1003, "协议错误");
    int code;
    String message;

    BusinessCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public static String getServerStatusValue(Integer code) {
        for (BusinessCode businessCode : BusinessCode.values()) {
            if (businessCode.getCode() == code) {
                return businessCode.getMessage();
            }
        }
        return "";
    }
}
