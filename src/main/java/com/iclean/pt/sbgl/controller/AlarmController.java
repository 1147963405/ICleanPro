package com.iclean.pt.sbgl.controller;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.iclean.pt.sbgl.bean.AlarmBean;
import com.iclean.pt.sbgl.service.AlarmService;
import com.iclean.pt.utils.CommUtil;
import com.iclean.pt.utils.Result;
import com.iclean.pt.yhgl.service.CustomerService;
import net.sf.json.JSONArray;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static java.lang.Integer.parseInt;

@RestController
public class AlarmController {

    private final static Logger logger = LoggerFactory.getLogger(AlarmController.class);

    @Autowired
    private CustomerService customerService;

    @Autowired
    private AlarmService alarmService;

    @Autowired
    private CommUtil commUtil;


    /**
     * @param
     * @param
     * @return Result
     * @description 获取报警列表
     **/
    @RequestMapping(value = "/alarm/lists", method = RequestMethod.POST)
//    public Result getAarmLists(@RequestBody Map map) {
    public Result getAarmLists(@RequestParam Map map) {
        logger.info("获取报警列表："+map);
/*{"user_id":1,"start_index":0,"beginTime":"","endTime":"","count":100,"count_flag":true,"status":0}
iclean/robot/status: 处理消息 {"data":"结束任务 [10], 地图[L5]","device_id":146,"message_type":"event"}
{start_index=0, beginTime=, endTime=, count=20, count_flag=true, status=0, user_id=1, device_id=145}
{"user_id":1,"start_index":0,"device_id":54,"beginTime":"","endTime":"","count":10,"count_flag":true,"status":0}:
*/
        /*步骤：
         * 2.通过user_id获取指定客户下的所有设备告警信息 user_id:customer_id:devices
         * 3.如果user_id指定的客户不存在，则查询所有客户下的所有设备的告警信息
         * 4.组装JSON对象返回
         * */

        JSONObject jsonObj = commUtil.getJson(map);
        int startIndex = parseInt(jsonObj.get("start_index").toString());
        int count = parseInt(jsonObj.get("count").toString());
        int userId = parseInt(jsonObj.get("user_id").toString());
        List<Map<String, Object>> alarmsList =null;
        int counts = customerService.selectCustomer(userId);
        if(counts==0){
            if(jsonObj.get("device_id")!=null){
                StringBuilder deviceNameSql = new StringBuilder();
                deviceNameSql.append(" and r.device_id="+jsonObj.get("device_id")+"  and r.status="+jsonObj.get("status"));
                PageHelper.offsetPage(startIndex, count);
                alarmsList=alarmService.selectAlarmsBySelective(deviceNameSql.toString());
            }else if(jsonObj.get("status")!=null){
                     if(!jsonObj.get("beginTime").equals("") || !jsonObj.get("endTime").equals("")){
                         StringBuilder timeSql = new StringBuilder();
                         timeSql.append("  and r.status="+jsonObj.get("status") +"  and     FROM_UNIXTIME(r.update_time, '%Y-%m-%d')  between '"+jsonObj.get("beginTime")+"'  and  '"+jsonObj.get("endTime")+"'");
                         PageHelper.offsetPage(startIndex, count);
                         alarmsList=alarmService.selectAlarmsBySelective(timeSql.toString());
                     }else {
                         StringBuilder statusSql = new StringBuilder();
                         statusSql.append(" and r.status="+jsonObj.get("status"));
                         PageHelper.offsetPage(startIndex, count);
                         alarmsList=alarmService.selectAlarmsBySelective(statusSql.toString());
                     }

            }else if(!jsonObj.get("beginTime").equals("") || !jsonObj.get("endTime").equals("")){
                StringBuilder timeSql = new StringBuilder();
                timeSql.append(" and     FROM_UNIXTIME(r.update_time, '%Y-%m-%d')  between '"+jsonObj.get("beginTime")+"'  and  '"+jsonObj.get("endTime")+"'");
                PageHelper.offsetPage(startIndex, count);
                alarmsList=alarmService.selectAlarmsBySelective(timeSql.toString());
            }else {
                PageHelper.offsetPage(startIndex, count);
                alarmsList=alarmService.selectAlarms();
            }

        }else {
            if(jsonObj.get("device_id")!=null){
                StringBuilder deviceNameSql = new StringBuilder();
                deviceNameSql.append(" and r.device_id="+jsonObj.get("device_id"));
                PageHelper.offsetPage(startIndex, count);
                alarmsList=alarmService.selectAlarmsByParams(userId,deviceNameSql.toString());
            }else if(jsonObj.get("status")!=null){

                    if(!jsonObj.get("beginTime").equals("") || !jsonObj.get("endTime").equals("")){
                        StringBuilder timeSql = new StringBuilder();
                        timeSql.append("  and r.status="+jsonObj.get("status") +" and     FROM_UNIXTIME(r.update_time, '%Y-%m-%d')  between '"+jsonObj.get("beginTime")+"'  and  '"+jsonObj.get("endTime")+"'");
                        PageHelper.offsetPage(startIndex, count);
                        alarmsList=alarmService.selectAlarmsByParams(userId,timeSql.toString());
                    }else {
                        StringBuilder statusSql = new StringBuilder();
                        statusSql.append(" and r.status="+jsonObj.get("status"));
                        PageHelper.offsetPage(startIndex, count);
                        alarmsList=alarmService.selectAlarmsByParams(userId,statusSql.toString());
                    }

            }else if(!jsonObj.get("beginTime").equals("") || !jsonObj.get("endTime").equals("")){
                StringBuilder timeSql = new StringBuilder();
                timeSql.append(" and   FROM_UNIXTIME(r.update_time, '%Y-%m-%d')  between '"+jsonObj.get("beginTime")+"'  and  '"+jsonObj.get("endTime")+"'");
                PageHelper.offsetPage(startIndex, count);
                alarmsList=alarmService.selectAlarmsByParams(userId,timeSql.toString());
            }else {
                PageHelper.offsetPage(startIndex, count);
                alarmsList=alarmService.selectAlarmByUserId(userId);
            }

        }
        long total = ((com.github.pagehelper.Page) alarmsList).getTotal();

        Map<String,Object> jsonMap=new ConcurrentHashMap<>();
        jsonMap.put("count",total);
        jsonMap.put("alarms",alarmsList);
        return Result.ok().data(jsonMap).msg("");
    }
    /**
     * @param
     * @param
     * @return Result
     * @description 告警状态设置
     **/
    @PostMapping(value = "/alarm/set_status")
    public Result  setStatus(@RequestParam Map<String,Object> alarmMap) {

        JSONObject jsonObj = commUtil.getJson(alarmMap);
        JSONArray jsonArray = new JSONArray().fromObject(jsonObj.get("ids"));
        AlarmBean alarmBean = alarmService.selectByPrimaryKey((Integer) jsonArray.get(0));
        System.out.println("alarmBean: "+alarmBean);
        if(alarmBean==null){
            return Result.error().msg("");
        }
        return Result.ok().msg("");
    }
}
