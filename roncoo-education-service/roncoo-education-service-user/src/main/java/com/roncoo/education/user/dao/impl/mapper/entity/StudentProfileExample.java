package com.roncoo.education.user.dao.impl.mapper.entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class StudentProfileExample {
    protected String orderByClause;
    protected boolean distinct;
    protected List<Criteria> oredCriteria;
    protected int limitStart = -1;
    protected int pageSize = -1;

    public StudentProfileExample() { oredCriteria = new ArrayList<Criteria>(); }
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
        public Criteria andRealNameIsNull() { addCriterion("real_name is null"); return (Criteria) this; }
        public Criteria andRealNameIsNotNull() { addCriterion("real_name is not null"); return (Criteria) this; }
        public Criteria andRealNameEqualTo(String value) { addCriterion("real_name =", value, "realName"); return (Criteria) this; }
        public Criteria andRealNameNotEqualTo(String value) { addCriterion("real_name <>", value, "realName"); return (Criteria) this; }
        public Criteria andRealNameIn(List<String> values) { addCriterion("real_name in", values, "realName"); return (Criteria) this; }
        public Criteria andRealNameLike(String value) { addCriterion("real_name like", value, "realName"); return (Criteria) this; }
        public Criteria andGenderIsNull() { addCriterion("gender is null"); return (Criteria) this; }
        public Criteria andGenderIsNotNull() { addCriterion("gender is not null"); return (Criteria) this; }
        public Criteria andGenderEqualTo(Integer value) { addCriterion("gender =", value, "gender"); return (Criteria) this; }
        public Criteria andGenderNotEqualTo(Integer value) { addCriterion("gender <>", value, "gender"); return (Criteria) this; }
        public Criteria andGenderIn(List<Integer> values) { addCriterion("gender in", values, "gender"); return (Criteria) this; }
        public Criteria andGenderGreaterThan(Integer value) { addCriterion("gender >", value, "gender"); return (Criteria) this; }
        public Criteria andGenderLessThan(Integer value) { addCriterion("gender <", value, "gender"); return (Criteria) this; }
        public Criteria andGenderBetween(Integer value1, Integer value2) { addCriterion("gender between", value1, value2, "gender"); return (Criteria) this; }
        public Criteria andGradeIsNull() { addCriterion("grade is null"); return (Criteria) this; }
        public Criteria andGradeIsNotNull() { addCriterion("grade is not null"); return (Criteria) this; }
        public Criteria andGradeEqualTo(String value) { addCriterion("grade =", value, "grade"); return (Criteria) this; }
        public Criteria andGradeNotEqualTo(String value) { addCriterion("grade <>", value, "grade"); return (Criteria) this; }
        public Criteria andGradeIn(List<String> values) { addCriterion("grade in", values, "grade"); return (Criteria) this; }
        public Criteria andGradeLike(String value) { addCriterion("grade like", value, "grade"); return (Criteria) this; }
        public Criteria andSchoolIsNull() { addCriterion("school is null"); return (Criteria) this; }
        public Criteria andSchoolIsNotNull() { addCriterion("school is not null"); return (Criteria) this; }
        public Criteria andSchoolEqualTo(String value) { addCriterion("school =", value, "school"); return (Criteria) this; }
        public Criteria andSchoolNotEqualTo(String value) { addCriterion("school <>", value, "school"); return (Criteria) this; }
        public Criteria andSchoolIn(List<String> values) { addCriterion("school in", values, "school"); return (Criteria) this; }
        public Criteria andSchoolLike(String value) { addCriterion("school like", value, "school"); return (Criteria) this; }
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
        public Criteria andDistrictIdIsNull() { addCriterion("district_id is null"); return (Criteria) this; }
        public Criteria andDistrictIdIsNotNull() { addCriterion("district_id is not null"); return (Criteria) this; }
        public Criteria andDistrictIdEqualTo(Long value) { addCriterion("district_id =", value, "districtId"); return (Criteria) this; }
        public Criteria andDistrictIdNotEqualTo(Long value) { addCriterion("district_id <>", value, "districtId"); return (Criteria) this; }
        public Criteria andDistrictIdIn(List<Long> values) { addCriterion("district_id in", values, "districtId"); return (Criteria) this; }
        public Criteria andDistrictIdGreaterThan(Long value) { addCriterion("district_id >", value, "districtId"); return (Criteria) this; }
        public Criteria andDistrictIdLessThan(Long value) { addCriterion("district_id <", value, "districtId"); return (Criteria) this; }
        public Criteria andDistrictIdBetween(Long value1, Long value2) { addCriterion("district_id between", value1, value2, "districtId"); return (Criteria) this; }
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
