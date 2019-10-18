package com.rxh.square.pojo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class AgentMerchantSettingExample implements Serializable {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public AgentMerchantSettingExample() {
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

        public Criteria andIdIsNull() {
            addCriterion("id is null");
            return (Criteria) this;
        }

        public Criteria andIdIsNotNull() {
            addCriterion("id is not null");
            return (Criteria) this;
        }

        public Criteria andIdEqualTo(String value) {
            addCriterion("id =", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotEqualTo(String value) {
            addCriterion("id <>", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdGreaterThan(String value) {
            addCriterion("id >", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdGreaterThanOrEqualTo(String value) {
            addCriterion("id >=", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLessThan(String value) {
            addCriterion("id <", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLessThanOrEqualTo(String value) {
            addCriterion("id <=", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLike(String value) {
            addCriterion("id like", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotLike(String value) {
            addCriterion("id not like", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdIn(List<String> values) {
            addCriterion("id in", values, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotIn(List<String> values) {
            addCriterion("id not in", values, "id");
            return (Criteria) this;
        }

        public Criteria andIdBetween(String value1, String value2) {
            addCriterion("id between", value1, value2, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotBetween(String value1, String value2) {
            addCriterion("id not between", value1, value2, "id");
            return (Criteria) this;
        }

        public Criteria andAgentMerchantIdIsNull() {
            addCriterion("agent_merchant_id is null");
            return (Criteria) this;
        }

        public Criteria andAgentMerchantIdIsNotNull() {
            addCriterion("agent_merchant_id is not null");
            return (Criteria) this;
        }

        public Criteria andAgentMerchantIdEqualTo(String value) {
            addCriterion("agent_merchant_id =", value, "agentMerchantId");
            return (Criteria) this;
        }

        public Criteria andAgentMerchantIdNotEqualTo(String value) {
            addCriterion("agent_merchant_id <>", value, "agentMerchantId");
            return (Criteria) this;
        }

        public Criteria andAgentMerchantIdGreaterThan(String value) {
            addCriterion("agent_merchant_id >", value, "agentMerchantId");
            return (Criteria) this;
        }

        public Criteria andAgentMerchantIdGreaterThanOrEqualTo(String value) {
            addCriterion("agent_merchant_id >=", value, "agentMerchantId");
            return (Criteria) this;
        }

        public Criteria andAgentMerchantIdLessThan(String value) {
            addCriterion("agent_merchant_id <", value, "agentMerchantId");
            return (Criteria) this;
        }

        public Criteria andAgentMerchantIdLessThanOrEqualTo(String value) {
            addCriterion("agent_merchant_id <=", value, "agentMerchantId");
            return (Criteria) this;
        }

        public Criteria andAgentMerchantIdLike(String value) {
            addCriterion("agent_merchant_id like", value, "agentMerchantId");
            return (Criteria) this;
        }

        public Criteria andAgentMerchantIdNotLike(String value) {
            addCriterion("agent_merchant_id not like", value, "agentMerchantId");
            return (Criteria) this;
        }

        public Criteria andAgentMerchantIdIn(List<String> values) {
            addCriterion("agent_merchant_id in", values, "agentMerchantId");
            return (Criteria) this;
        }

        public Criteria andAgentMerchantIdNotIn(List<String> values) {
            addCriterion("agent_merchant_id not in", values, "agentMerchantId");
            return (Criteria) this;
        }

        public Criteria andAgentMerchantIdBetween(String value1, String value2) {
            addCriterion("agent_merchant_id between", value1, value2, "agentMerchantId");
            return (Criteria) this;
        }

        public Criteria andAgentMerchantIdNotBetween(String value1, String value2) {
            addCriterion("agent_merchant_id not between", value1, value2, "agentMerchantId");
            return (Criteria) this;
        }

        public Criteria andPayTypeIsNull() {
            addCriterion("pay_type is null");
            return (Criteria) this;
        }

        public Criteria andPayTypeIsNotNull() {
            addCriterion("pay_type is not null");
            return (Criteria) this;
        }

        public Criteria andPayTypeEqualTo(String value) {
            addCriterion("pay_type =", value, "payType");
            return (Criteria) this;
        }

        public Criteria andPayTypeNotEqualTo(String value) {
            addCriterion("pay_type <>", value, "payType");
            return (Criteria) this;
        }

        public Criteria andPayTypeGreaterThan(String value) {
            addCriterion("pay_type >", value, "payType");
            return (Criteria) this;
        }

        public Criteria andPayTypeGreaterThanOrEqualTo(String value) {
            addCriterion("pay_type >=", value, "payType");
            return (Criteria) this;
        }

        public Criteria andPayTypeLessThan(String value) {
            addCriterion("pay_type <", value, "payType");
            return (Criteria) this;
        }

        public Criteria andPayTypeLessThanOrEqualTo(String value) {
            addCriterion("pay_type <=", value, "payType");
            return (Criteria) this;
        }

        public Criteria andPayTypeLike(String value) {
            addCriterion("pay_type like", value, "payType");
            return (Criteria) this;
        }

        public Criteria andPayTypeNotLike(String value) {
            addCriterion("pay_type not like", value, "payType");
            return (Criteria) this;
        }

        public Criteria andPayTypeIn(List<String> values) {
            addCriterion("pay_type in", values, "payType");
            return (Criteria) this;
        }

        public Criteria andPayTypeNotIn(List<String> values) {
            addCriterion("pay_type not in", values, "payType");
            return (Criteria) this;
        }

        public Criteria andPayTypeBetween(String value1, String value2) {
            addCriterion("pay_type between", value1, value2, "payType");
            return (Criteria) this;
        }

        public Criteria andPayTypeNotBetween(String value1, String value2) {
            addCriterion("pay_type not between", value1, value2, "payType");
            return (Criteria) this;
        }

        public Criteria andSingleFeeIsNull() {
            addCriterion("single_fee is null");
            return (Criteria) this;
        }

        public Criteria andSingleFeeIsNotNull() {
            addCriterion("single_fee is not null");
            return (Criteria) this;
        }

        public Criteria andSingleFeeEqualTo(BigDecimal value) {
            addCriterion("single_fee =", value, "singleFee");
            return (Criteria) this;
        }

        public Criteria andSingleFeeNotEqualTo(BigDecimal value) {
            addCriterion("single_fee <>", value, "singleFee");
            return (Criteria) this;
        }

        public Criteria andSingleFeeGreaterThan(BigDecimal value) {
            addCriterion("single_fee >", value, "singleFee");
            return (Criteria) this;
        }

        public Criteria andSingleFeeGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("single_fee >=", value, "singleFee");
            return (Criteria) this;
        }

        public Criteria andSingleFeeLessThan(BigDecimal value) {
            addCriterion("single_fee <", value, "singleFee");
            return (Criteria) this;
        }

        public Criteria andSingleFeeLessThanOrEqualTo(BigDecimal value) {
            addCriterion("single_fee <=", value, "singleFee");
            return (Criteria) this;
        }

        public Criteria andSingleFeeIn(List<BigDecimal> values) {
            addCriterion("single_fee in", values, "singleFee");
            return (Criteria) this;
        }

        public Criteria andSingleFeeNotIn(List<BigDecimal> values) {
            addCriterion("single_fee not in", values, "singleFee");
            return (Criteria) this;
        }

        public Criteria andSingleFeeBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("single_fee between", value1, value2, "singleFee");
            return (Criteria) this;
        }

        public Criteria andSingleFeeNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("single_fee not between", value1, value2, "singleFee");
            return (Criteria) this;
        }

        public Criteria andRateFeeIsNull() {
            addCriterion("rate_fee is null");
            return (Criteria) this;
        }

        public Criteria andRateFeeIsNotNull() {
            addCriterion("rate_fee is not null");
            return (Criteria) this;
        }

        public Criteria andRateFeeEqualTo(BigDecimal value) {
            addCriterion("rate_fee =", value, "rateFee");
            return (Criteria) this;
        }

        public Criteria andRateFeeNotEqualTo(BigDecimal value) {
            addCriterion("rate_fee <>", value, "rateFee");
            return (Criteria) this;
        }

        public Criteria andRateFeeGreaterThan(BigDecimal value) {
            addCriterion("rate_fee >", value, "rateFee");
            return (Criteria) this;
        }

        public Criteria andRateFeeGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("rate_fee >=", value, "rateFee");
            return (Criteria) this;
        }

        public Criteria andRateFeeLessThan(BigDecimal value) {
            addCriterion("rate_fee <", value, "rateFee");
            return (Criteria) this;
        }

        public Criteria andRateFeeLessThanOrEqualTo(BigDecimal value) {
            addCriterion("rate_fee <=", value, "rateFee");
            return (Criteria) this;
        }

        public Criteria andRateFeeIn(List<BigDecimal> values) {
            addCriterion("rate_fee in", values, "rateFee");
            return (Criteria) this;
        }

        public Criteria andRateFeeNotIn(List<BigDecimal> values) {
            addCriterion("rate_fee not in", values, "rateFee");
            return (Criteria) this;
        }

        public Criteria andRateFeeBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("rate_fee between", value1, value2, "rateFee");
            return (Criteria) this;
        }

        public Criteria andRateFeeNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("rate_fee not between", value1, value2, "rateFee");
            return (Criteria) this;
        }

        public Criteria andBondRateIsNull() {
            addCriterion("bond_rate is null");
            return (Criteria) this;
        }

        public Criteria andBondRateIsNotNull() {
            addCriterion("bond_rate is not null");
            return (Criteria) this;
        }

        public Criteria andBondRateEqualTo(BigDecimal value) {
            addCriterion("bond_rate =", value, "bondRate");
            return (Criteria) this;
        }

        public Criteria andBondRateNotEqualTo(BigDecimal value) {
            addCriterion("bond_rate <>", value, "bondRate");
            return (Criteria) this;
        }

        public Criteria andBondRateGreaterThan(BigDecimal value) {
            addCriterion("bond_rate >", value, "bondRate");
            return (Criteria) this;
        }

        public Criteria andBondRateGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("bond_rate >=", value, "bondRate");
            return (Criteria) this;
        }

        public Criteria andBondRateLessThan(BigDecimal value) {
            addCriterion("bond_rate <", value, "bondRate");
            return (Criteria) this;
        }

        public Criteria andBondRateLessThanOrEqualTo(BigDecimal value) {
            addCriterion("bond_rate <=", value, "bondRate");
            return (Criteria) this;
        }

        public Criteria andBondRateIn(List<BigDecimal> values) {
            addCriterion("bond_rate in", values, "bondRate");
            return (Criteria) this;
        }

        public Criteria andBondRateNotIn(List<BigDecimal> values) {
            addCriterion("bond_rate not in", values, "bondRate");
            return (Criteria) this;
        }

        public Criteria andBondRateBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("bond_rate between", value1, value2, "bondRate");
            return (Criteria) this;
        }

        public Criteria andBondRateNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("bond_rate not between", value1, value2, "bondRate");
            return (Criteria) this;
        }

        public Criteria andBondCycleIsNull() {
            addCriterion("bond_cycle is null");
            return (Criteria) this;
        }

        public Criteria andBondCycleIsNotNull() {
            addCriterion("bond_cycle is not null");
            return (Criteria) this;
        }

        public Criteria andBondCycleEqualTo(Integer value) {
            addCriterion("bond_cycle =", value, "bondCycle");
            return (Criteria) this;
        }

        public Criteria andBondCycleNotEqualTo(Integer value) {
            addCriterion("bond_cycle <>", value, "bondCycle");
            return (Criteria) this;
        }

        public Criteria andBondCycleGreaterThan(Integer value) {
            addCriterion("bond_cycle >", value, "bondCycle");
            return (Criteria) this;
        }

        public Criteria andBondCycleGreaterThanOrEqualTo(Integer value) {
            addCriterion("bond_cycle >=", value, "bondCycle");
            return (Criteria) this;
        }

        public Criteria andBondCycleLessThan(Integer value) {
            addCriterion("bond_cycle <", value, "bondCycle");
            return (Criteria) this;
        }

        public Criteria andBondCycleLessThanOrEqualTo(Integer value) {
            addCriterion("bond_cycle <=", value, "bondCycle");
            return (Criteria) this;
        }

        public Criteria andBondCycleIn(List<Integer> values) {
            addCriterion("bond_cycle in", values, "bondCycle");
            return (Criteria) this;
        }

        public Criteria andBondCycleNotIn(List<Integer> values) {
            addCriterion("bond_cycle not in", values, "bondCycle");
            return (Criteria) this;
        }

        public Criteria andBondCycleBetween(Integer value1, Integer value2) {
            addCriterion("bond_cycle between", value1, value2, "bondCycle");
            return (Criteria) this;
        }

        public Criteria andBondCycleNotBetween(Integer value1, Integer value2) {
            addCriterion("bond_cycle not between", value1, value2, "bondCycle");
            return (Criteria) this;
        }

        public Criteria andSettlecycleIsNull() {
            addCriterion("settlecycle is null");
            return (Criteria) this;
        }

        public Criteria andSettlecycleIsNotNull() {
            addCriterion("settlecycle is not null");
            return (Criteria) this;
        }

        public Criteria andSettlecycleEqualTo(String value) {
            addCriterion("settlecycle =", value, "settlecycle");
            return (Criteria) this;
        }

        public Criteria andSettlecycleNotEqualTo(String value) {
            addCriterion("settlecycle <>", value, "settlecycle");
            return (Criteria) this;
        }

        public Criteria andSettlecycleGreaterThan(String value) {
            addCriterion("settlecycle >", value, "settlecycle");
            return (Criteria) this;
        }

        public Criteria andSettlecycleGreaterThanOrEqualTo(String value) {
            addCriterion("settlecycle >=", value, "settlecycle");
            return (Criteria) this;
        }

        public Criteria andSettlecycleLessThan(String value) {
            addCriterion("settlecycle <", value, "settlecycle");
            return (Criteria) this;
        }

        public Criteria andSettlecycleLessThanOrEqualTo(String value) {
            addCriterion("settlecycle <=", value, "settlecycle");
            return (Criteria) this;
        }

        public Criteria andSettlecycleLike(String value) {
            addCriterion("settlecycle like", value, "settlecycle");
            return (Criteria) this;
        }

        public Criteria andSettlecycleNotLike(String value) {
            addCriterion("settlecycle not like", value, "settlecycle");
            return (Criteria) this;
        }

        public Criteria andSettlecycleIn(List<String> values) {
            addCriterion("settlecycle in", values, "settlecycle");
            return (Criteria) this;
        }

        public Criteria andSettlecycleNotIn(List<String> values) {
            addCriterion("settlecycle not in", values, "settlecycle");
            return (Criteria) this;
        }

        public Criteria andSettlecycleBetween(String value1, String value2) {
            addCriterion("settlecycle between", value1, value2, "settlecycle");
            return (Criteria) this;
        }

        public Criteria andSettlecycleNotBetween(String value1, String value2) {
            addCriterion("settlecycle not between", value1, value2, "settlecycle");
            return (Criteria) this;
        }

        public Criteria andStatusIsNull() {
            addCriterion("status is null");
            return (Criteria) this;
        }

        public Criteria andStatusIsNotNull() {
            addCriterion("status is not null");
            return (Criteria) this;
        }

        public Criteria andStatusEqualTo(Integer value) {
            addCriterion("status =", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusNotEqualTo(Integer value) {
            addCriterion("status <>", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusGreaterThan(Integer value) {
            addCriterion("status >", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusGreaterThanOrEqualTo(Integer value) {
            addCriterion("status >=", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusLessThan(Integer value) {
            addCriterion("status <", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusLessThanOrEqualTo(Integer value) {
            addCriterion("status <=", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusIn(List<Integer> values) {
            addCriterion("status in", values, "status");
            return (Criteria) this;
        }

        public Criteria andStatusNotIn(List<Integer> values) {
            addCriterion("status not in", values, "status");
            return (Criteria) this;
        }

        public Criteria andStatusBetween(Integer value1, Integer value2) {
            addCriterion("status between", value1, value2, "status");
            return (Criteria) this;
        }

        public Criteria andStatusNotBetween(Integer value1, Integer value2) {
            addCriterion("status not between", value1, value2, "status");
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
