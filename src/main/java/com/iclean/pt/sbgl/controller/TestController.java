package com.iclean.pt.sbgl.controller;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.google.common.base.Strings;
import com.iclean.pt.common.config.Annotation.AnRateLimiter;
import com.iclean.pt.sbgl.bean.CleanReportBean;
import com.iclean.pt.sbgl.service.AlarmService;
import com.iclean.pt.sbgl.service.CleanReportService;
import com.iclean.pt.sbgl.service.EventsService;
import com.iclean.pt.sbgl.service.TaskService;
import com.iclean.pt.utils.Result;
import com.iclean.pt.yhgl.bean.CustomerBean;
import com.iclean.pt.yhgl.service.CustomerDeviceService;
import com.iclean.pt.yhgl.service.CustomerService;
import com.iclean.pt.yhgl.service.DeviceService;
import com.iclean.pt.yhgl.service.Imp.CustomerDeviceServiceImp;
import net.sf.json.JSONArray;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@RestController
public class TestController {
    @Autowired
    private CustomerService cleanReportService;
    @Autowired
    private TaskService eventsService;
    @GetMapping("/test")
    public Result addDevice(@RequestParam Map<String,Object> addDevice) throws JSONException {
        /*PageHelper.startPage(1, 20);
        List<Map<String,Object>> cleanReportLst = eventsService.selectDevices();
        JSONArray jsonArray = JSONArray.fromObject(cleanReportLst);
        long total = ((com.github.pagehelper.Page) cleanReportLst).getTotal();*/
        String str="t.name='漕河泾受理中心'";
//        PageHelper.offsetPage(0, 20);
//        List<Map<String, Object>> maps = eventsService.selectTasks(str);
//        int customer = cleanReportService.selectCustomer(1);
//        System.out.println("data: " + maps);
//        int i = cleanReportService.selectUsers(1);
//        int customerBeans = customerService.selectCustomer(1);
//        System.out.println("Total: " + customerBeans);
//        List<User> users = userMapper.findAll(); CleanReportBean

        return Result.ok().msg("");
    }


    @GetMapping("/index")
    @AnRateLimiter(permitsPerSecond = 100,timeout = 500, timeunit = TimeUnit.MILLISECONDS,msg = "亲,现在流量过大,请稍后再试.")
    public String index() {
        return System.currentTimeMillis() + "";
    }


    public static void main(String[] args) throws IOException{

        File file = new File("http://47.92.192.154:9077/home/apiServer/Hello1.txt");
        // 创建文件
        file.createNewFile();
        // creates a FileWriter Object
        FileWriter writer = new FileWriter(file);
        // 向文件写入内容
        writer.write("This\n is\n an\n example\n");
        writer.flush();
        writer.close();
        // 创建 FileReader 对象
        FileReader fr = new FileReader(file);
        char[] a = new char[50];
        fr.read(a); // 从数组中读取内容
        for (char c : a) {
            System.out.print(c); // 一个个打印字符
        }
        fr.close();
        }
    }

