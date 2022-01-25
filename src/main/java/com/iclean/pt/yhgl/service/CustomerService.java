package com.iclean.pt.yhgl.service;

import com.iclean.pt.yhgl.bean.CustomerBean;
import com.iclean.pt.yhgl.bean.CustomerGroupBean;

import java.util.List;

public interface CustomerService {

    /*新增客户*/
    int insertSelective(CustomerBean record);
    /*通过客户id删除客户*/
    int deleteByPrimaryKey(Integer id);
    /*更新客户*/
    int updateByPrimaryKeySelective(CustomerBean record);
    /*通过客户id获取指定客户*/
    CustomerBean selectByPrimaryKey(Integer id);
    /*根据条件获取客户信息列表*/
    List<CustomerBean> selectBySelective(Integer customerGroupId,Integer customerId);



}
