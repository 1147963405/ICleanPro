package com.iclean.pt.sbgl.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PathsCopyBean implements Serializable {
    private Integer id;

    private String name;

    private Integer type;

    private Object path;

}