package com.iclean.pt.yhgl.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderBean {
    private Integer sid;

    private String id;

    private Integer customerId;

    private Integer createCustomerId;

    private String type;

    private String status;

    private Long startTime;

    private Long endTime;

    private Integer cashPledge;

    private Integer rent;

    private Integer isNotice;

    private Long noticeTime;

    private String appendfile;

    private String description;

    private Long updateTime;

}