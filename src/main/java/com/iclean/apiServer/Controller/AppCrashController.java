package com.iclean.apiServer.Controller;


import com.iclean.apiServer.bean.AppCrashBean;
import com.iclean.apiServer.service.Impl.AppCrashServiceImp;
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
       /* String msg = map.getMsg();
        File file = new File("D:/myLog.txt");
        // 创建文件
        file.createNewFile();
        // creates a FileWriter Object
        FileWriter writer = new FileWriter(file);
        // 向文件写入内容
        writer.write(msg);
        writer.flush();
        writer.close();
        // 创建 FileReader 对象
        FileReader fr = new FileReader(file);
        char[] a = new char[msg.length()];
        fr.read(a); // 从数组中读取内容
        for (char c : a) {
            System.out.print(c); // 一个个打印字符
        }
        fr.close();*/

        int count = appCrashService.insert(map);
         if(count<0){
             return Result.error().msg("add is wrong!");
         }
        return Result.ok().msg("");
    }
}
