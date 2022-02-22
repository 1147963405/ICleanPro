package com.iclean.pt.sbgl.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AlarmBean {
    private Integer id;

    private String uuid;

    private String context;

    private Integer level;

    private Integer deviceId;

    private Integer status;

    private String updateTime;

    private String startTime;
}