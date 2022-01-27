package com.iclean.pt.sbgl.controller;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.PropertyNamingStrategy;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.iclean.pt.sbgl.bean.*;
import com.iclean.pt.sbgl.dao.MqttGateway;
import com.iclean.pt.sbgl.service.*;
import com.iclean.pt.utils.CommnosResult;
import com.iclean.pt.utils.Constants;
import com.iclean.pt.utils.RedisUtil;
import com.iclean.pt.utils.Result;
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


import static java.lang.Integer.parseInt;

@RestController
public class DeviceController {

    private final static Logger logger = LoggerFactory.getLogger(DeviceController.class);

    /*JSON格式转换*/
    static  SerializeConfig config=new SerializeConfig() ;
    @Autowired
    private CustomerDeviceService customerDeviceService;
    @Autowired
    private DeviceService deviceService;
    @Autowired
    private DeviceTypeService deviceTypeService;
    @Autowired
    private PathsService pathsService;
    @Autowired
    private MapsService mapsService;
  /*  @Autowired
    private UserService userService;*/
    @Autowired
    private CustomerService customerService;
    @Autowired
    private AlarmService alarmService;
   /* @Autowired
    private MqttGateway mqttGateway;*/
    @Autowired
    private EventsService eventsService;
    @Autowired
    private TaskService taskService;
    @Autowired
    private CleanReportService cleanReportService;
    @Autowired
    private RedisUtil redisUtil;


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
        JSONObject dm=null;
        for (String km : addDevice.keySet()) {
            dm = JSONObject.parseObject(km);
        }
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


        String key ="";
        Iterator<String> it = updateDevice.keySet().iterator();
        while(it.hasNext()){
            key = it.next();
        }
        Map<String,Object> deviceMap = JSONObject.parseObject(key);
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
    public Result getDeviceList(@RequestParam  Map<String,String> deviceList)  {
//        获取设备列表：{start_index=0, count=20, count_flag=true, customer_id=0}
        logger.info("获取设备列表："+deviceList);
        /*通过customer_id筛选：{"customer_id":0,"start_index":0,"count":20,"count_flag":true}: */
        /*通过type_id筛选：{"customer_id":0,"start_index":1,"count":20,"count_flag":true,"type_id":3}: */
        /* 通过status筛选:{"customer_id":0,"start_index":0,"count":20,"count_flag":true,"status":0}:
         */
        /*0离线 1空闲 2任务中 3充电中 4急停中*/
//        String toJSONString = JSON.toJSONString(deviceList);

        //解析deviceList
        JSONObject jsonObject=null;
        int lastIndexOf = deviceList.toString().lastIndexOf(":");
if(lastIndexOf==-1){
    String jsonStr = deviceList.toString().replace("=", ":");
    jsonObject=JSONObject.parseObject(jsonStr);
}else {
    String subStr = deviceList.toString().substring(0, deviceList.toString().length() - 2);
    jsonObject = JSONObject.parseObject(subStr.substring(1));
}
        logger.info("获取设备json："+jsonObject);
        List<JsonBean>   jsonBeans=new ArrayList<>();
      /*  List<JsonBean>   jsonBeans=new ArrayList<>();
        Map<String, Object> deviceMap=null;
        for (String km : deviceList.keySet()) {
            deviceMap = JSONObject.parseObject(km);
        }*/
        int start_index = parseInt(String.valueOf(jsonObject.get("start_index")));
        int count = parseInt(String.valueOf(jsonObject.get("count")));
        Integer status =(Integer) jsonObject.get("status");
        Integer typeId = (Integer)jsonObject.get("type_id");
        String serial = String.valueOf(jsonObject.get("serial"));
        String name = String.valueOf(jsonObject.get("name"));
        JsonBean jsonBean =null;
        /*通过customer_id从客户设备备中间表中查询出设备列表*/
        List<CustomerDeviceBean> customerDeviceBeans = customerDeviceService.selectCustomerWithDevices((Integer) jsonObject.get("customer_id"));
        /*如果customerDeviceBeans等于0，说明customer_id对应的客户不能存在设备信息，则通过查询tb_device列表*/
        if (customerDeviceBeans.size() == 0) {
            /*根据条件获取设备列表*/
            List<DeviceInfoBean> dibs=deviceService.selectBySelective(typeId,status,name,serial);//67
            if (dibs.size() <= 0) {
                return Result.ok().msg("error");
            }
            /*把线上的和数据库中的设备分憋做处理*/
//            for (int i=0;i<dibs.size();i++) {
                for (DeviceInfoBean dib:dibs) {
                    Object message = redisUtil.hget("message", String.valueOf(dib.getId()));
//                    logger.info("redis数据:",JSON.toJSONString(message));
                    JSONObject jsonObj = JSONObject.parseObject((String) message);
//                    logger.info("redis数据22:",jsonObj);
                    if(jsonObj!=null){
//                    for (Object km : redisUtil.hmget("message").keySet()) {
                    logger.info("redis数据:",jsonObj);
                    /*通过device_id对比数据库中的设备与线上的设备，并回显的数据为device_id相同的线上设备信息*/
//                    if (dib.getId().equals(km) || dib.getId()==km) {
//                        lst.add(dib.getId());
                        jsonBean = new JsonBean();
                        jsonBean.setId(dib.getId());
                        jsonBean.setSerial(dib.getSerial());
                        jsonBean.setAddress(dib.getAddress());
                        jsonBean.setTypeId(dib.getTypeId());
                        jsonBean.setStatus(dib.getStatus());
                        jsonBean.setScene(dib.getScene());
                        jsonBean.setName(dib.getName());
                        /*组装JSON串*//*
                         *//*通过设备device_id从设备类型表中获取指定设备类型*/
                        DeviceTypeBean deviceTypeBean = deviceTypeService.selectByPrimaryKey(dib.getTypeId());
                        /*通过device_id从地图表中获取指定地图信息*/
                        Object msgHget = redisUtil.hget("message", String.valueOf(dib.getId()));
                        JSONObject deviceJson = JSONObject.parseObject(String.valueOf(msgHget));
                        /*获取设备*/
                        Object statusHget = redisUtil.hget(Constants.Mqtt.TOPIC_SUB_ROBOT_STATUS.getValue(), String.valueOf(dib.getId()));
//                        logger.info("redis设备状态",statusHget);
                        JSONObject statusJson = JSONObject.parseObject(String.valueOf(statusHget));
//                        System.out.println("deviceJson: "+deviceJson);
                        Object task = deviceJson.get("task");//获取task外层
//                        System.out.println("task:  "+task);
                        Object sonser = deviceJson.get("sonser");
                        JSONObject sta = JSONObject.parseObject(task.toString());//获取task里层
                        JSONObject ser = JSONObject.parseObject(sonser.toString());
                        jsonBean.setIsCharging(String.valueOf(ser.get("chargeStatus")));
                        jsonBean.setBattery((Integer) ser.get("battery"));
                        jsonBean.setCurrMap((String) sta.get("curr_map"));
                        jsonBean.setLastLocatedAddress(deviceJson.get("motion"));
                        /*如果设备版本发生变化，则则使用变更的版本，否则使用之前版本*/
                        if(statusJson!=null&&statusJson.get("module_info")!=null){
                            jsonBean.setModuleInfo(statusJson.get("module_info"));
                        }else {
                            jsonBean.setModuleInfo(deviceJson.get("module_info"));
                        }
                        jsonBean.setPosition(deviceJson.get("motion"));
                        jsonBean.setCurrMapId(1);
                        jsonBean.setCurrMapName((String) sta.get("curr_map"));
                        jsonBean.setType(deviceTypeBean.getType());
                        jsonBeans.add(jsonBean);
                }
            }

            /*不在线的设备*/

        }else {

            /*否则通过device_id遍历中间表查询出该客户中的所有设备信息列表*/
//            for (int i = 0; i < customerDeviceBeans.size(); i++) {
            for (CustomerDeviceBean cdb : customerDeviceBeans) {
                DeviceInfoBean deviceInfoBean = deviceService.selectByPrimaryKey(cdb.getDeviceId());
                if (deviceInfoBean == null) {
                    return Result.ok().msg("error");
                }
                /*组装JSON串*/
                jsonBean = new JsonBean();
                jsonBean.setId(deviceInfoBean.getId());
                jsonBean.setSerial(deviceInfoBean.getSerial());
                jsonBean.setAddress(deviceInfoBean.getAddress());
                jsonBean.setTypeId(deviceInfoBean.getTypeId());
                jsonBean.setStatus(deviceInfoBean.getStatus());
                jsonBean.setScene(deviceInfoBean.getScene());
                jsonBean.setName(deviceInfoBean.getName());
                Object pInfo = JSONObject.parse(deviceInfoBean.getModuleInfo());
                jsonBean.setModuleInfo(pInfo);
                Object parse = JSONObject.parse(deviceInfoBean.getLastLocatedAddress());
                jsonBean.setLastLocatedAddress(parse);
                jsonBean.setDescription(deviceInfoBean.getDescription());

                /*通过设备device_id从设备类型表中获取指定设备类型*/
                DeviceTypeBean deviceTypeBean = deviceTypeService.selectByPrimaryKey(deviceInfoBean.getTypeId());
                /*通过device_id从地图表中获取指定地图信息*/
                jsonBean.setType(deviceTypeBean.getType());
                jsonBeans.add(jsonBean);
            }

        }

        List<JsonBean> jsonBeanList = null;
        if (start_index == 0) {
            if(count > jsonBeans.size()){
                jsonBeanList = jsonBeans.subList(start_index, jsonBeans.size());
            }else {
                jsonBeanList = jsonBeans.subList(start_index, count);
            }
        } else {
            int Tcount = count + start_index;
            int size = jsonBeans.size();
            if (Tcount >size) {
                jsonBeanList = jsonBeans.subList(start_index, size);
            } else {
                jsonBeanList = jsonBeans.subList(start_index, Tcount);
            }

        }
        Map<String, Object> map = new HashMap<>();
        map.put("count", jsonBeans.size());
        map.put("device", jsonBeanList);
        /*通过JSON转换为下划线格式参数*/
        config.propertyNamingStrategy = PropertyNamingStrategy.SnakeCase;
        String toJSON = JSON.toJSONString(map, config);
        JSONObject deviceM = JSONObject.parseObject(toJSON);
        return Result.ok().data(deviceM).msg("");
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
        Integer device_id=null;
        /*步骤
        1.通过device_id获取线上指定设备实时状态
        2.如果设备状态发生改变，则实时更新状态回数据库
        * */

        logger.info("更新设备状态","测试。。。。。。");
        JSONObject jsonObject = JSONObject.parseObject(String.valueOf(redisUtil.hget(Constants.Mqtt.TOPIC_SUB_ROBOT_STATUS.getValue(), String.valueOf(device_id))));
        if(jsonObject==null){
            return  Result.error().msg("the device_id is not exist!");
        }
        /*获取设备对象*/
        DeviceInfoBean deviceInfoBean = deviceService.selectByPrimaryKey(device_id);
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
     * @description 添加路径
     **/
    @PostMapping(value = "/path/add")
    public Result addPaths() {

        /*
        * 步骤：
        * 1.通过mqtt订阅的主题iclean/robot/messageEx获取到设备新增的地图信息
        * 2.通过device_id获取tb_maps表中的map_id
        * 3.通过map封装
        * */
          /*1*/
        logger.info("添加路径","功能待定。。。。。");
        JSONObject jb = JSON.parseObject("");
        /*2*/
        MapsBean mapsBean = mapsService.selectBySelective((Integer) jb.get("device_id"), String.valueOf(jb.get("map_name")));
        /*组装实体PathsBean*/
        PathsBean pathsBean=new PathsBean();
        pathsBean.setPath(JSONObject.toJSONString(jb.get("msg")));
        pathsBean.setType((Integer) jb.get("type"));
        pathsBean.setName( String.valueOf(jb.get("origin_name")));
        pathsBean.setDeviceId((Integer) jb.get("device_id"));
        pathsBean.setMapId(mapsBean.getId());
        pathsBean.setUpdateTime(  new Date().getTime());
        int insert = pathsService.insert(pathsBean);
        if(insert>0){
            return Result.ok().msg("路径添加成功");
        }
        return Result.ok().msg("路径添加失败");
    }

    /**
     * @param
     * @param
     * @return Result
     * @description 修改路径
     **/
    @PostMapping(value = "/path/update")
    public Result updatePaths() {

        /*步骤：
        * 1.通过mqtt获取paths名称并查询数据库表tb_paths获取指定路径实体bean
        * 2.更新mqtt中已变更的指定路径信息
        * 3.*/
        /*1*/
        logger.info("修改路径","功能未做。。。。。");
        JSONObject jb = JSON.parseObject("");
        /*通过map_id和path_name获取指定地图路径*/
        PathsBean pathsBean = pathsService.selectByPrimaryKey(null, String.valueOf(jb.get("origin_name")));
//        System.out.println("pathsBean: "+pathsBean);
        /*2*/
        pathsBean.setPath(JSONObject.toJSONString(jb.get("msg")));
        pathsBean.setType((Integer) jb.get("type"));
        pathsBean.setName( String.valueOf(jb.get("new_name")));
//        pathsBean.setDeviceId(pathsBean.getDeviceId());
        pathsBean.setUpdateTime( new Date().getTime());
//        pathsBean.setMapId(pathsBean.getMapId());
        int updateByPrimaryKey = pathsService.updateByPrimaryKey(pathsBean);
        if(updateByPrimaryKey>0){
//            System.out.println("updateByPrimaryKey: "+pathsBean);
            return Result.ok().msg("修改路径成功");
        }
        return Result.ok().msg("修改路径失败");
    }



    /**
     * @param
     * @param
     * @return Result
     * @description 获取报警列表
     **/
    @RequestMapping(value = "/alarm/lists", method = RequestMethod.POST)
    public Result getAarmLists(@RequestParam Map<String,Object> map) {
        logger.info("获取报警列表："+map);
/*{"user_id":1,"start_index":0,"beginTime":"","endTime":"","count":100,"count_flag":true,"status":0}
{
context: "急停按下"
device_id: 183
device_name: "老化168"
id: 195
start_time: "2021-12-03 12:00:26"
status: 0
update_time: "2021-12-03 12:00:26"}
iclean/robot/status: 处理消息 {"data":"结束任务 [10], 地图[L5]","device_id":146,"message_type":"event"}
{"user_id":1,"start_index":0,"device_id":54,"beginTime":"","endTime":"","count":10,"count_flag":true,"status":0}:
*/
        /*步骤：
         * 2.通过user_id获取指定客户下的所有设备告警信息 user_id:customer_id:devices
         * 3.如果user_id指定的客户不存在，则查询所有客户下的所有设备的告警信息
         * 4.组装JSON对象返回
         * */


        JSONObject jsonObj=null;
        int lastIndexOf = map.toString().lastIndexOf(":");
        if(lastIndexOf==-1){
            String jsonStr = map.toString().replace("=", ":");
            jsonObj=JSONObject.parseObject(jsonStr);
        }else {
            String subStr = map.toString().substring(0, map.toString().length() - 2);
            jsonObj = JSONObject.parseObject(subStr.substring(1));
        }

        int counts;
        int startIndex = parseInt(String.valueOf(jsonObj.get("start_index")));
        int count = parseInt(String.valueOf(jsonObj.get("count")));
        int status = parseInt(String.valueOf(jsonObj.get("status")));
        /*2*/
        List<CustomerBean> customerBeans = null;
        List<AlarmBean> alarmBeans =null;
        List lst=new ArrayList();
        if (jsonObj.get("device_id")!=null){
            JSONObject jsonObject = JSONObject.parseObject(String.valueOf(redisUtil.hget("users",String.valueOf(jsonObj.get("user_id")))));
            CustomerBean customerBean = customerService.selectByPrimaryKey((Integer) jsonObject.get("customerId"));
            /*2*/
            if (customerBean == null) {
                customerBeans = customerService.selectBySelective(null, null);
            } else {
                customerBeans = customerService.selectBySelective(null, customerBean.getId());
            }
            Map<String, Object> maps = new HashMap<>();
            for (CustomerBean cb : customerBeans) {
                List<CustomerDeviceBean> customerDeviceBeans = customerDeviceService.selectCustomerWithDevices(cb.getId());
                for (CustomerDeviceBean cdb : customerDeviceBeans) {
                    if (cdb.getDeviceId() == jsonObj.get("device_id") || cdb.getDeviceId().equals(jsonObj.get("device_id"))) {
                        maps.put("deviceId", cdb.getDeviceId());
                        break;
                    }
                }
            }
            alarmBeans = alarmService.selectBySelective((Integer) maps.get("deviceId"), status, startIndex, count);
            counts = alarmService.selectBySelective((Integer) maps.get("deviceId"), status, 0, 0).size();
        }else {
              alarmBeans =alarmService.selectList(status,startIndex,count);
              counts= alarmService.selectList(status,0,0).size();
        }

        if(alarmBeans.size()>0){
            for (AlarmBean ab:alarmBeans) {
                DeviceInfoBean deviceInfoBean = deviceService.selectByPrimaryKey(ab.getDeviceId());
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
        }
        Map<String,Object> jsonMap=new HashMap<>();
        jsonMap.put("count",counts);
        jsonMap.put("alarms",lst);
        return Result.ok().data(jsonMap).msg("");
    }

    /**
     * @param
     * @param
     * @return Result
     * @description 获取任务列表
     **/
    @RequestMapping(value = "/task/lists", method = RequestMethod.POST)
    public Result getTaskList(@RequestParam Map<String,Object> map){
        logger.info("获取任务列表:"+map);
        /*{"user_id":1,"start_index":0,"count":10}:
         * 条件筛选：{"user_id":1,"start_index":0,"count":10,"device_type_id":1}: 设备类型 device_type_id
           {"user_id":1,"start_index":0,"count":10,"device_status":"1"}:  设备状态 device_status
            {"user_id":1,"start_index":0,"count":10,"task_mode":"0"}:   任务类型work_type
             {"user_id":1,"start_index":0,"count":10,"device_name":"长桥街"}:
             {"user_id":1,"device_id":128,"start_index":0,"count":9999}: */
      /*步骤
      * 1通过user获取到指定的客户
      * 2如果客户存在则通过该客户查询名下的所有设备，以及设备下的所有任务
      * 3.否则查询所有客户及客户名下的所有设备，以及设备下的任务
      *
      * */

        JSONObject jsonObj=null;
        int lastIndexOf = map.toString().lastIndexOf(":");
        if(lastIndexOf==-1){
            String jsonStr = map.toString().replace("=", ":");
            jsonObj=JSONObject.parseObject(jsonStr);
        }else {
            String subStr = map.toString().substring(0, map.toString().length() - 2);
            jsonObj = JSONObject.parseObject(subStr.substring(1));
        }


        int startIndex = parseInt(String.valueOf(jsonObj.get("start_index")));
        int count = parseInt(String.valueOf(jsonObj.get("count")));
//        int userId = parseInt(String.valueOf(taskmMap.get("user_id")));
//        Integer deviceTypeId = (Integer) taskmMap.get("device_type_id");
//        String deviceStatus = String.valueOf(taskmMap.get("device_status"));
//        String workType = String.valueOf(taskmMap.get("task_mode"));
//        String deviceName = String.valueOf(taskmMap.get("device_name"));
//        System.out.println("  deviceStatus: "+deviceStatus+"  workType: "+workType+"  deviceName: "+deviceName);
        Integer deviceId =1;//存在bug,前端没提供该参数
        List<CustomerBean> customerBeans =null;
        JSONObject alarmObject =JSONObject.parseObject(String.valueOf(redisUtil.hget("users",String.valueOf(jsonObj.get("user_id")))));
    CustomerBean customerBean = customerService.selectByPrimaryKey((Integer) alarmObject.get("customerId"));
    if(customerBean==null){
        customerBeans = customerService.selectBySelective(null, null);
    }else {
        customerBeans = customerService.selectBySelective(null, customerBean.getId());
    }
    Map<Integer,Object> hp=new LinkedHashMap<>();

    Map<Integer,Object> customerMap=new HashMap<>();
        Map<Integer,Object> deviceMaps=new HashMap<>();
    for (CustomerBean cb:customerBeans ) {
        List<CustomerDeviceBean> customerDeviceBeans = customerDeviceService.selectCustomerWithDevices(cb.getId());
        if(customerDeviceBeans.size()>0){
            for (CustomerDeviceBean cdb : customerDeviceBeans) {
                deviceMaps.put(cdb.getDeviceId(),cdb.getDeviceInfoBean());
            }
        }
    }

        List lists=new ArrayList();
        for (Map.Entry<Integer,Object> entry:deviceMaps.entrySet()) {
            List<TaskBean> taskBeans = taskService.selectSelective(entry.getKey(), startIndex, count);
            JSONObject jsonObject = JSONObject.parseObject(JSON.toJSONString(entry.getValue()));
            for (TaskBean tbs:taskBeans) {
//            List<TaskBean> taskBeans = taskService.selectBySelective((Integer) os,workType);
                //组装JSON对象
                Map<String,Object> beanMap=new HashMap<>();
                beanMap.put("id",tbs.getId());
                beanMap.put("task_name",tbs.getName());
                beanMap.put("loop_count",tbs.getLoopCount());
                beanMap.put("device_id",tbs.getDeviceId());
                beanMap.put("parameter",tbs.getParameter());
                if(jsonObject==null){
                    beanMap.put("device_serial","");
                    beanMap.put("device_name","");
                }else {
                    beanMap.put("device_serial",jsonObject.get("serial"));
                    beanMap.put("device_name",jsonObject.get("name"));
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
                lists.add(beanMap);
            }
        }

        Map<String,Object> jsonMap=new HashMap<>();
        jsonMap.put("count",lists.size());
        jsonMap.put("tasks",lists);
        return Result.ok().data(jsonMap).msg("");
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
        JSONObject jsonObj=null;
        int lastIndexOf = map.toString().lastIndexOf(":");
        if(lastIndexOf==-1){
            String jsonStr = map.toString().replace("=", ":");
            jsonObj=JSONObject.parseObject(jsonStr);
        }else {
            String subStr = map.toString().substring(0, map.toString().length() - 2);
            jsonObj = JSONObject.parseObject(subStr.substring(1));
        }


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
    public Result getEventLists(@RequestParam Map<String,Object> map){
/*{"user_id":1,"start_index":0,"beginTime":"","endTime":"","count":10,"count_flag":true,"device_id":113,"status":0}:
 {
                "type": 0,
                "status": 0,
                "update_time": "2021-12-16 09:21:05",
                "device_name": "老化消杀37",
                "context": "结束任务 [37], 地图[37]",
                "id": 3886
            }
 */


        JSONObject jsonObj=null;
        int lastIndexOf = map.toString().lastIndexOf(":");
        if(lastIndexOf==-1){
            String jsonStr = map.toString().replace("=", ":");
            jsonObj=JSONObject.parseObject(jsonStr);
        }else {
            String subStr = map.toString().substring(0, map.toString().length() - 2);
            jsonObj = JSONObject.parseObject(subStr.substring(1));
        }

        List eventLists=new ArrayList();
        int size=0;
     List<EventsBean> eventsBeans = eventsService.selectBySelective((Integer) jsonObj.get("device_id"),(Integer) jsonObj.get("status"), (Integer) jsonObj.get("start_index"), (Integer) jsonObj.get("count"));
   if(eventsBeans.size()>0){
       size= eventsService.selectBySelective((Integer) jsonObj.get("device_id"),(Integer) jsonObj.get("status"),0,0).size();
    for (EventsBean ebs:eventsBeans) {
        //组装JSON对象
        Map<String,Object> beanMap=new HashMap<>();
        beanMap.put("context",ebs.getContext());
        beanMap.put("device_name",deviceService.selectByPrimaryKey(ebs.getDeviceId()).getName());
        beanMap.put("id",ebs.getId());
        beanMap.put("type",ebs.getType());
        beanMap.put("status",ebs.getStatus());
        Date d=new Date(ebs.getUpdateTime());
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateString = formatter.format(d);
        beanMap.put("update_time",dateString);
        eventLists.add(beanMap);
    }
}
        Map<String,Object> jsonMap=new HashMap<>();
        jsonMap.put("count",size);
        jsonMap.put("events",eventLists);
        return Result.ok().data(jsonMap).msg("");
    }


    /**
     * @param
     * @param
     * @return Result
     * @description 新增任务
     **/
    @PostMapping(value = "/task/add")
    public Result addTask(@RequestParam Map<String,Object> map) {

       /*{"name":"20211216",
       "device_id":182,
       "parameter":[{"name":"handdraw","start_param":{"name":"2","zid":1074,"path":"path1"}}],
       "map_id":486,
       "work_time":0,
       "work_day":0,
       "work_type":0,
       "work_types":[16],
       "loop_count":1,
       "type":16,
       "work_parts":{"speed":0,"BLDC":0,"SuckerRod":0,"DiscBrush":0,"DiscBrushRod":0},
       "map_name":"2"}: */

       /*步骤
       * 1.获取map
       * 2.解析map,获取map中的数据
       * 3.组装taskbean实例对象
       * */
        JSONObject jsonObj=null;
        int lastIndexOf = map.toString().lastIndexOf(":");
        if(lastIndexOf==-1){
            String jsonStr = map.toString().replace("=", ":");
            jsonObj=JSONObject.parseObject(jsonStr);
        }else {
            String subStr = map.toString().substring(0, map.toString().length() - 2);
            jsonObj = JSONObject.parseObject(subStr.substring(1));
        }


        /*2、3*/
        TaskBean taskBean=new TaskBean();
        taskBean.setDeviceId(parseInt(String.valueOf(jsonObj.get("device_id"))));
        taskBean.setLoopCount(parseInt(String.valueOf(jsonObj.get("loop_count"))));
        taskBean.setMapId(parseInt(String.valueOf(jsonObj.get("map_id"))));
        taskBean.setName(String.valueOf(jsonObj.get("name")));
        taskBean.setParameter(String.valueOf(jsonObj.get("parameter")));
        taskBean.setType(parseInt(String.valueOf(jsonObj.get("type"))));
        taskBean.setWorkDay(parseInt(String.valueOf(jsonObj.get("work_day"))));
        taskBean.setWorkParts(String.valueOf(jsonObj.get("work_parts")));
        taskBean.setWorkTime(parseInt(String.valueOf(jsonObj.get("work_time"))));
        taskBean.setWorkType(parseInt(String.valueOf(jsonObj.get("work_type"))));
        taskBean.setUpdateTime( new Date().getTime());

        int insert = taskService.insert(taskBean);
        if(insert<=0){
            return Result.ok().msg("新增任务失败");
        }
        return Result.ok().msg("新增任务成功");
    }

    /**
     * @param
     * @param
     * @return Result
     * @description 更新任务
     **/
    @PostMapping(value = "/task/update")
    public Result updateTask(@RequestParam Map<String,Object> map) {
/*{"work_day":1,
"work_parts":{"speed":0,"BLDC":0,"SuckerRod":0,"DiscBrush":0,"DiscBrushRod":0},
"id":1221,
"loop_count":5,
"parameter":[{"start_param":{"path":"path1","zid":1048,"name":"a"},"name":"ZoneTask"}],
"map_name":"2",
"work_type":0,
"work_time":54000,
"type":16,
"name":"20211216t",
"old_name":"20211216",
"device_id":182
}: */
        /*步骤
        * 1.获取map并解析map中的数据
        * 2.通过任务id获取任务对象
        * 3.对任务对象进行更新
        * */
/*1*/
        JSONObject jsonObj=null;
        int lastIndexOf = map.toString().lastIndexOf(":");
        if(lastIndexOf==-1){
            String jsonStr = map.toString().replace("=", ":");
            jsonObj=JSONObject.parseObject(jsonStr);
        }else {
            String subStr = map.toString().substring(0, map.toString().length() - 2);
            jsonObj = JSONObject.parseObject(subStr.substring(1));
        }

        TaskBean taskBean = taskService.selectByPrimaryKey(parseInt(String.valueOf(jsonObj.get("id"))));
        taskBean.setLoopCount(parseInt(String.valueOf(jsonObj.get("loop_count"))));
        taskBean.setName(String.valueOf(jsonObj.get("name")));
        taskBean.setParameter(String.valueOf(jsonObj.get("parameter")));
        taskBean.setType(parseInt(String.valueOf(jsonObj.get("type"))));
        taskBean.setWorkDay(parseInt(String.valueOf(jsonObj.get("work_day"))));
        taskBean.setWorkParts(String.valueOf(jsonObj.get("work_parts")));
        taskBean.setWorkTime(parseInt(String.valueOf(jsonObj.get("work_time"))));
        taskBean.setWorkType(parseInt(String.valueOf(jsonObj.get("work_type"))));
        taskBean.setUpdateTime(  new Date().getTime());

        int count = taskService.updateByPrimaryKey(taskBean);
        if(count<=0){
            return Result.ok().msg("更新任务失败");
        }
        return Result.ok().msg("更新任务成功");
    }

    /**
     * @param
     * @param
     * @return Result
     * @description 删除任务
     **/
    @GetMapping(value = "/task/delete")
    public Result deleteTask(@RequestParam Map<String,Object> map) {
/*id: 1221
map_name: 2
device_id: 182
name: 20211216t*/

        int count = taskService.deleteByPrimaryKey(parseInt(String.valueOf(map.get("id"))));
        if(count>0){
            return Result.ok().msg("删除任务失败");
        }
        return Result.ok().msg("删除任务成功");
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
    /**
     * @param
     * @param
     * @return Result
     * @description 获取清洁报告列表
     **/
    @PostMapping(value = "/clean_report/detail")
    public Result getCleanReportList(@RequestParam Map<String,Object> cleanReportMap) {
 /*
{"user_id":1,"start_index":0,"count":10,"count_flag":true,"beginTime":"","endTime":""}:
* */
        JSONObject jsonObj=null;
        int lastIndexOf = cleanReportMap.toString().lastIndexOf(":");
        if(lastIndexOf==-1){
            String jsonStr = cleanReportMap.toString().replace("=", ":");
            jsonObj=JSONObject.parseObject(jsonStr);
        }else {
            String subStr = cleanReportMap.toString().substring(0, cleanReportMap.toString().length() - 2);
            jsonObj = JSONObject.parseObject(subStr.substring(1));
        }
        /*2*/
        List<CustomerBean> customerBeans =null;
        JSONObject alarmObject =JSONObject.parseObject(String.valueOf(redisUtil.hget("users",String.valueOf(jsonObj.get("user_id")))));
        CustomerBean customerBean = customerService.selectByPrimaryKey((Integer) alarmObject.get("customerId"));
        /*2*/
        if(customerBean==null){
            customerBeans = customerService.selectBySelective(null, null);
        }else {
            customerBeans = customerService.selectBySelective(null, customerBean.getId());
        }
//        System.out.println("customerBeans.size: "+customerBeans.size());
        List cleanReports=new ArrayList();
        Map<Integer,Object> customerBeanMap=new HashMap<>();
//        List<Map> mapList=new ArrayList<>();
        for (CustomerBean cb:customerBeans ) {
            customerBeanMap.put(cb.getId(),cb);
        }

        Map<Integer,Object> customerDeviceMap=new HashMap<>();
        for (Integer customerId:customerBeanMap.keySet()) {
                List<CustomerDeviceBean> customerDeviceBeans = customerDeviceService.selectCustomerWithDevices(customerId);
            if(customerDeviceBeans.size()>0){
                for (CustomerDeviceBean cbd:customerDeviceBeans) {
                    Map<String,Object> paramMap=new HashMap<>();
                    paramMap.put("device_serial",cbd.getDeviceInfoBean().getSerial());
                    paramMap.put("device_name",cbd.getDeviceInfoBean().getName());
                    paramMap.put("device_type_id",cbd.getDeviceInfoBean().getTypeId());
                    paramMap.put("device_id",cbd.getDeviceInfoBean().getId());
                    customerDeviceMap.put(cbd.getDeviceInfoBean().getId(),paramMap);
                }
            }
        }
        Map<Integer,Object> resultMap=new HashMap<>();
        for (Map.Entry<Integer,Object> entry : customerDeviceMap.entrySet()) {
            //获取所有所有匹配的客户下的所有设备
            JSONObject jsonMap = JSONObject.parseObject(JSON.toJSONString(entry.getValue()));
            List<CleanReportBean>  cleanReportLst = cleanReportService.selectSelective((Integer) jsonMap.get("device_id"),0, 0);
            if(cleanReportLst.size()>0){
                resultMap.put(entry.getKey(),cleanReportLst);
            }
        }
        for (Map.Entry<Integer,Object> entry : resultMap.entrySet()) {
            System.out.println(entry.getValue());
        }

//        System.out.println("cleanReportList: "+cleanReportList.size());
//        System.out.println("futures: "+resultMap.size());
        /*for (Map.Entry<Integer,Object> entry : resultMap.entrySet()) {
            JSONObject cleanMap = JSONObject.parseObject(JSON.toJSONString(entry.getValue()));
//            System.out.println("key = " + entry.getKey()+", value" +jsonMap);
            MapsBean mapsBean =mapsService.selectByIdAndDeviceId((Integer) cleanMap.get("map_id"),(Integer)cleanMap.get("device_id"));
            Map<String,Object> beanMap=new HashMap<>();
            if(mapsBean!=null){
//                            通过比较线上的地图name来获取指定的地图
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
            beanMap.put("update_time",cleanMap.get("update_time"));
            beanMap.put("clean_area",cleanMap.get("clean_area"));
            beanMap.put("id",cleanMap.get("id"));
            beanMap.put("name",cleanMap.get("name"));
            beanMap.put("type",cleanMap.get("type"));
            beanMap.put("use_time",cleanMap.get("use_time"));
            beanMap.put("report",cleanMap.get("report"));
            beanMap.put("device_serial",cleanMap.get("device_serial"));
            beanMap.put("device_name",cleanMap.get("device_name"));
            beanMap.put("device_type_id",cleanMap.get("device_type_id"));
            beanMap.put("device_id",cleanMap.get("device_id"));
            cleanReportList.add(beanMap);
        }*/


        /*    Map<String,Object> maps=new HashMap<>();
            maps.put("count",cleanReportLst.size());
            maps.put("reports",cleanReportLst);*/
            return  Result.ok().msg("");
    }


    /**
     * @param
     * @param
     * @return Result
     * @description 获取指定设备下的清洁报告
     **/
    @PostMapping(value = "/clean_report/info")
    public Result getCleanReport(@RequestParam Map<String,Object> cleanReportMap) {
logger.info("获取指定设备下的清洁报告:"+cleanReportMap);



        JSONObject jsonObj=null;
        int lastIndexOf = cleanReportMap.toString().lastIndexOf(":");
        if(lastIndexOf==-1){
            String jsonStr = cleanReportMap.toString().replace("=", ":");
            jsonObj=JSONObject.parseObject(jsonStr);
        }else {
            String subStr = cleanReportMap.toString().substring(0, cleanReportMap.toString().length() - 2);
            jsonObj = JSONObject.parseObject(subStr.substring(1));
        }

        DeviceInfoBean deviceInfoBean = deviceService.selectByIdOrName(null, String.valueOf(jsonObj.get("device_name")));
        if(deviceInfoBean==null){
            return  Result.ok().msg("该设备不存在");
        }
        List<CleanReportBean> cleanReportBeans = cleanReportService.selectSelective(deviceInfoBean.getId(),(Integer) jsonObj.get("start_index"),(Integer) jsonObj.get("count"));
        List<CleanReportBean> cleanReportLsts = cleanReportService.selectSelective(deviceInfoBean.getId(), 0, 0);
        if(cleanReportBeans.size()<0){
            return  Result.ok().msg("该设备没有清洁报告");
        }
        List cleanReportLists=new ArrayList();
        for (CleanReportBean crb:cleanReportBeans) {
            MapsBean mapsBean = mapsService.selectByIdAndDeviceId(crb.getMapId(),crb.getDeviceId());
            Map<String,Object> beanMap=new HashMap<>();
            if(mapsBean!=null){
//                            通过比较线上的地图name来获取指定的地图
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
            beanMap.put("report",JSONObject.parseObject(crb.getReport()));
            beanMap.put("device_serial",deviceInfoBean.getSerial());
            beanMap.put("device_name",deviceInfoBean.getName());
            beanMap.put("device_type_id",deviceInfoBean.getTypeId());
            beanMap.put("device_id",deviceInfoBean.getId());
            cleanReportLists.add(beanMap);
        }
        Map<String,Object> map=new HashMap<>();
        map.put("count",cleanReportLsts.size());
        map.put("reports",cleanReportLists);
        return  Result.ok().data(map).msg("");
    }

    /**
     * @param
     * @param
     * @return Result
     * @description 告警状态设置
     **/
    @PostMapping(value = "/alarm/set_status")
    public Result  setStatus(@RequestParam Map<String,Object> alarmMap) {

        JSONObject jsonObj=null;
        int lastIndexOf = alarmMap.toString().lastIndexOf(":");
        if(lastIndexOf==-1){
            String jsonStr = alarmMap.toString().replace("=", ":");
            jsonObj=JSONObject.parseObject(jsonStr);
        }else {
            String subStr = alarmMap.toString().substring(0, alarmMap.toString().length() - 2);
            jsonObj = JSONObject.parseObject(subStr.substring(1));
        }

        JSONArray jsonArray = new JSONArray().fromObject(jsonObj.get("ids"));
        AlarmBean alarmBean = alarmService.selectByPrimaryKey((Integer) jsonArray.get(0));
        System.out.println("alarmBean: "+alarmBean);
        if(alarmBean==null){
            return Result.error().msg("");
        }
        return Result.ok().msg("");
    }

    /**
     * @param
     * @param
     * @return Result
     * @description 获取今天的任务信息
     **/
    @PostMapping(value = "/task/today_task_info")
    public Result  getTodayTaskInfo(Integer user_id) {

        /*步骤：
         * 1.通过用户id获取到指定客户，然后通过客户id获取到指定客户下的所有设备，并通过设备id获取到指定设备名下的所有任务
         * 2.如果用户id对应的客户id不存在,则查询所有客户信息
         * 3.
         * */

        return null;
    }
 }
