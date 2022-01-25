package com.iclean.pt.yhgl.service;

import com.iclean.pt.yhgl.bean.CustomerDeviceBean;

import java.util.List;
import java.util.concurrent.Future;

public interface CustomerDeviceService {
    List<CustomerDeviceBean> selectCustomerWithDevices(Integer customerId);
    Future<List<CustomerDeviceBean>> asyncCustomerWithDevices(Integer customerId);
    int insert(CustomerDeviceBean record);
    int deleteBySelective(Integer deviceId);
    List<CustomerDeviceBean> selectByDeviceId(Integer deviceId);

}

