package com.iclean.pt.yhgl.dao;

import com.iclean.pt.yhgl.bean.DeviceInfoBean;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface DeviceInfoBeanMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(DeviceInfoBean record);

    int insertSelective(DeviceInfoBean record);

    DeviceInfoBean selectByPrimaryKey( Integer id);
    DeviceInfoBean selectBySerialOrName( @Param("serial") String serial,@Param("name") String name);
    DeviceInfoBean selectByIdOrName( @Param("id") Integer id,@Param("name") String name);
    List<DeviceInfoBean> selectBySelective(@Param("typeId") Integer typeId,@Param("status") Integer status,@Param("name") String name,@Param("serial") String serial);
    List<DeviceInfoBean> selectList();
    List<DeviceInfoBean> selectSelective(@Param("serial")String serial,@Param("typeId")Integer typeId,@Param("status")String status,@Param("name")String name);
    int updateByPrimaryKeySelective(DeviceInfoBean record);

    int updateByPrimaryKey(DeviceInfoBean record);


 /*   List<Map<String,Object>> selectDeviceByCustomerId(Integer customerId);
    List<Map<String,Object>> selectDevices();*/



    List<Map<String,Object>> selectDevicesByCustomerId(@Param("customerId") Integer customerId);
    List<Map<String,Object>> selectDevicesByParams(@Param("customerId") Integer customerId,@Param("devicesParams") String params);
    List<Map<String,Object>> selectDevices();
    List<Map<String,Object>> selectDevicesBySelective(@Param("devicesParams") String params);

}