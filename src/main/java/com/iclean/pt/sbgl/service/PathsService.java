package com.iclean.pt.sbgl.service;

import com.iclean.pt.sbgl.bean.PathsBean;

import java.util.List;

public interface PathsService {
    List<PathsBean> selectBySelective(Integer mapId);
    List<PathsBean> selectByMapIdAndDeviceId(Integer mapId,Integer deviceId);
    List<PathsBean> deleteByMapIdAndDeviceId(Integer mapId,Integer deviceId);
    /*新添路径*/
    int insert(PathsBean record);
    /*更新路径信息*/
    int updateByPrimaryKey(PathsBean record);
    PathsBean selectByPrimaryKey(Integer id,String name);
    /*通过指定参数删除指定路径*/
    int deleteByPrimaryKey(Integer id,String name,Integer mapId,Integer deviceId);
}
