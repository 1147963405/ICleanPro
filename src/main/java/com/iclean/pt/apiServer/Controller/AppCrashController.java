package com.iclean.pt.apiServer.Controller;

import com.iclean.pt.apiServer.bean.AppCrashBean;
import com.iclean.pt.apiServer.service.AppCrashService;
import com.iclean.pt.utils.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
public class AppCrashController {

 private final static Logger logger = LoggerFactory.getLogger(AppCrashController.class);

 @Autowired
private AppCrashService appCrashService;

    @RequestMapping(value = "/appCrash/info",method = RequestMethod.GET)
    public Result getAppCrashInfo(String serial){
        List<AppCrashBean> appCrashBeans = appCrashService.selectBySerial(serial);
        if(appCrashBeans.size()<0){
            return Result.error().msg("the serial is not exist!");
        }
        logger.info("appCrash",appCrashBeans);
        Map<String,Object> map=new HashMap<>();
        map.put("count",appCrashBeans.size());
        map.put("appCrashs",appCrashBeans);
        return Result.ok().data(map).msg("");
    }

    @RequestMapping(value = "/appCrash/add",method = RequestMethod.POST)
    public Result addAppCrash( @RequestBody  AppCrashBean map){
        int count = appCrashService.insert(map);
         if(count<0){
             return Result.error().msg("add is wrong!");
         }
        return Result.ok().msg("");
    }
}
