package com.iclean.pt.yhgl.dao;

import com.iclean.pt.yhgl.bean.CustomerBean;
import com.iclean.pt.yhgl.bean.CustomerGroupBean;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CustomerBeanMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(CustomerBean record);

    int insertSelective(CustomerBean record);

    List<CustomerBean> selectBySelective(Integer customerGroupId,Integer customerId);
    CustomerBean selectByPrimaryKey(Integer id);
    int updateByPrimaryKeySelective(CustomerBean record);
    int updateByPrimaryKey(CustomerBean record);
}