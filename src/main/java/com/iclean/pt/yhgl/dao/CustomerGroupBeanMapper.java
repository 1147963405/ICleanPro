package com.iclean.pt.yhgl.dao;

import com.iclean.pt.yhgl.bean.CustomerGroupBean;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
@Mapper
public interface CustomerGroupBeanMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(CustomerGroupBean record);

    int insertSelective(CustomerGroupBean record);

    List<CustomerGroupBean> selectByPrimaryKey(Integer pId);

    CustomerGroupBean queryByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(CustomerGroupBean record);

    int updateByPrimaryKey(CustomerGroupBean record);
}