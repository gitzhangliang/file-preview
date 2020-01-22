package com.tzxx.common.support;

import lombok.Getter;
import lombok.Setter;

/**
 * @author tzxx
 */
@Getter
@Setter
public class JsonResult<T> {
    private int code = 200;
    private T data;
    private String errMsg;

    public JsonResult() {
    }

    public JsonResult(T data) {
        this.data = data;
    }

    public JsonResult(int code, String errMsg) {
        this.code = code;
        this.errMsg = errMsg;
    }

    public JsonResult(int code, T data, String errMsg) {
        this.code = code;
        this.data = data;
        this.errMsg = errMsg;
    }
}
