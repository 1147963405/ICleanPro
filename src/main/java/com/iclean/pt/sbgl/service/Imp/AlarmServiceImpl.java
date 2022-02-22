package com.iclean.pt.sbgl.service.Imp;

import com.iclean.pt.sbgl.bean.AlarmBean;
import com.iclean.pt.sbgl.dao.AlarmBeanMapper;
import com.iclean.pt.sbgl.service.AlarmService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class AlarmServiceImpl implements AlarmService {

    @Autowired
    private AlarmBeanMapper alarmBeanMapper;

    @Override
    public List<AlarmBean> selectBySelective(Integer deviceId,Integer status,Integer startIndex,Integer count) {
        return alarmBeanMapper.selectBySelective(deviceId,status,startIndex,count);
    }

    @Override
    public AlarmBean selectByPrimaryKey(Integer id) {
        return alarmBeanMapper.selectByPrimaryKey(id);
    }

    @Override
    public List<AlarmBean> selectList(Integer status, Integer startIndex, Integer count) {
        return alarmBeanMapper.selectList(status,startIndex,count);
    }

    @Override
    public List<Map<String, Object>> selectAlarmByUserId(Integer userId) {
        return alarmBeanMapper.selectAlarmByUserId(userId);
    }

    @Override
    public List<Map<String, Object>> selectAlarmsByParams(Integer userId, String params) {
        return alarmBeanMapper.selectAlarmsByParams(userId,params);
    }

    @Override
    public List<Map<String, Object>> selectAlarms() {
        return alarmBeanMapper.selectAlarms();
    }

    @Override
    public List<Map<String, Object>> selectAlarmsBySelective(String params) {
        return alarmBeanMapper.selectAlarmsBySelective(params);
    }

    @Override
    public List<Map<String, Object>> selectAlarmByDeviceId(Integer deviceId) {
        return alarmBeanMapper.selectAlarmByDeviceId(deviceId);
    }


}
