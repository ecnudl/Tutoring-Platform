package com.roncoo.education.system.service.admin.req;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@Accessors(chain = true)
public class AdminFaqSaveReq implements Serializable {
    private static final long serialVersionUID = 1L;
    private Integer statusId;
    private Integer sort;
    private String category;
    private String categoryIcon;
    private String categoryLabel;
    private String question;
    private String answer;
}
