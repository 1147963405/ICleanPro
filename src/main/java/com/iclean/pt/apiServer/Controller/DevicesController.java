package com.iclean.pt.apiServer.Controller;

import com.iclean.pt.utils.RedisUtil;
import com.iclean.pt.utils.Result;
import com.iclean.pt.yhgl.bean.CustomerDeviceBean;
import com.iclean.pt.yhgl.bean.DeviceInfoBean;
import com.iclean.pt.yhgl.service.CustomerDeviceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
public class DevicesController {

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private CustomerDeviceService customerDeviceService;


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
        if(redisToken.equals(token)||redisToken==token){
            List<DeviceInfoBean> dib=new ArrayList<>();
            for (CustomerDeviceBean cdb:customerDeviceBeans) {
                dib.add(cdb.getDeviceInfoBean());
            }
            Map<String,Object> map=new HashMap<>();
            map.put("count",dib.size());
            map.put("devices",dib);
            return Result.ok().data(map).msg("");
        }
        return Result.error().msg("the token is not exist or the customer_id is wrong");
    }
}
