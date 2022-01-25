package com.iclean.pt.sbgl.service;

import com.iclean.pt.sbgl.bean.EventsBean;

import java.util.List;

public interface EventsService {
    List<EventsBean> selectBySelective(Integer deviceId,Integer status,Integer startIndex,Integer count);
}
