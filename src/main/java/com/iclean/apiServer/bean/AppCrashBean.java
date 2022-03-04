package com.iclean.apiServer.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AppCrashBean {
    private Integer id;

    private Integer sdk;

    private String brand;

    private String model;

    private Integer versionCode;

    private String versionName;

    private String versionSystem;

    private String versionNav;

    private String serial;

    private Integer robotType;

    private String robotName;

    private Long crashDate;

    private String msg;

}