package com.iclean.pt.utils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

//工具类
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommnosResult<T> {

    //200成功 444 失败。。。。
    private Integer code;
    private String msg;
    private T data;

    //重载
    public CommnosResult(Integer code, String msg) {
       this(code,msg,null);
    }
}
