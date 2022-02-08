package com.iclean.pt.sbgl.service.Imp;

import com.iclean.pt.sbgl.bean.MapsBean;
import com.iclean.pt.sbgl.dao.MapsBeanMapper;
import com.iclean.pt.sbgl.service.MapsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

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

    @Override
    public List<MapsBean> selectByName(String name) {
        return mapsBeanMapper.selectByName(name);
    }

    @Override
    public List<Map<String, Object>> selectMapsByUserId(Integer userId) {
        return mapsBeanMapper.selectMapsByUserId(userId);
    }

    @Override
    public List<Map<String, Object>> selectMapsByParams(Integer userId, String params) {
        return mapsBeanMapper.selectMapsByParams(userId,params);
    }

    @Override
    public List<Map<String, Object>> selectMaps() {
        return mapsBeanMapper.selectMaps();
    }

    @Override
    public List<Map<String, Object>> selectMapsBySelective(String params) {
        return mapsBeanMapper.selectMapsBySelective(params);
    }


}
