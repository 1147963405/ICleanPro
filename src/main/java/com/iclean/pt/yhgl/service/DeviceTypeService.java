package com.iclean.pt.yhgl.service;

import com.iclean.pt.yhgl.bean.DeviceTypeBean;

import java.util.List;
import java.util.Map;

public interface DeviceTypeService {
    /*通过id获取设备类型列表*/
//    List<DeviceTypeBean> selectList(Integer id);
    /*通过id获取设备信息*/
    DeviceTypeBean selectByPrimaryKey(Integer id);

    List<Map<String,Object>> selectDeviceTypeList();
}
