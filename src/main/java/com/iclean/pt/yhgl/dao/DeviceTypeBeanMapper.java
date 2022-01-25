package com.iclean.pt.yhgl.dao;

import com.iclean.pt.yhgl.bean.DeviceTypeBean;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface DeviceTypeBeanMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(DeviceTypeBean record);

    int insertSelective(DeviceTypeBean record);

    DeviceTypeBean selectByPrimaryKey(Integer id);
    List<DeviceTypeBean> selectList(Integer id);
    int updateByPrimaryKeySelective(DeviceTypeBean record);

    int updateByPrimaryKey(DeviceTypeBean record);
}