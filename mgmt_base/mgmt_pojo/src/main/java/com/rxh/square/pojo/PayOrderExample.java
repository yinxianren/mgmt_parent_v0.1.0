package com.rxh.square.pojo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PayOrderExample implements Serializable {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public PayOrderExample() {
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

        public Criteria andPayIdIsNull() {
            addCriterion("pay_id is null");
            return (Criteria) this;
        }

        public Criteria andPayIdIsNotNull() {
            addCriterion("pay_id is not null");
            return (Criteria) this;
        }

        public Criteria andPayIdEqualTo(String value) {
            addCriterion("pay_id =", value, "payId");
            return (Criteria) this;
        }

        public Criteria andPayIdNotEqualTo(String value) {
            addCriterion("pay_id <>", value, "payId");
            return (Criteria) this;
        }

        public Criteria andPayIdGreaterThan(String value) {
            addCriterion("pay_id >", value, "payId");
            return (Criteria) this;
        }

        public Criteria andPayIdGreaterThanOrEqualTo(String value) {
            addCriterion("pay_id >=", value, "payId");
            return (Criteria) this;
        }

        public Criteria andPayIdLessThan(String value) {
            addCriterion("pay_id <", value, "payId");
            return (Criteria) this;
        }

        public Criteria andPayIdLessThanOrEqualTo(String value) {
            addCriterion("pay_id <=", value, "payId");
            return (Criteria) this;
        }

        public Criteria andPayIdLike(String value) {
            addCriterion("pay_id like", value, "payId");
            return (Criteria) this;
        }

        public Criteria andPayIdNotLike(String value) {
            addCriterion("pay_id not like", value, "payId");
            return (Criteria) this;
        }

        public Criteria andPayIdIn(List<String> values) {
            addCriterion("pay_id in", values, "payId");
            return (Criteria) this;
        }

        public Criteria andPayIdNotIn(List<String> values) {
            addCriterion("pay_id not in", values, "payId");
            return (Criteria) this;
        }

        public Criteria andPayIdBetween(String value1, String value2) {
            addCriterion("pay_id between", value1, value2, "payId");
            return (Criteria) this;
        }

        public Criteria andPayIdNotBetween(String value1, String value2) {
            addCriterion("pay_id not between", value1, value2, "payId");
            return (Criteria) this;
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

        public Criteria andMerOrderIdIsNull() {
            addCriterion("mer_order_id is null");
            return (Criteria) this;
        }

        public Criteria andMerOrderIdIsNotNull() {
            addCriterion("mer_order_id is not null");
            return (Criteria) this;
        }

        public Criteria andMerOrderIdEqualTo(String value) {
            addCriterion("mer_order_id =", value, "merOrderId");
            return (Criteria) this;
        }

        public Criteria andMerOrderIdNotEqualTo(String value) {
            addCriterion("mer_order_id <>", value, "merOrderId");
            return (Criteria) this;
        }

        public Criteria andMerOrderIdGreaterThan(String value) {
            addCriterion("mer_order_id >", value, "merOrderId");
            return (Criteria) this;
        }

        public Criteria andMerOrderIdGreaterThanOrEqualTo(String value) {
            addCriterion("mer_order_id >=", value, "merOrderId");
            return (Criteria) this;
        }

        public Criteria andMerOrderIdLessThan(String value) {
            addCriterion("mer_order_id <", value, "merOrderId");
            return (Criteria) this;
        }

        public Criteria andMerOrderIdLessThanOrEqualTo(String value) {
            addCriterion("mer_order_id <=", value, "merOrderId");
            return (Criteria) this;
        }

        public Criteria andMerOrderIdLike(String value) {
            addCriterion("mer_order_id like", value, "merOrderId");
            return (Criteria) this;
        }

        public Criteria andMerOrderIdNotLike(String value) {
            addCriterion("mer_order_id not like", value, "merOrderId");
            return (Criteria) this;
        }

        public Criteria andMerOrderIdIn(List<String> values) {
            addCriterion("mer_order_id in", values, "merOrderId");
            return (Criteria) this;
        }

        public Criteria andMerOrderIdNotIn(List<String> values) {
            addCriterion("mer_order_id not in", values, "merOrderId");
            return (Criteria) this;
        }

        public Criteria andMerOrderIdBetween(String value1, String value2) {
            addCriterion("mer_order_id between", value1, value2, "merOrderId");
            return (Criteria) this;
        }

        public Criteria andMerOrderIdNotBetween(String value1, String value2) {
            addCriterion("mer_order_id not between", value1, value2, "merOrderId");
            return (Criteria) this;
        }

        public Criteria andOrganizationIdIsNull() {
            addCriterion("organization_id is null");
            return (Criteria) this;
        }

        public Criteria andOrganizationIdIsNotNull() {
            addCriterion("organization_id is not null");
            return (Criteria) this;
        }

        public Criteria andOrganizationIdEqualTo(String value) {
            addCriterion("organization_id =", value, "organizationId");
            return (Criteria) this;
        }

        public Criteria andOrganizationIdNotEqualTo(String value) {
            addCriterion("organization_id <>", value, "organizationId");
            return (Criteria) this;
        }

        public Criteria andOrganizationIdGreaterThan(String value) {
            addCriterion("organization_id >", value, "organizationId");
            return (Criteria) this;
        }

        public Criteria andOrganizationIdGreaterThanOrEqualTo(String value) {
            addCriterion("organization_id >=", value, "organizationId");
            return (Criteria) this;
        }

        public Criteria andOrganizationIdLessThan(String value) {
            addCriterion("organization_id <", value, "organizationId");
            return (Criteria) this;
        }

        public Criteria andOrganizationIdLessThanOrEqualTo(String value) {
            addCriterion("organization_id <=", value, "organizationId");
            return (Criteria) this;
        }

        public Criteria andOrganizationIdLike(String value) {
            addCriterion("organization_id like", value, "organizationId");
            return (Criteria) this;
        }

        public Criteria andOrganizationIdNotLike(String value) {
            addCriterion("organization_id not like", value, "organizationId");
            return (Criteria) this;
        }

        public Criteria andOrganizationIdIn(List<String> values) {
            addCriterion("organization_id in", values, "organizationId");
            return (Criteria) this;
        }

        public Criteria andOrganizationIdNotIn(List<String> values) {
            addCriterion("organization_id not in", values, "organizationId");
            return (Criteria) this;
        }

        public Criteria andOrganizationIdBetween(String value1, String value2) {
            addCriterion("organization_id between", value1, value2, "organizationId");
            return (Criteria) this;
        }

        public Criteria andOrganizationIdNotBetween(String value1, String value2) {
            addCriterion("organization_id not between", value1, value2, "organizationId");
            return (Criteria) this;
        }

        public Criteria andChannelIdIsNull() {
            addCriterion("channel_id is null");
            return (Criteria) this;
        }

        public Criteria andChannelIdIsNotNull() {
            addCriterion("channel_id is not null");
            return (Criteria) this;
        }

        public Criteria andChannelIdEqualTo(String value) {
            addCriterion("channel_id =", value, "channelId");
            return (Criteria) this;
        }

        public Criteria andChannelIdNotEqualTo(String value) {
            addCriterion("channel_id <>", value, "channelId");
            return (Criteria) this;
        }

        public Criteria andChannelIdGreaterThan(String value) {
            addCriterion("channel_id >", value, "channelId");
            return (Criteria) this;
        }

        public Criteria andChannelIdGreaterThanOrEqualTo(String value) {
            addCriterion("channel_id >=", value, "channelId");
            return (Criteria) this;
        }

        public Criteria andChannelIdLessThan(String value) {
            addCriterion("channel_id <", value, "channelId");
            return (Criteria) this;
        }

        public Criteria andChannelIdLessThanOrEqualTo(String value) {
            addCriterion("channel_id <=", value, "channelId");
            return (Criteria) this;
        }

        public Criteria andChannelIdLike(String value) {
            addCriterion("channel_id like", value, "channelId");
            return (Criteria) this;
        }

        public Criteria andChannelIdNotLike(String value) {
            addCriterion("channel_id not like", value, "channelId");
            return (Criteria) this;
        }

        public Criteria andChannelIdIn(List<String> values) {
            addCriterion("channel_id in", values, "channelId");
            return (Criteria) this;
        }

        public Criteria andChannelIdNotIn(List<String> values) {
            addCriterion("channel_id not in", values, "channelId");
            return (Criteria) this;
        }

        public Criteria andChannelIdBetween(String value1, String value2) {
            addCriterion("channel_id between", value1, value2, "channelId");
            return (Criteria) this;
        }

        public Criteria andChannelIdNotBetween(String value1, String value2) {
            addCriterion("channel_id not between", value1, value2, "channelId");
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

        public Criteria andChannelTransCodeIsNull() {
            addCriterion("channel_trans_code is null");
            return (Criteria) this;
        }

        public Criteria andChannelTransCodeIsNotNull() {
            addCriterion("channel_trans_code is not null");
            return (Criteria) this;
        }

        public Criteria andChannelTransCodeEqualTo(String value) {
            addCriterion("channel_trans_code =", value, "channelTransCode");
            return (Criteria) this;
        }

        public Criteria andChannelTransCodeNotEqualTo(String value) {
            addCriterion("channel_trans_code <>", value, "channelTransCode");
            return (Criteria) this;
        }

        public Criteria andChannelTransCodeGreaterThan(String value) {
            addCriterion("channel_trans_code >", value, "channelTransCode");
            return (Criteria) this;
        }

        public Criteria andChannelTransCodeGreaterThanOrEqualTo(String value) {
            addCriterion("channel_trans_code >=", value, "channelTransCode");
            return (Criteria) this;
        }

        public Criteria andChannelTransCodeLessThan(String value) {
            addCriterion("channel_trans_code <", value, "channelTransCode");
            return (Criteria) this;
        }

        public Criteria andChannelTransCodeLessThanOrEqualTo(String value) {
            addCriterion("channel_trans_code <=", value, "channelTransCode");
            return (Criteria) this;
        }

        public Criteria andChannelTransCodeLike(String value) {
            addCriterion("channel_trans_code like", value, "channelTransCode");
            return (Criteria) this;
        }

        public Criteria andChannelTransCodeNotLike(String value) {
            addCriterion("channel_trans_code not like", value, "channelTransCode");
            return (Criteria) this;
        }

        public Criteria andChannelTransCodeIn(List<String> values) {
            addCriterion("channel_trans_code in", values, "channelTransCode");
            return (Criteria) this;
        }

        public Criteria andChannelTransCodeNotIn(List<String> values) {
            addCriterion("channel_trans_code not in", values, "channelTransCode");
            return (Criteria) this;
        }

        public Criteria andChannelTransCodeBetween(String value1, String value2) {
            addCriterion("channel_trans_code between", value1, value2, "channelTransCode");
            return (Criteria) this;
        }

        public Criteria andChannelTransCodeNotBetween(String value1, String value2) {
            addCriterion("channel_trans_code not between", value1, value2, "channelTransCode");
            return (Criteria) this;
        }

        public Criteria andCurrencyIsNull() {
            addCriterion("currency is null");
            return (Criteria) this;
        }

        public Criteria andCurrencyIsNotNull() {
            addCriterion("currency is not null");
            return (Criteria) this;
        }

        public Criteria andCurrencyEqualTo(String value) {
            addCriterion("currency =", value, "currency");
            return (Criteria) this;
        }

        public Criteria andCurrencyNotEqualTo(String value) {
            addCriterion("currency <>", value, "currency");
            return (Criteria) this;
        }

        public Criteria andCurrencyGreaterThan(String value) {
            addCriterion("currency >", value, "currency");
            return (Criteria) this;
        }

        public Criteria andCurrencyGreaterThanOrEqualTo(String value) {
            addCriterion("currency >=", value, "currency");
            return (Criteria) this;
        }

        public Criteria andCurrencyLessThan(String value) {
            addCriterion("currency <", value, "currency");
            return (Criteria) this;
        }

        public Criteria andCurrencyLessThanOrEqualTo(String value) {
            addCriterion("currency <=", value, "currency");
            return (Criteria) this;
        }

        public Criteria andCurrencyLike(String value) {
            addCriterion("currency like", value, "currency");
            return (Criteria) this;
        }

        public Criteria andCurrencyNotLike(String value) {
            addCriterion("currency not like", value, "currency");
            return (Criteria) this;
        }

        public Criteria andCurrencyIn(List<String> values) {
            addCriterion("currency in", values, "currency");
            return (Criteria) this;
        }

        public Criteria andCurrencyNotIn(List<String> values) {
            addCriterion("currency not in", values, "currency");
            return (Criteria) this;
        }

        public Criteria andCurrencyBetween(String value1, String value2) {
            addCriterion("currency between", value1, value2, "currency");
            return (Criteria) this;
        }

        public Criteria andCurrencyNotBetween(String value1, String value2) {
            addCriterion("currency not between", value1, value2, "currency");
            return (Criteria) this;
        }

        public Criteria andAmountIsNull() {
            addCriterion("amount is null");
            return (Criteria) this;
        }

        public Criteria andAmountIsNotNull() {
            addCriterion("amount is not null");
            return (Criteria) this;
        }

        public Criteria andAmountEqualTo(BigDecimal value) {
            addCriterion("amount =", value, "amount");
            return (Criteria) this;
        }

        public Criteria andAmountNotEqualTo(BigDecimal value) {
            addCriterion("amount <>", value, "amount");
            return (Criteria) this;
        }

        public Criteria andAmountGreaterThan(BigDecimal value) {
            addCriterion("amount >", value, "amount");
            return (Criteria) this;
        }

        public Criteria andAmountGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("amount >=", value, "amount");
            return (Criteria) this;
        }

        public Criteria andAmountLessThan(BigDecimal value) {
            addCriterion("amount <", value, "amount");
            return (Criteria) this;
        }

        public Criteria andAmountLessThanOrEqualTo(BigDecimal value) {
            addCriterion("amount <=", value, "amount");
            return (Criteria) this;
        }

        public Criteria andAmountIn(List<BigDecimal> values) {
            addCriterion("amount in", values, "amount");
            return (Criteria) this;
        }

        public Criteria andAmountNotIn(List<BigDecimal> values) {
            addCriterion("amount not in", values, "amount");
            return (Criteria) this;
        }

        public Criteria andAmountBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("amount between", value1, value2, "amount");
            return (Criteria) this;
        }

        public Criteria andAmountNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("amount not between", value1, value2, "amount");
            return (Criteria) this;
        }

        public Criteria andRealAmountIsNull() {
            addCriterion("real_amount is null");
            return (Criteria) this;
        }

        public Criteria andRealAmountIsNotNull() {
            addCriterion("real_amount is not null");
            return (Criteria) this;
        }

        public Criteria andRealAmountEqualTo(BigDecimal value) {
            addCriterion("real_amount =", value, "realAmount");
            return (Criteria) this;
        }

        public Criteria andRealAmountNotEqualTo(BigDecimal value) {
            addCriterion("real_amount <>", value, "realAmount");
            return (Criteria) this;
        }

        public Criteria andRealAmountGreaterThan(BigDecimal value) {
            addCriterion("real_amount >", value, "realAmount");
            return (Criteria) this;
        }

        public Criteria andRealAmountGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("real_amount >=", value, "realAmount");
            return (Criteria) this;
        }

        public Criteria andRealAmountLessThan(BigDecimal value) {
            addCriterion("real_amount <", value, "realAmount");
            return (Criteria) this;
        }

        public Criteria andRealAmountLessThanOrEqualTo(BigDecimal value) {
            addCriterion("real_amount <=", value, "realAmount");
            return (Criteria) this;
        }

        public Criteria andRealAmountIn(List<BigDecimal> values) {
            addCriterion("real_amount in", values, "realAmount");
            return (Criteria) this;
        }

        public Criteria andRealAmountNotIn(List<BigDecimal> values) {
            addCriterion("real_amount not in", values, "realAmount");
            return (Criteria) this;
        }

        public Criteria andRealAmountBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("real_amount between", value1, value2, "realAmount");
            return (Criteria) this;
        }

        public Criteria andRealAmountNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("real_amount not between", value1, value2, "realAmount");
            return (Criteria) this;
        }

        public Criteria andTerminalFeeIsNull() {
            addCriterion("terminal_fee is null");
            return (Criteria) this;
        }

        public Criteria andTerminalFeeIsNotNull() {
            addCriterion("terminal_fee is not null");
            return (Criteria) this;
        }

        public Criteria andTerminalFeeEqualTo(BigDecimal value) {
            addCriterion("terminal_fee =", value, "terminalFee");
            return (Criteria) this;
        }

        public Criteria andTerminalFeeNotEqualTo(BigDecimal value) {
            addCriterion("terminal_fee <>", value, "terminalFee");
            return (Criteria) this;
        }

        public Criteria andTerminalFeeGreaterThan(BigDecimal value) {
            addCriterion("terminal_fee >", value, "terminalFee");
            return (Criteria) this;
        }

        public Criteria andTerminalFeeGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("terminal_fee >=", value, "terminalFee");
            return (Criteria) this;
        }

        public Criteria andTerminalFeeLessThan(BigDecimal value) {
            addCriterion("terminal_fee <", value, "terminalFee");
            return (Criteria) this;
        }

        public Criteria andTerminalFeeLessThanOrEqualTo(BigDecimal value) {
            addCriterion("terminal_fee <=", value, "terminalFee");
            return (Criteria) this;
        }

        public Criteria andTerminalFeeIn(List<BigDecimal> values) {
            addCriterion("terminal_fee in", values, "terminalFee");
            return (Criteria) this;
        }

        public Criteria andTerminalFeeNotIn(List<BigDecimal> values) {
            addCriterion("terminal_fee not in", values, "terminalFee");
            return (Criteria) this;
        }

        public Criteria andTerminalFeeBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("terminal_fee between", value1, value2, "terminalFee");
            return (Criteria) this;
        }

        public Criteria andTerminalFeeNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("terminal_fee not between", value1, value2, "terminalFee");
            return (Criteria) this;
        }

        public Criteria andFeeIsNull() {
            addCriterion("fee is null");
            return (Criteria) this;
        }

        public Criteria andFeeIsNotNull() {
            addCriterion("fee is not null");
            return (Criteria) this;
        }

        public Criteria andFeeEqualTo(BigDecimal value) {
            addCriterion("fee =", value, "fee");
            return (Criteria) this;
        }

        public Criteria andFeeNotEqualTo(BigDecimal value) {
            addCriterion("fee <>", value, "fee");
            return (Criteria) this;
        }

        public Criteria andFeeGreaterThan(BigDecimal value) {
            addCriterion("fee >", value, "fee");
            return (Criteria) this;
        }

        public Criteria andFeeGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("fee >=", value, "fee");
            return (Criteria) this;
        }

        public Criteria andFeeLessThan(BigDecimal value) {
            addCriterion("fee <", value, "fee");
            return (Criteria) this;
        }

        public Criteria andFeeLessThanOrEqualTo(BigDecimal value) {
            addCriterion("fee <=", value, "fee");
            return (Criteria) this;
        }

        public Criteria andFeeIn(List<BigDecimal> values) {
            addCriterion("fee in", values, "fee");
            return (Criteria) this;
        }

        public Criteria andFeeNotIn(List<BigDecimal> values) {
            addCriterion("fee not in", values, "fee");
            return (Criteria) this;
        }

        public Criteria andFeeBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("fee between", value1, value2, "fee");
            return (Criteria) this;
        }

        public Criteria andFeeNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("fee not between", value1, value2, "fee");
            return (Criteria) this;
        }

        public Criteria andChannelFeeIsNull() {
            addCriterion("channel_fee is null");
            return (Criteria) this;
        }

        public Criteria andChannelFeeIsNotNull() {
            addCriterion("channel_fee is not null");
            return (Criteria) this;
        }

        public Criteria andChannelFeeEqualTo(BigDecimal value) {
            addCriterion("channel_fee =", value, "channelFee");
            return (Criteria) this;
        }

        public Criteria andChannelFeeNotEqualTo(BigDecimal value) {
            addCriterion("channel_fee <>", value, "channelFee");
            return (Criteria) this;
        }

        public Criteria andChannelFeeGreaterThan(BigDecimal value) {
            addCriterion("channel_fee >", value, "channelFee");
            return (Criteria) this;
        }

        public Criteria andChannelFeeGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("channel_fee >=", value, "channelFee");
            return (Criteria) this;
        }

        public Criteria andChannelFeeLessThan(BigDecimal value) {
            addCriterion("channel_fee <", value, "channelFee");
            return (Criteria) this;
        }

        public Criteria andChannelFeeLessThanOrEqualTo(BigDecimal value) {
            addCriterion("channel_fee <=", value, "channelFee");
            return (Criteria) this;
        }

        public Criteria andChannelFeeIn(List<BigDecimal> values) {
            addCriterion("channel_fee in", values, "channelFee");
            return (Criteria) this;
        }

        public Criteria andChannelFeeNotIn(List<BigDecimal> values) {
            addCriterion("channel_fee not in", values, "channelFee");
            return (Criteria) this;
        }

        public Criteria andChannelFeeBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("channel_fee between", value1, value2, "channelFee");
            return (Criteria) this;
        }

        public Criteria andChannelFeeNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("channel_fee not between", value1, value2, "channelFee");
            return (Criteria) this;
        }

        public Criteria andAgentFeeIsNull() {
            addCriterion("agent_fee is null");
            return (Criteria) this;
        }

        public Criteria andAgentFeeIsNotNull() {
            addCriterion("agent_fee is not null");
            return (Criteria) this;
        }

        public Criteria andAgentFeeEqualTo(BigDecimal value) {
            addCriterion("agent_fee =", value, "agentFee");
            return (Criteria) this;
        }

        public Criteria andAgentFeeNotEqualTo(BigDecimal value) {
            addCriterion("agent_fee <>", value, "agentFee");
            return (Criteria) this;
        }

        public Criteria andAgentFeeGreaterThan(BigDecimal value) {
            addCriterion("agent_fee >", value, "agentFee");
            return (Criteria) this;
        }

        public Criteria andAgentFeeGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("agent_fee >=", value, "agentFee");
            return (Criteria) this;
        }

        public Criteria andAgentFeeLessThan(BigDecimal value) {
            addCriterion("agent_fee <", value, "agentFee");
            return (Criteria) this;
        }

        public Criteria andAgentFeeLessThanOrEqualTo(BigDecimal value) {
            addCriterion("agent_fee <=", value, "agentFee");
            return (Criteria) this;
        }

        public Criteria andAgentFeeIn(List<BigDecimal> values) {
            addCriterion("agent_fee in", values, "agentFee");
            return (Criteria) this;
        }

        public Criteria andAgentFeeNotIn(List<BigDecimal> values) {
            addCriterion("agent_fee not in", values, "agentFee");
            return (Criteria) this;
        }

        public Criteria andAgentFeeBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("agent_fee between", value1, value2, "agentFee");
            return (Criteria) this;
        }

        public Criteria andAgentFeeNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("agent_fee not between", value1, value2, "agentFee");
            return (Criteria) this;
        }

        public Criteria andIncomeIsNull() {
            addCriterion("income is null");
            return (Criteria) this;
        }

        public Criteria andIncomeIsNotNull() {
            addCriterion("income is not null");
            return (Criteria) this;
        }

        public Criteria andIncomeEqualTo(BigDecimal value) {
            addCriterion("income =", value, "income");
            return (Criteria) this;
        }

        public Criteria andIncomeNotEqualTo(BigDecimal value) {
            addCriterion("income <>", value, "income");
            return (Criteria) this;
        }

        public Criteria andIncomeGreaterThan(BigDecimal value) {
            addCriterion("income >", value, "income");
            return (Criteria) this;
        }

        public Criteria andIncomeGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("income >=", value, "income");
            return (Criteria) this;
        }

        public Criteria andIncomeLessThan(BigDecimal value) {
            addCriterion("income <", value, "income");
            return (Criteria) this;
        }

        public Criteria andIncomeLessThanOrEqualTo(BigDecimal value) {
            addCriterion("income <=", value, "income");
            return (Criteria) this;
        }

        public Criteria andIncomeIn(List<BigDecimal> values) {
            addCriterion("income in", values, "income");
            return (Criteria) this;
        }

        public Criteria andIncomeNotIn(List<BigDecimal> values) {
            addCriterion("income not in", values, "income");
            return (Criteria) this;
        }

        public Criteria andIncomeBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("income between", value1, value2, "income");
            return (Criteria) this;
        }

        public Criteria andIncomeNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("income not between", value1, value2, "income");
            return (Criteria) this;
        }

        public Criteria andSettleCycleIsNull() {
            addCriterion("settle_cycle is null");
            return (Criteria) this;
        }

        public Criteria andSettleCycleIsNotNull() {
            addCriterion("settle_cycle is not null");
            return (Criteria) this;
        }

        public Criteria andSettleCycleEqualTo(String value) {
            addCriterion("settle_cycle =", value, "settleCycle");
            return (Criteria) this;
        }

        public Criteria andSettleCycleNotEqualTo(String value) {
            addCriterion("settle_cycle <>", value, "settleCycle");
            return (Criteria) this;
        }

        public Criteria andSettleCycleGreaterThan(String value) {
            addCriterion("settle_cycle >", value, "settleCycle");
            return (Criteria) this;
        }

        public Criteria andSettleCycleGreaterThanOrEqualTo(String value) {
            addCriterion("settle_cycle >=", value, "settleCycle");
            return (Criteria) this;
        }

        public Criteria andSettleCycleLessThan(String value) {
            addCriterion("settle_cycle <", value, "settleCycle");
            return (Criteria) this;
        }

        public Criteria andSettleCycleLessThanOrEqualTo(String value) {
            addCriterion("settle_cycle <=", value, "settleCycle");
            return (Criteria) this;
        }

        public Criteria andSettleCycleLike(String value) {
            addCriterion("settle_cycle like", value, "settleCycle");
            return (Criteria) this;
        }

        public Criteria andSettleCycleNotLike(String value) {
            addCriterion("settle_cycle not like", value, "settleCycle");
            return (Criteria) this;
        }

        public Criteria andSettleCycleIn(List<String> values) {
            addCriterion("settle_cycle in", values, "settleCycle");
            return (Criteria) this;
        }

        public Criteria andSettleCycleNotIn(List<String> values) {
            addCriterion("settle_cycle not in", values, "settleCycle");
            return (Criteria) this;
        }

        public Criteria andSettleCycleBetween(String value1, String value2) {
            addCriterion("settle_cycle between", value1, value2, "settleCycle");
            return (Criteria) this;
        }

        public Criteria andSettleCycleNotBetween(String value1, String value2) {
            addCriterion("settle_cycle not between", value1, value2, "settleCycle");
            return (Criteria) this;
        }

        public Criteria andOrderStatusIsNull() {
            addCriterion("order_status is null");
            return (Criteria) this;
        }

        public Criteria andOrderStatusIsNotNull() {
            addCriterion("order_status is not null");
            return (Criteria) this;
        }

        public Criteria andOrderStatusEqualTo(Integer value) {
            addCriterion("order_status =", value, "orderStatus");
            return (Criteria) this;
        }

        public Criteria andOrderStatusNotEqualTo(Integer value) {
            addCriterion("order_status <>", value, "orderStatus");
            return (Criteria) this;
        }

        public Criteria andOrderStatusGreaterThan(Integer value) {
            addCriterion("order_status >", value, "orderStatus");
            return (Criteria) this;
        }

        public Criteria andOrderStatusGreaterThanOrEqualTo(Integer value) {
            addCriterion("order_status >=", value, "orderStatus");
            return (Criteria) this;
        }

        public Criteria andOrderStatusLessThan(Integer value) {
            addCriterion("order_status <", value, "orderStatus");
            return (Criteria) this;
        }

        public Criteria andOrderStatusLessThanOrEqualTo(Integer value) {
            addCriterion("order_status <=", value, "orderStatus");
            return (Criteria) this;
        }

        public Criteria andOrderStatusIn(List<Integer> values) {
            addCriterion("order_status in", values, "orderStatus");
            return (Criteria) this;
        }

        public Criteria andOrderStatusNotIn(List<Integer> values) {
            addCriterion("order_status not in", values, "orderStatus");
            return (Criteria) this;
        }

        public Criteria andOrderStatusBetween(Integer value1, Integer value2) {
            addCriterion("order_status between", value1, value2, "orderStatus");
            return (Criteria) this;
        }

        public Criteria andOrderStatusNotBetween(Integer value1, Integer value2) {
            addCriterion("order_status not between", value1, value2, "orderStatus");
            return (Criteria) this;
        }

        public Criteria andSettleStatusIsNull() {
            addCriterion("settle_status is null");
            return (Criteria) this;
        }

        public Criteria andSettleStatusIsNotNull() {
            addCriterion("settle_status is not null");
            return (Criteria) this;
        }

        public Criteria andSettleStatusEqualTo(Integer value) {
            addCriterion("settle_status =", value, "settleStatus");
            return (Criteria) this;
        }

        public Criteria andSettleStatusNotEqualTo(Integer value) {
            addCriterion("settle_status <>", value, "settleStatus");
            return (Criteria) this;
        }

        public Criteria andSettleStatusGreaterThan(Integer value) {
            addCriterion("settle_status >", value, "settleStatus");
            return (Criteria) this;
        }

        public Criteria andSettleStatusGreaterThanOrEqualTo(Integer value) {
            addCriterion("settle_status >=", value, "settleStatus");
            return (Criteria) this;
        }

        public Criteria andSettleStatusLessThan(Integer value) {
            addCriterion("settle_status <", value, "settleStatus");
            return (Criteria) this;
        }

        public Criteria andSettleStatusLessThanOrEqualTo(Integer value) {
            addCriterion("settle_status <=", value, "settleStatus");
            return (Criteria) this;
        }

        public Criteria andSettleStatusIn(List<Integer> values) {
            addCriterion("settle_status in", values, "settleStatus");
            return (Criteria) this;
        }

        public Criteria andSettleStatusNotIn(List<Integer> values) {
            addCriterion("settle_status not in", values, "settleStatus");
            return (Criteria) this;
        }

        public Criteria andSettleStatusBetween(Integer value1, Integer value2) {
            addCriterion("settle_status between", value1, value2, "settleStatus");
            return (Criteria) this;
        }

        public Criteria andSettleStatusNotBetween(Integer value1, Integer value2) {
            addCriterion("settle_status not between", value1, value2, "settleStatus");
            return (Criteria) this;
        }

        public Criteria andTradeTimeIsNull() {
            addCriterion("trade_time is null");
            return (Criteria) this;
        }

        public Criteria andTradeTimeIsNotNull() {
            addCriterion("trade_time is not null");
            return (Criteria) this;
        }

        public Criteria andTradeTimeEqualTo(Date value) {
            addCriterion("trade_time =", value, "tradeTime");
            return (Criteria) this;
        }

        public Criteria andTradeTimeNotEqualTo(Date value) {
            addCriterion("trade_time <>", value, "tradeTime");
            return (Criteria) this;
        }

        public Criteria andTradeTimeGreaterThan(Date value) {
            addCriterion("trade_time >", value, "tradeTime");
            return (Criteria) this;
        }

        public Criteria andTradeTimeGreaterThanOrEqualTo(Date value) {
            addCriterion("trade_time >=", value, "tradeTime");
            return (Criteria) this;
        }

        public Criteria andTradeTimeLessThan(Date value) {
            addCriterion("trade_time <", value, "tradeTime");
            return (Criteria) this;
        }

        public Criteria andTradeTimeLessThanOrEqualTo(Date value) {
            addCriterion("trade_time <=", value, "tradeTime");
            return (Criteria) this;
        }

        public Criteria andTradeTimeIn(List<Date> values) {
            addCriterion("trade_time in", values, "tradeTime");
            return (Criteria) this;
        }

        public Criteria andTradeTimeNotIn(List<Date> values) {
            addCriterion("trade_time not in", values, "tradeTime");
            return (Criteria) this;
        }

        public Criteria andTradeTimeBetween(Date value1, Date value2) {
            addCriterion("trade_time between", value1, value2, "tradeTime");
            return (Criteria) this;
        }

        public Criteria andTradeTimeNotBetween(Date value1, Date value2) {
            addCriterion("trade_time not between", value1, value2, "tradeTime");
            return (Criteria) this;
        }

        public Criteria andBankTimeIsNull() {
            addCriterion("bank_time is null");
            return (Criteria) this;
        }

        public Criteria andBankTimeIsNotNull() {
            addCriterion("bank_time is not null");
            return (Criteria) this;
        }

        public Criteria andBankTimeEqualTo(Date value) {
            addCriterion("bank_time =", value, "bankTime");
            return (Criteria) this;
        }

        public Criteria andBankTimeNotEqualTo(Date value) {
            addCriterion("bank_time <>", value, "bankTime");
            return (Criteria) this;
        }

        public Criteria andBankTimeGreaterThan(Date value) {
            addCriterion("bank_time >", value, "bankTime");
            return (Criteria) this;
        }

        public Criteria andBankTimeGreaterThanOrEqualTo(Date value) {
            addCriterion("bank_time >=", value, "bankTime");
            return (Criteria) this;
        }

        public Criteria andBankTimeLessThan(Date value) {
            addCriterion("bank_time <", value, "bankTime");
            return (Criteria) this;
        }

        public Criteria andBankTimeLessThanOrEqualTo(Date value) {
            addCriterion("bank_time <=", value, "bankTime");
            return (Criteria) this;
        }

        public Criteria andBankTimeIn(List<Date> values) {
            addCriterion("bank_time in", values, "bankTime");
            return (Criteria) this;
        }

        public Criteria andBankTimeNotIn(List<Date> values) {
            addCriterion("bank_time not in", values, "bankTime");
            return (Criteria) this;
        }

        public Criteria andBankTimeBetween(Date value1, Date value2) {
            addCriterion("bank_time between", value1, value2, "bankTime");
            return (Criteria) this;
        }

        public Criteria andBankTimeNotBetween(Date value1, Date value2) {
            addCriterion("bank_time not between", value1, value2, "bankTime");
            return (Criteria) this;
        }

        public Criteria andOrgOrderIdIsNull() {
            addCriterion("org_order_id is null");
            return (Criteria) this;
        }

        public Criteria andOrgOrderIdIsNotNull() {
            addCriterion("org_order_id is not null");
            return (Criteria) this;
        }

        public Criteria andOrgOrderIdEqualTo(String value) {
            addCriterion("org_order_id =", value, "orgOrderId");
            return (Criteria) this;
        }

        public Criteria andOrgOrderIdNotEqualTo(String value) {
            addCriterion("org_order_id <>", value, "orgOrderId");
            return (Criteria) this;
        }

        public Criteria andOrgOrderIdGreaterThan(String value) {
            addCriterion("org_order_id >", value, "orgOrderId");
            return (Criteria) this;
        }

        public Criteria andOrgOrderIdGreaterThanOrEqualTo(String value) {
            addCriterion("org_order_id >=", value, "orgOrderId");
            return (Criteria) this;
        }

        public Criteria andOrgOrderIdLessThan(String value) {
            addCriterion("org_order_id <", value, "orgOrderId");
            return (Criteria) this;
        }

        public Criteria andOrgOrderIdLessThanOrEqualTo(String value) {
            addCriterion("org_order_id <=", value, "orgOrderId");
            return (Criteria) this;
        }

        public Criteria andOrgOrderIdLike(String value) {
            addCriterion("org_order_id like", value, "orgOrderId");
            return (Criteria) this;
        }

        public Criteria andOrgOrderIdNotLike(String value) {
            addCriterion("org_order_id not like", value, "orgOrderId");
            return (Criteria) this;
        }

        public Criteria andOrgOrderIdIn(List<String> values) {
            addCriterion("org_order_id in", values, "orgOrderId");
            return (Criteria) this;
        }

        public Criteria andOrgOrderIdNotIn(List<String> values) {
            addCriterion("org_order_id not in", values, "orgOrderId");
            return (Criteria) this;
        }

        public Criteria andOrgOrderIdBetween(String value1, String value2) {
            addCriterion("org_order_id between", value1, value2, "orgOrderId");
            return (Criteria) this;
        }

        public Criteria andOrgOrderIdNotBetween(String value1, String value2) {
            addCriterion("org_order_id not between", value1, value2, "orgOrderId");
            return (Criteria) this;
        }

        public Criteria andTradeResultIsNull() {
            addCriterion("trade_result is null");
            return (Criteria) this;
        }

        public Criteria andTradeResultIsNotNull() {
            addCriterion("trade_result is not null");
            return (Criteria) this;
        }

        public Criteria andTradeResultEqualTo(String value) {
            addCriterion("trade_result =", value, "tradeResult");
            return (Criteria) this;
        }

        public Criteria andTradeResultNotEqualTo(String value) {
            addCriterion("trade_result <>", value, "tradeResult");
            return (Criteria) this;
        }

        public Criteria andTradeResultGreaterThan(String value) {
            addCriterion("trade_result >", value, "tradeResult");
            return (Criteria) this;
        }

        public Criteria andTradeResultGreaterThanOrEqualTo(String value) {
            addCriterion("trade_result >=", value, "tradeResult");
            return (Criteria) this;
        }

        public Criteria andTradeResultLessThan(String value) {
            addCriterion("trade_result <", value, "tradeResult");
            return (Criteria) this;
        }

        public Criteria andTradeResultLessThanOrEqualTo(String value) {
            addCriterion("trade_result <=", value, "tradeResult");
            return (Criteria) this;
        }

        public Criteria andTradeResultLike(String value) {
            addCriterion("trade_result like", value, "tradeResult");
            return (Criteria) this;
        }

        public Criteria andTradeResultNotLike(String value) {
            addCriterion("trade_result not like", value, "tradeResult");
            return (Criteria) this;
        }

        public Criteria andTradeResultIn(List<String> values) {
            addCriterion("trade_result in", values, "tradeResult");
            return (Criteria) this;
        }

        public Criteria andTradeResultNotIn(List<String> values) {
            addCriterion("trade_result not in", values, "tradeResult");
            return (Criteria) this;
        }

        public Criteria andTradeResultBetween(String value1, String value2) {
            addCriterion("trade_result between", value1, value2, "tradeResult");
            return (Criteria) this;
        }

        public Criteria andTradeResultNotBetween(String value1, String value2) {
            addCriterion("trade_result not between", value1, value2, "tradeResult");
            return (Criteria) this;
        }

        public Criteria andTerminalMerIdIsNull() {
            addCriterion("terminal_mer_id is null");
            return (Criteria) this;
        }

        public Criteria andTerminalMerIdIsNotNull() {
            addCriterion("terminal_mer_id is not null");
            return (Criteria) this;
        }

        public Criteria andTerminalMerIdEqualTo(String value) {
            addCriterion("terminal_mer_id =", value, "terminalMerId");
            return (Criteria) this;
        }

        public Criteria andTerminalMerIdNotEqualTo(String value) {
            addCriterion("terminal_mer_id <>", value, "terminalMerId");
            return (Criteria) this;
        }

        public Criteria andTerminalMerIdGreaterThan(String value) {
            addCriterion("terminal_mer_id >", value, "terminalMerId");
            return (Criteria) this;
        }

        public Criteria andTerminalMerIdGreaterThanOrEqualTo(String value) {
            addCriterion("terminal_mer_id >=", value, "terminalMerId");
            return (Criteria) this;
        }

        public Criteria andTerminalMerIdLessThan(String value) {
            addCriterion("terminal_mer_id <", value, "terminalMerId");
            return (Criteria) this;
        }

        public Criteria andTerminalMerIdLessThanOrEqualTo(String value) {
            addCriterion("terminal_mer_id <=", value, "terminalMerId");
            return (Criteria) this;
        }

        public Criteria andTerminalMerIdLike(String value) {
            addCriterion("terminal_mer_id like", value, "terminalMerId");
            return (Criteria) this;
        }

        public Criteria andTerminalMerIdNotLike(String value) {
            addCriterion("terminal_mer_id not like", value, "terminalMerId");
            return (Criteria) this;
        }

        public Criteria andTerminalMerIdIn(List<String> values) {
            addCriterion("terminal_mer_id in", values, "terminalMerId");
            return (Criteria) this;
        }

        public Criteria andTerminalMerIdNotIn(List<String> values) {
            addCriterion("terminal_mer_id not in", values, "terminalMerId");
            return (Criteria) this;
        }

        public Criteria andTerminalMerIdBetween(String value1, String value2) {
            addCriterion("terminal_mer_id between", value1, value2, "terminalMerId");
            return (Criteria) this;
        }

        public Criteria andTerminalMerIdNotBetween(String value1, String value2) {
            addCriterion("terminal_mer_id not between", value1, value2, "terminalMerId");
            return (Criteria) this;
        }

        public Criteria andTerminalMerNameIsNull() {
            addCriterion("terminal_mer_name is null");
            return (Criteria) this;
        }

        public Criteria andTerminalMerNameIsNotNull() {
            addCriterion("terminal_mer_name is not null");
            return (Criteria) this;
        }

        public Criteria andTerminalMerNameEqualTo(String value) {
            addCriterion("terminal_mer_name =", value, "terminalMerName");
            return (Criteria) this;
        }

        public Criteria andTerminalMerNameNotEqualTo(String value) {
            addCriterion("terminal_mer_name <>", value, "terminalMerName");
            return (Criteria) this;
        }

        public Criteria andTerminalMerNameGreaterThan(String value) {
            addCriterion("terminal_mer_name >", value, "terminalMerName");
            return (Criteria) this;
        }

        public Criteria andTerminalMerNameGreaterThanOrEqualTo(String value) {
            addCriterion("terminal_mer_name >=", value, "terminalMerName");
            return (Criteria) this;
        }

        public Criteria andTerminalMerNameLessThan(String value) {
            addCriterion("terminal_mer_name <", value, "terminalMerName");
            return (Criteria) this;
        }

        public Criteria andTerminalMerNameLessThanOrEqualTo(String value) {
            addCriterion("terminal_mer_name <=", value, "terminalMerName");
            return (Criteria) this;
        }

        public Criteria andTerminalMerNameLike(String value) {
            addCriterion("terminal_mer_name like", value, "terminalMerName");
            return (Criteria) this;
        }

        public Criteria andTerminalMerNameNotLike(String value) {
            addCriterion("terminal_mer_name not like", value, "terminalMerName");
            return (Criteria) this;
        }

        public Criteria andTerminalMerNameIn(List<String> values) {
            addCriterion("terminal_mer_name in", values, "terminalMerName");
            return (Criteria) this;
        }

        public Criteria andTerminalMerNameNotIn(List<String> values) {
            addCriterion("terminal_mer_name not in", values, "terminalMerName");
            return (Criteria) this;
        }

        public Criteria andTerminalMerNameBetween(String value1, String value2) {
            addCriterion("terminal_mer_name between", value1, value2, "terminalMerName");
            return (Criteria) this;
        }

        public Criteria andTerminalMerNameNotBetween(String value1, String value2) {
            addCriterion("terminal_mer_name not between", value1, value2, "terminalMerName");
            return (Criteria) this;
        }

        public Criteria andAgmentIdIsNull() {
            addCriterion("agment_id is null");
            return (Criteria) this;
        }

        public Criteria andAgmentIdIsNotNull() {
            addCriterion("agment_id is not null");
            return (Criteria) this;
        }

        public Criteria andAgmentIdEqualTo(String value) {
            addCriterion("agment_id =", value, "agmentId");
            return (Criteria) this;
        }

        public Criteria andAgmentIdNotEqualTo(String value) {
            addCriterion("agment_id <>", value, "agmentId");
            return (Criteria) this;
        }

        public Criteria andAgmentIdGreaterThan(String value) {
            addCriterion("agment_id >", value, "agmentId");
            return (Criteria) this;
        }

        public Criteria andAgmentIdGreaterThanOrEqualTo(String value) {
            addCriterion("agment_id >=", value, "agmentId");
            return (Criteria) this;
        }

        public Criteria andAgmentIdLessThan(String value) {
            addCriterion("agment_id <", value, "agmentId");
            return (Criteria) this;
        }

        public Criteria andAgmentIdLessThanOrEqualTo(String value) {
            addCriterion("agment_id <=", value, "agmentId");
            return (Criteria) this;
        }

        public Criteria andAgmentIdLike(String value) {
            addCriterion("agment_id like", value, "agmentId");
            return (Criteria) this;
        }

        public Criteria andAgmentIdNotLike(String value) {
            addCriterion("agment_id not like", value, "agmentId");
            return (Criteria) this;
        }

        public Criteria andAgmentIdIn(List<String> values) {
            addCriterion("agment_id in", values, "agmentId");
            return (Criteria) this;
        }

        public Criteria andAgmentIdNotIn(List<String> values) {
            addCriterion("agment_id not in", values, "agmentId");
            return (Criteria) this;
        }

        public Criteria andAgmentIdBetween(String value1, String value2) {
            addCriterion("agment_id between", value1, value2, "agmentId");
            return (Criteria) this;
        }

        public Criteria andAgmentIdNotBetween(String value1, String value2) {
            addCriterion("agment_id not between", value1, value2, "agmentId");
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