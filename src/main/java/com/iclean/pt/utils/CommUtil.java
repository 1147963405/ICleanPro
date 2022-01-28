package com.iclean.pt.utils;

import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Component;

import java.util.Map;
/*
* 对前端传回的参数进行解析转换为JSON对象
* */
@Component
public class CommUtil {


    public JSONObject getJson(Map map){

        JSONObject jsonObject=null;
        int lastIndexOf = map.toString().lastIndexOf(":");
        if(lastIndexOf==-1){
            String jsonStr = map.toString().replace("=", ":");
            jsonObject=JSONObject.parseObject(jsonStr);
        }else {
            String subStr = map.toString().substring(0, map.toString().length() - 2);
            jsonObject = JSONObject.parseObject(subStr.substring(1));
        }
        return jsonObject;
    }
}
