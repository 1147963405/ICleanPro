package com.iclean.pt.sbgl.controller;

import com.iclean.pt.utils.Result;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
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
}
