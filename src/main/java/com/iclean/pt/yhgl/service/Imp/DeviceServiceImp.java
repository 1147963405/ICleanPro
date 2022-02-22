package com.iclean.pt.yhgl.service.Imp;

import com.iclean.pt.yhgl.bean.DeviceInfoBean;
import com.iclean.pt.yhgl.dao.DeviceInfoBeanMapper;
import com.iclean.pt.yhgl.service.DeviceService;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class DeviceServiceImp  implements DeviceService {

    @Autowired
    private DeviceInfoBeanMapper deviceInfoBeanMapper;

    @Override
    public Map<String, Object> selectByPrimaryKey (Integer id) {
        return deviceInfoBeanMapper.selectByPrimaryKey(id);
    }

    @Override
    public int updateByPrimaryKey(DeviceInfoBean record) {
        return deviceInfoBeanMapper.updateByPrimaryKey(record);
    }

    @Override
    public int insert(DeviceInfoBean record) {
        return deviceInfoBeanMapper.insert(record);
    }

    @Override
    public int deleteByPrimaryKey(Integer id) {
        return deviceInfoBeanMapper.deleteByPrimaryKey(id);
    }

    @Override
    public List<Map<String, Object>> selectDevicesByCustomerId(Integer customerId) {
        return deviceInfoBeanMapper.selectDevicesByCustomerId(customerId);
    }

    @Override
    public List<Map<String, Object>> selectDevicesByParams(Integer customerId, String params) {
        return deviceInfoBeanMapper.selectDevicesByParams(customerId,params);
    }

    @Override
    public List<Map<String, Object>> selectDevices() {
        return deviceInfoBeanMapper.selectDevices();
    }

    @Override
    public List<Map<String, Object>> selectDevicesBySelective(String params) {
        return deviceInfoBeanMapper.selectDevicesBySelective(params);
    }

    @Override
    public DeviceInfoBean selectBySelective(String params) {
        return deviceInfoBeanMapper.selectBySelective(params);
    }


}
