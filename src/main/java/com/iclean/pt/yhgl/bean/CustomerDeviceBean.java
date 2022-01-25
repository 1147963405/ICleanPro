package com.iclean.pt.yhgl.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerDeviceBean {
    private Integer id;

    private Integer customerId;

    private Integer deviceId;

    private String description;

    private CustomerBean customerBean;
    private DeviceInfoBean deviceInfoBean;


}