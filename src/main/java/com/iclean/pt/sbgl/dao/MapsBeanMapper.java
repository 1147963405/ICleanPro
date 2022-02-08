package com.iclean.pt.sbgl.dao;

import com.iclean.pt.sbgl.bean.MapsBean;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface MapsBeanMapper {

    int deleteByPrimaryKey(Integer id);

    int insert(MapsBean record);

    int insertSelective(MapsBean record);

    MapsBean selectBySelective(Integer deviceId, String name);
    MapsBean selectByPrimaryKey(Integer id);
    MapsBean selectByIdAndDeviceId(Integer id, Integer deviceId);
    int updateByPrimaryKeySelective(MapsBean record);

    int updateByPrimaryKey(MapsBean record);

    /*通过设备id获取到指定设备下的所有地图*/
    List<MapsBean> selectByDeviceId(Integer deviceId, Integer startIndex, Integer count);
    List<MapsBean> selectByName(String name);


    List<Map<String,Object>> selectMapsByUserId(@Param("userId") Integer userId);
    List<Map<String,Object>> selectMapsByParams(@Param("userId") Integer userId, @Param("mapsParams") String params);
    List<Map<String,Object>> selectMaps();
    List<Map<String,Object>> selectMapsBySelective(@Param("mapsParams") String params);



}