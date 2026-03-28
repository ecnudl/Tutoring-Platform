package com.roncoo.education.user.service.auth.resp;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;
import java.io.Serializable;

@Data
@Accessors(chain = true)
public class AuthRequirementResp implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long id;
    private String title;
    private String subjectIds;
    private Long gradeId;
    private Integer reqStatus;
    private java.math.BigDecimal budgetMin;
    private java.math.BigDecimal budgetMax;
    private java.time.LocalDateTime gmtCreate;
}
