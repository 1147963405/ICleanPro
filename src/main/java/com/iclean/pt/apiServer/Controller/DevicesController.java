package com.iclean.pt.apiServer.Controller;

import com.alibaba.fastjson.JSONObject;
import com.iclean.pt.sbgl.bean.*;
import com.iclean.pt.sbgl.service.*;
import com.iclean.pt.utils.Constants;
import com.iclean.pt.utils.RedisUtil;
import com.iclean.pt.utils.Result;
import com.iclean.pt.yhgl.bean.CustomerDeviceBean;
import com.iclean.pt.yhgl.bean.DeviceInfoBean;
import com.iclean.pt.yhgl.bean.DeviceTypeBean;
import com.iclean.pt.yhgl.service.CustomerDeviceService;
import com.iclean.pt.yhgl.service.DeviceService;
import com.iclean.pt.yhgl.service.DeviceTypeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.*;


@RestController
@RequestMapping("/apiSever")
public class DevicesController {

    private final static Logger logger = LoggerFactory.getLogger(DevicesController.class);

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private CustomerDeviceService customerDeviceService;
    @Autowired
    private DeviceTypeService deviceTypeService;
    @Autowired
    private AlarmService alarmService;
    @Autowired
    private DeviceService deviceService;
    @Autowired
    private TaskService taskService;
    @Autowired
    private MapsService mapsService;
    @Autowired
    private EventsService eventsService;
    @Autowired
    private CleanReportService cleanReportService;

    /**
     * @param customer_id
     * @return Result
     * @description 通过客户id获取设备列表
     **/
    @RequestMapping(value = "/device/info", method = RequestMethod.POST)
    public Result getDevicesList(Integer customer_id,String token)  {
        List<CustomerDeviceBean> customerDeviceBeans = customerDeviceService.selectCustomerWithDevices(customer_id);
        if(customerDeviceBeans.size()==0){
            return Result.error().msg("the customer_id is not exist");
        }
        /*通过redis缓存登录的用户信息 */
        Object redisToken = redisUtil.hget("token", String.valueOf(customer_id));
        /*通过device_id从地图表中获取指定地图信息*/
      /*  Object msgHget = redisUtil.hget("message", String.valueOf(cdb.getDeviceInfoBean().getId()));
        JSONObject deviceJson = JSONObject.parseObject(String.valueOf(msgHget));*/
        if(redisToken.equals(token)||redisToken==token){
            List<JsonBean> dib=new ArrayList<>();
            for (CustomerDeviceBean cdb:customerDeviceBeans) {
                JsonBean jsonBean = new JsonBean();
                jsonBean.setId(cdb.getDeviceInfoBean().getId());
                jsonBean.setSerial(cdb.getDeviceInfoBean().getSerial());
                jsonBean.setAddress(cdb.getDeviceInfoBean().getAddress());
                jsonBean.setTypeId(cdb.getDeviceInfoBean().getTypeId());
                jsonBean.setStatus(cdb.getDeviceInfoBean().getStatus());
                jsonBean.setScene(cdb.getDeviceInfoBean().getScene());
                jsonBean.setName(cdb.getDeviceInfoBean().getName());
                DeviceTypeBean deviceTypeBean = deviceTypeService.selectByPrimaryKey(cdb.getDeviceInfoBean().getTypeId());
                Object devices = redisUtil.hget("message", String.valueOf(cdb.getDeviceId()));
//                System.out.println("json:"+devices);
//                String toJSONString = JSON.toJSONString(devices);
                JSONObject deviceJson = JSONObject.parseObject((String) devices);
                if(deviceJson==null){
                    jsonBean.setBattery(0);
                    jsonBean.setCurrMap("");
                    jsonBean.setIsCharging("");
                    jsonBean.setCurrMapName("");
                    jsonBean.setPosition("");
                    jsonBean.setModuleInfo(cdb.getDeviceInfoBean().getModuleInfo());
                }else {
                    Object task = deviceJson.get("task");//获取task外层
                    Object sonser = deviceJson.get("sonser");
                    JSONObject sta = JSONObject.parseObject(task.toString());//获取task里层
                    JSONObject ser = JSONObject.parseObject(sonser.toString());
                    jsonBean.setCurrMapName((String) sta.get("curr_map"));
                    jsonBean.setIsCharging(String.valueOf(ser.get("chargeStatus")));
                    jsonBean.setBattery((Integer) ser.get("battery"));
                    jsonBean.setCurrMap((String) sta.get("curr_map"));
                    jsonBean.setLastLocatedAddress(deviceJson.get("motion"));
                    jsonBean.setPosition(deviceJson.get("motion"));
                    jsonBean.setModuleInfo(deviceJson.get("module_info"));
                }
                jsonBean.setType(deviceTypeBean.getType());
                dib.add(jsonBean);
            }
            Map<String,Object> map=new HashMap<>();
            map.put("count",dib.size());
            map.put("devices",dib);
            return Result.ok().data(map).msg("");
        }
        return Result.error().msg("the token is not exist or the customer_id is wrong");
    }


    /**
     * @param
     * @param
     * @return Result
     * @description 通过设备ID获取指定设备的告警信息列表
     **/
    @RequestMapping(value = "/alarm/infos", method = RequestMethod.POST)
    public Result getAarmInfo(Integer device_id) {

        List<AlarmBean> alarmBeans = alarmService.selectBySelective(device_id, 0, 0, 0);
        if(alarmBeans.size()<0){
            return Result.ok().msg("the alarm is null!");
        }
        DeviceInfoBean deviceInfoBean = deviceService.selectByPrimaryKey(device_id);
        List lst=new ArrayList();
        for (AlarmBean ab:alarmBeans) {
            //组装JSON对象
            Map<String, Object> beanMap = new HashMap<>();
            beanMap.put("context", ab.getContext());
            beanMap.put("device_id", ab.getDeviceId());
            if(deviceInfoBean==null){
                beanMap.put("device_name","");
            }else {
                beanMap.put("device_name",deviceInfoBean.getName());
            }
            beanMap.put("id", ab.getId());
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String ut = simpleDateFormat.format(ab.getUpdateTime());
            String st = simpleDateFormat.format(ab.getUpdateTime());
            beanMap.put("start_time", st);
            beanMap.put("status", ab.getStatus());
            beanMap.put("update_time", ut);
            lst.add(beanMap);
        }

        Map<String,Object> jsonMap=new HashMap<>();
        jsonMap.put("count",lst.size());
        jsonMap.put("alarms",lst);
        return Result.ok().data(jsonMap).msg("");
    }

    /**
     * @param
     * @param
     * @return Result
     * @description 通过设备ID获取指定的任务信息列表
     **/
    @RequestMapping(value = "/task/infos", method = RequestMethod.POST)
    public Result getTaskInfo(Integer device_id){

        List<TaskBean> taskBeans = taskService.selectSelective(device_id,0, 0);
        DeviceInfoBean deviceInfoBean = deviceService.selectByPrimaryKey(device_id);
        if (taskBeans.size()<0){
            return Result.ok().msg("the task is null!");
        }
        List taskList=new ArrayList();
        for (TaskBean tbs:taskBeans) {
//            List<TaskBean> taskBeans = taskService.selectBySelective((Integer) os,workType);
            //组装JSON对象
            Map<String,Object> beanMap=new HashMap<>();
            beanMap.put("id",tbs.getId());
            beanMap.put("task_name",tbs.getName());
            beanMap.put("loop_count",tbs.getLoopCount());
            beanMap.put("device_id",tbs.getDeviceId());
            beanMap.put("parameter",tbs.getParameter());
            if(deviceInfoBean==null){
                beanMap.put("device_serial","");
                beanMap.put("device_name","");
            }else {
                beanMap.put("device_serial",deviceInfoBean.getSerial());
                beanMap.put("device_name",deviceInfoBean.getName());
            }
            beanMap.put("work_day",tbs.getWorkDay());
            beanMap.put("type",tbs.getType());
            beanMap.put("work_type",tbs.getWorkType());
            beanMap.put("work_time",tbs.getWorkTime());
            MapsBean mapsBean = mapsService.selectByPrimaryKey(tbs.getMapId());
            if(mapsBean!=null){
                beanMap.put("map_name",mapsBean.getName());
            }else {
                beanMap.put("map_name","");
            }
            beanMap.put("work_parts",tbs.getWorkParts());
            taskList.add(beanMap);
        }
        Map<String,Object> jsonMap=new HashMap<>();
        jsonMap.put("count",taskList.size());
        jsonMap.put("tasks",taskList);
        return Result.ok().data(jsonMap).msg("");
    }

    /**
     * @param
     * @param
     * @return Result
     * @description 根据设备ID获取指定的事件信息列表
     **/
    @RequestMapping(value = "/event/infos", method = RequestMethod.POST)
    public Result getEventInfo(Integer device_id){

        List<EventsBean> eventsBeans = eventsService.selectBySelective(device_id,0, 0, 0);
        if(eventsBeans.size()<0){
            return Result.ok().msg("the event is null!");
        }
        DeviceInfoBean deviceInfoBean = deviceService.selectByPrimaryKey(device_id);
        List eventLists=new ArrayList();
        for (EventsBean ebs:eventsBeans) {
                //组装JSON对象
                Map<String,Object> beanMap=new HashMap<>();
                beanMap.put("context",ebs.getContext());
                beanMap.put("device_name",deviceInfoBean.getName());
                beanMap.put("id",ebs.getId());
                beanMap.put("type",ebs.getType());
                beanMap.put("status",ebs.getStatus());
                Date d=new Date(ebs.getUpdateTime());
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String dateString = formatter.format(d);
                beanMap.put("update_time",dateString);
                eventLists.add(beanMap);
            }
        Map<String,Object> jsonMap=new HashMap<>();
        jsonMap.put("count",eventLists.size());
        jsonMap.put("events",eventLists);
        return Result.ok().data(jsonMap).msg("");
    }

    /**
     * @param
     * @param
     * @return Result
     * @description 通过设备ID获取指定清洁报告信息列表
     **/
    @PostMapping(value = "/clean_report/infos")
    public Result getCleanReportInfo(Integer device_id) {
        List<CleanReportBean>  cleanReportBeans = cleanReportService.selectSelective(device_id,0, 0);
        DeviceInfoBean deviceInfoBean = deviceService.selectByPrimaryKey(device_id);
        if(cleanReportBeans.size()<0){
           return Result.ok().msg("the cleanReport is null！");
        }

        List cleanReportList=new ArrayList();
        for (CleanReportBean crb:cleanReportBeans) {
            MapsBean mapsBean =mapsService.selectByIdAndDeviceId(crb.getMapId(),crb.getDeviceId());
            Map<String,Object> beanMap=new HashMap<>();
            if(mapsBean!=null){
           // 通过比较线上的地图name来获取指定的地图
                String path = Constants.Global.GLOBAL_STATIC_URL.getValue() + Constants.Global.GLOBAL_MAP_PATH.getValue() + "/" + mapsBean.getDeviceId() + "/" + mapsBean.getUuid() + "/map.png";
                //组装JSON对象
                beanMap.put("map_id",mapsBean.getId());
                beanMap.put("map_name",mapsBean.getName());
                beanMap.put("map_path",path);
            }else {
                beanMap.put("map_id","");
                beanMap.put("map_name","");
                beanMap.put("map_path","");
            }
            beanMap.put("update_time",crb.getUpdateTime());
            beanMap.put("clean_area",crb.getCleanArea());
            beanMap.put("id",crb.getId());
            beanMap.put("name",crb.getName());
            beanMap.put("type",crb.getType());
            beanMap.put("use_time",crb.getUseTime());
            beanMap.put("report",crb.getReport());
            beanMap.put("device_serial",deviceInfoBean.getSerial());
            beanMap.put("device_name",deviceInfoBean.getName());
            beanMap.put("device_type_id",deviceInfoBean.getTypeId());
            beanMap.put("device_id",deviceInfoBean.getId());
            cleanReportList.add(beanMap);
        }

            Map<String,Object> maps=new HashMap<>();
            maps.put("count",cleanReportList.size());
            maps.put("reports",cleanReportList);
              return  Result.ok().data(maps).msg("");
    }


}
