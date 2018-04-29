package com.xhm.simpleamoy.data.entity;

/**
 * Created by xhm on 2018/4/28.
 */

public class Event<T> {
    private String msg;
    private T data;

    public Event(String msg) {
        this.msg = msg;
    }

    public Event(String msg, T data) {
        this.msg = msg;
        this.data = data;
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
}
