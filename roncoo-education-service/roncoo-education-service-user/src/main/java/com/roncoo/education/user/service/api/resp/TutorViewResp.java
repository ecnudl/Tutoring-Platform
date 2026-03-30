package com.roncoo.education.user.service.api.resp;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * API-教员详情
 * </p>
 *
 * @author fengyw
 */
@Data
@Accessors(chain = true)
@ApiModel(description = "API-教员详情")
public class TutorViewResp implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    private Long id;

    @ApiModelProperty(value = "用户ID")
    private Long userId;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建时间")
    private LocalDateTime gmtCreate;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "修改时间")
    private LocalDateTime gmtModified;

    @ApiModelProperty(value = "真实姓名")
    private String realName;

    @ApiModelProperty(value = "头像")
    private String avatar;

    @ApiModelProperty(value = "性别(1:男,2:女)")
    private Integer gender;

    @ApiModelProperty(value = "出生日期")
    private String birthDate;

    @ApiModelProperty(value = "教员类型(1:大学生,2:专职教师,3:海外留学生,4:专业人士)")
    private Integer tutorType;

    @ApiModelProperty(value = "学历")
    private String degree;

    @ApiModelProperty(value = "毕业/在读院校")
    private String university;

    @ApiModelProperty(value = "专业")
    private String major;

    @ApiModelProperty(value = "可教科目，逗号分隔")
    private String subjects;

    @ApiModelProperty(value = "可教年级，逗号分隔")
    private String grades;

    @ApiModelProperty(value = "标签，逗号分隔")
    private String tags;

    @ApiModelProperty(value = "省份ID")
    private Long provinceId;

    @ApiModelProperty(value = "城市ID")
    private Long cityId;

    @ApiModelProperty(value = "区县ID")
    private Long districtId;

    @ApiModelProperty(value = "详细地址")
    private String address;

    @ApiModelProperty(value = "最低课时费(元/小时)")
    private BigDecimal priceMin;

    @ApiModelProperty(value = "最高课时费(元/小时)")
    private BigDecimal priceMax;

    @ApiModelProperty(value = "是否支持免费试课(0:否,1:是)")
    private Integer freeTrial;

    @ApiModelProperty(value = "教龄(年)")
    private Integer teachingAge;

    @ApiModelProperty(value = "自我介绍")
    private String selfIntroduction;

    @ApiModelProperty(value = "教学特点")
    private String teachingStyle;

    @ApiModelProperty(value = "成功案例")
    private String successCases;

    @ApiModelProperty(value = "可授课时间")
    private String availableTime;

    @ApiModelProperty(value = "浏览次数")
    private Integer viewCount;

    @ApiModelProperty(value = "成功次数")
    private Integer successCount;

    @ApiModelProperty(value = "资质认证列表")
    private List<CertificationResp> certifications;

    /**
     * 资质认证信息
     */
    @Data
    @Accessors(chain = true)
    @ApiModel(description = "资质认证信息")
    public static class CertificationResp implements Serializable {

        private static final long serialVersionUID = 1L;

        @ApiModelProperty(value = "主键")
        private Long id;

        @ApiModelProperty(value = "认证类型(1:身份证,2:学生证,3:学历证,4:教师资格证,5:其他)")
        private Integer certType;

        @ApiModelProperty(value = "认证名称")
        private String certName;

        @ApiModelProperty(value = "认证图片URL")
        private String certImageUrl;

        @ApiModelProperty(value = "审核状态(1:待审核,2:审核通过,3:审核不通过)")
        private Integer auditStatus;
    }
}
