package com.iclean.pt.sbgl.service.Imp;

import com.iclean.pt.sbgl.bean.PathsBean;
import com.iclean.pt.sbgl.dao.PathsBeanMapper;
import com.iclean.pt.sbgl.service.PathsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class PathsServiceImp implements PathsService {

    @Autowired
    private PathsBeanMapper pathsBeanMapper;
    @Override
    public List<PathsBean> selectBySelective(Integer mapId) {
        return pathsBeanMapper.selectBySelective(mapId);
    }

    @Override
    public List<PathsBean> selectByMapIdAndDeviceId(Integer mapId, Integer deviceId) {
        return pathsBeanMapper.selectByMapIdAndDeviceId(mapId,deviceId);
    }

    @Override
    public List<PathsBean> deleteByMapIdAndDeviceId(Integer mapId, Integer deviceId) {
        return pathsBeanMapper.deleteByMapIdAndDeviceId(mapId,deviceId);
    }

    @Override
    public int insert(PathsBean record) {
        return pathsBeanMapper.insert(record);
    }

    @Override
    public int updateByPrimaryKey(PathsBean record) {
        return pathsBeanMapper.updateByPrimaryKey(record);
    }

    @Override
    public PathsBean selectByPrimaryKey(Integer id, String name) {
        return pathsBeanMapper.selectByPrimaryKey(id,name);
    }

    @Override
    public int deleteByPrimaryKey(Integer id, String name, Integer mapId, Integer deviceId) {
        return pathsBeanMapper.deleteByPrimaryKey(id,name,mapId,deviceId);
    }
}
