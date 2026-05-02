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
    private java.time.LocalDate birthDate;

    @ApiModelProperty(value = "教员类型(1:大学生,2:专职,3:在职教师,4:海归外教)")
    private Integer tutorType;

    @ApiModelProperty(value = "详细身份(本科大四/在读硕士/在职高中教师等)")
    private String identityDetail;

    @ApiModelProperty(value = "学历(1:高中,2:大专,3:本科,4:硕士,5:博士)")
    private Integer degree;

    @ApiModelProperty(value = "高中母校")
    private String highSchool;

    @ApiModelProperty(value = "毕业/在读院校")
    private String university;

    @ApiModelProperty(value = "专业")
    private String major;

    @ApiModelProperty(value = "年级/年份 (如 大四 / 研二)")
    private String gradeYear;

    @ApiModelProperty(value = "籍贯")
    private String hometownProvince;

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

    @ApiModelProperty(value = "薪资备注 (教员自定的薪资说明)")
    private String salaryRemark;

    @ApiModelProperty(value = "自我介绍")
    private String selfIntroduction;

    @ApiModelProperty(value = "所获证书 (CSV/换行分隔)")
    private String certificatesDesc;

    @ApiModelProperty(value = "家教经验 / 教学成果")
    private String teachingExperience;

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

    @ApiModelProperty(value = "登录次数")
    private Integer loginCount;

    @ApiModelProperty(value = "最近登录时间")
    private java.time.LocalDateTime lastLoginTime;

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

    @ApiModelProperty(value = "是否在主页展示成功记录(1=是, 0=否)")
    private Integer showSuccessRecord;

    @ApiModelProperty(value = "成功记录列表 (脱敏)")
    private List<SuccessRecordItem> successRecords;

    @Data
    @Accessors(chain = true)
    @ApiModel(description = "成功记录(脱敏)")
    public static class SuccessRecordItem implements Serializable {
        private static final long serialVersionUID = 1L;
        @ApiModelProperty(value = "年级") private String grade;
        @ApiModelProperty(value = "科目 (CSV)") private String subjects;
        @ApiModelProperty(value = "区+地点") private String location;
        @ApiModelProperty(value = "需求摘要") private String detail;
        @ApiModelProperty(value = "撮合日期 yyyy-MM-dd") private String date;
    }

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
