package com.roncoo.education.user.dao.impl.mapper.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

public class TutorApplication implements Serializable {
    private Long id;
    private LocalDateTime gmtCreate;
    private LocalDateTime gmtModified;
    private Integer statusId;
    private Long requirementId;
    private Long tutorId;
    private Long userId;
    private String applyMessage;
    private Integer appStatus;
    private static final long serialVersionUID = 1L;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public LocalDateTime getGmtCreate() { return gmtCreate; }
    public void setGmtCreate(LocalDateTime gmtCreate) { this.gmtCreate = gmtCreate; }
    public LocalDateTime getGmtModified() { return gmtModified; }
    public void setGmtModified(LocalDateTime gmtModified) { this.gmtModified = gmtModified; }
    public Integer getStatusId() { return statusId; }
    public void setStatusId(Integer statusId) { this.statusId = statusId; }
    public Long getRequirementId() { return requirementId; }
    public void setRequirementId(Long requirementId) { this.requirementId = requirementId; }
    public Long getTutorId() { return tutorId; }
    public void setTutorId(Long tutorId) { this.tutorId = tutorId; }
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public String getApplyMessage() { return applyMessage; }
    public void setApplyMessage(String applyMessage) { this.applyMessage = applyMessage == null ? null : applyMessage.trim(); }
    public Integer getAppStatus() { return appStatus; }
    public void setAppStatus(Integer appStatus) { this.appStatus = appStatus; }

    @Override
    public String toString() {
        return "TutorApplication [id=" + id + ", requirementId=" + requirementId + ", tutorId=" + tutorId + "]";
    }
}
