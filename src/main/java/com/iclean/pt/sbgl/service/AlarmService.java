package com.iclean.pt.sbgl.service;

import com.iclean.pt.sbgl.bean.AlarmBean;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface AlarmService {
    /*通过条件获取告警列表*/
    List<AlarmBean> selectBySelective(Integer deviceId,Integer status,Integer startIndex,Integer count);
    AlarmBean selectByPrimaryKey(Integer id);
    List<AlarmBean> selectList(Integer status,Integer startIndex,Integer count);
//    Integer getCount(Integer deviceId,Integer status);

    List<Map<String,Object>> selectAlarmByUserId(@Param("userId") Integer userId);
    List<Map<String,Object>> selectAlarmsByParams(@Param("userId") Integer userId,@Param("alarmsParams") String params);
    List<Map<String,Object>> selectAlarms();
    List<Map<String,Object>> selectAlarmsBySelective(@Param("alarmsParams") String params);
}
