package com.iclean.pt.yhgl.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerGroupBean {
    private Integer id;

    private Integer parentId;

    private String name;

    private String description;

}