package com.rxh.square.pojo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AgentWalletExample implements Serializable {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public AgentWalletExample() {
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

        public Criteria andTotalAmountIsNull() {
            addCriterion("total_amount is null");
            return (Criteria) this;
        }

        public Criteria andTotalAmountIsNotNull() {
            addCriterion("total_amount is not null");
            return (Criteria) this;
        }

        public Criteria andTotalAmountEqualTo(BigDecimal value) {
            addCriterion("total_amount =", value, "totalAmount");
            return (Criteria) this;
        }

        public Criteria andTotalAmountNotEqualTo(BigDecimal value) {
            addCriterion("total_amount <>", value, "totalAmount");
            return (Criteria) this;
        }

        public Criteria andTotalAmountGreaterThan(BigDecimal value) {
            addCriterion("total_amount >", value, "totalAmount");
            return (Criteria) this;
        }

        public Criteria andTotalAmountGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("total_amount >=", value, "totalAmount");
            return (Criteria) this;
        }

        public Criteria andTotalAmountLessThan(BigDecimal value) {
            addCriterion("total_amount <", value, "totalAmount");
            return (Criteria) this;
        }

        public Criteria andTotalAmountLessThanOrEqualTo(BigDecimal value) {
            addCriterion("total_amount <=", value, "totalAmount");
            return (Criteria) this;
        }

        public Criteria andTotalAmountIn(List<BigDecimal> values) {
            addCriterion("total_amount in", values, "totalAmount");
            return (Criteria) this;
        }

        public Criteria andTotalAmountNotIn(List<BigDecimal> values) {
            addCriterion("total_amount not in", values, "totalAmount");
            return (Criteria) this;
        }

        public Criteria andTotalAmountBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("total_amount between", value1, value2, "totalAmount");
            return (Criteria) this;
        }

        public Criteria andTotalAmountNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("total_amount not between", value1, value2, "totalAmount");
            return (Criteria) this;
        }

        public Criteria andIncomeAmountIsNull() {
            addCriterion("income_amount is null");
            return (Criteria) this;
        }

        public Criteria andIncomeAmountIsNotNull() {
            addCriterion("income_amount is not null");
            return (Criteria) this;
        }

        public Criteria andIncomeAmountEqualTo(BigDecimal value) {
            addCriterion("income_amount =", value, "incomeAmount");
            return (Criteria) this;
        }

        public Criteria andIncomeAmountNotEqualTo(BigDecimal value) {
            addCriterion("income_amount <>", value, "incomeAmount");
            return (Criteria) this;
        }

        public Criteria andIncomeAmountGreaterThan(BigDecimal value) {
            addCriterion("income_amount >", value, "incomeAmount");
            return (Criteria) this;
        }

        public Criteria andIncomeAmountGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("income_amount >=", value, "incomeAmount");
            return (Criteria) this;
        }

        public Criteria andIncomeAmountLessThan(BigDecimal value) {
            addCriterion("income_amount <", value, "incomeAmount");
            return (Criteria) this;
        }

        public Criteria andIncomeAmountLessThanOrEqualTo(BigDecimal value) {
            addCriterion("income_amount <=", value, "incomeAmount");
            return (Criteria) this;
        }

        public Criteria andIncomeAmountIn(List<BigDecimal> values) {
            addCriterion("income_amount in", values, "incomeAmount");
            return (Criteria) this;
        }

        public Criteria andIncomeAmountNotIn(List<BigDecimal> values) {
            addCriterion("income_amount not in", values, "incomeAmount");
            return (Criteria) this;
        }

        public Criteria andIncomeAmountBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("income_amount between", value1, value2, "incomeAmount");
            return (Criteria) this;
        }

        public Criteria andIncomeAmountNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("income_amount not between", value1, value2, "incomeAmount");
            return (Criteria) this;
        }

        public Criteria andOutAmountIsNull() {
            addCriterion("out_amount is null");
            return (Criteria) this;
        }

        public Criteria andOutAmountIsNotNull() {
            addCriterion("out_amount is not null");
            return (Criteria) this;
        }

        public Criteria andOutAmountEqualTo(BigDecimal value) {
            addCriterion("out_amount =", value, "outAmount");
            return (Criteria) this;
        }

        public Criteria andOutAmountNotEqualTo(BigDecimal value) {
            addCriterion("out_amount <>", value, "outAmount");
            return (Criteria) this;
        }

        public Criteria andOutAmountGreaterThan(BigDecimal value) {
            addCriterion("out_amount >", value, "outAmount");
            return (Criteria) this;
        }

        public Criteria andOutAmountGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("out_amount >=", value, "outAmount");
            return (Criteria) this;
        }

        public Criteria andOutAmountLessThan(BigDecimal value) {
            addCriterion("out_amount <", value, "outAmount");
            return (Criteria) this;
        }

        public Criteria andOutAmountLessThanOrEqualTo(BigDecimal value) {
            addCriterion("out_amount <=", value, "outAmount");
            return (Criteria) this;
        }

        public Criteria andOutAmountIn(List<BigDecimal> values) {
            addCriterion("out_amount in", values, "outAmount");
            return (Criteria) this;
        }

        public Criteria andOutAmountNotIn(List<BigDecimal> values) {
            addCriterion("out_amount not in", values, "outAmount");
            return (Criteria) this;
        }

        public Criteria andOutAmountBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("out_amount between", value1, value2, "outAmount");
            return (Criteria) this;
        }

        public Criteria andOutAmountNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("out_amount not between", value1, value2, "outAmount");
            return (Criteria) this;
        }

        public Criteria andTotalBalanceIsNull() {
            addCriterion("total_balance is null");
            return (Criteria) this;
        }

        public Criteria andTotalBalanceIsNotNull() {
            addCriterion("total_balance is not null");
            return (Criteria) this;
        }

        public Criteria andTotalBalanceEqualTo(BigDecimal value) {
            addCriterion("total_balance =", value, "totalBalance");
            return (Criteria) this;
        }

        public Criteria andTotalBalanceNotEqualTo(BigDecimal value) {
            addCriterion("total_balance <>", value, "totalBalance");
            return (Criteria) this;
        }

        public Criteria andTotalBalanceGreaterThan(BigDecimal value) {
            addCriterion("total_balance >", value, "totalBalance");
            return (Criteria) this;
        }

        public Criteria andTotalBalanceGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("total_balance >=", value, "totalBalance");
            return (Criteria) this;
        }

        public Criteria andTotalBalanceLessThan(BigDecimal value) {
            addCriterion("total_balance <", value, "totalBalance");
            return (Criteria) this;
        }

        public Criteria andTotalBalanceLessThanOrEqualTo(BigDecimal value) {
            addCriterion("total_balance <=", value, "totalBalance");
            return (Criteria) this;
        }

        public Criteria andTotalBalanceIn(List<BigDecimal> values) {
            addCriterion("total_balance in", values, "totalBalance");
            return (Criteria) this;
        }

        public Criteria andTotalBalanceNotIn(List<BigDecimal> values) {
            addCriterion("total_balance not in", values, "totalBalance");
            return (Criteria) this;
        }

        public Criteria andTotalBalanceBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("total_balance between", value1, value2, "totalBalance");
            return (Criteria) this;
        }

        public Criteria andTotalBalanceNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("total_balance not between", value1, value2, "totalBalance");
            return (Criteria) this;
        }

        public Criteria andTotalAvailableAmountIsNull() {
            addCriterion("total_available_amount is null");
            return (Criteria) this;
        }

        public Criteria andTotalAvailableAmountIsNotNull() {
            addCriterion("total_available_amount is not null");
            return (Criteria) this;
        }

        public Criteria andTotalAvailableAmountEqualTo(BigDecimal value) {
            addCriterion("total_available_amount =", value, "totalAvailableAmount");
            return (Criteria) this;
        }

        public Criteria andTotalAvailableAmountNotEqualTo(BigDecimal value) {
            addCriterion("total_available_amount <>", value, "totalAvailableAmount");
            return (Criteria) this;
        }

        public Criteria andTotalAvailableAmountGreaterThan(BigDecimal value) {
            addCriterion("total_available_amount >", value, "totalAvailableAmount");
            return (Criteria) this;
        }

        public Criteria andTotalAvailableAmountGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("total_available_amount >=", value, "totalAvailableAmount");
            return (Criteria) this;
        }

        public Criteria andTotalAvailableAmountLessThan(BigDecimal value) {
            addCriterion("total_available_amount <", value, "totalAvailableAmount");
            return (Criteria) this;
        }

        public Criteria andTotalAvailableAmountLessThanOrEqualTo(BigDecimal value) {
            addCriterion("total_available_amount <=", value, "totalAvailableAmount");
            return (Criteria) this;
        }

        public Criteria andTotalAvailableAmountIn(List<BigDecimal> values) {
            addCriterion("total_available_amount in", values, "totalAvailableAmount");
            return (Criteria) this;
        }

        public Criteria andTotalAvailableAmountNotIn(List<BigDecimal> values) {
            addCriterion("total_available_amount not in", values, "totalAvailableAmount");
            return (Criteria) this;
        }

        public Criteria andTotalAvailableAmountBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("total_available_amount between", value1, value2, "totalAvailableAmount");
            return (Criteria) this;
        }

        public Criteria andTotalAvailableAmountNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("total_available_amount not between", value1, value2, "totalAvailableAmount");
            return (Criteria) this;
        }

        public Criteria andTotalUnavailableAmountIsNull() {
            addCriterion("total_unavailable_amount is null");
            return (Criteria) this;
        }

        public Criteria andTotalUnavailableAmountIsNotNull() {
            addCriterion("total_unavailable_amount is not null");
            return (Criteria) this;
        }

        public Criteria andTotalUnavailableAmountEqualTo(BigDecimal value) {
            addCriterion("total_unavailable_amount =", value, "totalUnavailableAmount");
            return (Criteria) this;
        }

        public Criteria andTotalUnavailableAmountNotEqualTo(BigDecimal value) {
            addCriterion("total_unavailable_amount <>", value, "totalUnavailableAmount");
            return (Criteria) this;
        }

        public Criteria andTotalUnavailableAmountGreaterThan(BigDecimal value) {
            addCriterion("total_unavailable_amount >", value, "totalUnavailableAmount");
            return (Criteria) this;
        }

        public Criteria andTotalUnavailableAmountGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("total_unavailable_amount >=", value, "totalUnavailableAmount");
            return (Criteria) this;
        }

        public Criteria andTotalUnavailableAmountLessThan(BigDecimal value) {
            addCriterion("total_unavailable_amount <", value, "totalUnavailableAmount");
            return (Criteria) this;
        }

        public Criteria andTotalUnavailableAmountLessThanOrEqualTo(BigDecimal value) {
            addCriterion("total_unavailable_amount <=", value, "totalUnavailableAmount");
            return (Criteria) this;
        }

        public Criteria andTotalUnavailableAmountIn(List<BigDecimal> values) {
            addCriterion("total_unavailable_amount in", values, "totalUnavailableAmount");
            return (Criteria) this;
        }

        public Criteria andTotalUnavailableAmountNotIn(List<BigDecimal> values) {
            addCriterion("total_unavailable_amount not in", values, "totalUnavailableAmount");
            return (Criteria) this;
        }

        public Criteria andTotalUnavailableAmountBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("total_unavailable_amount between", value1, value2, "totalUnavailableAmount");
            return (Criteria) this;
        }

        public Criteria andTotalUnavailableAmountNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("total_unavailable_amount not between", value1, value2, "totalUnavailableAmount");
            return (Criteria) this;
        }

        public Criteria andTotalFeeIsNull() {
            addCriterion("total_fee is null");
            return (Criteria) this;
        }

        public Criteria andTotalFeeIsNotNull() {
            addCriterion("total_fee is not null");
            return (Criteria) this;
        }

        public Criteria andTotalFeeEqualTo(BigDecimal value) {
            addCriterion("total_fee =", value, "totalFee");
            return (Criteria) this;
        }

        public Criteria andTotalFeeNotEqualTo(BigDecimal value) {
            addCriterion("total_fee <>", value, "totalFee");
            return (Criteria) this;
        }

        public Criteria andTotalFeeGreaterThan(BigDecimal value) {
            addCriterion("total_fee >", value, "totalFee");
            return (Criteria) this;
        }

        public Criteria andTotalFeeGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("total_fee >=", value, "totalFee");
            return (Criteria) this;
        }

        public Criteria andTotalFeeLessThan(BigDecimal value) {
            addCriterion("total_fee <", value, "totalFee");
            return (Criteria) this;
        }

        public Criteria andTotalFeeLessThanOrEqualTo(BigDecimal value) {
            addCriterion("total_fee <=", value, "totalFee");
            return (Criteria) this;
        }

        public Criteria andTotalFeeIn(List<BigDecimal> values) {
            addCriterion("total_fee in", values, "totalFee");
            return (Criteria) this;
        }

        public Criteria andTotalFeeNotIn(List<BigDecimal> values) {
            addCriterion("total_fee not in", values, "totalFee");
            return (Criteria) this;
        }

        public Criteria andTotalFeeBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("total_fee between", value1, value2, "totalFee");
            return (Criteria) this;
        }

        public Criteria andTotalFeeNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("total_fee not between", value1, value2, "totalFee");
            return (Criteria) this;
        }

        public Criteria andTotalFreezeAmountIsNull() {
            addCriterion("total_freeze_amount is null");
            return (Criteria) this;
        }

        public Criteria andTotalFreezeAmountIsNotNull() {
            addCriterion("total_freeze_amount is not null");
            return (Criteria) this;
        }

        public Criteria andTotalFreezeAmountEqualTo(BigDecimal value) {
            addCriterion("total_freeze_amount =", value, "totalFreezeAmount");
            return (Criteria) this;
        }

        public Criteria andTotalFreezeAmountNotEqualTo(BigDecimal value) {
            addCriterion("total_freeze_amount <>", value, "totalFreezeAmount");
            return (Criteria) this;
        }

        public Criteria andTotalFreezeAmountGreaterThan(BigDecimal value) {
            addCriterion("total_freeze_amount >", value, "totalFreezeAmount");
            return (Criteria) this;
        }

        public Criteria andTotalFreezeAmountGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("total_freeze_amount >=", value, "totalFreezeAmount");
            return (Criteria) this;
        }

        public Criteria andTotalFreezeAmountLessThan(BigDecimal value) {
            addCriterion("total_freeze_amount <", value, "totalFreezeAmount");
            return (Criteria) this;
        }

        public Criteria andTotalFreezeAmountLessThanOrEqualTo(BigDecimal value) {
            addCriterion("total_freeze_amount <=", value, "totalFreezeAmount");
            return (Criteria) this;
        }

        public Criteria andTotalFreezeAmountIn(List<BigDecimal> values) {
            addCriterion("total_freeze_amount in", values, "totalFreezeAmount");
            return (Criteria) this;
        }

        public Criteria andTotalFreezeAmountNotIn(List<BigDecimal> values) {
            addCriterion("total_freeze_amount not in", values, "totalFreezeAmount");
            return (Criteria) this;
        }

        public Criteria andTotalFreezeAmountBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("total_freeze_amount between", value1, value2, "totalFreezeAmount");
            return (Criteria) this;
        }

        public Criteria andTotalFreezeAmountNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("total_freeze_amount not between", value1, value2, "totalFreezeAmount");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeIsNull() {
            addCriterion("update_time is null");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeIsNotNull() {
            addCriterion("update_time is not null");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeEqualTo(Date value) {
            addCriterion("update_time =", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeNotEqualTo(Date value) {
            addCriterion("update_time <>", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeGreaterThan(Date value) {
            addCriterion("update_time >", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeGreaterThanOrEqualTo(Date value) {
            addCriterion("update_time >=", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeLessThan(Date value) {
            addCriterion("update_time <", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeLessThanOrEqualTo(Date value) {
            addCriterion("update_time <=", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeIn(List<Date> values) {
            addCriterion("update_time in", values, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeNotIn(List<Date> values) {
            addCriterion("update_time not in", values, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeBetween(Date value1, Date value2) {
            addCriterion("update_time between", value1, value2, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeNotBetween(Date value1, Date value2) {
            addCriterion("update_time not between", value1, value2, "updateTime");
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