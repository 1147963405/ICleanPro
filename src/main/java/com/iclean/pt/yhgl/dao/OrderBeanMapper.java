package com.iclean.pt.yhgl.dao;

import com.iclean.pt.yhgl.bean.OrderBean;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface OrderBeanMapper {
    int deleteByPrimaryKey(Integer sid);

    int insert(OrderBean record);

    int insertSelective(OrderBean record);

    OrderBean selectByPrimaryKey(Integer sid);

    List<OrderBean> selectBySelective(Integer customerId);

    int updateByPrimaryKeySelective(OrderBean record);

    int updateByPrimaryKey(OrderBean record);
}