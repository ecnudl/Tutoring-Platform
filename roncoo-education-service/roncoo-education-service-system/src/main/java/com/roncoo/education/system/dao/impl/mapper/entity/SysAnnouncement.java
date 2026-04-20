package com.roncoo.education.system.dao.impl.mapper.entity;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Accessors(chain = true)
public class SysAnnouncement implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;
    private LocalDateTime gmtCreate;
    private LocalDateTime gmtModified;
    private Integer statusId;
    private Integer sort;
    private String category;
    private String title;
    private String content;
    private String linkUrl;
    private Integer isTop;
}
