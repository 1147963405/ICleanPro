package com.iclean.pt.sbgl.service.Imp;

import com.iclean.pt.sbgl.bean.EventsBean;
import com.iclean.pt.sbgl.dao.EventsBeanMapper;
import com.iclean.pt.sbgl.service.EventsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EventsServiceImpl implements EventsService {

    @Autowired
    private EventsBeanMapper eventsBeanMapper;

    @Override
    public List<EventsBean> selectBySelective(Integer deviceId,Integer status,Integer startIndex,Integer count) {
        return eventsBeanMapper.selectBySelective(deviceId,status,startIndex,count);
    }
}
