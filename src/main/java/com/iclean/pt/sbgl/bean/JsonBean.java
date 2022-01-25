package com.iclean.pt.sbgl.bean;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;



@Data
@AllArgsConstructor
@NoArgsConstructor
public class JsonBean {


    private String isCharging;
    private Integer battery;
    private String currMapName;
    private String currMap;
    private Integer alarmCount;
    private Integer currMapId;
    private Integer type;
    private Object position;


    /*deviceinfobean*/
    private Integer id;
    private String name;
    private String serial;
    private String address;
    private Integer typeId;
    private Integer status;
    private String scene;
    private Object moduleInfo;
    private Object lastLocatedAddress;
    private String description;


}
