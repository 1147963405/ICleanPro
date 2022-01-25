package com.iclean.pt.yhgl.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserBean  implements Serializable {

    private Integer id;

    private String name;

    private String userName;

    private String passwd;

    private String phone;

    private String contactPerson;

    private String email;

    private Integer typeId;

    private Integer customerId;

    private Integer status;

    private String description;

    private Long updateTime;


}