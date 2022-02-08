package com.iclean.pt.yhgl.service;

import com.iclean.pt.yhgl.bean.DeviceInfoBean;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface DeviceService {

    /*通过设备id获取设备*/
    List<DeviceInfoBean> selectBySelective(Integer typeId,Integer status,String name,String serial);
    List<DeviceInfoBean> selectSelective(String serial,Integer typeId,String status,String name);
//    DeviceInfoBean selectByPrimaryKey(Integer id);
    DeviceInfoBean selectByPrimaryKey(Integer id);
    DeviceInfoBean selectBySerialOrName(@Param("serial") String serial, @Param("name") String name);
    /*获取设备列表*/
    List<DeviceInfoBean> selectList();
    DeviceInfoBean selectByIdOrName( @Param("id") Integer id,@Param("name") String name);
    int updateByPrimaryKey(DeviceInfoBean record);
/*新增设备*/
    int insert(DeviceInfoBean record);
    /*删除设备*/
    int deleteByPrimaryKey(Integer id);

   /* List<Map<String,Object>> selectDeviceByCustomerId(Integer customerId);
    List<Map<String,Object>> selectDevices();*/

    List<Map<String,Object>> selectDevicesByCustomerId(@Param("customerId") Integer customerId);
    List<Map<String,Object>> selectDevicesByParams(@Param("customerId") Integer customerId,@Param("devicesParams") String params);
    List<Map<String,Object>> selectDevices();
    List<Map<String,Object>> selectDevicesBySelective(@Param("devicesParams") String params);

}
