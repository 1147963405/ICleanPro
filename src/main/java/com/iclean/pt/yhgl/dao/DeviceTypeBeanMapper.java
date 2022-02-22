package com.iclean.pt.yhgl.dao;

import com.iclean.pt.yhgl.bean.DeviceTypeBean;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface DeviceTypeBeanMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(DeviceTypeBean record);

    int insertSelective(DeviceTypeBean record);

    DeviceTypeBean selectByPrimaryKey(Integer id);
//    List<DeviceTypeBean> selectList(Integer id);
    int updateByPrimaryKeySelective(DeviceTypeBean record);

    int updateByPrimaryKey(DeviceTypeBean record);

//out api
    List<Map<String,Object>> selectDeviceTypeList();
}