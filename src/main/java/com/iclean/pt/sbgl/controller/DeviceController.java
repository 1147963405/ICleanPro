package com.iclean.pt.sbgl.controller;
import cn.hutool.core.bean.BeanUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.PropertyNamingStrategy;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.iclean.pt.sbgl.bean.*;
import com.iclean.pt.sbgl.service.*;
import com.iclean.pt.utils.*;
import com.iclean.pt.yhgl.bean.*;
import com.iclean.pt.yhgl.service.*;
import net.sf.json.JSONArray;

import org.json.JSONException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;


import static java.lang.Integer.parseInt;

@RestController
public class DeviceController {

    private final static Logger logger = LoggerFactory.getLogger(DeviceController.class);
//    @Autowired
//    private CustomerDeviceService customerDeviceService;
    @Autowired
    private DeviceService deviceService;
    @Autowired
    private DeviceTypeService deviceTypeService;

    @Autowired
    private MapsService mapsService;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private EventsService eventsService;

    @Autowired
    private CustomerDeviceService customerDeviceService;
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private CommUtil commUtil;


    /**
     * @param addDevice
     * @return Result
     * @description 添加设备
     **/
    @RequestMapping(value = "/device/add", method = RequestMethod.POST)
    public Result addDevice(@RequestParam Map<String,Object> addDevice) throws JSONException {
/*{"type_id":1,"name":"iclean","serial":"2106-004-01-00089",
"address":"广东省广州市番禺区石楼","description":"无无无",
"scene":"机场","customer_ids":[{"id":77}]}:  */

        /*
         * 步骤：
         *      1.获取前台的传参进行设备添加
         *      2.设备添加成功后，随便把设备对应和客户添加到设备客户表tb_customer_device
         * */
         JSONObject dm = commUtil.getJson(addDevice);
        /*1新增设备*/
        DeviceInfoBean deviceInfoBean=new DeviceInfoBean();
        //当前时间
        long time = new Date().getTime();

        deviceInfoBean.setTypeId((Integer) dm.get("type_id"));
        deviceInfoBean.setSerial((String) dm.get("serial"));
        deviceInfoBean.setDescription((String) dm.get("description"));
        deviceInfoBean.setScene((String) dm.get("scene"));
        deviceInfoBean.setAddress((String) dm.get("address"));
        deviceInfoBean.setName((String) dm.get("name"));
        deviceInfoBean.setUpdateTime(time);
        int count = deviceService.insert(deviceInfoBean);
        if(count<=0){
            return Result.ok().msg("该设备添加失败");
        }
         DeviceInfoBean dib = deviceService.selectBySerialOrName(String.valueOf( dm.get("serial")), "");
     /*2把该设备对应的客户添加到中间表*/
        CustomerDeviceBean customerDeviceBean=new CustomerDeviceBean();
//        String customer_ids = dm.get("customer_ids").toString();
        JSONArray jsonArray = JSONArray.fromObject(dm.get("customer_ids").toString());
        Object deviceId= jsonArray.getJSONObject(0).get("id");
        customerDeviceBean.setCustomerId((Integer) deviceId);
        customerDeviceBean.setDeviceId(dib.getId());
        int cdbCount = customerDeviceService.insert(customerDeviceBean);
   if(cdbCount<=0){
       return Result.ok().msg("添加客户设备中间表失败");
   }
        return Result.ok().msg("添加客户设备成功");
    }


    /**
     * @param id
     * @return Result
     * @description 删除设备
     **/
    @GetMapping(value = "/device/delete")
    public Result deleteCustomer(Integer id) {

        /*
        * 步骤：
        * 1.通过device_id删除指定设备
        * 2.通过device_id到tb_customer_device表中把对应的设备客户记录删除
        * */

        /*删除设备*/
        int cdCount = customerDeviceService.deleteBySelective(id);
        /*删除设备对应的中间表记录*/
        int dCount = deviceService.deleteByPrimaryKey(id);

        if(cdCount>0 && dCount>0){

            return Result.error().msg("删除客户失败");
        }
        return Result.ok().msg("删除设备成功");
    }



    /**
     * @description  更新客户
     * @param updateDevice
     * @return Result
     **/
    @PostMapping(value = "/device/update")
    public Result updateCustomer(@RequestParam Map<String,Object> updateDevice){

        /*{"id":178,"type_id":1,"name":"iclean",
        "serial":"2106-004-01-00089",
        "address":"广东省广州市番禺区石楼",
        "description":"无无","scene":"机场"}: */

        JSONObject deviceMap = commUtil.getJson(updateDevice);
        //当前时间
        long time = new Date().getTime();
        DeviceInfoBean dib = deviceService.selectByPrimaryKey((Integer) deviceMap.get("id"));
        dib.setTypeId((Integer) deviceMap.get("type_id"));
        dib.setName((String) deviceMap.get("name"));
        dib.setSerial((String) deviceMap.get("serial"));
        dib.setAddress((String) deviceMap.get("address"));
        dib.setDescription((String) deviceMap.get("description"));
        dib.setUpdateTime(time);
        int count = deviceService.updateByPrimaryKey(dib);
        if(count<0){
            return Result.error().msg("更新设备失败");
        }
        return Result.ok().msg("更新设备成功");
    }

    /**
     * @param
     * @return Result
     * @description 获取设备类型
     **/
    @RequestMapping(value = "/device/lists_type", method = RequestMethod.GET)
    public JSONObject getDeviceTypeList() {
        List deviceList=new ArrayList();
        List<DeviceTypeBean> deviceTypeBeans = deviceTypeService.selectList(null);
        if(deviceTypeBeans.size()<=0){
            return null;
        }
        for (DeviceTypeBean dib:deviceTypeBeans) {
            Map<String,Object> deviceMap=new HashMap<>();
            deviceMap.put("name",dib.getName());
            deviceMap.put("description",dib.getDescription());
            deviceMap.put("id",dib.getId());
            deviceMap.put("type",dib.getType());
            deviceList.add(deviceMap);
        }
        JSONArray jsonArray = new JSONArray().fromObject(deviceList);
       String jStr="{ successed: true,errorCode: 200, msg: '', data:"+ jsonArray+"}";
        return JSONObject.parseObject(jStr);
    }

    /**
     * @param deviceList
     * @return Result
     * @description 获取设备列表
     **/
    @RequestMapping(value = "/device/lists", method = RequestMethod.POST)
    public Result getDeviceList(@RequestParam  Map deviceList)  {
//        public Result getDeviceList(@RequestBody  Map deviceList)  {
        logger.debug("获取设备列表"+deviceList);
        /*通过customer_id筛选：{"customer_id":0,"start_index":0,"count":20,"count_flag":true}: */
        /*通过type_id筛选：{"customer_id":0,"start_index":1,"count":20,"count_flag":true,"type_id":3} */
        /* 通过status筛选:{"customer_id":0,"start_index":0,"count":20,"count_flag":true,"status":0}:
        {"customer_id":0,"start_index":0,"count":20,"count_flag":true,"serial":"2110"}:
         */
        /*0离线 1空闲 2任务中 3充电中 4急停中*/
        JSONObject jsonObject = commUtil.getJson(deviceList);
        //解析deviceList
        int start_index = parseInt(String.valueOf(jsonObject.get("start_index")));
        int count = parseInt(String.valueOf(jsonObject.get("count")));
        int customerId = parseInt(String.valueOf(jsonObject.get("customer_id")));

        PageHelper.offsetPage(start_index, count);
        List<Map<String, Object>> customerList = deviceService.selectDevicesByCustomerId(customerId);
        if(customerList.size()==0){
                    if(jsonObject.get("type_id")!=null){
                        StringBuilder typeIdSql = new StringBuilder();
                        typeIdSql.append(" and  r.type_id="+jsonObject.get("type_id"));
                        PageHelper.offsetPage(start_index, count);
                        customerList=deviceService.selectDevicesBySelective(typeIdSql.toString());
                    }else if(jsonObject.get("status")!=null){
                        StringBuilder statusSql = new StringBuilder();
                        statusSql.append(" and r.status="+jsonObject.get("status"));
                        PageHelper.offsetPage(start_index, count);
                        customerList=deviceService.selectDevicesBySelective(statusSql.toString());
                    }else if(jsonObject.get("serial")!=null){
                        StringBuilder statusSql = new StringBuilder();
                        statusSql.append(" and r.serial="+jsonObject.get("serial"));
                        PageHelper.offsetPage(start_index, count);
                        customerList=deviceService.selectDevicesBySelective(statusSql.toString());
                    }else if(jsonObject.get("name")!=null){
                        //" and  r.name like '%"+jsonObj.get("device_name")+"%'"
                        StringBuilder statusSql = new StringBuilder();
                        statusSql.append(" and  r.name like '%"+jsonObject.get("name")+"%'");
                        PageHelper.offsetPage(start_index, count);
                        customerList=deviceService.selectDevicesBySelective(statusSql.toString());
                    }else {
                        PageHelper.offsetPage(start_index, count);
                        customerList = deviceService.selectDevices();
                    }
        }else {
                if (jsonObject.get("type_id") != null) {
                    StringBuilder typeIdSql = new StringBuilder();
                    typeIdSql.append(" and  r.type_id=" + jsonObject.get("type_id"));
                    PageHelper.offsetPage(start_index, count);
                    customerList = deviceService.selectDevicesByParams(customerId, typeIdSql.toString());
                } else if (jsonObject.get("status") != null) {
                    StringBuilder statusSql = new StringBuilder();
                    statusSql.append(" and r.status=" + jsonObject.get("status"));
                    PageHelper.offsetPage(start_index, count);
                    customerList = deviceService.selectDevicesByParams(customerId, statusSql.toString());
                }else if (jsonObject.get("serial") != null) {
                    StringBuilder statusSql = new StringBuilder();
                    statusSql.append(" and r.serial=" + jsonObject.get("serial"));
                    PageHelper.offsetPage(start_index, count);
                    customerList = deviceService.selectDevicesByParams(customerId, statusSql.toString());
                }else if (jsonObject.get("name") != null) {
                    StringBuilder statusSql = new StringBuilder();
                    statusSql.append(" and  r.name like '%"+jsonObject.get("name")+"%'");
                    PageHelper.offsetPage(start_index, count);
                    customerList = deviceService.selectDevicesByParams(customerId, statusSql.toString());
                }
        }
        long total = ((com.github.pagehelper.Page) customerList).getTotal();

        for (Map<String,Object> map:customerList) {
//            logger.info("deviceIds"+map.get("module_info"));
            //redis
            Object message = redisUtil.hget("message", String.valueOf(map.get("id")));
            JSONObject messageJson = JSONObject.parseObject((String) message);
            if(messageJson!=null){
                Object task = messageJson.get("task");//获取task外层
                Object sonser = messageJson.get("sonser");
                JSONObject sta = JSONObject.parseObject(task.toString());//获取task里层
                JSONObject ser = JSONObject.parseObject(sonser.toString());
                map.put("isCharging",ser.get("chargeStatus"));
                map.put("battery",ser.get("battery"));
                JSONObject motion = JSONObject.parseObject(messageJson.get("motion").toString());
                map.put("last_located_address",motion);
                map.put("position",motion);
                map.put("alarm_count",0);
//                map.put("status",sta.get("status"));
                //maps
                MapsBean mapsBean = mapsService.selectBySelective(Integer.parseInt(map.get("id").toString()), String.valueOf(sta.get("curr_map")));
                if(mapsBean!=null){
                    String path = Constants.Global.GLOBAL_STATIC_URL.getValue() + Constants.Global.GLOBAL_MAP_PATH.getValue() + "/" + mapsBean.getDeviceId() + "/" + mapsBean.getUuid() + "/map.png";
                    map.put("curr_map_id",mapsBean.getId());
                    map.put("curr_map_name",mapsBean.getName());
                    map.put("curr_map",path);
                }else {
                    map.put("curr_map_id",-1);
                    map.put("curr_map_name","");
                    map.put("curr_map","");
                }
            }else {
                map.put("isCharging",0);
                map.put("battery",0);
                JSONObject last_located_address = JSONObject.parseObject(map.get("last_located_address").toString());
                map.put("last_located_address",last_located_address);
                map.put("position",last_located_address);
                map.put("curr_map_id",-1);
                map.put("curr_map_name","");
                map.put("curr_map","");//alarm_count
                map.put("alarm_count",0);
            }
        }

        Map<String, Object> map = new ConcurrentHashMap<>();
        map.put("count", total);
        map.put("device", customerList);
            return Result.ok().data(map).msg("");
    }


    /**
     * @param
     * @param
     * @return Result
     * @description 获取设备实时状态
     **/
    @GetMapping(value = "/device/real_status")
    public Result getDeviceStatus(Integer id) {

        /*步骤：
         * 1.通过device_id获取到指定设备信息，以及设备实时数据，还有地图名称
         * 2.通过设备type_id获取设备类型
         * 3.通过map封装
         * */
        Map<String,Object> dp=new HashMap<>();
        /*1.通过device_id获取到指定设备信息，以及设备实时数据，还有地图名称*/
        DeviceInfoBean deviceInfoBean = deviceService.selectByPrimaryKey(id);
        if(deviceInfoBean==null){
            return Result.ok().msg("the device_id is not exist!");
        }
        dp.put("device_id",id);
        JSONObject jsonObject = JSONObject.parseObject(String.valueOf(redisUtil.hget("message", String.valueOf(id))));
        logger.info("jsonObject"+jsonObject);
        if(jsonObject==null){
            return Result.error().msg("the device_id is not exist!");
        }
                Object task = jsonObject.get("task");//获取task外层
                JSONObject sta = JSONObject.parseObject(task.toString());//获取task里层
                dp.put("motion_state",jsonObject.get("motion"));
                dp.put("task_state",task);
                dp.put("sonser_state",jsonObject.get("sonser"));
                dp.put("map_name",sta.get("curr_map"));

        /*2.通过设备type_id获取设备类型*/
        DeviceTypeBean deviceTypeBean = deviceTypeService.selectByPrimaryKey(deviceInfoBean.getTypeId());
        if(deviceTypeBean==null){
            return Result.ok().msg("该设备不存在设备类型");
        }
        dp.put("device_type",deviceTypeBean.getType());

        return Result.ok().data(dp).msg("");

    }

    /**
     * @param
     * @param
     * @return Result
     * @description 获取设备实时数据
     **/
    @GetMapping(value = "/device/real_time_data")
    public Result getDeviceInfo(Integer device_id) {
        Map<String,Object> dp=new HashMap<>();
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
    public Result getBaseDeviceInfo(Integer id) {

        /*
         * 步骤
         * 1.通过设备序列号id获取指定设备信息
         * 2.通过设备id获取设备实时数据
         * 3.通过设备id到ftp图片服务器中获取到指定图片，从实时数据中获取到map相关的信息
         * 4.通过设备id到设备客户中间表中获取到指定的客户集合
         * 5.通过map封装
         * */

//System.out.println("id: "+id);
        /*1*/
        Map<String,Object> dp=new HashMap<>();
        DeviceInfoBean deviceInfoBean = deviceService.selectByPrimaryKey(id);
        if(deviceInfoBean==null){
            return Result.ok().msg("设备为空");
        }
        /*2*/
            /*通过device_id对比数据库中的设备与线上的设备，并回显的数据为device_id相同的线上设备信息*/
//            if (deviceInfoBean.getId().equals(km) || deviceInfoBean.getId()==km) {
        JSONObject deviceJson = JSONObject.parseObject(String.valueOf(redisUtil.hget("message", String.valueOf(id))));
                if(deviceJson==null){
                    return Result.ok().msg("the devicejson is null!");
                }
                JSONObject sta = JSONObject.parseObject(deviceJson.get("task").toString());
                JSONObject ser = JSONObject.parseObject(deviceJson.get("sonser").toString());
                /*4*/
                List<CustomerDeviceBean> customerDeviceBeans = customerDeviceService.selectByDeviceId(deviceInfoBean.getId());
                dp.put("customers",customerDeviceBeans);
                dp.put("id",deviceInfoBean.getId());
                dp.put("type_id",deviceInfoBean.getTypeId());
                dp.put("serial",deviceInfoBean.getSerial());
                dp.put("status",deviceInfoBean.getStatus());
                dp.put("name",deviceInfoBean.getName());
                dp.put("scene",deviceInfoBean.getScene());
                dp.put("models",deviceInfoBean.getModels());
                dp.put("address",deviceInfoBean.getAddress());
                dp.put("description",deviceInfoBean.getDescription());
                dp.put("update_time",deviceInfoBean.getUpdateTime());
                /*moduleInfo转为JSON对象*/
                String moduleInfo = deviceInfoBean.getModuleInfo();
                if(moduleInfo.equals("")||moduleInfo==null){
                    dp.put("module_info","");
                }else {
                    JSONArray jsonObject = JSONArray.fromObject(moduleInfo);
                    dp.put("module_info",jsonObject);
                }
                dp.put("isCharging",ser.get("chargeStatus"));
                dp.put("battery",ser.get("battery"));
                dp.put("curr_map_name",sta.get("curr_map"));
                /*地图关联到ftp服务，后续解决*/
                // 组装图片路径
                MapsBean mapsBean = mapsService.selectBySelective(deviceInfoBean.getId(), String.valueOf(sta.get("curr_map")));
                if(mapsBean!=null){
                    /*通过比较线上的地图name来获取指定的地图*/
                    String path = Constants.Global.GLOBAL_STATIC_URL.getValue() + Constants.Global.GLOBAL_MAP_PATH.getValue() + "/" + mapsBean.getDeviceId() + "/" + mapsBean.getUuid() + "/map.png";
                    dp.put("curr_map",path);
                    dp.put("curr_map_id",mapsBean.getId());
                }else {
                    dp.put("curr_map","");
                    dp.put("curr_map_id","");
                }

                dp.put("last_located_address",JSONObject.parseObject(deviceInfoBean.getLastLocatedAddress()));
                dp.put("position",deviceJson.get("motion"));

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
        logger.info("更新设备状态："+mp);

        /*{
        "device_id": 1,
        "status": 1,
       "curr_map": ""
       }*/
        /*步骤
        1.通过device_id获取线上指定设备实时状态
        2.如果设备状态发生改变，则实时更新状态回数据库
        * */
        JSONObject json = commUtil.getJson(mp);
        logger.info("更新设备状态","测试。。。。。。");
        JSONObject jsonObject = JSONObject.parseObject(String.valueOf(redisUtil.hget(Constants.Mqtt.TOPIC_SUB_ROBOT_STATUS.getValue(), String.valueOf(json.get("device_id")))));
        if(jsonObject==null){
            return  Result.error().msg("the device_id is not exist!");
        }
        /*获取设备对象*/
        DeviceInfoBean deviceInfoBean = deviceService.selectByPrimaryKey((Integer) json.get("device_id"));
        if(!jsonObject.get("status").equals(deviceInfoBean.getStatus())||jsonObject.get("status")!=deviceInfoBean.getStatus()){
            /*更新状态*/
            deviceInfoBean.setStatus((Integer) jsonObject.get("status"));
            deviceService.updateByPrimaryKey(deviceInfoBean);
            return  Result.ok().msg("the status is updated!");
        }
        return Result.error().msg("update is wrong!");
    }
    /**
     * @param
     * @param
     * @return CommnosResult
     * @description 判断设备序列号是否存在
     **/
    @GetMapping(value = "/device/serial/exist")
    public Result isDeviceSerial(String serial) {

        CommnosResult cr=new CommnosResult();
        DeviceInfoBean deviceInfoBean = deviceService.selectBySerialOrName(serial,"");
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
    public Result isDeviceName(Integer user_id,String name) {
        DeviceInfoBean deviceInfoBean = deviceService.selectBySerialOrName("",name);
        if(deviceInfoBean!=null){
            return Result.ok().data("exist",false).msg("");
        }

        return Result.error().data("exist",true).msg("the device name is not exist!");
    }


    /**
     * @param
     * @param
     * @return Result
     * @description 机器人定位
     **/
    @PostMapping(value = "/localization")
    public Result getLocalization( @RequestParam Map<String,Object> map) {
        /*{"device_id":163,"position":{"x":926,"y":428,"yaw":95}}:*/
        /*{device_id=163, position={x=926, y=428, yaw=95}}*/
        /*
         * 步骤：
         * 1.通过获取前端机器重新定位的机器相关数据
         * 2.根据机器id获取线上设备数据，并进行设备定位更改
         * 3.根据格式重组JSON
         * 4.发布到指定主题
         * */
        logger.info("机器人定位","测试。。。。。");
        JSONObject jsonObj = commUtil.getJson(map);
        /*1*/
        Map<String,Object> tMap=new HashMap<>();
        JSONObject JsonPosition = JSONObject.parseObject(String.valueOf(jsonObj.get("position")));
        /*2拼接JSON对象*/
        String jsonP="{position:{x:"+JsonPosition.get("x")+",y:"+JsonPosition.get("y")+",r:"+JsonPosition.get("yaw")+"}}";
        JSONObject jsonObject = JSONObject.parseObject(jsonP);
        Object newPosition = jsonObject.get("position");
        /*通过设备id获取到指定线上设备信息*/
        JSONObject onlineJson = JSONObject.parseObject(String.valueOf(redisUtil.hget("message", String.valueOf(jsonObj.get("device_id")))));
//        Object deviceBean = MSG_map.get((Integer) mMap.get("device_id"));
//        JSONObject onlineJson = JSONObject.parseObject(String.valueOf(deviceBean));
        JSONObject motionJson = JSONObject.parseObject(String.valueOf(onlineJson.get("motion")));
        /*更新设备坐标*/
        motionJson.put("position",newPosition);
        /*map封装 */
        tMap.put("device_id",(Integer) jsonObj.get("device_id"));
        tMap.put("sonser",onlineJson.get("sonser"));
        tMap.put("task",onlineJson.get("task"));
        tMap.put("motion",motionJson);
//        mqttGateway.publishMqttMessageWithTopic(JSONObject.toJSONString(tMap),Constants.Mqtt.TOPIC_PUB_ROBOT_MSG.getValue(),2);
        return Result.ok().data(onlineJson).msg("机器人定位成功");
    }
    /**
     * @param
     * @param
     * @return Result
     * @description 获取事件列表
     **/
    @RequestMapping(value = "/event/lists", method = RequestMethod.POST)
    public Result getEventLists(@RequestParam Map map){
//        public Result getEventLists(@RequestBody Map map){
        logger.info("获取设备列表"+map);
/*{start_index=0, beginTime=, endTime=, count=20, count_flag=true, status=0, user_id=1}
 {"user_id":1,"start_index":0,"beginTime":"","endTime":"","count":10,"count_flag":true,"device_id":113,"status":0}:
 {
                "type": 0,
                "status": 0,
                "update_time": "2021-12-16 09:21:05",
                "device_name": "老化消杀37",
                "context": "结束任务 [37], 地图[37]",
                "id": 3886
            }
 */
        JSONObject jsonObj = commUtil.getJson(map);
        logger.info("获取设备列表jsonObj"+jsonObj);
        int startIndex = parseInt(jsonObj.get("start_index").toString());
        int count = parseInt(jsonObj.get("count").toString());
        int userId = parseInt(jsonObj.get("user_id").toString());
        List<Map<String, Object>> eventsList =null;
        int counts = customerService.selectCustomer(userId);
        if(counts==0){
            if(jsonObj.get("device_id")!=null){
                StringBuilder deviceNameSql = new StringBuilder();
                deviceNameSql.append(" and r.device_id="+jsonObj.get("device_id"));
                PageHelper.offsetPage(startIndex, count);
                eventsList=eventsService.selectEventsBySelective(deviceNameSql.toString());
            }/*else if(jsonObj.get("status")!=null){
                StringBuilder statusSql = new StringBuilder();
                statusSql.append(" and t.status="+jsonObj.get("status"));
                PageHelper.offsetPage(startIndex, count);
                eventsList=eventsService.selectEventsBySelective(statusSql.toString());
            }*/else if(!jsonObj.get("beginTime").equals("") || !jsonObj.get("endTime").equals("")){
                StringBuilder timeSql = new StringBuilder();
                timeSql.append(" and   FROM_UNIXTIME(r.update_time, '%Y-%m-%d %H:%i:%S')  between '"+jsonObj.get("beginTime")+"'  and  '"+jsonObj.get("endTime")+"'");
                PageHelper.offsetPage(startIndex, count);
                eventsList=eventsService.selectEventsBySelective(timeSql.toString());
            }else {
                PageHelper.offsetPage(startIndex, count);
                eventsList=eventsService.selectEvents();
            }

        }else {
            if(jsonObj.get("device_id")!=null){
                StringBuilder deviceNameSql = new StringBuilder();
                deviceNameSql.append(" and r.device_id="+jsonObj.get("device_id"));
                PageHelper.offsetPage(startIndex, count);
                eventsList=eventsService.selectEventsByParams(userId,deviceNameSql.toString());
            }/*else if(jsonObj.get("status")!=null){
                StringBuilder statusSql = new StringBuilder();
                statusSql.append(" and t.status="+jsonObj.get("status"));
                PageHelper.offsetPage(startIndex, count);
                eventsList=eventsService.selectEventsByParams(userId,statusSql.toString());
            }*/else if(!jsonObj.get("beginTime").equals("") || !jsonObj.get("endTime").equals("")){
                StringBuilder timeSql = new StringBuilder();
                timeSql.append(" and   FROM_UNIXTIME(r.update_time, '%Y-%m-%d %H:%i:%S')  between '"+jsonObj.get("beginTime")+"'  and  '"+jsonObj.get("endTime")+"'");
                PageHelper.offsetPage(startIndex, count);
                eventsList=eventsService.selectEventsByParams(userId,timeSql.toString());
            }else {
                PageHelper.offsetPage(startIndex, count);
                eventsList=eventsService.selectEventsByUserId(userId);
            }
        }

        long total = ((com.github.pagehelper.Page) eventsList).getTotal();

        Map<String,Object> jsonMap=new ConcurrentHashMap<>();
        jsonMap.put("count",total);
        jsonMap.put("events",eventsList);
        return Result.ok().data(jsonMap).msg("");
    }

    /**
     * @param
     * @param
     * @return Result
     * @description 发命令给设备
     **/
    @GetMapping(value = "/command")
    public Result sendCommand(@RequestParam Map<String,Object> map) {

        /*
        * 步骤
        * 1.获取前端发送数据device_id、command
        * 2.通过
        * */

        return Result.ok().msg("发命令给设备成功");
    }


 }
