package com.exception;

public enum ErrorCode implements IErrorCode{
    QUESTION_NOT_CREATE(1,"问题发布失败,重新试试?"),
    QUESTION_NOT_FOUND(2,"你找的问题不存在,换个试试?"),
    PAGE_NOT_FOUND(3,"该页码不存在,请重试!"),
    QUESTION_NOT_UPDATE(4,"问题修改失败,重新试试?"),
    TARGET_PARAM_NOT_FOUND(2002, "未选中任何问题或评论进行回复"),
    NO_LOGIN(2003, "当前操作需要登录,请登陆后重试"),
    SYS_ERROR(2004, "服务冒烟了,要不然你稍后再试试！！！"),
    TYPE_PARAM_WRONG(2005, "评论类型错误或不存在"),
    COMMENT_NOT_FOUND(2006, "回复的评论不存在了,要不要换个试试？"),
    CONTENT_IS_EMPTY(2007, "输入内容不能为空"),
    READ_NOTIFICATION_FAIL(2008, "兄弟你这是读别人的信息呢？"),
    NOTIFICATION_NOT_FOUND(2009, "消息莫非是不翼而飞了？"),
    FILE_UPLOAD_FAIL(2010, "图片上传失败"),
    INVALID_INPUT(2011, "非法输入"),
    INVALID_OPERATION(2012, "兄弟,是不是走错房间了？"),
    USER_DISABLE(2013, "操作被禁用,如有疑问请联系管理员"),
    RATE_LIMIT(2014, "操作太快了,请稍后重试"),
    LOGIN_FAIL(2015, "登陆失败,网络不稳定,请稍后再试"),
    FAIL_SPIDER(2016,"热搜爬取失败"),
    FAIL_GET(2017,"热搜请求失败"),
    FAIL_SPIDER_PARSE(2018, "爬虫解析失败");

    private final Integer code;

    private final String message;

    ErrorCode(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public Integer getCode() {
        return code;
    }
}
