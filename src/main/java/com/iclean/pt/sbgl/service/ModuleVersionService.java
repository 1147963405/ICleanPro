package com.iclean.pt.sbgl.service;

import com.iclean.pt.sbgl.bean.ModuleVersionBean;

import java.util.List;

public interface ModuleVersionService {
    List<ModuleVersionBean> selectBySelective(Integer type,Integer startIndex,Integer count);
    int insert(ModuleVersionBean record);
    int deleteByPrimaryKey(Integer id,Integer type);
}
