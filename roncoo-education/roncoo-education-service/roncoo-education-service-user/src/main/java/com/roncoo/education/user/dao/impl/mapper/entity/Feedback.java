package com.roncoo.education.user.dao.impl.mapper.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

public class Feedback implements Serializable {
    private Long id;
    private LocalDateTime gmtCreate;
    private LocalDateTime gmtModified;
    private Integer statusId;
    private Long userId;
    private String content;
    private String contact;
    private String images;
    private Integer fbStatus;
    private String reply;
    private LocalDateTime replyTime;
    private Long replierId;
    private static final long serialVersionUID = 1L;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public LocalDateTime getGmtCreate() { return gmtCreate; }
    public void setGmtCreate(LocalDateTime gmtCreate) { this.gmtCreate = gmtCreate; }
    public LocalDateTime getGmtModified() { return gmtModified; }
    public void setGmtModified(LocalDateTime gmtModified) { this.gmtModified = gmtModified; }
    public Integer getStatusId() { return statusId; }
    public void setStatusId(Integer statusId) { this.statusId = statusId; }
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content == null ? null : content.trim(); }
    public String getContact() { return contact; }
    public void setContact(String contact) { this.contact = contact == null ? null : contact.trim(); }
    public String getImages() { return images; }
    public void setImages(String images) { this.images = images == null ? null : images.trim(); }
    public Integer getFbStatus() { return fbStatus; }
    public void setFbStatus(Integer fbStatus) { this.fbStatus = fbStatus; }
    public String getReply() { return reply; }
    public void setReply(String reply) { this.reply = reply == null ? null : reply.trim(); }
    public LocalDateTime getReplyTime() { return replyTime; }
    public void setReplyTime(LocalDateTime replyTime) { this.replyTime = replyTime; }
    public Long getReplierId() { return replierId; }
    public void setReplierId(Long replierId) { this.replierId = replierId; }

    @Override
    public String toString() { return "Feedback [id=" + id + ", userId=" + userId + ", fbStatus=" + fbStatus + "]"; }
}
