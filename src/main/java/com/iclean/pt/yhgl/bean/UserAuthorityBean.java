package com.iclean.pt.yhgl.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserAuthorityBean {
    private int id;

    private int userTypeId;

    private int monitorAuth;

    private int customerMangerAuth;

        private int deviceMangerAuth;

    private int mapMangerAuth;

    private int taskMangerAuth;

    private int videoMangerAuth;

    private int reportDataAuth;

    private int upgradeAuth;

    private int orderAuth;

    private int gpsDataAuth;

}