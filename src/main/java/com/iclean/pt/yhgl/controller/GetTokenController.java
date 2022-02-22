package com.iclean.pt.yhgl.controller;


import com.iclean.pt.utils.RedisUtil;
import com.iclean.pt.utils.Result;
import com.iclean.pt.utils.TokenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RestController
public class GetTokenController {

    @Autowired
    RedisUtil redisUtil;


    @RequestMapping(value = "/token/getToken",method = RequestMethod.GET)
//    @ApiOperation("获取token令牌")
    public Result getToken(Integer customer_id){
        long time = new Date().getTime();
        String str=String.valueOf(customer_id)+time;
        String token = TokenUtils.getToken(str);
        /*通过redis缓存登录的用户信息 */
        redisUtil.hset("token",String.valueOf(customer_id),token);
        redisUtil.expire(String.valueOf(customer_id),2626560);//token一个月过期，过期必须重新生成
        Map<String,Object> map=new HashMap<>();
        map.put("customer_id",customer_id);
        map.put("token",token);
        return Result.ok().data(map).msg("");
    }
}
