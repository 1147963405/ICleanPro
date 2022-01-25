package com.iclean.pt.sbgl.bean;

public class AlarmBean {
    private Integer id;

    private String uuid;

    private String context;

    private Integer level;

    private Integer deviceId;

    private Integer status;

    private Long updateTime;

    private Long startTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid == null ? null : uuid.trim();
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context == null ? null : context.trim();
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public Integer getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(Integer deviceId) {
        this.deviceId = deviceId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Long updateTime) {
        this.updateTime = updateTime;
    }

    public Long getStartTime() {
        return startTime;
    }

    public void setStartTime(Long startTime) {
        this.startTime = startTime;
    }

    @Override
    public String toString() {
        return "AlarmBean{" +
                "id=" + id +
                ", uuid='" + uuid + '\'' +
                ", context='" + context + '\'' +
                ", level=" + level +
                ", deviceId=" + deviceId +
                ", status=" + status +
                ", updateTime=" + updateTime +
                ", startTime=" + startTime +
                '}';
    }
}