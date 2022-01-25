package com.iclean.pt.sbgl.service;

import com.iclean.pt.sbgl.bean.AlarmBean;

import java.util.List;

public interface AlarmService {
    /*通过条件获取告警列表*/
    List<AlarmBean> selectBySelective(Integer deviceId,Integer status,Integer startIndex,Integer count);
    AlarmBean selectByPrimaryKey(Integer id);
    List<AlarmBean> selectList(Integer status,Integer startIndex,Integer count);
//    Integer getCount(Integer deviceId,Integer status);
}
