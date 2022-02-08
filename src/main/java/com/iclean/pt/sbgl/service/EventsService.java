package com.iclean.pt.sbgl.service;

import com.iclean.pt.sbgl.bean.EventsBean;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface EventsService {
    List<EventsBean> selectBySelective(Integer deviceId,Integer status,Integer startIndex,Integer count);


    List<Map<String,Object>> selectEventsByUserId(@Param("userId") Integer userId);
    List<Map<String,Object>> selectEventsByParams(@Param("userId") Integer userId,@Param("params") String params);
    List<Map<String,Object>> selectEvents();
    List<Map<String,Object>> selectEventsBySelective(@Param("params") String params);
}
