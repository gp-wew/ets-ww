package com.swire.etsww.base;


import lombok.extern.slf4j.Slf4j;

/**
 * dwz.core.js
 * statusCode: {ok:200, error:300, timeout:301}
 */
@Slf4j
public class Dwz {

    public final static String ERROR="300";
    public final static String NOT_LOGIN="401";
    public final static String SUCCESS="200";
    public final static String TIP  = "201";
    public final static String CLIENT_VERSION_LOW  = "402";


    public static final Dwz OK=new Dwz();

    public String statusCode=SUCCESS;

    public String message="处理成功";

    public Object content;

    public Dwz(String statusCode, String message, Object content) {
        this.statusCode = statusCode;
        this.message = message;
        this.content = content;
    }

    public Dwz(String statusCode){
        this.statusCode=statusCode;
    }

    public Dwz(String statusCode,String message){
        this.statusCode=statusCode;
        this.message=message;
    }

    public Dwz(){
    }

    public static Dwz error(String msg){
        return new Dwz(ERROR,msg);
    }

    public static Dwz error(String statusCode, String message){
        return new Dwz(statusCode, message);
    }

    public static Dwz ok(String message, Object content){
        return new Dwz(SUCCESS,message,content);
    }

    public static Dwz success(Object content){
        return new Dwz(SUCCESS,"处理成功",content);
    }

}