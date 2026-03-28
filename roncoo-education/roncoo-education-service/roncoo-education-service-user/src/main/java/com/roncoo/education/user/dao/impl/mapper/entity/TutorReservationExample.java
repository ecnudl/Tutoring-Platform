package com.roncoo.education.user.dao.impl.mapper.entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class TutorReservationExample {
    protected String orderByClause;
    protected boolean distinct;
    protected List<Criteria> oredCriteria;
    protected int limitStart = -1;
    protected int pageSize = -1;

    public TutorReservationExample() { oredCriteria = new ArrayList<Criteria>(); }
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
        public Criteria andStudentUserIdIsNull() { addCriterion("student_user_id is null"); return (Criteria) this; }
        public Criteria andStudentUserIdIsNotNull() { addCriterion("student_user_id is not null"); return (Criteria) this; }
        public Criteria andStudentUserIdEqualTo(Long value) { addCriterion("student_user_id =", value, "studentUserId"); return (Criteria) this; }
        public Criteria andStudentUserIdNotEqualTo(Long value) { addCriterion("student_user_id <>", value, "studentUserId"); return (Criteria) this; }
        public Criteria andStudentUserIdIn(List<Long> values) { addCriterion("student_user_id in", values, "studentUserId"); return (Criteria) this; }
        public Criteria andStudentUserIdGreaterThan(Long value) { addCriterion("student_user_id >", value, "studentUserId"); return (Criteria) this; }
        public Criteria andStudentUserIdLessThan(Long value) { addCriterion("student_user_id <", value, "studentUserId"); return (Criteria) this; }
        public Criteria andStudentUserIdBetween(Long value1, Long value2) { addCriterion("student_user_id between", value1, value2, "studentUserId"); return (Criteria) this; }
        public Criteria andTutorUserIdIsNull() { addCriterion("tutor_user_id is null"); return (Criteria) this; }
        public Criteria andTutorUserIdIsNotNull() { addCriterion("tutor_user_id is not null"); return (Criteria) this; }
        public Criteria andTutorUserIdEqualTo(Long value) { addCriterion("tutor_user_id =", value, "tutorUserId"); return (Criteria) this; }
        public Criteria andTutorUserIdNotEqualTo(Long value) { addCriterion("tutor_user_id <>", value, "tutorUserId"); return (Criteria) this; }
        public Criteria andTutorUserIdIn(List<Long> values) { addCriterion("tutor_user_id in", values, "tutorUserId"); return (Criteria) this; }
        public Criteria andTutorUserIdGreaterThan(Long value) { addCriterion("tutor_user_id >", value, "tutorUserId"); return (Criteria) this; }
        public Criteria andTutorUserIdLessThan(Long value) { addCriterion("tutor_user_id <", value, "tutorUserId"); return (Criteria) this; }
        public Criteria andTutorUserIdBetween(Long value1, Long value2) { addCriterion("tutor_user_id between", value1, value2, "tutorUserId"); return (Criteria) this; }
        public Criteria andTutorIdIsNull() { addCriterion("tutor_id is null"); return (Criteria) this; }
        public Criteria andTutorIdIsNotNull() { addCriterion("tutor_id is not null"); return (Criteria) this; }
        public Criteria andTutorIdEqualTo(Long value) { addCriterion("tutor_id =", value, "tutorId"); return (Criteria) this; }
        public Criteria andTutorIdNotEqualTo(Long value) { addCriterion("tutor_id <>", value, "tutorId"); return (Criteria) this; }
        public Criteria andTutorIdIn(List<Long> values) { addCriterion("tutor_id in", values, "tutorId"); return (Criteria) this; }
        public Criteria andTutorIdGreaterThan(Long value) { addCriterion("tutor_id >", value, "tutorId"); return (Criteria) this; }
        public Criteria andTutorIdLessThan(Long value) { addCriterion("tutor_id <", value, "tutorId"); return (Criteria) this; }
        public Criteria andTutorIdBetween(Long value1, Long value2) { addCriterion("tutor_id between", value1, value2, "tutorId"); return (Criteria) this; }
        public Criteria andSubjectIdIsNull() { addCriterion("subject_id is null"); return (Criteria) this; }
        public Criteria andSubjectIdIsNotNull() { addCriterion("subject_id is not null"); return (Criteria) this; }
        public Criteria andSubjectIdEqualTo(Long value) { addCriterion("subject_id =", value, "subjectId"); return (Criteria) this; }
        public Criteria andSubjectIdNotEqualTo(Long value) { addCriterion("subject_id <>", value, "subjectId"); return (Criteria) this; }
        public Criteria andSubjectIdIn(List<Long> values) { addCriterion("subject_id in", values, "subjectId"); return (Criteria) this; }
        public Criteria andSubjectIdGreaterThan(Long value) { addCriterion("subject_id >", value, "subjectId"); return (Criteria) this; }
        public Criteria andSubjectIdLessThan(Long value) { addCriterion("subject_id <", value, "subjectId"); return (Criteria) this; }
        public Criteria andSubjectIdBetween(Long value1, Long value2) { addCriterion("subject_id between", value1, value2, "subjectId"); return (Criteria) this; }
        public Criteria andResStatusIsNull() { addCriterion("res_status is null"); return (Criteria) this; }
        public Criteria andResStatusIsNotNull() { addCriterion("res_status is not null"); return (Criteria) this; }
        public Criteria andResStatusEqualTo(Integer value) { addCriterion("res_status =", value, "resStatus"); return (Criteria) this; }
        public Criteria andResStatusNotEqualTo(Integer value) { addCriterion("res_status <>", value, "resStatus"); return (Criteria) this; }
        public Criteria andResStatusIn(List<Integer> values) { addCriterion("res_status in", values, "resStatus"); return (Criteria) this; }
        public Criteria andResStatusGreaterThan(Integer value) { addCriterion("res_status >", value, "resStatus"); return (Criteria) this; }
        public Criteria andResStatusLessThan(Integer value) { addCriterion("res_status <", value, "resStatus"); return (Criteria) this; }
        public Criteria andResStatusBetween(Integer value1, Integer value2) { addCriterion("res_status between", value1, value2, "resStatus"); return (Criteria) this; }
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
