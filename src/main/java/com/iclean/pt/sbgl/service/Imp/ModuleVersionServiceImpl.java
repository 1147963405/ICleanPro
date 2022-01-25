package com.iclean.pt.sbgl.service.Imp;

import com.iclean.pt.sbgl.bean.ModuleVersionBean;
import com.iclean.pt.sbgl.dao.ModuleVersionBeanMapper;
import com.iclean.pt.sbgl.service.ModuleVersionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ModuleVersionServiceImpl implements ModuleVersionService {

    @Autowired
    private ModuleVersionBeanMapper moduleVersionBeanMapper;

    @Override
    public List<ModuleVersionBean> selectBySelective(Integer type,Integer startIndex,Integer count) {

        return  moduleVersionBeanMapper.selectBySelective(type,startIndex,count);
    }

    @Override
    public int insert(ModuleVersionBean record) {
        return moduleVersionBeanMapper.insert(record);
    }

    @Override
    public int deleteByPrimaryKey(Integer id, Integer type) {
        return moduleVersionBeanMapper.deleteByPrimaryKey(id,type);
    }
}
