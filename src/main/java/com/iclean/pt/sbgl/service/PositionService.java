package com.iclean.pt.sbgl.service;

import com.iclean.pt.sbgl.bean.PositionBean;

import java.util.List;

public interface PositionService {
    /*通过map_id和device_id获取指定点列表*/
    List<PositionBean> selectBySelective(Integer mapId, Integer deviceId);
    /*通过点id删除指定记录*/
    int deleteByPrimaryKey(Integer id);
}
