package com.iclean.pt.yhgl.service.Imp;

import com.iclean.pt.yhgl.bean.DeviceTypeBean;
import com.iclean.pt.yhgl.dao.DeviceTypeBeanMapper;
import com.iclean.pt.yhgl.service.DeviceService;
import com.iclean.pt.yhgl.service.DeviceTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DeviceTypeImp implements DeviceTypeService {

    @Autowired
    private DeviceTypeBeanMapper deviceTypeBeanMapper;

    @Override
    public List<DeviceTypeBean> selectList(Integer id) {
        return deviceTypeBeanMapper.selectList(id);
    }

    @Override
    public DeviceTypeBean selectByPrimaryKey(Integer id) {
        return deviceTypeBeanMapper.selectByPrimaryKey(id);
    }
}
