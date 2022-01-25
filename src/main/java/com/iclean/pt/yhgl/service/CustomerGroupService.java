package com.iclean.pt.yhgl.service;

import com.iclean.pt.yhgl.bean.CustomerGroupBean;

import java.util.List;

public interface CustomerGroupService {
    /*通过客户组parent_id获取到客户组所有匹配信息*/
    List<CustomerGroupBean> selectByPrimaryKey(Integer pId);
    /*通过客户组id获取指定客户组*/
  CustomerGroupBean queryByPrimaryKey(Integer id);
    /*新增客户组*/
    int insertSelective(CustomerGroupBean record);
    /*更新客户组*/
    int updateByPrimaryKeySelective(CustomerGroupBean record);
   /*根据客户组id删除对应客户组*/
    int deleteByPrimaryKey(Integer id);
}
