package com.iclean.pt.sbgl.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.iclean.pt.utils.Result;
import jdk.nashorn.internal.runtime.regexp.joni.Regex;
import net.sf.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@RestController
public class TaskController {

//    @Autowired
//    private JdbcTemplate jdbcTemplate;
    /**
     * @param
     * @param
     * @return Result
     * @description 获取任务列表
     **/
    @GetMapping(value = "/task/listss")
    public Result getTaskList( HttpServletRequest request) {
//3 这里就不在查询db,直接从内存获取
        // ……
        ServletContext servletContext = request.getServletContext();
//        Object myTable = servletContext.getAttribute("deviceInfo");
        JSONArray jsonArray = new JSONArray().fromObject(servletContext.getAttribute("deviceInfo"));
//        JSONObject jsonObject = JSONObject.parseObject(String.valueOf(jsonArray));

        //处理你的业务

        //……

return null;
    }

    public static void main(String[] args){

        String s1="{start_index=0, count=20, count_flag=true, customer_id=0}";
        String s2="{{\"customer_id\":0,\"start_index\":0,\"count\":20,\"count_flag\":true}=}";
//        String s1s = s2.replaceAll("[=]$", "");
        String subStr = s2.substring(0, s2.length() - 2);
        String substring1 = subStr.substring(1);
        String replace1 = s2.replace("=", ":");
        System.out.println("asdf"+substring1);
        int i = s1.lastIndexOf(":");
        JSONObject jsonObject=null;
//{"start_index":0,"count":20,"count_flag":true,"customer_id":0}
//{"start_index":0,"count":20,"count_flag":true,"customer_id":0}
       /* StringBuffer stringBuffer1=new StringBuffer(s2.lastIndexOf(":"));
        StringBuffer stringBuffer= stringBuffer1.deleteCharAt(i);*/
//        System.out.println(s1);
        if(i==-1){
            String replace = s1.replace("=", ":");
            jsonObject = JSONObject.parseObject(replace);

        }else {
            String substring = s2.substring(0, s2.length() - 1);
            jsonObject = JSONObject.parseObject(substring);
        }
//        System.out.println(stringBuffer);
        System.out.println(jsonObject);

    }
}
