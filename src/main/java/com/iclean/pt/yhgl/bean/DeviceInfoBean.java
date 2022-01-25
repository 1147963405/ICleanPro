package com.iclean.pt.yhgl.bean;

public class DeviceInfoBean {
    private Integer id;

    private String name;

    private String serial;

    private String address;

    private Integer typeId;

    private Integer status;

    private String models;

    private String scene;

    private String moduleInfo;

    private String lastLocatedAddress;

    private String description;

    private Long updateTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getSerial() {
        return serial;
    }

    public void setSerial(String serial) {
        this.serial = serial == null ? null : serial.trim();
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address == null ? null : address.trim();
    }

    public Integer getTypeId() {
        return typeId;
    }

    public void setTypeId(Integer typeId) {
        this.typeId = typeId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getModels() {
        return models;
    }

    public void setModels(String models) {
        this.models = models == null ? null : models.trim();
    }

    public String getScene() {
        return scene;
    }

    public void setScene(String scene) {
        this.scene = scene == null ? null : scene.trim();
    }

    public String getModuleInfo() {
        return moduleInfo;
    }

    public void setModuleInfo(String moduleInfo) {
        this.moduleInfo = moduleInfo == null ? null : moduleInfo.trim();
    }

    public String getLastLocatedAddress() {
        return lastLocatedAddress;
    }

    public void setLastLocatedAddress(String lastLocatedAddress) {
        this.lastLocatedAddress = lastLocatedAddress == null ? null : lastLocatedAddress.trim();
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description == null ? null : description.trim();
    }

    public Long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Long updateTime) {
        this.updateTime = updateTime;
    }

    @Override
    public String toString() {
        return "DeviceInfoBean{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", serial='" + serial + '\'' +
                ", address='" + address + '\'' +
                ", typeId=" + typeId +
                ", status=" + status +
                ", models='" + models + '\'' +
                ", scene='" + scene + '\'' +
                ", moduleInfo='" + moduleInfo + '\'' +
                ", lastLocatedAddress='" + lastLocatedAddress + '\'' +
                ", description='" + description + '\'' +
                ", updateTime=" + updateTime +
                '}';
    }
}