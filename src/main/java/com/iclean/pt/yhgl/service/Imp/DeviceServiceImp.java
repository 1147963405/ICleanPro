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
    public List<DeviceInfoBean> selectBySelective(Integer typeId,Integer status,String name,String serial) {
        return deviceInfoBeanMapper.selectBySelective(typeId,status,name,serial);
    }

    @Override
    public List<DeviceInfoBean> selectSelective(String serial, Integer typeId, String status,String name) {
        return deviceInfoBeanMapper.selectSelective(serial,typeId,status,name);
    }

    @Override
    public DeviceInfoBean selectByPrimaryKey (Integer id) {
        return deviceInfoBeanMapper.selectByPrimaryKey(id);
    }

    @Override
    public DeviceInfoBean selectBySerialOrName(String serial, String name) {
        return deviceInfoBeanMapper.selectBySerialOrName(serial,name);
    }

    @Override
    public List<DeviceInfoBean> selectList() {
        return deviceInfoBeanMapper.selectList();
    }

    @Override
    public DeviceInfoBean selectByIdOrName(Integer id, String name) {
        return deviceInfoBeanMapper.selectByIdOrName(id,name);
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



}
