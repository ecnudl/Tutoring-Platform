package com.roncoo.education.user.dao.impl.mapper.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

public class SmsLog implements Serializable {
    private Long id;
    private LocalDateTime gmtCreate;
    private LocalDateTime gmtModified;
    private Integer statusId;
    private String mobile;
    private Integer smsType;
    private String content;
    private Integer sendStatus;
    private Integer platform;
    private String ip;
    private static final long serialVersionUID = 1L;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public LocalDateTime getGmtCreate() { return gmtCreate; }
    public void setGmtCreate(LocalDateTime gmtCreate) { this.gmtCreate = gmtCreate; }
    public LocalDateTime getGmtModified() { return gmtModified; }
    public void setGmtModified(LocalDateTime gmtModified) { this.gmtModified = gmtModified; }
    public Integer getStatusId() { return statusId; }
    public void setStatusId(Integer statusId) { this.statusId = statusId; }
    public String getMobile() { return mobile; }
    public void setMobile(String mobile) { this.mobile = mobile == null ? null : mobile.trim(); }
    public Integer getSmsType() { return smsType; }
    public void setSmsType(Integer smsType) { this.smsType = smsType; }
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content == null ? null : content.trim(); }
    public Integer getSendStatus() { return sendStatus; }
    public void setSendStatus(Integer sendStatus) { this.sendStatus = sendStatus; }
    public Integer getPlatform() { return platform; }
    public void setPlatform(Integer platform) { this.platform = platform; }
    public String getIp() { return ip; }
    public void setIp(String ip) { this.ip = ip == null ? null : ip.trim(); }

    @Override
    public String toString() { return "SmsLog [id=" + id + ", mobile=" + mobile + ", smsType=" + smsType + "]"; }
}
