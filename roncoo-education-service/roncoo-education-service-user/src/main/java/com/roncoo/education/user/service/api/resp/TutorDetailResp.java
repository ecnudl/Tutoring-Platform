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
 * API-教员详情(完整)
 * </p>
 *
 * @author fengyw
 */
@Data
@Accessors(chain = true)
@ApiModel(description = "API-教员详情(完整)")
public class TutorDetailResp implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    private Long id;

    @ApiModelProperty(value = "展示编号")
    private String displayNo;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建时间")
    private LocalDateTime gmtCreate;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "修改时间")
    private LocalDateTime gmtModified;

    @ApiModelProperty(value = "真实姓名(仅姓氏)")
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

    @ApiModelProperty(value = "城市名称")
    private String cityName;

    @ApiModelProperty(value = "区县名称")
    private String districtName;

    @ApiModelProperty(value = "授课方式(1:教员上门,2:学员上门,3:线上授课)")
    private Integer teachingMethod;

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

    @ApiModelProperty(value = "是否明星教员(0:否,1:是)")
    private Integer isStar;

    @ApiModelProperty(value = "是否实名认证(0:否,1:是)")
    private Integer isVerified;

    @ApiModelProperty(value = "可教科目名称列表")
    private List<String> subjectNames;

    @ApiModelProperty(value = "标签列表")
    private List<String> tags;

    @ApiModelProperty(value = "可授课区域列表")
    private List<TeachingAreaItem> teachingAreas;

    @ApiModelProperty(value = "可教科目列表")
    private List<SubjectItem> subjects;

    @ApiModelProperty(value = "资质认证列表")
    private List<CertificationItem> certifications;

    @Data
    @Accessors(chain = true)
    @ApiModel(description = "授课区域")
    public static class TeachingAreaItem implements Serializable {

        private static final long serialVersionUID = 1L;

        @ApiModelProperty(value = "主键")
        private Long id;

        @ApiModelProperty(value = "城市ID")
        private Long cityId;

        @ApiModelProperty(value = "区县ID")
        private Long districtId;

        @ApiModelProperty(value = "区县名称")
        private String districtName;
    }

    @Data
    @Accessors(chain = true)
    @ApiModel(description = "教员科目")
    public static class SubjectItem implements Serializable {

        private static final long serialVersionUID = 1L;

        @ApiModelProperty(value = "主键")
        private Long id;

        @ApiModelProperty(value = "科目ID")
        private Long subjectId;

        @ApiModelProperty(value = "科目名称")
        private String subjectName;
    }

    @Data
    @Accessors(chain = true)
    @ApiModel(description = "资质认证")
    public static class CertificationItem implements Serializable {

        private static final long serialVersionUID = 1L;

        @ApiModelProperty(value = "主键")
        private Long id;

        @ApiModelProperty(value = "认证类型(1:身份证,2:学生证,3:学历证,4:教师资格证,5:其他)")
        private Integer certType;

        @ApiModelProperty(value = "认证名称")
        private String certName;

        @ApiModelProperty(value = "审核状态(1:待审核,2:审核通过,3:审核不通过)")
        private Integer auditStatus;
    }
}
