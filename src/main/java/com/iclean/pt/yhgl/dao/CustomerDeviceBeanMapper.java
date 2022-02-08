package com.iclean.pt.yhgl.dao;

import com.iclean.pt.sbgl.bean.CleanReportBean;
import com.iclean.pt.yhgl.bean.CustomerDeviceBean;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface CustomerDeviceBeanMapper {
    int deleteByPrimaryKey(Integer id);

    int deleteBySelective(Integer deviceId);

    int insert(CustomerDeviceBean record);

    int insertSelective(CustomerDeviceBean record);

    CustomerDeviceBean selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(CustomerDeviceBean record);

    int updateByPrimaryKey(CustomerDeviceBean record);

    /*通过中间表查询客户详情信息*/
    List<CustomerDeviceBean> selectCustomerWithDevices(Integer customerId);
    List<CustomerDeviceBean> selectByDeviceId(Integer deviceId);
}