package com.iclean.pt.sbgl.dao;

import com.iclean.pt.sbgl.bean.EventsBean;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface EventsBeanMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(EventsBean record);

    int insertSelective(EventsBean record);

    EventsBean selectByPrimaryKey(Integer id);
    List<EventsBean> selectBySelective(Integer deviceId,Integer status,Integer startIndex,Integer count);
    int updateByPrimaryKeySelective(EventsBean record);

    int updateByPrimaryKey(EventsBean record);
}