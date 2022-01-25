package com.iclean.pt.yhgl.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerWithDevicesAndOrders {

    private Integer id;

    private Integer customerGroupId;

    private String name;

    private String email;

    private String phone;

    private String address;

    private String contactPerson;

    private String leaderPerson;

    private String description;

    private Long updateTime;

    private List<DeviceInfoBean> abort_device;

    private List<OrderBean> abort_order;

}
