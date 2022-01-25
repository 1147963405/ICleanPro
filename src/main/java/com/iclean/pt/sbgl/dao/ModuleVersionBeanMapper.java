package com.iclean.pt.sbgl.dao;

import com.iclean.pt.sbgl.bean.ModuleVersionBean;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ModuleVersionBeanMapper {
    int deleteByPrimaryKey(Integer id,Integer type);

    int insert(ModuleVersionBean record);

    int insertSelective(ModuleVersionBean record);

    ModuleVersionBean selectByPrimaryKey(Integer id);
    List<ModuleVersionBean> selectBySelective(@Param("type") Integer type,@Param("startIndex") Integer startIndex,@Param("count") Integer count);
    int updateByPrimaryKeySelective(ModuleVersionBean record);

    int updateByPrimaryKey(ModuleVersionBean record);
}