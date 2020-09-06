package top.alanlee.template.dto;

import java.io.Serializable;

public class ApiJson<T> implements Serializable {
    private int code;
    private String msg;
    private T data;

    public ApiJson(int code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public ApiJson() {
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "ApiJson{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                '}';
    }

    public static ApiJson ok(){
        return new ApiJson(1, "ok", null);
    }

    public static ApiJson ok(String msg){
        return new ApiJson(1, msg, null);
    }

    public static ApiJson ok(Object data){
        return new ApiJson(1, "ok", data);
    }

    public static ApiJson ok(String msg, Object data){
        return new ApiJson(1, msg, data);
    }

    public static ApiJson error(){
        return new ApiJson(0, "error", null);
    }

    public static ApiJson error(String msg){
        return new ApiJson(0, msg, null);
    }

    public static ApiJson error(String msg, Object data){
        return new ApiJson(0, msg, data);
    }
}

