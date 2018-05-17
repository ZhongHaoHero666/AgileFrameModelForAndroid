package com.android.szh.common.http;

import android.util.SparseArray;

/**
 * HTTP状态码辅助类
 */
public final class HttpCode {

    private static final SparseArray<String> HTTP_CODE_MAP = new SparseArray<>();

    static {
        HTTP_CODE_MAP.put(100, "客户端应当继续发出请求");
        HTTP_CODE_MAP.put(101, "客户端要求服务器根据请求转换HTTP协议版本");
        HTTP_CODE_MAP.put(102, "处理将被继续执行");
        HTTP_CODE_MAP.put(200, "请求成功");
        HTTP_CODE_MAP.put(201, "请求成功并创建了新的资源");
        HTTP_CODE_MAP.put(202, "服务器已接受请求，但尚未处理");
        HTTP_CODE_MAP.put(203, "服务器已成功处理了请求，但返回的信息可能来自另一来源");
        HTTP_CODE_MAP.put(204, "服务器成功处理了请求，但没有返回任何内容");
        HTTP_CODE_MAP.put(205, "服务器成功处理了请求，但没有返回任何内容，且要求请求者重置文档视图");
        HTTP_CODE_MAP.put(206, "服务器已经成功处理了部分GET请求");
        HTTP_CODE_MAP.put(300, "请求的资源有多种可供选择的回馈信息");
        HTTP_CODE_MAP.put(301, "请求的资源已被永久的移动到新URI，并且会自动定向到新URI");
        HTTP_CODE_MAP.put(302, "请求的资源已被临时的移动到新URI，并且会自动定向到新URI");
        HTTP_CODE_MAP.put(303, "请求的资源可在另一个URI上被找到，应当采用GET的方式访问那个资源");
        HTTP_CODE_MAP.put(304, "自从上次请求后，请求的资源未修改过，HTTP缓存有效");
        HTTP_CODE_MAP.put(305, "被请求的资源需要通过Location请求头信息中的代理服务器访问");
        HTTP_CODE_MAP.put(306, "前一版本HTTP中使用的代码，现行版本中不再使用");
        HTTP_CODE_MAP.put(307, "申明请求的资源临时性删除");
        HTTP_CODE_MAP.put(400, "请求格式错误");
        HTTP_CODE_MAP.put(401, "请求要求身份验证");
        HTTP_CODE_MAP.put(403, "服务器拒绝访问");
        HTTP_CODE_MAP.put(404, "服务器找不到请求的资源");
        HTTP_CODE_MAP.put(405, "请求中指定的方法不被允许");
        HTTP_CODE_MAP.put(406, "用户请求的格式不可得");
        HTTP_CODE_MAP.put(407, "请求者应当授权使用代理");
        HTTP_CODE_MAP.put(408, "服务器等候请求时发生超时");
        HTTP_CODE_MAP.put(409, "服务器在完成请求时发生冲突");
        HTTP_CODE_MAP.put(410, "请求的资源被永久删除");
        HTTP_CODE_MAP.put(411, "服务器拒绝处理不包含Content-Length请求头字段的请求");
        HTTP_CODE_MAP.put(412, "服务器未满足请求者在请求中设置的其中一个前提条件");
        HTTP_CODE_MAP.put(413, "请求实体过大，超出服务器的处理能力");
        HTTP_CODE_MAP.put(414, "请求的URI过长，服务器无法处理");
        HTTP_CODE_MAP.put(415, "请求的格式不受请求资源的支持");
        HTTP_CODE_MAP.put(416, "服务器不能满足Range请求头字的要求");
        HTTP_CODE_MAP.put(417, "服务器不满足Expect请求头字段的要求");
        HTTP_CODE_MAP.put(421, "从当前客户端所在的IP地址到服务器的连接数超过了服务器许可的最大范围");
        HTTP_CODE_MAP.put(422, "请求格式正确，但是含有语义错误");
        HTTP_CODE_MAP.put(424, "由于之前的某个请求发生的错误，导致当前请求失败");
        HTTP_CODE_MAP.put(426, "客户端应当切换到TLS/1.0");
        HTTP_CODE_MAP.put(429, "请求过多");
        HTTP_CODE_MAP.put(500, "服务器发生错误");
        HTTP_CODE_MAP.put(501, "服务器不具备完成请求的功能");
        HTTP_CODE_MAP.put(502, "网关错误");
        HTTP_CODE_MAP.put(503, "服务不可用");
        HTTP_CODE_MAP.put(504, "网关超时");
        HTTP_CODE_MAP.put(505, "服务器不支持请求中所用的HTTP协议版本");
    }

    public static String get(int code) {
        return HTTP_CODE_MAP.get(code);
    }

}
