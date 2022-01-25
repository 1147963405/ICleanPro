package com.iclean.pt.sbgl.service;

import com.iclean.pt.sbgl.bean.MapsBean;
import com.iclean.pt.sbgl.bean.PathsBean;

import java.util.List;

public interface MapsService {

    /*通过设备id获取到指定设备下的所有地图*/
    List<MapsBean> selectByDeviceId(Integer deviceId,Integer startIndex,Integer count);
    /*通过设备id删除地图*/
    int deleteByPrimaryKey(Integer id);
   /*通过设备id和地图名称获取哦指定地图信息*/
    MapsBean selectBySelective(Integer deviceId,String name);
    MapsBean selectByPrimaryKey(Integer id);
    MapsBean selectByIdAndDeviceId(Integer id,Integer deviceId);


}
