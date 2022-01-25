package com.iclean.pt.sbgl.dao;

import com.iclean.pt.sbgl.bean.MapsBean;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface MapsBeanMapper {

    int deleteByPrimaryKey(Integer id);

    int insert(MapsBean record);

    int insertSelective(MapsBean record);

    MapsBean selectBySelective(Integer deviceId,String name);
    MapsBean selectByPrimaryKey(Integer id);
    MapsBean selectByIdAndDeviceId(Integer id,Integer deviceId);
    int updateByPrimaryKeySelective(MapsBean record);

    int updateByPrimaryKey(MapsBean record);

    /*通过设备id获取到指定设备下的所有地图*/
    List<MapsBean> selectByDeviceId(Integer deviceId,Integer startIndex,Integer count);



}