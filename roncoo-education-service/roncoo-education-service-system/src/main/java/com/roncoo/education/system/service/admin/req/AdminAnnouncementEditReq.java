package com.roncoo.education.system.service.admin.req;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@Accessors(chain = true)
public class AdminAnnouncementEditReq implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long id;
    private Integer statusId;
    private Integer sort;
    private String category;
    private String title;
    private String content;
    private String linkUrl;
    private Integer isTop;
}
