package com.iclean.pt.yhgl.service.Imp;

import com.iclean.pt.yhgl.bean.CustomerDeviceBean;
import com.iclean.pt.yhgl.dao.CustomerDeviceBeanMapper;
import com.iclean.pt.yhgl.service.CustomerDeviceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.Future;

@Service
public class CustomerDeviceServiceImp implements CustomerDeviceService {

    @Autowired
    private CustomerDeviceBeanMapper customerDeviceBeanMapper;

    @Override
    public List<CustomerDeviceBean> selectCustomerWithDevices(Integer customerId) {
        return customerDeviceBeanMapper.selectCustomerWithDevices(customerId);
    }

    @Override
    public int insert(CustomerDeviceBean record) {
        return customerDeviceBeanMapper.insert(record);
    }

    @Override
    public int deleteBySelective(Integer deviceId) {
        return customerDeviceBeanMapper.deleteBySelective(deviceId);
    }

    @Override
    public List<CustomerDeviceBean> selectByDeviceId(Integer deviceId) {
        return customerDeviceBeanMapper.selectByDeviceId(deviceId);
    }
    @Override
    @Async("myTaskExecutor")
    public Future<List<CustomerDeviceBean>> asyncCustomerWithDevices(Integer customerId) {
        return new AsyncResult<List<CustomerDeviceBean>>(customerDeviceBeanMapper.selectCustomerWithDevices(customerId));
    }

}
