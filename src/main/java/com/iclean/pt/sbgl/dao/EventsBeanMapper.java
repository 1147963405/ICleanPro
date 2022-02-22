package com.iclean.pt.sbgl.dao;

import com.iclean.pt.sbgl.bean.EventsBean;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface EventsBeanMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(EventsBean record);

    int insertSelective(EventsBean record);

    EventsBean selectByPrimaryKey(Integer id);
    List<EventsBean> selectBySelective(Integer deviceId, Integer status, Integer startIndex, Integer count);
    int updateByPrimaryKeySelective(EventsBean record);

    int updateByPrimaryKey(EventsBean record);

//云平台 小程序
    List<Map<String,Object>> selectEventsByUserId(@Param("userId") Integer userId);
    List<Map<String,Object>> selectEventsByParams(@Param("userId") Integer userId, @Param("eventsParams") String params);
    List<Map<String,Object>> selectEvents();
    List<Map<String,Object>> selectEventsBySelective(@Param("eventsParams") String params);

//out api
List<Map<String,Object>> selectEventByDeviceId(Integer deviceId);
}