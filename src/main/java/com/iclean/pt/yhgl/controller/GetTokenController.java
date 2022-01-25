package com.iclean.pt.yhgl.controller;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.iclean.pt.utils.Constants;
import com.iclean.pt.utils.RedisUtil;
import com.iclean.pt.utils.Result;
import com.iclean.pt.utils.TokenUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.Jedis;

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
        redisUtil.expire(String.valueOf(customer_id),7200);
        Map<String,Object> map=new HashMap<>();
        map.put("customer_id",customer_id);
        map.put("token",token);
        return Result.ok().data(map).msg("");
    }
}
