package com.iclean.pt.yhgl.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerBean {
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

    @Override
    public String toString() {
        return "CustomerBean{" +
                "id=" + id +
                ", customerGroupId=" + customerGroupId +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", address='" + address + '\'' +
                ", contactPerson='" + contactPerson + '\'' +
                ", leaderPerson='" + leaderPerson + '\'' +
                ", description='" + description + '\'' +
                ", updateTime=" + updateTime +
                '}';
    }
}