package com.iclean.pt.sbgl.service.Imp;

import com.iclean.pt.sbgl.bean.MapsBean;
import com.iclean.pt.sbgl.dao.MapsBeanMapper;
import com.iclean.pt.sbgl.service.MapsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MapsServiceImpl implements MapsService {

    @Autowired
    private MapsBeanMapper mapsBeanMapper;

    @Override
    public List<MapsBean> selectByDeviceId(Integer deviceId,Integer startIndex,Integer count) {
        return mapsBeanMapper.selectByDeviceId(deviceId,startIndex,count);
    }

    @Override
    public int deleteByPrimaryKey(Integer id) {
        return mapsBeanMapper.deleteByPrimaryKey(id);
    }

    @Override
    public MapsBean selectBySelective(Integer deviceId, String name) {
        return mapsBeanMapper.selectBySelective(deviceId,name);
    }

    @Override
    public MapsBean selectByPrimaryKey(Integer id) {
        return mapsBeanMapper.selectByPrimaryKey(id);
    }

    @Override
    public MapsBean selectByIdAndDeviceId(Integer id, Integer deviceId) {
        return mapsBeanMapper.selectByIdAndDeviceId(id,deviceId);
    }
}
