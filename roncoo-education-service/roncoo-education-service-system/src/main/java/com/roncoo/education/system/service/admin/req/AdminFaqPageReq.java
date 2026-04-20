package com.roncoo.education.system.service.admin.req;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@Accessors(chain = true)
public class AdminFaqPageReq implements Serializable {
    private static final long serialVersionUID = 1L;
    private String category;
    private Integer statusId;
    private String keyword;
    private int pageCurrent = 1;
    private int pageSize = 20;
}
