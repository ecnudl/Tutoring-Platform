package com.roncoo.education.user.service.auth.resp;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;
import java.io.Serializable;

@Data
@Accessors(chain = true)
public class AuthCertResp implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long id;
    private Integer certType;
    private String certName;
    private String certUrl;
    private String certNo;
    private Integer auditStatus;
}
