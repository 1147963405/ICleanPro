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
//       System.out.println("beginTime"+ map.get("beginTime").toString());
        if(map.keySet().contains("beginTime") ){
            String str="'"+map.get("beginTime")+"'";
            map.put("beginTime",str);
        }
        if(map.keySet().contains("endTime")){
            String str="'"+map.get("endTime")+"'";
            map.put("endTime",str);
        }
      /*
        if(map.keySet().contains("beginTime")||map.keySet().contains("endTime")){
            map.put("beginTime","0");
            map.put("endTime","0");
        }*/
        if(map.keySet().contains("device_name")){
            String str="\""+map.get("device_name")+"\"";
            map.put("device_name",str);
        }else if(map.keySet().contains("robot_name")){
            String str="\""+map.get("robot_name")+"\"";
            map.put("robot_name",str);
        }else if(map.keySet().contains("name")){
            String str="\""+map.get("name")+"\"";
            map.put("name",str);
        }
        int lastIndexOf;
        if(map.keySet().contains("beginTime") || map.keySet().contains("endTime")){
            lastIndexOf =-1;
        }else {
            lastIndexOf = map.toString().lastIndexOf(":");
        }
//        int lastIndexOf = map.toString().lastIndexOf(":");
        if(lastIndexOf==-1){
            String jsonStr = map.toString().replace("=", ":");
           /* if(jsonStr.contains("beginTime")||jsonStr.contains("endTime")){

            }*/
            jsonObject=JSONObject.parseObject(jsonStr);
        }else {
            String subStr = map.toString().substring(0, map.toString().length() - 2);
            jsonObject = JSONObject.parseObject(subStr.substring(1));
        }
        return jsonObject;
    }
}
