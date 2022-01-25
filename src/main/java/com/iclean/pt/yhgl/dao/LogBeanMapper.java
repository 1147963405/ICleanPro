package com.iclean.pt.yhgl.dao;

import com.iclean.pt.yhgl.bean.LogBean;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface LogBeanMapper {

    /*通过用户id查询该用户的日志信息*/
    LogBean selectByUserId(Integer uid);
    /*通过客户id查询该客户的日志信息*/
    LogBean selectByCustomerId(Integer cid);

}