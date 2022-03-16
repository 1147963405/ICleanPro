package com.iclean.pt.apiServer.Controller;


import com.iclean.pt.apiServer.bean.AppCrashBean;
import com.iclean.pt.apiServer.service.Impl.AppCrashServiceImp;
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
private AppCrashServiceImp appCrashService;

    @RequestMapping(value = "/appCrash/info",method = RequestMethod.GET)
    public Result getAppCrashInfo(String serial){
        List<AppCrashBean> appCrashBeans = appCrashService.selectBySerial(serial);
        if(appCrashBeans.size()<0){
            return Result.error().msg("the serial is not exist!");
        }
//        logger.info("appCrash",appCrashBeans);
        Map<String,Object> map=new HashMap<>();
        map.put("count",appCrashBeans.size());
        map.put("appCrashs",appCrashBeans);
        return Result.ok().data(map).msg("");
    }

    @RequestMapping(value = "/appCrash/add",method = RequestMethod.POST)
    public Result addAppCrash( @RequestBody  AppCrashBean map) {
//        logger.info("异常信息:"+map);
        int count = appCrashService.insert(map);
         if(count<0){
             return Result.error().msg("添加失败");
         }
        return Result.ok().msg("添加成功");
    }
}
