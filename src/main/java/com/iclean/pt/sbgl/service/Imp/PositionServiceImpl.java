package com.iclean.pt.sbgl.service.Imp;

import com.iclean.pt.sbgl.bean.PositionBean;
import com.iclean.pt.sbgl.dao.PositionBeanMapper;
import com.iclean.pt.sbgl.service.PositionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PositionServiceImpl implements PositionService {

    @Autowired
    private PositionBeanMapper positionBeanMapper;

    @Override
    public List<PositionBean> selectBySelective(Integer mapId, Integer deviceId) {
        return positionBeanMapper.selectBySelective(mapId,deviceId);
    }

    @Override
    public int deleteByPrimaryKey(Integer id) {
        return positionBeanMapper.deleteByPrimaryKey(id);
    }
}
