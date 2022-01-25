package com.iclean.pt.sbgl.service.Imp;

import com.iclean.pt.sbgl.bean.AlarmBean;
import com.iclean.pt.sbgl.dao.AlarmBeanMapper;
import com.iclean.pt.sbgl.service.AlarmService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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


}
