package com.rxh.square.pojo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class MerchantQuotaRiskExample implements Serializable {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public MerchantQuotaRiskExample() {
        oredCriteria = new ArrayList<Criteria>();
    }

    public void setOrderByClause(String orderByClause) {
        this.orderByClause = orderByClause;
    }

    public String getOrderByClause() {
        return orderByClause;
    }

    public void setDistinct(boolean distinct) {
        this.distinct = distinct;
    }

    public boolean isDistinct() {
        return distinct;
    }

    public List<Criteria> getOredCriteria() {
        return oredCriteria;
    }

    public void or(Criteria criteria) {
        oredCriteria.add(criteria);
    }

    public Criteria or() {
        Criteria criteria = createCriteriaInternal();
        oredCriteria.add(criteria);
        return criteria;
    }

    public Criteria createCriteria() {
        Criteria criteria = createCriteriaInternal();
        if (oredCriteria.size() == 0) {
            oredCriteria.add(criteria);
        }
        return criteria;
    }

    protected Criteria createCriteriaInternal() {
        Criteria criteria = new Criteria();
        return criteria;
    }

    public void clear() {
        oredCriteria.clear();
        orderByClause = null;
        distinct = false;
    }

    protected abstract static class GeneratedCriteria {
        protected List<Criterion> criteria;

        protected GeneratedCriteria() {
            super();
            criteria = new ArrayList<Criterion>();
        }

        public boolean isValid() {
            return criteria.size() > 0;
        }

        public List<Criterion> getAllCriteria() {
            return criteria;
        }

        public List<Criterion> getCriteria() {
            return criteria;
        }

        protected void addCriterion(String condition) {
            if (condition == null) {
                throw new RuntimeException("Value for condition cannot be null");
            }
            criteria.add(new Criterion(condition));
        }

        protected void addCriterion(String condition, Object value, String property) {
            if (value == null) {
                throw new RuntimeException("Value for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value));
        }

        protected void addCriterion(String condition, Object value1, Object value2, String property) {
            if (value1 == null || value2 == null) {
                throw new RuntimeException("Between values for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value1, value2));
        }

        public Criteria andMerIdIsNull() {
            addCriterion("mer_id is null");
            return (Criteria) this;
        }

        public Criteria andMerIdIsNotNull() {
            addCriterion("mer_id is not null");
            return (Criteria) this;
        }

        public Criteria andMerIdEqualTo(String value) {
            addCriterion("mer_id =", value, "merId");
            return (Criteria) this;
        }

        public Criteria andMerIdNotEqualTo(String value) {
            addCriterion("mer_id <>", value, "merId");
            return (Criteria) this;
        }

        public Criteria andMerIdGreaterThan(String value) {
            addCriterion("mer_id >", value, "merId");
            return (Criteria) this;
        }

        public Criteria andMerIdGreaterThanOrEqualTo(String value) {
            addCriterion("mer_id >=", value, "merId");
            return (Criteria) this;
        }

        public Criteria andMerIdLessThan(String value) {
            addCriterion("mer_id <", value, "merId");
            return (Criteria) this;
        }

        public Criteria andMerIdLessThanOrEqualTo(String value) {
            addCriterion("mer_id <=", value, "merId");
            return (Criteria) this;
        }

        public Criteria andMerIdLike(String value) {
            addCriterion("mer_id like", value, "merId");
            return (Criteria) this;
        }

        public Criteria andMerIdNotLike(String value) {
            addCriterion("mer_id not like", value, "merId");
            return (Criteria) this;
        }

        public Criteria andMerIdIn(List<String> values) {
            addCriterion("mer_id in", values, "merId");
            return (Criteria) this;
        }

        public Criteria andMerIdNotIn(List<String> values) {
            addCriterion("mer_id not in", values, "merId");
            return (Criteria) this;
        }

        public Criteria andMerIdBetween(String value1, String value2) {
            addCriterion("mer_id between", value1, value2, "merId");
            return (Criteria) this;
        }

        public Criteria andMerIdNotBetween(String value1, String value2) {
            addCriterion("mer_id not between", value1, value2, "merId");
            return (Criteria) this;
        }

        public Criteria andSingleQuotaAmountIsNull() {
            addCriterion("single_quota_amount is null");
            return (Criteria) this;
        }

        public Criteria andSingleQuotaAmountIsNotNull() {
            addCriterion("single_quota_amount is not null");
            return (Criteria) this;
        }

        public Criteria andSingleQuotaAmountEqualTo(BigDecimal value) {
            addCriterion("single_quota_amount =", value, "singleQuotaAmount");
            return (Criteria) this;
        }

        public Criteria andSingleQuotaAmountNotEqualTo(BigDecimal value) {
            addCriterion("single_quota_amount <>", value, "singleQuotaAmount");
            return (Criteria) this;
        }

        public Criteria andSingleQuotaAmountGreaterThan(BigDecimal value) {
            addCriterion("single_quota_amount >", value, "singleQuotaAmount");
            return (Criteria) this;
        }

        public Criteria andSingleQuotaAmountGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("single_quota_amount >=", value, "singleQuotaAmount");
            return (Criteria) this;
        }

        public Criteria andSingleQuotaAmountLessThan(BigDecimal value) {
            addCriterion("single_quota_amount <", value, "singleQuotaAmount");
            return (Criteria) this;
        }

        public Criteria andSingleQuotaAmountLessThanOrEqualTo(BigDecimal value) {
            addCriterion("single_quota_amount <=", value, "singleQuotaAmount");
            return (Criteria) this;
        }

        public Criteria andSingleQuotaAmountIn(List<BigDecimal> values) {
            addCriterion("single_quota_amount in", values, "singleQuotaAmount");
            return (Criteria) this;
        }

        public Criteria andSingleQuotaAmountNotIn(List<BigDecimal> values) {
            addCriterion("single_quota_amount not in", values, "singleQuotaAmount");
            return (Criteria) this;
        }

        public Criteria andSingleQuotaAmountBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("single_quota_amount between", value1, value2, "singleQuotaAmount");
            return (Criteria) this;
        }

        public Criteria andSingleQuotaAmountNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("single_quota_amount not between", value1, value2, "singleQuotaAmount");
            return (Criteria) this;
        }

        public Criteria andDayQuotaAmountIsNull() {
            addCriterion("day_quota_amount is null");
            return (Criteria) this;
        }

        public Criteria andDayQuotaAmountIsNotNull() {
            addCriterion("day_quota_amount is not null");
            return (Criteria) this;
        }

        public Criteria andDayQuotaAmountEqualTo(BigDecimal value) {
            addCriterion("day_quota_amount =", value, "dayQuotaAmount");
            return (Criteria) this;
        }

        public Criteria andDayQuotaAmountNotEqualTo(BigDecimal value) {
            addCriterion("day_quota_amount <>", value, "dayQuotaAmount");
            return (Criteria) this;
        }

        public Criteria andDayQuotaAmountGreaterThan(BigDecimal value) {
            addCriterion("day_quota_amount >", value, "dayQuotaAmount");
            return (Criteria) this;
        }

        public Criteria andDayQuotaAmountGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("day_quota_amount >=", value, "dayQuotaAmount");
            return (Criteria) this;
        }

        public Criteria andDayQuotaAmountLessThan(BigDecimal value) {
            addCriterion("day_quota_amount <", value, "dayQuotaAmount");
            return (Criteria) this;
        }

        public Criteria andDayQuotaAmountLessThanOrEqualTo(BigDecimal value) {
            addCriterion("day_quota_amount <=", value, "dayQuotaAmount");
            return (Criteria) this;
        }

        public Criteria andDayQuotaAmountIn(List<BigDecimal> values) {
            addCriterion("day_quota_amount in", values, "dayQuotaAmount");
            return (Criteria) this;
        }

        public Criteria andDayQuotaAmountNotIn(List<BigDecimal> values) {
            addCriterion("day_quota_amount not in", values, "dayQuotaAmount");
            return (Criteria) this;
        }

        public Criteria andDayQuotaAmountBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("day_quota_amount between", value1, value2, "dayQuotaAmount");
            return (Criteria) this;
        }

        public Criteria andDayQuotaAmountNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("day_quota_amount not between", value1, value2, "dayQuotaAmount");
            return (Criteria) this;
        }

        public Criteria andMonthQuotaAmountIsNull() {
            addCriterion("month_quota_amount is null");
            return (Criteria) this;
        }

        public Criteria andMonthQuotaAmountIsNotNull() {
            addCriterion("month_quota_amount is not null");
            return (Criteria) this;
        }

        public Criteria andMonthQuotaAmountEqualTo(BigDecimal value) {
            addCriterion("month_quota_amount =", value, "monthQuotaAmount");
            return (Criteria) this;
        }

        public Criteria andMonthQuotaAmountNotEqualTo(BigDecimal value) {
            addCriterion("month_quota_amount <>", value, "monthQuotaAmount");
            return (Criteria) this;
        }

        public Criteria andMonthQuotaAmountGreaterThan(BigDecimal value) {
            addCriterion("month_quota_amount >", value, "monthQuotaAmount");
            return (Criteria) this;
        }

        public Criteria andMonthQuotaAmountGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("month_quota_amount >=", value, "monthQuotaAmount");
            return (Criteria) this;
        }

        public Criteria andMonthQuotaAmountLessThan(BigDecimal value) {
            addCriterion("month_quota_amount <", value, "monthQuotaAmount");
            return (Criteria) this;
        }

        public Criteria andMonthQuotaAmountLessThanOrEqualTo(BigDecimal value) {
            addCriterion("month_quota_amount <=", value, "monthQuotaAmount");
            return (Criteria) this;
        }

        public Criteria andMonthQuotaAmountIn(List<BigDecimal> values) {
            addCriterion("month_quota_amount in", values, "monthQuotaAmount");
            return (Criteria) this;
        }

        public Criteria andMonthQuotaAmountNotIn(List<BigDecimal> values) {
            addCriterion("month_quota_amount not in", values, "monthQuotaAmount");
            return (Criteria) this;
        }

        public Criteria andMonthQuotaAmountBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("month_quota_amount between", value1, value2, "monthQuotaAmount");
            return (Criteria) this;
        }

        public Criteria andMonthQuotaAmountNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("month_quota_amount not between", value1, value2, "monthQuotaAmount");
            return (Criteria) this;
        }
    }

    public static class Criteria extends GeneratedCriteria {

        protected Criteria() {
            super();
        }
    }

    public static class Criterion {
        private String condition;

        private Object value;

        private Object secondValue;

        private boolean noValue;

        private boolean singleValue;

        private boolean betweenValue;

        private boolean listValue;

        private String typeHandler;

        public String getCondition() {
            return condition;
        }

        public Object getValue() {
            return value;
        }

        public Object getSecondValue() {
            return secondValue;
        }

        public boolean isNoValue() {
            return noValue;
        }

        public boolean isSingleValue() {
            return singleValue;
        }

        public boolean isBetweenValue() {
            return betweenValue;
        }

        public boolean isListValue() {
            return listValue;
        }

        public String getTypeHandler() {
            return typeHandler;
        }

        protected Criterion(String condition) {
            super();
            this.condition = condition;
            this.typeHandler = null;
            this.noValue = true;
        }

        protected Criterion(String condition, Object value, String typeHandler) {
            super();
            this.condition = condition;
            this.value = value;
            this.typeHandler = typeHandler;
            if (value instanceof List<?>) {
                this.listValue = true;
            } else {
                this.singleValue = true;
            }
        }

        protected Criterion(String condition, Object value) {
            this(condition, value, null);
        }

        protected Criterion(String condition, Object value, Object secondValue, String typeHandler) {
            super();
            this.condition = condition;
            this.value = value;
            this.secondValue = secondValue;
            this.typeHandler = typeHandler;
            this.betweenValue = true;
        }

        protected Criterion(String condition, Object value, Object secondValue) {
            this(condition, value, secondValue, null);
        }
    }
}