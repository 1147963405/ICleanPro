package com.iclean.pt.sbgl.controller;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.PropertyNamingStrategy;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.google.gson.Gson;
import com.iclean.pt.sbgl.bean.MapsBean;
import com.iclean.pt.sbgl.bean.MapsDeviceBean;
import com.iclean.pt.sbgl.bean.PathsBean;
import com.iclean.pt.sbgl.bean.PositionBean;
import com.iclean.pt.sbgl.service.MapsService;
import com.iclean.pt.sbgl.service.PathsService;
import com.iclean.pt.sbgl.service.PositionService;
import com.iclean.pt.utils.Constants;
import com.iclean.pt.utils.RedisUtil;
import com.iclean.pt.utils.Result;
import com.iclean.pt.yhgl.bean.CustomerBean;
import com.iclean.pt.yhgl.bean.CustomerDeviceBean;
import com.iclean.pt.yhgl.bean.DeviceInfoBean;
import com.iclean.pt.yhgl.service.CustomerDeviceService;
import com.iclean.pt.yhgl.service.CustomerService;
import com.iclean.pt.yhgl.service.DeviceService;
import com.iclean.pt.yhgl.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

import static java.lang.Integer.parseInt;

@RestController
//@Api(tags = "设备管理相关功能模块" )
//@CrossOrigin(origins = "*", methods = {RequestMethod.GET, RequestMethod.POST})
//@RequestMapping("/iclean-cloud")
public class MapsController {

     static SerializeConfig config=new SerializeConfig() ;

    @Autowired
    private MapsService mapsService;
    @Autowired
    private UserService userService;
    @Autowired
    private CustomerService customerService;
    @Autowired
    private CustomerDeviceService customerDeviceService;
    @Autowired
    private PathsService pathsService;
    @Autowired
    private DeviceService deviceService;
    @Autowired
    private PositionService positionService;
    @Autowired
    private RedisUtil redisUtil;

    /**
     * @param
     * @param
     * @return Result
     * @description 获取地图列表
     **/
    @RequestMapping(value = "/map/lists",method = RequestMethod.POST)
    public Result getMapsList( @RequestParam  Map<String,Object> mp) {
        /*{"user_id":1,"start_index":0,"count":10,"count_flag":true}: */
        JSONObject jsonObj=null;
        int lastIndexOf = mp.toString().lastIndexOf(":");
        if(lastIndexOf==-1){
            String jsonStr = mp.toString().replace("=", ":");
            jsonObj=JSONObject.parseObject(jsonStr);
        }else {
            String subStr = mp.toString().substring(0, mp.toString().length() - 2);
            jsonObj = JSONObject.parseObject(subStr.substring(1));
        }
        Object user = redisUtil.hget("users", String.valueOf(jsonObj.get("user_id")));
        JSONObject jsonObject = JSONObject.parseObject((String) user);
        CustomerBean customerBean = customerService.selectByPrimaryKey((Integer) jsonObject.get("customerId"));//通过user获取指定customer客户
        List<CustomerBean> cbs =null;
        if(customerBean==null){
            /*该账户下的客户不能存在,则查询所有客户*/
            cbs = customerService.selectBySelective(null,null);
//             System.out.println("null cbs: "+cbs);
        }else {
            /*该账户下的客户存在,则查询指定客户下的信息*/
            cbs = customerService.selectBySelective(null,customerBean.getId());
//            System.out.println("cbs: "+cbs);
        }
        /*通过获取到的客户信息去关联中间表，查询出匹配的所有设备信息*/
        Map<Integer,Object> maps=new HashMap<>();
        for (CustomerBean cbn:cbs) {
                /*通过客户id获取中间表中与之匹配的所有设备id，如果device相同则只取一次*/
                maps.put(cbn.getId(),cbn);
            }

        Map<Integer,Object> mapsMap=new HashMap<>();
        for (Integer customerId:maps.keySet()) {
            List<CustomerDeviceBean> customerDeviceBeans = customerDeviceService.selectCustomerWithDevices(customerId);
            if (customerDeviceBeans.size() > 0) {
                for (CustomerDeviceBean cbd : customerDeviceBeans) {
                    mapsMap.put(cbd.getDeviceInfoBean().getId(), cbd.getDeviceInfoBean().getName());
                }
            }
        }
        List<MapsDeviceBean> mdb=new ArrayList<>();
        for (Map.Entry<Integer,Object> entry : mapsMap.entrySet()) {
            List<MapsBean> mapsBeanList = mapsService.selectByDeviceId(entry.getKey(),0,0);
            for (MapsBean mb : mapsBeanList) {
                MapsDeviceBean mapsDeviceBean = new MapsDeviceBean();
                mapsDeviceBean.setDeviceId(mb.getDeviceId());
                mapsDeviceBean.setDeviceName(String.valueOf( entry.getValue()));
                mapsDeviceBean.setUserId((Integer) jsonObj.get("user_id"));
//                组装图片路径
                String path = Constants.Global.GLOBAL_STATIC_URL.getValue() + Constants.Global.GLOBAL_MAP_PATH.getValue() + "/" + mb.getDeviceId() + "/" + mb.getUuid() + "/map.png";
//                组装下载路径
                String download = Constants.Global.MAP_DOWNLOAD_URL.getValue() + Constants.Global.MAP_DOWNLOAD_PATH.getValue() + "?device_id=" + mb.getDeviceId() + "&uuid=" + mb.getUuid();
                mapsDeviceBean.setPath(path);
                mapsDeviceBean.setDownloadUrl(download);
                mapsDeviceBean.setMapId(mb.getId());
                mapsDeviceBean.setMapName(mb.getName());
                mdb.add(mapsDeviceBean);
            }
        }
        int start_index = parseInt(String.valueOf(jsonObj.get("start_index")));
        int count = parseInt(String.valueOf(jsonObj.get("count")));
        /*分页*/
        List<MapsDeviceBean> jsonBeanList = null;
        if (start_index==0) {
            if(count > mdb.size()){
                jsonBeanList = mdb.subList(start_index, mdb.size()); //subList(int fromIndex, int toIndex);
            }else {
                jsonBeanList = mdb.subList(start_index, count);
            }
        } else {
            int Tcount = count + start_index;
            if (Tcount >mdb.size()) {
                jsonBeanList = mdb.subList(start_index, mdb.size());
            } else {
                jsonBeanList = mdb.subList(start_index, Tcount);
            }

        }
        /*组装JSON返回对象*/
        Map<String,Object> map=new HashMap<>();
        map.put("count",mdb.size());
        map.put("map",jsonBeanList);
        config.propertyNamingStrategy = PropertyNamingStrategy.SnakeCase;
        String json = JSON.toJSONString(map, config);
        JSONObject jsonMap = JSONObject.parseObject(json);//json转map
        return Result.ok().data(jsonMap).msg("");
    }

    /**
     * @param
     * @param
     * @return Result
     * @description 获取指定地图列表
     **/
    @RequestMapping(value = "/map/list",method = RequestMethod.POST)
    public Result getMapInfo( @RequestParam  Map<String,Object> mp) {
        /*{"user_id":1,"start_index":0,"robot_name":"南京国金中心1","count":100,"count_flag":true}: */
        JSONObject jsonObj=null;
        int lastIndexOf = mp.toString().lastIndexOf(":");
        if(lastIndexOf==-1){
            String jsonStr = mp.toString().replace("=", ":");
            jsonObj=JSONObject.parseObject(jsonStr);
        }else {
            String subStr = mp.toString().substring(0, mp.toString().length() - 2);
            jsonObj = JSONObject.parseObject(subStr.substring(1));
        }
        DeviceInfoBean deviceInfoBean = deviceService.selectByIdOrName(null, String.valueOf(jsonObj.get("robot_name")));
        if(deviceInfoBean==null){
            return  Result.ok().msg("该设备不存在");
        }
        List<MapsBean> mapsBeanLists = mapsService.selectByDeviceId(deviceInfoBean.getId(),0,0);
        if(mapsBeanLists.size()<0){
            return  Result.ok().msg("该设备不存在地图");
        }
        List<MapsDeviceBean> mdb=new ArrayList<>();
        for (MapsBean mb : mapsBeanLists) {
            MapsDeviceBean mapsDeviceBean = new MapsDeviceBean();
            mapsDeviceBean.setDeviceId(mb.getDeviceId());
            mapsDeviceBean.setDeviceName(deviceInfoBean.getName());
            mapsDeviceBean.setUserId((Integer) jsonObj.get("user_id"));
//                组装图片路径
            String path = Constants.Global.GLOBAL_STATIC_URL.getValue() + Constants.Global.GLOBAL_MAP_PATH.getValue() + "/" + mb.getDeviceId() + "/" + mb.getUuid() + "/map.png";
//                组装下载路径
            String download = Constants.Global.MAP_DOWNLOAD_URL.getValue() + Constants.Global.MAP_DOWNLOAD_PATH.getValue() + "?device_id=" + mb.getDeviceId() + "&uuid=" + mb.getUuid();
            mapsDeviceBean.setPath(path);
            mapsDeviceBean.setDownloadUrl(download);
            mapsDeviceBean.setMapId(mb.getId());
            mapsDeviceBean.setMapName(mb.getName());
            mdb.add(mapsDeviceBean);
        }

        Map<String,Object> mmap=new HashMap<>();
        mmap.put("count",mapsBeanLists.size());
        mmap.put("map",mdb);
        config.propertyNamingStrategy = PropertyNamingStrategy.SnakeCase;
        String json = JSON.toJSONString(mmap, config);
        JSONObject jsonMap = JSONObject.parseObject(json);
        return  Result.ok().data(jsonMap).msg("");

    }

    /**
     * @param
     * @param
     * @return Result
     * @description 获取路径列表
     **/
    @GetMapping(value = "/path/lists")
    public JSONObject getPathsList( Integer map_id, Integer device_id) {
/*map_id: 2
device_id: 22*/
//    System.out.println("map_id: "+map_id +" device_id: " +device_id);
        List<PathsBean> pathsBeans = pathsService.selectByMapIdAndDeviceId(map_id, device_id);
        List lst=new ArrayList();
        if(pathsBeans.size()<=0){
            return null;
        }
        for (PathsBean pb:pathsBeans) {
            Map<String,Object> map=new HashMap<>();
            map.put("id",pb.getId());
            map.put("name",pb.getName());
            map.put("type",pb.getType());
            map.put("path",JSONObject.parse( pb.getPath()));
            lst.add(map);
        }
        /*組裝JSON格式*/
        String toJson = new Gson().toJson(lst);
        String jsonString="{ successed : true , errorCode: 200, msg: '' ,data:" +toJson+ "}";
        JSONObject jsonObjec = JSONObject.parseObject(jsonString);
//        System.out.println("jsonObjec: "+jsonObjec);
        return jsonObjec;
    }

    /**
     * @param
     * @param
     * @return Result
     * @description 删除地图
     **/
    @GetMapping(value = "/map/delete")
    public Result deleteMaps(Integer map_id, Integer device_id) {

        /*步骤
        * 1.先通过map_id和device_id删除指定地图路径
        * 2.再通过map_id删除设备指定地图
        * */
        int mapCount = mapsService.deleteByPrimaryKey(device_id);
        List<PathsBean> pathsBeans = pathsService.deleteByMapIdAndDeviceId(map_id, device_id);
        if(mapCount==0 && pathsBeans.size()<=0){

            return  Result.ok().msg("删除地图成功");
        }
        return  Result.ok().msg("删除地图失败");
    }
    /**
     * @param
     * @param
     * @return Result
     * @description 删除路径
     **/
    @GetMapping(value = "/path/delete")
    public Result deletePaths(@RequestParam Map<String,Object> map) {


//       通过路径编号、路径name、mapname、设备id删除指定路径
//        Object msgex = MSGEX_map.get("msgex");
//        JSONObject jb = JSON.parseObject(msgex.toString());
//        通过mqtt拿到map_id和path_name获取指定地图路径
//        PathsBean pathsBean = pathsService.selectByPrimaryKey(null, String.valueOf(jb.get("origin_name")));
        /*PathsBean pathsBean = pathsService.selectByPrimaryKey(null, String.valueOf(map.get("name")));
        if(pathsBean==null){
            return Result.ok().msg("该路径不存在");
        }*/
//        System.out.println("map: "+map);
        MapsBean mapsBean = mapsService.selectBySelective((Integer) map.get("device_id"),  String.valueOf(map.get("map_name")));
//        Object deviceId = map.get("device_id");
//        System.out.println("mapsBean: "+mapsBean);
        int id = parseInt(String.valueOf(map.get("id")));
        int deviceId = parseInt(String.valueOf( map.get("device_id")));
        String name = String.valueOf(map.get("name"));
//        System.out.println("id: "+id+"  deviceid: "+deviceId+"  name: "+name );
        int count = pathsService.deleteByPrimaryKey(id, name, mapsBean.getId(), deviceId);
        if(count<0){
            return Result.ok().msg("删除路径失败");
        }
        return Result.ok().msg("删除路径成功");
    }

    /**
     * @param
     * @param
     * @return Result
     * @description 获取点列表
     **/
    @GetMapping(value = "/position/lists")
    public JSONObject getPoints(@RequestParam Map<String,Object> map) {
        /*{"data":[{"type":7,"name":"2025","angle":237,"gridx":302,"gridy":494}],"map_name":"20211207Test","device_id":106,"obj":13,"cmd":0}*/
//        System.out.println("mapId: "+mapId+ "  deviceId: "+deviceId);
        /*回调：type，name，point*/
        int mapId = parseInt(String.valueOf(map.get("map_id")));
        int deviceId = parseInt(String.valueOf( map.get("device_id")));
        List lst=new ArrayList();
        List<PositionBean> positionBeans = positionService.selectBySelective(mapId, deviceId);
        for (PositionBean pb:positionBeans) {
            Map<String,Object> mp=new HashMap<>();
            mp.put("name",pb.getName());
            mp.put("type",pb.getType());
            mp.put("point",JSONObject.parse(pb.getPoint()));
            lst.add(mp);
        }
//        System.out.println("positionBeans: "+positionBeans);
        if(lst.size()<=0){
            String jsonString="{ successed : false , errorCode: 201, msg: '' ,data:'{}'}";
            JSONObject jsonObjec = JSONObject.parseObject(jsonString);
            return jsonObjec;
        }

        /*組裝JSON格式*/
        String toJson = new Gson().toJson(lst);
        String jsonString="{ successed : true , errorCode: 200, msg: '' ,data:" +toJson+ "}";
        JSONObject jsonObjec = JSONObject.parseObject(jsonString);
//        System.out.println("jsonObjec: "+jsonObjec);
        return jsonObjec;
    }
    /**
     * @param
     * @param
     * @return Result
     * @description 删除点
     **/
    @GetMapping(value = "/position/delete")
    @ResponseBody
    public Result deletePoints(@RequestParam Integer id) {

        int count = positionService.deleteByPrimaryKey(id);
        if(count<0){

            return Result.ok().msg("删除点失败");
        }
        return Result.ok().msg("删除点成功");
    }

    /**
     * @param
     * @param
     * @return Result
     * @description 地图共享
     **/
    @PostMapping(value = "/data/map_shared")
    public Result getMapShared(@RequestParam Map<String,Object> map) {
/*{"devices":[1,2],"map_id":12,"device_id":48}: */
        /*步骤
        1.通过device_id获取指定设备信息
        2.通过device_id获取到匹配的地图列表
        3.通过map_id与获取到的地图列表比对，获取到指定地图
        4.
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
//        System.out.println("获取地图列表根据条件mmp: "+deviceMap);
        int mapId = parseInt(String.valueOf(jsonObj.get("map_id")));
        int deviceId = parseInt(String.valueOf(jsonObj.get("device_id")));

       /* int mapId = parseInt(String.valueOf(map.get("map_id")));
        int deviceId = parseInt(String.valueOf( map.get("device_id")));
        String devices = String.valueOf(map.get("devices"));*/
        /*1*/
        DeviceInfoBean deviceInfoBean = deviceService.selectByIdOrName(deviceId, "");
        if(deviceInfoBean==null){
            return Result.error().msg("该设备不存在");
        }
        /*2*/
        List<MapsBean> mapsBeans = mapsService.selectByDeviceId(deviceId,0,0);
        for (MapsBean mapBean:mapsBeans) {
            if(mapBean.getId()==mapId){
                /*组装地图路径*/
//                String path = Constants.Global.GLOBAL_STATIC_URL.getValue() + Constants.Global.GLOBAL_MAP_PATH.getValue() + "/" + mapBean.getDeviceId() + "/" + mapBean.getUuid() + "/map.png";
                return Result.ok().msg("");
          }
        }
        return null;
    }
    /**
     * @param
     * @param
     * @return Result
     * @description 地图下载
     **/
    @GetMapping(value = "/data/download/map")
    public Result downloadMap(String uuid, Integer device_id) {
        /*device_id: 54
           uuid: a4e5e695-ebd0-4ec6-95fa-928e5b230d2d */
        /*步骤
        1.通过device_id获取指定设备信息
        2.通过device_id获取到匹配的地图列表
        3.通过map_id与获取到的地图列表比对，获取到指定地图
        4.
        * */
       /* int deviceId = parseInt(String.valueOf(map.get("device_id")));
        String uuid = String.valueOf(map.get("uuid"));*/
        /*1*/
        DeviceInfoBean deviceInfoBean = deviceService.selectByIdOrName(device_id, "");
        if(deviceInfoBean==null){
            return Result.error().msg("该设备不存在");
        }
        /*2*/
        List<MapsBean> mapsBeans = mapsService.selectByDeviceId(device_id,0,0);
        String download ="";
        for (MapsBean mapBean:mapsBeans) {
            if(mapBean.getUuid().equals(uuid)||mapBean.getUuid()==uuid){
//                组装下载路径
               download = Constants.Global.MAP_DOWNLOAD_URL.getValue() + Constants.Global.MAP_DOWNLOAD_PATH.getValue() + "?device_id=" + mapBean.getDeviceId() + "&uuid=" + mapBean.getUuid();
            }
        }
        return Result.ok().data("download_url",download).msg("地图下载成功");
    }
}
