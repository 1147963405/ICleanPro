package com.iclean.pt.yhgl.dao;

import com.iclean.pt.yhgl.bean.DeviceInfoBean;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface DeviceInfoBeanMapper {

    int insert(DeviceInfoBean record);
    int insertSelective(DeviceInfoBean record);
    int deleteByPrimaryKey(Integer id);
    int updateByPrimaryKeySelective(DeviceInfoBean record);
    int updateByPrimaryKey(DeviceInfoBean record);

    Map<String,Object> selectByPrimaryKey( Integer id);
    DeviceInfoBean selectBySelective(@Param("params")String params);
    List<Map<String,Object>> selectDevicesByCustomerId(@Param("customerId") Integer customerId);
    List<Map<String,Object>> selectDevicesByParams(@Param("customerId") Integer customerId,@Param("devicesParams") String params);
    List<Map<String,Object>> selectDevices();
    List<Map<String,Object>> selectDevicesBySelective(@Param("devicesParams") String params);



}