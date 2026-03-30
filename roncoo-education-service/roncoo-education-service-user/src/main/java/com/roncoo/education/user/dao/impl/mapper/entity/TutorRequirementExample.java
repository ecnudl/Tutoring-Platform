package com.roncoo.education.user.dao.impl.mapper.entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class TutorRequirementExample {
    protected String orderByClause;
    protected boolean distinct;
    protected List<Criteria> oredCriteria;
    protected int limitStart = -1;
    protected int pageSize = -1;

    public TutorRequirementExample() { oredCriteria = new ArrayList<Criteria>(); }
    public void setOrderByClause(String orderByClause) { this.orderByClause = orderByClause; }
    public String getOrderByClause() { return orderByClause; }
    public void setDistinct(boolean distinct) { this.distinct = distinct; }
    public boolean isDistinct() { return distinct; }
    public List<Criteria> getOredCriteria() { return oredCriteria; }
    public void or(Criteria criteria) { oredCriteria.add(criteria); }
    public Criteria or() { Criteria c = createCriteriaInternal(); oredCriteria.add(c); return c; }
    public Criteria createCriteria() { Criteria c = createCriteriaInternal(); if (oredCriteria.size() == 0) { oredCriteria.add(c); } return c; }
    protected Criteria createCriteriaInternal() { return new Criteria(); }
    public void clear() { oredCriteria.clear(); orderByClause = null; distinct = false; }
    public void setLimitStart(int limitStart) { this.limitStart = limitStart; }
    public int getLimitStart() { return limitStart; }
    public void setPageSize(int pageSize) { this.pageSize = pageSize; }
    public int getPageSize() { return pageSize; }

    protected abstract static class GeneratedCriteria {
        protected List<Criterion> criteria;
        protected GeneratedCriteria() { super(); criteria = new ArrayList<Criterion>(); }
        public boolean isValid() { return criteria.size() > 0; }
        public List<Criterion> getAllCriteria() { return criteria; }
        public List<Criterion> getCriteria() { return criteria; }
        protected void addCriterion(String condition) { if (condition == null) throw new RuntimeException("Value for condition cannot be null"); criteria.add(new Criterion(condition)); }
        protected void addCriterion(String condition, Object value, String property) { if (value == null) throw new RuntimeException("Value for " + property + " cannot be null"); criteria.add(new Criterion(condition, value)); }
        protected void addCriterion(String condition, Object value1, Object value2, String property) { if (value1 == null || value2 == null) throw new RuntimeException("Between values for " + property + " cannot be null"); criteria.add(new Criterion(condition, value1, value2)); }
        public Criteria andIdIsNull() { addCriterion("id is null"); return (Criteria) this; }
        public Criteria andIdIsNotNull() { addCriterion("id is not null"); return (Criteria) this; }
        public Criteria andIdEqualTo(Long value) { addCriterion("id =", value, "id"); return (Criteria) this; }
        public Criteria andIdNotEqualTo(Long value) { addCriterion("id <>", value, "id"); return (Criteria) this; }
        public Criteria andIdIn(List<Long> values) { addCriterion("id in", values, "id"); return (Criteria) this; }
        public Criteria andIdGreaterThan(Long value) { addCriterion("id >", value, "id"); return (Criteria) this; }
        public Criteria andIdLessThan(Long value) { addCriterion("id <", value, "id"); return (Criteria) this; }
        public Criteria andIdBetween(Long value1, Long value2) { addCriterion("id between", value1, value2, "id"); return (Criteria) this; }
        public Criteria andStatusIdIsNull() { addCriterion("status_id is null"); return (Criteria) this; }
        public Criteria andStatusIdIsNotNull() { addCriterion("status_id is not null"); return (Criteria) this; }
        public Criteria andStatusIdEqualTo(Integer value) { addCriterion("status_id =", value, "statusId"); return (Criteria) this; }
        public Criteria andStatusIdNotEqualTo(Integer value) { addCriterion("status_id <>", value, "statusId"); return (Criteria) this; }
        public Criteria andStatusIdIn(List<Integer> values) { addCriterion("status_id in", values, "statusId"); return (Criteria) this; }
        public Criteria andStatusIdGreaterThan(Integer value) { addCriterion("status_id >", value, "statusId"); return (Criteria) this; }
        public Criteria andStatusIdLessThan(Integer value) { addCriterion("status_id <", value, "statusId"); return (Criteria) this; }
        public Criteria andStatusIdBetween(Integer value1, Integer value2) { addCriterion("status_id between", value1, value2, "statusId"); return (Criteria) this; }
        public Criteria andUserIdIsNull() { addCriterion("user_id is null"); return (Criteria) this; }
        public Criteria andUserIdIsNotNull() { addCriterion("user_id is not null"); return (Criteria) this; }
        public Criteria andUserIdEqualTo(Long value) { addCriterion("user_id =", value, "userId"); return (Criteria) this; }
        public Criteria andUserIdNotEqualTo(Long value) { addCriterion("user_id <>", value, "userId"); return (Criteria) this; }
        public Criteria andUserIdIn(List<Long> values) { addCriterion("user_id in", values, "userId"); return (Criteria) this; }
        public Criteria andUserIdGreaterThan(Long value) { addCriterion("user_id >", value, "userId"); return (Criteria) this; }
        public Criteria andUserIdLessThan(Long value) { addCriterion("user_id <", value, "userId"); return (Criteria) this; }
        public Criteria andUserIdBetween(Long value1, Long value2) { addCriterion("user_id between", value1, value2, "userId"); return (Criteria) this; }
        public Criteria andTitleIsNull() { addCriterion("title is null"); return (Criteria) this; }
        public Criteria andTitleIsNotNull() { addCriterion("title is not null"); return (Criteria) this; }
        public Criteria andTitleEqualTo(String value) { addCriterion("title =", value, "title"); return (Criteria) this; }
        public Criteria andTitleNotEqualTo(String value) { addCriterion("title <>", value, "title"); return (Criteria) this; }
        public Criteria andTitleIn(List<String> values) { addCriterion("title in", values, "title"); return (Criteria) this; }
        public Criteria andTitleLike(String value) { addCriterion("title like", value, "title"); return (Criteria) this; }
        public Criteria andGradeIdIsNull() { addCriterion("grade_id is null"); return (Criteria) this; }
        public Criteria andGradeIdIsNotNull() { addCriterion("grade_id is not null"); return (Criteria) this; }
        public Criteria andGradeIdEqualTo(Long value) { addCriterion("grade_id =", value, "gradeId"); return (Criteria) this; }
        public Criteria andGradeIdNotEqualTo(Long value) { addCriterion("grade_id <>", value, "gradeId"); return (Criteria) this; }
        public Criteria andGradeIdIn(List<Long> values) { addCriterion("grade_id in", values, "gradeId"); return (Criteria) this; }
        public Criteria andGradeIdGreaterThan(Long value) { addCriterion("grade_id >", value, "gradeId"); return (Criteria) this; }
        public Criteria andGradeIdLessThan(Long value) { addCriterion("grade_id <", value, "gradeId"); return (Criteria) this; }
        public Criteria andGradeIdBetween(Long value1, Long value2) { addCriterion("grade_id between", value1, value2, "gradeId"); return (Criteria) this; }
        public Criteria andProvinceIdIsNull() { addCriterion("province_id is null"); return (Criteria) this; }
        public Criteria andProvinceIdIsNotNull() { addCriterion("province_id is not null"); return (Criteria) this; }
        public Criteria andProvinceIdEqualTo(Long value) { addCriterion("province_id =", value, "provinceId"); return (Criteria) this; }
        public Criteria andProvinceIdNotEqualTo(Long value) { addCriterion("province_id <>", value, "provinceId"); return (Criteria) this; }
        public Criteria andProvinceIdIn(List<Long> values) { addCriterion("province_id in", values, "provinceId"); return (Criteria) this; }
        public Criteria andProvinceIdGreaterThan(Long value) { addCriterion("province_id >", value, "provinceId"); return (Criteria) this; }
        public Criteria andProvinceIdLessThan(Long value) { addCriterion("province_id <", value, "provinceId"); return (Criteria) this; }
        public Criteria andProvinceIdBetween(Long value1, Long value2) { addCriterion("province_id between", value1, value2, "provinceId"); return (Criteria) this; }
        public Criteria andCityIdIsNull() { addCriterion("city_id is null"); return (Criteria) this; }
        public Criteria andCityIdIsNotNull() { addCriterion("city_id is not null"); return (Criteria) this; }
        public Criteria andCityIdEqualTo(Long value) { addCriterion("city_id =", value, "cityId"); return (Criteria) this; }
        public Criteria andCityIdNotEqualTo(Long value) { addCriterion("city_id <>", value, "cityId"); return (Criteria) this; }
        public Criteria andCityIdIn(List<Long> values) { addCriterion("city_id in", values, "cityId"); return (Criteria) this; }
        public Criteria andCityIdGreaterThan(Long value) { addCriterion("city_id >", value, "cityId"); return (Criteria) this; }
        public Criteria andCityIdLessThan(Long value) { addCriterion("city_id <", value, "cityId"); return (Criteria) this; }
        public Criteria andCityIdBetween(Long value1, Long value2) { addCriterion("city_id between", value1, value2, "cityId"); return (Criteria) this; }
        public Criteria andTutorGenderIsNull() { addCriterion("tutor_gender is null"); return (Criteria) this; }
        public Criteria andTutorGenderIsNotNull() { addCriterion("tutor_gender is not null"); return (Criteria) this; }
        public Criteria andTutorGenderEqualTo(Integer value) { addCriterion("tutor_gender =", value, "tutorGender"); return (Criteria) this; }
        public Criteria andTutorGenderNotEqualTo(Integer value) { addCriterion("tutor_gender <>", value, "tutorGender"); return (Criteria) this; }
        public Criteria andTutorGenderIn(List<Integer> values) { addCriterion("tutor_gender in", values, "tutorGender"); return (Criteria) this; }
        public Criteria andTutorGenderGreaterThan(Integer value) { addCriterion("tutor_gender >", value, "tutorGender"); return (Criteria) this; }
        public Criteria andTutorGenderLessThan(Integer value) { addCriterion("tutor_gender <", value, "tutorGender"); return (Criteria) this; }
        public Criteria andTutorGenderBetween(Integer value1, Integer value2) { addCriterion("tutor_gender between", value1, value2, "tutorGender"); return (Criteria) this; }
        public Criteria andReqStatusIsNull() { addCriterion("req_status is null"); return (Criteria) this; }
        public Criteria andReqStatusIsNotNull() { addCriterion("req_status is not null"); return (Criteria) this; }
        public Criteria andReqStatusEqualTo(Integer value) { addCriterion("req_status =", value, "reqStatus"); return (Criteria) this; }
        public Criteria andReqStatusNotEqualTo(Integer value) { addCriterion("req_status <>", value, "reqStatus"); return (Criteria) this; }
        public Criteria andReqStatusIn(List<Integer> values) { addCriterion("req_status in", values, "reqStatus"); return (Criteria) this; }
        public Criteria andReqStatusGreaterThan(Integer value) { addCriterion("req_status >", value, "reqStatus"); return (Criteria) this; }
        public Criteria andReqStatusLessThan(Integer value) { addCriterion("req_status <", value, "reqStatus"); return (Criteria) this; }
        public Criteria andReqStatusBetween(Integer value1, Integer value2) { addCriterion("req_status between", value1, value2, "reqStatus"); return (Criteria) this; }
        public Criteria andBudgetMinIsNull() { addCriterion("budget_min is null"); return (Criteria) this; }
        public Criteria andBudgetMinIsNotNull() { addCriterion("budget_min is not null"); return (Criteria) this; }
        public Criteria andBudgetMinEqualTo(BigDecimal value) { addCriterion("budget_min =", value, "budgetMin"); return (Criteria) this; }
        public Criteria andBudgetMinNotEqualTo(BigDecimal value) { addCriterion("budget_min <>", value, "budgetMin"); return (Criteria) this; }
        public Criteria andBudgetMinIn(List<BigDecimal> values) { addCriterion("budget_min in", values, "budgetMin"); return (Criteria) this; }
        public Criteria andBudgetMinGreaterThan(BigDecimal value) { addCriterion("budget_min >", value, "budgetMin"); return (Criteria) this; }
        public Criteria andBudgetMinLessThan(BigDecimal value) { addCriterion("budget_min <", value, "budgetMin"); return (Criteria) this; }
        public Criteria andBudgetMinBetween(BigDecimal value1, BigDecimal value2) { addCriterion("budget_min between", value1, value2, "budgetMin"); return (Criteria) this; }
        public Criteria andBudgetMaxIsNull() { addCriterion("budget_max is null"); return (Criteria) this; }
        public Criteria andBudgetMaxIsNotNull() { addCriterion("budget_max is not null"); return (Criteria) this; }
        public Criteria andBudgetMaxEqualTo(BigDecimal value) { addCriterion("budget_max =", value, "budgetMax"); return (Criteria) this; }
        public Criteria andBudgetMaxNotEqualTo(BigDecimal value) { addCriterion("budget_max <>", value, "budgetMax"); return (Criteria) this; }
        public Criteria andBudgetMaxIn(List<BigDecimal> values) { addCriterion("budget_max in", values, "budgetMax"); return (Criteria) this; }
        public Criteria andBudgetMaxGreaterThan(BigDecimal value) { addCriterion("budget_max >", value, "budgetMax"); return (Criteria) this; }
        public Criteria andBudgetMaxLessThan(BigDecimal value) { addCriterion("budget_max <", value, "budgetMax"); return (Criteria) this; }
        public Criteria andBudgetMaxBetween(BigDecimal value1, BigDecimal value2) { addCriterion("budget_max between", value1, value2, "budgetMax"); return (Criteria) this; }
        public Criteria andViewCountIsNull() { addCriterion("view_count is null"); return (Criteria) this; }
        public Criteria andViewCountIsNotNull() { addCriterion("view_count is not null"); return (Criteria) this; }
        public Criteria andViewCountEqualTo(Integer value) { addCriterion("view_count =", value, "viewCount"); return (Criteria) this; }
        public Criteria andViewCountNotEqualTo(Integer value) { addCriterion("view_count <>", value, "viewCount"); return (Criteria) this; }
        public Criteria andViewCountIn(List<Integer> values) { addCriterion("view_count in", values, "viewCount"); return (Criteria) this; }
        public Criteria andViewCountGreaterThan(Integer value) { addCriterion("view_count >", value, "viewCount"); return (Criteria) this; }
        public Criteria andViewCountLessThan(Integer value) { addCriterion("view_count <", value, "viewCount"); return (Criteria) this; }
        public Criteria andViewCountBetween(Integer value1, Integer value2) { addCriterion("view_count between", value1, value2, "viewCount"); return (Criteria) this; }
        public Criteria andApplicationCountIsNull() { addCriterion("application_count is null"); return (Criteria) this; }
        public Criteria andApplicationCountIsNotNull() { addCriterion("application_count is not null"); return (Criteria) this; }
        public Criteria andApplicationCountEqualTo(Integer value) { addCriterion("application_count =", value, "applicationCount"); return (Criteria) this; }
        public Criteria andApplicationCountNotEqualTo(Integer value) { addCriterion("application_count <>", value, "applicationCount"); return (Criteria) this; }
        public Criteria andApplicationCountIn(List<Integer> values) { addCriterion("application_count in", values, "applicationCount"); return (Criteria) this; }
        public Criteria andApplicationCountGreaterThan(Integer value) { addCriterion("application_count >", value, "applicationCount"); return (Criteria) this; }
        public Criteria andApplicationCountLessThan(Integer value) { addCriterion("application_count <", value, "applicationCount"); return (Criteria) this; }
        public Criteria andApplicationCountBetween(Integer value1, Integer value2) { addCriterion("application_count between", value1, value2, "applicationCount"); return (Criteria) this; }
    }

    public static class Criteria extends GeneratedCriteria { protected Criteria() { super(); } }

    public static class Criterion {
        private String condition; private Object value; private Object secondValue;
        private boolean noValue; private boolean singleValue; private boolean betweenValue; private boolean listValue;
        private String typeHandler;
        public String getCondition() { return condition; }
        public Object getValue() { return value; }
        public Object getSecondValue() { return secondValue; }
        public boolean isNoValue() { return noValue; }
        public boolean isSingleValue() { return singleValue; }
        public boolean isBetweenValue() { return betweenValue; }
        public boolean isListValue() { return listValue; }
        public String getTypeHandler() { return typeHandler; }
        protected Criterion(String condition) { super(); this.condition = condition; this.typeHandler = null; this.noValue = true; }
        protected Criterion(String condition, Object value, String typeHandler) { super(); this.condition = condition; this.value = value; this.typeHandler = typeHandler; if (value instanceof List<?>) { this.listValue = true; } else { this.singleValue = true; } }
        protected Criterion(String condition, Object value) { this(condition, value, null); }
        protected Criterion(String condition, Object value, Object secondValue, String typeHandler) { super(); this.condition = condition; this.value = value; this.secondValue = secondValue; this.typeHandler = typeHandler; this.betweenValue = true; }
        protected Criterion(String condition, Object value, Object secondValue) { this(condition, value, secondValue, null); }
    }
}
