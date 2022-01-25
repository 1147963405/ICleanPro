package com.iclean.pt.sbgl.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MapsDeviceBean  implements Serializable {

    /*"download_url": "http://47.92.192.154:9077/iclean-cloud/data/download/map?device_id=22&uuid=af098920-acaf-4b76-8cfa-e8667ca392c3",
                "path": "http://47.92.192.154:9993/data/map/22/af098920-acaf-4b76-8cfa-e8667ca392c3/map.png",
                "user_id": 1,
                "device_id": 22,
                "device_name": "中创云谷-D栋",
                "map_name": "h",
                "map_id": 2*/
    /*构建实体bean*/
    private String downloadUrl;
    private String path;
    private Integer userId;
    private Integer deviceId;
    private String deviceName;
    private Integer mapId;
    private String mapName;
}
