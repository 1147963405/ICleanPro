package com.iclean.pt.apiServer.Controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.iclean.pt.sbgl.bean.JsonBean;
import com.iclean.pt.sbgl.controller.DeviceController;
import com.iclean.pt.utils.RedisUtil;
import com.iclean.pt.utils.Result;
import com.iclean.pt.yhgl.bean.CustomerDeviceBean;
import com.iclean.pt.yhgl.bean.DeviceInfoBean;
import com.iclean.pt.yhgl.bean.DeviceTypeBean;
import com.iclean.pt.yhgl.service.CustomerDeviceService;
import com.iclean.pt.yhgl.service.DeviceTypeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
public class DevicesController {

    private final static Logger logger = LoggerFactory.getLogger(DevicesController.class);

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private CustomerDeviceService customerDeviceService;
    @Autowired
    private DeviceTypeService deviceTypeService;


    /**
     * @param customer_id
     * @return Result
     * @description 通过客户id获取设备列表
     **/
    @RequestMapping(value = "/apiSever/device/info", method = RequestMethod.POST)
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
                System.out.println("json:"+devices);
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
}
