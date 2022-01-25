package com.iclean.pt.sbgl.bean;

import com.iclean.pt.yhgl.bean.DeviceInfoBean;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
public class PathsBean implements Serializable {
    private Integer id;

    private Integer mapId;

    private Integer deviceId;

    private String name;

    private Integer type;

    private String path;

    private Long updateTime;

    public PathsBean(Integer id, Integer mapId, Integer deviceId, String name, Integer type, String path, Long updateTime) {
        this.id = id;
        this.mapId = mapId;
        this.deviceId = deviceId;
        this.name = name;
        this.type = type;
        this.path = path;
        this.updateTime = updateTime;
    }

    public PathsBean(Integer id, String name, Integer type, String path) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.path = path;
    }

    public PathsBean() {

    }
}