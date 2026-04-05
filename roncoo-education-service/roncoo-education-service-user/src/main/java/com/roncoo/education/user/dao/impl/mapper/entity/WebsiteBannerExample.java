package com.roncoo.education.user.dao.impl.mapper.entity;

import java.util.ArrayList;
import java.util.List;

public class WebsiteBannerExample {
    protected String orderByClause;
    protected boolean distinct;
    protected List<Criteria> oredCriteria;
    protected int limitStart = -1;
    protected int pageSize = -1;

    public WebsiteBannerExample() { oredCriteria = new ArrayList<Criteria>(); }
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
        public Criteria andCityIdIsNull() { addCriterion("city_id is null"); return (Criteria) this; }
        public Criteria andCityIdIsNotNull() { addCriterion("city_id is not null"); return (Criteria) this; }
        public Criteria andCityIdEqualTo(Long value) { addCriterion("city_id =", value, "cityId"); return (Criteria) this; }
        public Criteria andCityIdNotEqualTo(Long value) { addCriterion("city_id <>", value, "cityId"); return (Criteria) this; }
        public Criteria andCityIdIn(List<Long> values) { addCriterion("city_id in", values, "cityId"); return (Criteria) this; }
        public Criteria andCityIdGreaterThan(Long value) { addCriterion("city_id >", value, "cityId"); return (Criteria) this; }
        public Criteria andCityIdLessThan(Long value) { addCriterion("city_id <", value, "cityId"); return (Criteria) this; }
        public Criteria andCityIdBetween(Long value1, Long value2) { addCriterion("city_id between", value1, value2, "cityId"); return (Criteria) this; }
        public Criteria andBannerTitleIsNull() { addCriterion("banner_title is null"); return (Criteria) this; }
        public Criteria andBannerTitleIsNotNull() { addCriterion("banner_title is not null"); return (Criteria) this; }
        public Criteria andBannerTitleEqualTo(String value) { addCriterion("banner_title =", value, "bannerTitle"); return (Criteria) this; }
        public Criteria andBannerTitleNotEqualTo(String value) { addCriterion("banner_title <>", value, "bannerTitle"); return (Criteria) this; }
        public Criteria andBannerTitleIn(List<String> values) { addCriterion("banner_title in", values, "bannerTitle"); return (Criteria) this; }
        public Criteria andBannerTitleLike(String value) { addCriterion("banner_title like", value, "bannerTitle"); return (Criteria) this; }
        public Criteria andPositionIsNull() { addCriterion("position is null"); return (Criteria) this; }
        public Criteria andPositionIsNotNull() { addCriterion("position is not null"); return (Criteria) this; }
        public Criteria andPositionEqualTo(String value) { addCriterion("position =", value, "position"); return (Criteria) this; }
        public Criteria andPositionNotEqualTo(String value) { addCriterion("position <>", value, "position"); return (Criteria) this; }
        public Criteria andPositionIn(List<String> values) { addCriterion("position in", values, "position"); return (Criteria) this; }
        public Criteria andPositionLike(String value) { addCriterion("position like", value, "position"); return (Criteria) this; }
        public Criteria andSortIsNull() { addCriterion("sort is null"); return (Criteria) this; }
        public Criteria andSortIsNotNull() { addCriterion("sort is not null"); return (Criteria) this; }
        public Criteria andSortEqualTo(Integer value) { addCriterion("sort =", value, "sort"); return (Criteria) this; }
        public Criteria andSortNotEqualTo(Integer value) { addCriterion("sort <>", value, "sort"); return (Criteria) this; }
        public Criteria andSortIn(List<Integer> values) { addCriterion("sort in", values, "sort"); return (Criteria) this; }
        public Criteria andSortGreaterThan(Integer value) { addCriterion("sort >", value, "sort"); return (Criteria) this; }
        public Criteria andSortLessThan(Integer value) { addCriterion("sort <", value, "sort"); return (Criteria) this; }
        public Criteria andSortBetween(Integer value1, Integer value2) { addCriterion("sort between", value1, value2, "sort"); return (Criteria) this; }
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
