package com.iclean.pt.sbgl.controller;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
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

import java.util.List;
import java.util.Map;

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

    public static void main(String[] args){

        Object o="[{\"ver_number\":46,\"type\":0,\"name\":\"2.0.2(2021.11.30_16:00)_029\"},{\"ver_number\":2,\"type\":2,\"name\":\"2.4.0(2021-11-12)\"}]";
        JSONArray jsonArray = JSONArray.fromObject(o);
        System.out.println("j"+jsonArray);
    }
}
