package com.iclean.pt.apiServer.Controller;

import cn.hutool.core.bean.BeanUtil;
import com.alibaba.fastjson.JSONObject;
import com.iclean.pt.sbgl.bean.*;
import com.iclean.pt.sbgl.service.*;
import com.iclean.pt.utils.*;
import com.iclean.pt.yhgl.bean.CustomerDeviceBean;
import com.iclean.pt.yhgl.bean.DeviceInfoBean;
import com.iclean.pt.yhgl.service.CustomerDeviceService;
import com.iclean.pt.yhgl.service.DeviceService;
import com.iclean.pt.yhgl.service.DeviceTypeService;
import net.sf.json.JSONArray;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;


@RestController
@RequestMapping("/apiServer")
public class ApiController {

    private final static Logger logger = LoggerFactory.getLogger(ApiController.class);

    @Autowired
    private RedisUtil redisUtil;

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
    @Autowired
    private DeviceTypeService deviceTypeService;
    @Autowired
    private CustomerDeviceService customerDeviceService;


    /**
     * @param customer_id
     * @return Result
     * @description 通过客户id获取设备列表
     **/
    @RequestMapping(value = "/device/info", method = RequestMethod.POST)
    public Result getDevicesList(Integer customer_id,String token)  {
        List<Map<String, Object>> customerList = deviceService.selectDevicesByCustomerId(customer_id);
        if(customerList.size()==0){
            return Result.error().msg("the customer_id is not exist");
        }
        Object redisToken = redisUtil.hget("token", String.valueOf(customer_id));
        if(redisToken.equals(token)||redisToken==token) {
            for (Map<String, Object> map : customerList) {
                if (map.get("module_info").equals("")) {
                    map.put("module_info", new ArrayList<>());
                } else {
                    map.put("module_info", JSONArray.fromObject(map.get("module_info").toString()));
                }
                //redis
                Object message = redisUtil.hget("message", String.valueOf(map.get("id")));
                JSONObject messageJson = JSONObject.parseObject((String) message);
                if (messageJson != null) {
                    Object task = messageJson.get("task");//获取task外层
                    Object sonser = messageJson.get("sonser");
                    JSONObject sta = JSONObject.parseObject(task.toString());//获取task里层
                    JSONObject ser = JSONObject.parseObject(sonser.toString());
                    map.put("isCharging", ser.get("chargeStatus"));
                    map.put("battery", ser.get("battery"));
                    JSONObject motion = JSONObject.parseObject(messageJson.get("motion").toString());
                    map.put("last_located_address", motion);
                    map.put("position", motion);
                    map.put("alarm_count", 0);
                    //maps
                    MapsBean mapsBean = mapsService.selectBySelective(Integer.parseInt(map.get("id").toString()), String.valueOf(sta.get("curr_map")));
                    if (mapsBean != null) {
//                        String path =Constants.Global.GLOBAL_URL.getValue()+ Constants.Global.GLOBAL_MAP_PATH.getValue() + "/" +mapsBean.getDeviceId()+"/"+ mapsBean.getUuid() + "/map.png";
                        String path = Constants.Global.IMAGES_URL.getValue() + "map/" + mapsBean.getDeviceId() + "/" + mapsBean.getUuid() + "/map.png";
                        map.put("curr_map_id", mapsBean.getId());
                        map.put("curr_map_name", mapsBean.getName());
                        map.put("curr_map", path);
                    } else {
                        map.put("curr_map_id", -1);
                        map.put("curr_map_name", "");
                        map.put("curr_map", "");
                    }
                } else {
                    map.put("isCharging", 0);
                    map.put("battery", 0);
                    if (map.get("last_located_address").equals("")) {
                        map.put("last_located_address", "{}");
                        map.put("position", "{}");
                    } else {
                        JSONObject last_located_address = JSONObject.parseObject(map.get("last_located_address").toString());
                        map.put("last_located_address", last_located_address);
                        map.put("position", last_located_address);
                    }
                    map.put("curr_map_id", -1);
                    map.put("curr_map_name", "");
                    map.put("curr_map", "");//alarm_count
                    map.put("alarm_count", 0);
                }
            }
            Map<String, Object> map = new ConcurrentHashMap<>();
            map.put("count", customerList.size());
            map.put("devices", customerList);
            return Result.ok().data(map).msg("");
        }
        return Result.error().msg("the token is not exist or expire!");
    }


    /**
     * @param
     * @param
     * @return Result
     * @description 通过设备ID获取指定设备的告警信息列表
     **/
    @RequestMapping(value = "/alarm/info", method = RequestMethod.POST)
    public Result getAlarmInfo(Integer device_id,Integer customer_id,String token) {

        String redisToken = String.valueOf(redisUtil.hget("token", String.valueOf(customer_id)));
        if(redisToken==null){
            return Result.error().msg("the customer_id is not exist or the token is expire!");
        }
        if(!redisToken.equals(token)) {
            return Result.error().msg("the customer_id or token is wrong!");
        }
        List<Map<String, Object>> alarms = alarmService.selectAlarmByDeviceId(device_id);
        if (alarms.size() < 0) {
            return Result.ok().msg("the alarm is null!");
        }
        Map<String,Object> jsonMap=new ConcurrentHashMap<>();
        jsonMap.put("count",alarms.size());
        jsonMap.put("alarms",alarms);
        return Result.ok().data(jsonMap).msg("");
    }

    /**
     * @param
     * @param
     * @return Result
     * @description 通过设备ID获取指定的任务信息列表
     **/
    @RequestMapping(value = "/task/info", method = RequestMethod.POST)
    public Result getTaskInfo(Integer device_id,Integer customer_id,String token){

        String redisToken = String.valueOf(redisUtil.hget("token", String.valueOf(customer_id)));
        if(redisToken==null){
            return Result.error().msg("the customer_id is not exist or the token is expire!");
        }
        if(!redisToken.equals(token)) {
            return Result.error().msg("the customer_id or token is wrong!");
        }

        List<Map<String, Object>> tasks = taskService.selectTaskByDeviceId(device_id);
        if (tasks.size()<0){
            return Result.ok().msg("the task is null!");
        }
        for (Map<String,Object> map:tasks) {
            if(map.get("parameter").equals("")||map.get("parameter")==null){
                map.put("parameter",new ArrayList<>());
            }else {
                map.put("parameter",JSONArray.fromObject(map.get("parameter")));
            }
            if(map.get("work_parts").equals("")||map.get("work_parts")==null){
                map.put("work_parts","{}");
            }else {
                map.put("work_parts",JSONObject.parseObject(map.get("work_parts").toString()));
            }
            if(map.get("map_name")==null){
                map.put("map_name","");
            }
        }
        Map<String,Object> jsonMap=new ConcurrentHashMap<>();
        jsonMap.put("count",tasks.size());
        jsonMap.put("tasks",tasks);
        return Result.ok().data(jsonMap).msg("");
    }

    /**
     * @param
     * @param
     * @return Result
     * @description 根据设备ID获取指定的事件信息列表
     **/
    @RequestMapping(value = "/event/info", method = RequestMethod.POST)
    public Result getEventInfo(Integer device_id,Integer customer_id,String token){

        String redisToken = String.valueOf(redisUtil.hget("token", String.valueOf(customer_id)));
        if(redisToken==null){
            return Result.error().msg("the customer_id is not exist or the token is expire!");
        }
        if(!redisToken.equals(token)) {
            return Result.error().msg("the customer_id or token is wrong!");
        }
//        List<EventsBean> eventsBeans = eventsService.selectBySelective(device_id,0, 0, 0);
       List<Map<String, Object>> events = eventsService.selectEventByDeviceId(device_id);
        if(events.size()<0){
            return Result.ok().msg("the event is null!");
        }
        Map<String,Object> jsonMap=new ConcurrentHashMap<>();
        jsonMap.put("count",events.size());
        jsonMap.put("events",events);
        return Result.ok().data(jsonMap).msg("");
    }

    /**
     * @param
     * @param
     * @return Result
     * @description 通过设备ID获取指定清洁报告信息列表
     **/
    @PostMapping(value = "/clean_report/info")
    public Result getCleanReportInfo(Integer device_id,Integer customer_id,String token) {

        String redisToken = String.valueOf(redisUtil.hget("token", String.valueOf(customer_id)));
        if(redisToken==null){
            return Result.error().msg("the customer_id is not exist or the token is expire!");
        }
        if(!redisToken.equals(token)) {
            return Result.error().msg("the customer_id or token is wrong!");
        }
//        List<CleanReportBean>  cleanReportBeans = cleanReportService.selectSelective(device_id,0, 0);
//        DeviceInfoBean deviceInfoBean = deviceService.selectByPrimaryKey(device_id);
        List<Map<String, Object>> cleanReports = cleanReportService.selectClearReportByDeviceId(device_id);
        if(cleanReports.size()<0){
           return Result.ok().msg("the cleanReport is null！");
        }
        for (Map<String,Object> map:cleanReports) {

            if(map.get("map_path")==null){
                map.put("map_path","");
            }else {
                String path = Constants.Global.IMAGES_URL.getValue() + "map/" + map.get("device_id") + "/" + map.get("map_path") + "/map.png";
               map.put("map_path",path);
            }

            JSONObject report = JSONObject.parseObject(map.get("report").toString());
            if(report.get("pathFile")!=null){
                //http://47.92.192.154:9077/iclean-cloud/data/download/report_path?device_id=51&file_name=03c5fef0-4b02-4b49-a694-b83eb293ef52
                String pathList=Constants.Global.GLOBAL_URL.getValue()+Constants.Global.REPORT_DOWNLOAD_PATH.getValue()+"?device_id=/"+report.get("device_id")+"&file_name="+report.get("pathFile");
                report.put("path_list",pathList);
            }else {
                report.put("path_list",new ArrayList<>());
            }
            map.put("report",report);
        }
            Map<String,Object> maps=new ConcurrentHashMap<>();
            maps.put("count",cleanReports.size());
            maps.put("reports",cleanReports);
              return  Result.ok().data(maps).msg("");
    }

    /**
     * @param
     * @param
     * @return Result
     * @description 通过设备ID获取指定设备的地图列表
     **/
    @RequestMapping(value = "/map/info", method = RequestMethod.POST)
    public Result getMapInfo(Integer device_id,Integer customer_id,String token) {

        String redisToken = String.valueOf(redisUtil.hget("token", String.valueOf(customer_id)));
        if(redisToken==null){
            return Result.error().msg("the customer_id is not exist or the token is expire!");
        }
        if(!redisToken.equals(token)) {
            return Result.error().msg("the customer_id or token is wrong!");
        }

        List<Map<String, Object>> maps = mapsService.selectMapByDeviceId(device_id);
        if(maps.size()<0){
            return Result.ok().msg("the maps is null！");
        }

        for (Map<String,Object> map:maps) {
            // 组装图片路径
            String path = Constants.Global.IMAGES_URL.getValue() + "map/" +map.get("device_id") + "/" + map.get("path") + "/map.png";
//            String path =Constants.Global.GLOBAL_URL.getValue()+ Constants.Global.GLOBAL_MAP_PATH.getValue() + "/" +map.get("device_id")+"/"+ map.get("path") + "/map.png";
            //  组装下载路径
            String download = Constants.Global.GLOBAL_URL.getValue() + Constants.Global.MAP_DOWNLOAD_PATH.getValue() + "?device_id=" + map.get("device_id") + "&uuid=" + map.get("path");
//            String download = Constants.Global.IMAGES_URL.getValue()  + "map/"+ map.get("path");
            map.put("path",path);
            map.put("download_url",download);
        }

        Map<String,Object> jsonMap=new ConcurrentHashMap<>();
        jsonMap.put("count",maps.size());
        jsonMap.put("map",maps);
        return Result.ok().data(jsonMap).msg("");
    }
    /**
     * @param
     * @return Result
     * @description 获取设备类型
     **/
    @RequestMapping(value = "/device/lists_type", method = RequestMethod.GET)
    public JSONObject getDeviceTypeList(Integer customer_id,String token) {

        String redisToken = String.valueOf(redisUtil.hget("token", String.valueOf(customer_id)));
        if(redisToken==null){
            return JSONObject.parseObject("{ successed: true,errorCode: 500, msg: 'the customer_id is not exist or the token is expire!', data:{}}");
        }
        if(!redisToken.equals(token)) {
            return JSONObject.parseObject("{ successed: true,errorCode: 500, msg: 'the customer_id or token is wrong!', data:{}}");
        }
        List<Map<String, Object>> deviceTypeList = deviceTypeService.selectDeviceTypeList();
        JSONArray jsonArray = new JSONArray().fromObject(deviceTypeList);
        String jStr="{ successed: true,errorCode: 200, msg: '', data:"+ jsonArray+"}";
        return JSONObject.parseObject(jStr);
    }

    /**
     * @param
     * @param
     * @return Result
     * @description 获取设备实时状态
     **/
    @GetMapping(value = "/device/real_status")
    public Result getDeviceStatus(Integer id,Integer customer_id,String token) {

        /*步骤：
         * 1.通过device_id获取到指定设备信息，以及设备实时数据，还有地图名称
         * 2.通过设备type_id获取设备类型
         * 3.通过map封装
         * */

        String redisToken = String.valueOf(redisUtil.hget("token", String.valueOf(customer_id)));
        if(redisToken==null){
            return Result.error().msg("the customer_id is not exist or the token is expire!");
        }
        if(!redisToken.equals(token)) {
            return Result.error().msg("the customer_id or token is wrong!");
        }
        Map<String,Object> deviceInfoBean = deviceService.selectByPrimaryKey(id);
        if(deviceInfoBean==null){
            return Result.ok().msg("the device_id is not exist!");
        }
        JSONObject jsonObject = JSONObject.parseObject(String.valueOf(redisUtil.hget("message", String.valueOf(id))));
        logger.info("jsonObject"+jsonObject);
        if(jsonObject==null){
            return Result.error().msg("the device_id is not exist!");
        }
        Object task = jsonObject.get("task");//获取task外层
        JSONObject sta = JSONObject.parseObject(task.toString());//获取task里层
        Map<String,Object> dp=new ConcurrentHashMap<>();
        dp.put("motion_state",jsonObject.get("motion"));
        dp.put("task_state",task);
        dp.put("sonser_state",jsonObject.get("sonser"));
        dp.put("map_name",sta.get("curr_map"));
        dp.put("device_id",deviceInfoBean.get("device_id"));
        dp.put("device_type",deviceInfoBean.get("device_type"));
        return Result.ok().data(dp).msg("");

    }
    /**
     * @param
     * @param
     * @return Result
     * @description 获取设备实时数据
     **/
    @GetMapping(value = "/device/real_time_data")
    public Result getDeviceInfo(Integer device_id,Integer customer_id,String token) {

        String redisToken = String.valueOf(redisUtil.hget("token", String.valueOf(customer_id)));
        if(redisToken==null){
            return Result.error().msg("the customer_id is not exist or the token is expire!");
        }
        if(!redisToken.equals(token)) {
            return Result.error().msg("the customer_id or token is wrong!");
        }

        Map<String,Object> dp=new ConcurrentHashMap<>();
//        for (Integer km : MSG_map.keySet()) {
        /*通过device_id对比数据库中的设备与线上的设备，并回显的数据为device_id相同的线上设备信息*/
        JSONObject jsonObject = JSONObject.parseObject(String.valueOf(redisUtil.hget("message", String.valueOf(device_id))));
        if(jsonObject==null){
            dp.put("motion","");
            dp.put("task","");
            dp.put("sonser","");
            return Result.ok().msg("the device data is  null!");
        }
        dp.put("motion",jsonObject.get("motion"));
        dp.put("task",jsonObject.get("task"));
        dp.put("sonser",jsonObject.get("sonser"));
        return Result.ok().data(dp).msg("");
    }
    /**
     * @param
     * @param
     * @return Result
     * @description 获取设备基本信息
     **/
    @GetMapping(value = "/robot/info")
    public Result getBaseDeviceInfo(Integer id,Integer customer_id,String token) {

        /*
         * 步骤
         * 1.通过设备序列号id获取指定设备信息
         * 2.通过设备id获取设备实时数据
         * 3.通过设备id到ftp图片服务器中获取到指定图片，从实时数据中获取到map相关的信息
         * 4.通过设备id到设备客户中间表中获取到指定的客户集合
         * 5.通过map封装
         * */

        String redisToken = String.valueOf(redisUtil.hget("token", String.valueOf(customer_id)));
        if(redisToken==null){
            return Result.error().msg("the customer_id is not exist or the token is expire!");
        }
        if(!redisToken.equals(token)) {
            return Result.error().msg("the customer_id or token is wrong!");
        }

        logger.info("获取设备基本信息");
        Map<String,Object> dp=new HashMap<>();
        Map<String,Object> deviceInfoBean = deviceService.selectByPrimaryKey(id);
        if(deviceInfoBean==null){
            return Result.error().msg("the device_id is not exist!");
        }
        //customer
        List<CustomerDeviceBean> customerDeviceBeans = customerDeviceService.selectByDeviceId(Integer.parseInt(deviceInfoBean.get("device_id").toString()));
        dp.put("customers", customerDeviceBeans);
        //device
        dp.put("id", deviceInfoBean.get("device_id"));
        dp.put("type_id", deviceInfoBean.get("type_id"));
        dp.put("serial", deviceInfoBean.get("serial"));
        dp.put("status", deviceInfoBean.get("status"));
        dp.put("name", deviceInfoBean.get("name"));
        dp.put("scene", deviceInfoBean.get("scene"));
        dp.put("models", deviceInfoBean.get("models"));
        dp.put("address", deviceInfoBean.get("address"));
        dp.put("description", deviceInfoBean.get("description"));
        dp.put("update_time", deviceInfoBean.get("update_time"));
        if (deviceInfoBean.get("module_info").equals("") || deviceInfoBean.get("module_info") == null) {
            dp.put("module_info", new ArrayList<>());
        } else {
            dp.put("module_info", JSONArray.fromObject(deviceInfoBean.get("module_info")));
        }
        //redis
        JSONObject deviceJson = JSONObject.parseObject(String.valueOf(redisUtil.hget("message", String.valueOf(id))));
        if(deviceJson!=null) {
            JSONObject sta = JSONObject.parseObject(deviceJson.get("task").toString());
            JSONObject ser = JSONObject.parseObject(deviceJson.get("sonser").toString());
            JSONObject motion = JSONObject.parseObject(deviceJson.get("motion").toString());
            dp.put("last_located_address",motion);
            dp.put("position",motion);
            dp.put("isCharging", ser.get("chargeStatus"));
            dp.put("battery", ser.get("battery"));
            //map
            MapsBean mapsBean = mapsService.selectBySelective(Integer.parseInt(deviceInfoBean.get("device_id").toString()), String.valueOf(sta.get("curr_map")));
            if (mapsBean != null) {
//                String path =Constants.Global.GLOBAL_URL.getValue()+ Constants.Global.GLOBAL_MAP_PATH.getValue() + "/" +mapsBean.getDeviceId()+"/"+ mapsBean.getUuid() + "/map.png";
                String path = Constants.Global.IMAGES_URL.getValue() + "map/" + mapsBean.getDeviceId() + "/" + mapsBean.getUuid() + "/map.png";
                dp.put("curr_map_name", mapsBean.getName());
                dp.put("curr_map", path);
                dp.put("curr_map_id", mapsBean.getId());
            } else {
                dp.put("curr_map_name","");
                dp.put("curr_map", "");
                dp.put("curr_map_id", -1);
            }
        }else {
            dp.put("isCharging",0);
            dp.put("battery",0);
            dp.put("last_located_address",JSONObject.parseObject(deviceInfoBean.get("last_located_address").toString()));
            dp.put("position",JSONObject.parseObject(deviceInfoBean.get("last_located_address").toString()));
            dp.put("curr_map_id",-1);
            dp.put("curr_map_name","");
            dp.put("curr_map","");//alarm_count
//            dp.put("alarm_count",0);
        }
        return Result.ok().data(dp).msg("");
    }
    /**
     * @param
     * @param
     * @return Result
     * @description 更新设备状态
     **/
    @PostMapping(value = "/robot/task_status")
    public Result updateDeviceStatus(@RequestParam Map<String,Object> mp) {
        /*步骤
        1.通过device_id获取线上指定设备实时状态
        2.如果设备状态发生改变，则实时更新状态回数据库
        * */
        String redisToken = String.valueOf(redisUtil.hget("token", String.valueOf(mp.get("customer_id"))));
        if(redisToken==null){
            return Result.error().msg("the customer_id is not exist or the token is expire!");
        }
        if(!redisToken.equals(mp.get("token"))) {
            return Result.error().msg("the customer_id or token is wrong!");
        }

        logger.info("更新设备状态","测试。。。。。。");
        JSONObject jsonObject = JSONObject.parseObject(String.valueOf(redisUtil.hget(Constants.Mqtt.TOPIC_SUB_ROBOT_STATUS.getValue(), String.valueOf(mp.get("device_id")))));
        if(jsonObject==null){
            return  Result.error().msg("the device_id is not exist!");
        }
        /*获取设备对象*/
        Map<String,Object> deviceInfoBean = deviceService.selectByPrimaryKey(Integer.parseInt(mp.get("device_id").toString()));
        if(jsonObject.get("status")==null || jsonObject.get("status")==deviceInfoBean.get("status")){
            return Result.error().msg("The states are the same or the status is null!");
        }
        /*更新状态*/
        deviceInfoBean.put("status", jsonObject.get("status"));
        DeviceInfoBean deviceInfo = BeanUtil.copyProperties(deviceInfoBean, DeviceInfoBean.class);
        deviceInfo.setId(Integer.parseInt(deviceInfoBean.get("device_id").toString()));
        deviceService.updateByPrimaryKey(deviceInfo);
        return  Result.ok().msg("the status is updated!");
    }

    /**
     * @param
     * @param
     * @return Result
     * @description 判断设备序列号是否存在
     **/
    @GetMapping(value = "/device/serial/exist")
    public Result isDeviceSerial(String serial,Integer customer_id,String token) {

        String redisToken = String.valueOf(redisUtil.hget("token", String.valueOf(customer_id)));
        if(redisToken==null){
            return Result.error().msg("the customer_id is not exist or the token is expire!");
        }
        if(!redisToken.equals(token)) {
            return Result.error().msg("the customer_id or token is wrong!");
        }
        StringBuilder append = new StringBuilder().append(" serial='" + serial + "'");
        DeviceInfoBean deviceInfoBean = deviceService.selectBySelective(append.toString());
        if(deviceInfoBean==null){
            return Result.error().data("exist",false).msg("the device serial is not exist!");
        }
        return Result.ok().data("exist",true).msg("");
    }

    /**
     * @param
     * @param
     * @return CommnosResult
     * @description 判断设备名称是否存在
     **/
    @GetMapping(value = "/device/name/exist")
    public Result isDeviceName(Integer user_id,String name,Integer customer_id,String token) {


        String redisToken = String.valueOf(redisUtil.hget("token", String.valueOf(customer_id)));
        if(redisToken==null){
            return Result.error().msg("the customer_id is not exist or the token is expire!");
        }
        if(!redisToken.equals(token)) {
            return Result.error().msg("the customer_id or token is wrong!");
        }
        StringBuilder stringBuilder =new StringBuilder().append("  name= '" + name+"'");
        DeviceInfoBean deviceInfoBean = deviceService.selectBySelective(stringBuilder.toString());
        if(deviceInfoBean!=null){
            return Result.ok().data("exist",true).msg("");
        }

        return Result.error().data("exist",false).msg("the device name is not exist!");
    }
}
