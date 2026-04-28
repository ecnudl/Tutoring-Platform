package com.roncoo.education.user.dao.impl.mapper.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

public class TutorReservation implements Serializable {
    private Long id;
    private LocalDateTime gmtCreate;
    private LocalDateTime gmtModified;
    private Integer statusId;
    private Long studentUserId;
    private Long tutorUserId;
    private Long tutorId;
    private Long subjectId;
    private String contactName;
    private String contactMobile;
    private String contactWechat;
    private String scheduleTime;
    private String address;
    private String remark;
    private Integer resStatus;
    private String cancelReason;
    private java.time.LocalDateTime matchedAt;
    private Long requirementId;
    private static final long serialVersionUID = 1L;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public LocalDateTime getGmtCreate() { return gmtCreate; }
    public void setGmtCreate(LocalDateTime gmtCreate) { this.gmtCreate = gmtCreate; }
    public LocalDateTime getGmtModified() { return gmtModified; }
    public void setGmtModified(LocalDateTime gmtModified) { this.gmtModified = gmtModified; }
    public Integer getStatusId() { return statusId; }
    public void setStatusId(Integer statusId) { this.statusId = statusId; }
    public Long getStudentUserId() { return studentUserId; }
    public void setStudentUserId(Long studentUserId) { this.studentUserId = studentUserId; }
    public Long getTutorUserId() { return tutorUserId; }
    public void setTutorUserId(Long tutorUserId) { this.tutorUserId = tutorUserId; }
    public Long getTutorId() { return tutorId; }
    public void setTutorId(Long tutorId) { this.tutorId = tutorId; }
    public Long getSubjectId() { return subjectId; }
    public void setSubjectId(Long subjectId) { this.subjectId = subjectId; }
    public String getContactName() { return contactName; }
    public void setContactName(String contactName) { this.contactName = contactName == null ? null : contactName.trim(); }
    public String getContactMobile() { return contactMobile; }
    public void setContactMobile(String contactMobile) { this.contactMobile = contactMobile == null ? null : contactMobile.trim(); }
    public String getContactWechat() { return contactWechat; }
    public void setContactWechat(String contactWechat) { this.contactWechat = contactWechat == null ? null : contactWechat.trim(); }
    public String getScheduleTime() { return scheduleTime; }
    public void setScheduleTime(String scheduleTime) { this.scheduleTime = scheduleTime == null ? null : scheduleTime.trim(); }
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address == null ? null : address.trim(); }
    public String getRemark() { return remark; }
    public void setRemark(String remark) { this.remark = remark == null ? null : remark.trim(); }
    public Integer getResStatus() { return resStatus; }
    public void setResStatus(Integer resStatus) { this.resStatus = resStatus; }
    public String getCancelReason() { return cancelReason; }
    public void setCancelReason(String cancelReason) { this.cancelReason = cancelReason == null ? null : cancelReason.trim(); }
    public java.time.LocalDateTime getMatchedAt() { return matchedAt; }
    public void setMatchedAt(java.time.LocalDateTime matchedAt) { this.matchedAt = matchedAt; }
    public Long getRequirementId() { return requirementId; }
    public void setRequirementId(Long requirementId) { this.requirementId = requirementId; }


    @Override
    public String toString() {
        return "TutorReservation [id=" + id + ", studentUserId=" + studentUserId + ", tutorUserId=" + tutorUserId + "]";
    }
}
