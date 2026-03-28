package com.roncoo.education.user.service.auth.resp;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;
import java.io.Serializable;

@Data
@Accessors(chain = true)
public class AuthVipResp implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long id;
    private Integer vipLevel;
    private java.time.LocalDateTime startTime;
    private java.time.LocalDateTime endTime;
}
