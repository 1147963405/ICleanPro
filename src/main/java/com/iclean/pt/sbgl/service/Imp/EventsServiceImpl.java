package com.iclean.pt.sbgl.service.Imp;

import com.iclean.pt.sbgl.bean.EventsBean;
import com.iclean.pt.sbgl.dao.EventsBeanMapper;
import com.iclean.pt.sbgl.service.EventsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class EventsServiceImpl implements EventsService {

    @Autowired
    private EventsBeanMapper eventsBeanMapper;

    @Override
    public List<EventsBean> selectBySelective(Integer deviceId,Integer status,Integer startIndex,Integer count) {
        return eventsBeanMapper.selectBySelective(deviceId,status,startIndex,count);
    }

    @Override
    public List<Map<String, Object>> selectEventsByUserId(Integer userId) {
        return eventsBeanMapper.selectEventsByUserId(userId);
    }

    @Override
    public List<Map<String, Object>> selectEventsByParams(Integer userId, String params) {
        return eventsBeanMapper.selectEventsByParams(userId,params);
    }

    @Override
    public List<Map<String, Object>> selectEvents() {
        return eventsBeanMapper.selectEvents();
    }

    @Override
    public List<Map<String, Object>> selectEventsBySelective(String params) {
        return eventsBeanMapper.selectEventsBySelective(params);
    }

    @Override
    public List<Map<String, Object>> selectEventByDeviceId(Integer deviceId) {
        return eventsBeanMapper.selectEventByDeviceId(deviceId);
    }


}
