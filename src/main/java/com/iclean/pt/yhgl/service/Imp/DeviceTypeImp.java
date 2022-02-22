package com.iclean.pt.yhgl.service.Imp;

import com.iclean.pt.yhgl.bean.DeviceTypeBean;
import com.iclean.pt.yhgl.dao.DeviceTypeBeanMapper;
import com.iclean.pt.yhgl.service.DeviceService;
import com.iclean.pt.yhgl.service.DeviceTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class DeviceTypeImp implements DeviceTypeService {

    @Autowired
    private DeviceTypeBeanMapper deviceTypeBeanMapper;


    @Override
    public DeviceTypeBean selectByPrimaryKey(Integer id) {
        return deviceTypeBeanMapper.selectByPrimaryKey(id);
    }

    @Override
    public List<Map<String, Object>> selectDeviceTypeList() {
        return deviceTypeBeanMapper.selectDeviceTypeList();
    }
}
