package com.rxh.square.pojo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class MerchantRegisterInfoExample implements Serializable {

    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public MerchantRegisterInfoExample() {
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

        public Criteria andTradeFeeIsNull() {
            addCriterion("trade_fee is null");
            return (Criteria) this;
        }

        public Criteria andTradeFeeIsNotNull() {
            addCriterion("trade_fee is not null");
            return (Criteria) this;
        }

        public Criteria andTradeFeeEqualTo(BigDecimal value) {
            addCriterion("trade_fee =", value, "tradeFee");
            return (Criteria) this;
        }

        public Criteria andTradeFeeNotEqualTo(BigDecimal value) {
            addCriterion("trade_fee <>", value, "tradeFee");
            return (Criteria) this;
        }

        public Criteria andTradeFeeGreaterThan(BigDecimal value) {
            addCriterion("trade_fee >", value, "tradeFee");
            return (Criteria) this;
        }

        public Criteria andTradeFeeGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("trade_fee >=", value, "tradeFee");
            return (Criteria) this;
        }

        public Criteria andTradeFeeLessThan(BigDecimal value) {
            addCriterion("trade_fee <", value, "tradeFee");
            return (Criteria) this;
        }

        public Criteria andTradeFeeLessThanOrEqualTo(BigDecimal value) {
            addCriterion("trade_fee <=", value, "tradeFee");
            return (Criteria) this;
        }

        public Criteria andTradeFeeIn(List<BigDecimal> values) {
            addCriterion("trade_fee in", values, "tradeFee");
            return (Criteria) this;
        }

        public Criteria andTradeFeeNotIn(List<BigDecimal> values) {
            addCriterion("trade_fee not in", values, "tradeFee");
            return (Criteria) this;
        }

        public Criteria andTradeFeeBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("trade_fee between", value1, value2, "tradeFee");
            return (Criteria) this;
        }

        public Criteria andTradeFeeNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("trade_fee not between", value1, value2, "tradeFee");
            return (Criteria) this;
        }

        public Criteria andBackFeeIsNull() {
            addCriterion("back_fee is null");
            return (Criteria) this;
        }

        public Criteria andBackFeeIsNotNull() {
            addCriterion("back_fee is not null");
            return (Criteria) this;
        }

        public Criteria andBackFeeEqualTo(BigDecimal value) {
            addCriterion("back_fee =", value, "backFee");
            return (Criteria) this;
        }

        public Criteria andBackFeeNotEqualTo(BigDecimal value) {
            addCriterion("back_fee <>", value, "backFee");
            return (Criteria) this;
        }

        public Criteria andBackFeeGreaterThan(BigDecimal value) {
            addCriterion("back_fee >", value, "backFee");
            return (Criteria) this;
        }

        public Criteria andBackFeeGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("back_fee >=", value, "backFee");
            return (Criteria) this;
        }

        public Criteria andBackFeeLessThan(BigDecimal value) {
            addCriterion("back_fee <", value, "backFee");
            return (Criteria) this;
        }

        public Criteria andBackFeeLessThanOrEqualTo(BigDecimal value) {
            addCriterion("back_fee <=", value, "backFee");
            return (Criteria) this;
        }

        public Criteria andBackFeeIn(List<BigDecimal> values) {
            addCriterion("back_fee in", values, "backFee");
            return (Criteria) this;
        }

        public Criteria andBackFeeNotIn(List<BigDecimal> values) {
            addCriterion("back_fee not in", values, "backFee");
            return (Criteria) this;
        }

        public Criteria andBackFeeBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("back_fee between", value1, value2, "backFee");
            return (Criteria) this;
        }

        public Criteria andBackFeeNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("back_fee not between", value1, value2, "backFee");
            return (Criteria) this;
        }

        public Criteria andUserNameIsNull() {
            addCriterion("user_name is null");
            return (Criteria) this;
        }

        public Criteria andUserNameIsNotNull() {
            addCriterion("user_name is not null");
            return (Criteria) this;
        }

        public Criteria andUserNameEqualTo(String value) {
            addCriterion("user_name =", value, "userName");
            return (Criteria) this;
        }

        public Criteria andUserNameNotEqualTo(String value) {
            addCriterion("user_name <>", value, "userName");
            return (Criteria) this;
        }

        public Criteria andUserNameGreaterThan(String value) {
            addCriterion("user_name >", value, "userName");
            return (Criteria) this;
        }

        public Criteria andUserNameGreaterThanOrEqualTo(String value) {
            addCriterion("user_name >=", value, "userName");
            return (Criteria) this;
        }

        public Criteria andUserNameLessThan(String value) {
            addCriterion("user_name <", value, "userName");
            return (Criteria) this;
        }

        public Criteria andUserNameLessThanOrEqualTo(String value) {
            addCriterion("user_name <=", value, "userName");
            return (Criteria) this;
        }

        public Criteria andUserNameLike(String value) {
            addCriterion("user_name like", value, "userName");
            return (Criteria) this;
        }

        public Criteria andUserNameNotLike(String value) {
            addCriterion("user_name not like", value, "userName");
            return (Criteria) this;
        }

        public Criteria andUserNameIn(List<String> values) {
            addCriterion("user_name in", values, "userName");
            return (Criteria) this;
        }

        public Criteria andUserNameNotIn(List<String> values) {
            addCriterion("user_name not in", values, "userName");
            return (Criteria) this;
        }

        public Criteria andUserNameBetween(String value1, String value2) {
            addCriterion("user_name between", value1, value2, "userName");
            return (Criteria) this;
        }

        public Criteria andUserNameNotBetween(String value1, String value2) {
            addCriterion("user_name not between", value1, value2, "userName");
            return (Criteria) this;
        }

        public Criteria andUserShortNameIsNull() {
            addCriterion("user_short_name is null");
            return (Criteria) this;
        }

        public Criteria andUserShortNameIsNotNull() {
            addCriterion("user_short_name is not null");
            return (Criteria) this;
        }

        public Criteria andUserShortNameEqualTo(String value) {
            addCriterion("user_short_name =", value, "userShortName");
            return (Criteria) this;
        }

        public Criteria andUserShortNameNotEqualTo(String value) {
            addCriterion("user_short_name <>", value, "userShortName");
            return (Criteria) this;
        }

        public Criteria andUserShortNameGreaterThan(String value) {
            addCriterion("user_short_name >", value, "userShortName");
            return (Criteria) this;
        }

        public Criteria andUserShortNameGreaterThanOrEqualTo(String value) {
            addCriterion("user_short_name >=", value, "userShortName");
            return (Criteria) this;
        }

        public Criteria andUserShortNameLessThan(String value) {
            addCriterion("user_short_name <", value, "userShortName");
            return (Criteria) this;
        }

        public Criteria andUserShortNameLessThanOrEqualTo(String value) {
            addCriterion("user_short_name <=", value, "userShortName");
            return (Criteria) this;
        }

        public Criteria andUserShortNameLike(String value) {
            addCriterion("user_short_name like", value, "userShortName");
            return (Criteria) this;
        }

        public Criteria andUserShortNameNotLike(String value) {
            addCriterion("user_short_name not like", value, "userShortName");
            return (Criteria) this;
        }

        public Criteria andUserShortNameIn(List<String> values) {
            addCriterion("user_short_name in", values, "userShortName");
            return (Criteria) this;
        }

        public Criteria andUserShortNameNotIn(List<String> values) {
            addCriterion("user_short_name not in", values, "userShortName");
            return (Criteria) this;
        }

        public Criteria andUserShortNameBetween(String value1, String value2) {
            addCriterion("user_short_name between", value1, value2, "userShortName");
            return (Criteria) this;
        }

        public Criteria andUserShortNameNotBetween(String value1, String value2) {
            addCriterion("user_short_name not between", value1, value2, "userShortName");
            return (Criteria) this;
        }

        public Criteria andIdentityTypeIsNull() {
            addCriterion("identity_type is null");
            return (Criteria) this;
        }

        public Criteria andIdentityTypeIsNotNull() {
            addCriterion("identity_type is not null");
            return (Criteria) this;
        }

        public Criteria andIdentityTypeEqualTo(String value) {
            addCriterion("identity_type =", value, "identityType");
            return (Criteria) this;
        }

        public Criteria andIdentityTypeNotEqualTo(String value) {
            addCriterion("identity_type <>", value, "identityType");
            return (Criteria) this;
        }

        public Criteria andIdentityTypeGreaterThan(String value) {
            addCriterion("identity_type >", value, "identityType");
            return (Criteria) this;
        }

        public Criteria andIdentityTypeGreaterThanOrEqualTo(String value) {
            addCriterion("identity_type >=", value, "identityType");
            return (Criteria) this;
        }

        public Criteria andIdentityTypeLessThan(String value) {
            addCriterion("identity_type <", value, "identityType");
            return (Criteria) this;
        }

        public Criteria andIdentityTypeLessThanOrEqualTo(String value) {
            addCriterion("identity_type <=", value, "identityType");
            return (Criteria) this;
        }

        public Criteria andIdentityTypeLike(String value) {
            addCriterion("identity_type like", value, "identityType");
            return (Criteria) this;
        }

        public Criteria andIdentityTypeNotLike(String value) {
            addCriterion("identity_type not like", value, "identityType");
            return (Criteria) this;
        }

        public Criteria andIdentityTypeIn(List<String> values) {
            addCriterion("identity_type in", values, "identityType");
            return (Criteria) this;
        }

        public Criteria andIdentityTypeNotIn(List<String> values) {
            addCriterion("identity_type not in", values, "identityType");
            return (Criteria) this;
        }

        public Criteria andIdentityTypeBetween(String value1, String value2) {
            addCriterion("identity_type between", value1, value2, "identityType");
            return (Criteria) this;
        }

        public Criteria andIdentityTypeNotBetween(String value1, String value2) {
            addCriterion("identity_type not between", value1, value2, "identityType");
            return (Criteria) this;
        }

        public Criteria andIdentityNumIsNull() {
            addCriterion("identity_num is null");
            return (Criteria) this;
        }

        public Criteria andIdentityNumIsNotNull() {
            addCriterion("identity_num is not null");
            return (Criteria) this;
        }

        public Criteria andIdentityNumEqualTo(String value) {
            addCriterion("identity_num =", value, "identityNum");
            return (Criteria) this;
        }

        public Criteria andIdentityNumNotEqualTo(String value) {
            addCriterion("identity_num <>", value, "identityNum");
            return (Criteria) this;
        }

        public Criteria andIdentityNumGreaterThan(String value) {
            addCriterion("identity_num >", value, "identityNum");
            return (Criteria) this;
        }

        public Criteria andIdentityNumGreaterThanOrEqualTo(String value) {
            addCriterion("identity_num >=", value, "identityNum");
            return (Criteria) this;
        }

        public Criteria andIdentityNumLessThan(String value) {
            addCriterion("identity_num <", value, "identityNum");
            return (Criteria) this;
        }

        public Criteria andIdentityNumLessThanOrEqualTo(String value) {
            addCriterion("identity_num <=", value, "identityNum");
            return (Criteria) this;
        }

        public Criteria andIdentityNumLike(String value) {
            addCriterion("identity_num like", value, "identityNum");
            return (Criteria) this;
        }

        public Criteria andIdentityNumNotLike(String value) {
            addCriterion("identity_num not like", value, "identityNum");
            return (Criteria) this;
        }

        public Criteria andIdentityNumIn(List<String> values) {
            addCriterion("identity_num in", values, "identityNum");
            return (Criteria) this;
        }

        public Criteria andIdentityNumNotIn(List<String> values) {
            addCriterion("identity_num not in", values, "identityNum");
            return (Criteria) this;
        }

        public Criteria andIdentityNumBetween(String value1, String value2) {
            addCriterion("identity_num between", value1, value2, "identityNum");
            return (Criteria) this;
        }

        public Criteria andIdentityNumNotBetween(String value1, String value2) {
            addCriterion("identity_num not between", value1, value2, "identityNum");
            return (Criteria) this;
        }

        public Criteria andPhoneIsNull() {
            addCriterion("phone is null");
            return (Criteria) this;
        }

        public Criteria andPhoneIsNotNull() {
            addCriterion("phone is not null");
            return (Criteria) this;
        }

        public Criteria andPhoneEqualTo(String value) {
            addCriterion("phone =", value, "phone");
            return (Criteria) this;
        }

        public Criteria andPhoneNotEqualTo(String value) {
            addCriterion("phone <>", value, "phone");
            return (Criteria) this;
        }

        public Criteria andPhoneGreaterThan(String value) {
            addCriterion("phone >", value, "phone");
            return (Criteria) this;
        }

        public Criteria andPhoneGreaterThanOrEqualTo(String value) {
            addCriterion("phone >=", value, "phone");
            return (Criteria) this;
        }

        public Criteria andPhoneLessThan(String value) {
            addCriterion("phone <", value, "phone");
            return (Criteria) this;
        }

        public Criteria andPhoneLessThanOrEqualTo(String value) {
            addCriterion("phone <=", value, "phone");
            return (Criteria) this;
        }

        public Criteria andPhoneLike(String value) {
            addCriterion("phone like", value, "phone");
            return (Criteria) this;
        }

        public Criteria andPhoneNotLike(String value) {
            addCriterion("phone not like", value, "phone");
            return (Criteria) this;
        }

        public Criteria andPhoneIn(List<String> values) {
            addCriterion("phone in", values, "phone");
            return (Criteria) this;
        }

        public Criteria andPhoneNotIn(List<String> values) {
            addCriterion("phone not in", values, "phone");
            return (Criteria) this;
        }

        public Criteria andPhoneBetween(String value1, String value2) {
            addCriterion("phone between", value1, value2, "phone");
            return (Criteria) this;
        }

        public Criteria andPhoneNotBetween(String value1, String value2) {
            addCriterion("phone not between", value1, value2, "phone");
            return (Criteria) this;
        }

        public Criteria andCardTypeIsNull() {
            addCriterion("card_type is null");
            return (Criteria) this;
        }

        public Criteria andCardTypeIsNotNull() {
            addCriterion("card_type is not null");
            return (Criteria) this;
        }

        public Criteria andCardTypeEqualTo(String value) {
            addCriterion("card_type =", value, "cardType");
            return (Criteria) this;
        }

        public Criteria andCardTypeNotEqualTo(String value) {
            addCriterion("card_type <>", value, "cardType");
            return (Criteria) this;
        }

        public Criteria andCardTypeGreaterThan(String value) {
            addCriterion("card_type >", value, "cardType");
            return (Criteria) this;
        }

        public Criteria andCardTypeGreaterThanOrEqualTo(String value) {
            addCriterion("card_type >=", value, "cardType");
            return (Criteria) this;
        }

        public Criteria andCardTypeLessThan(String value) {
            addCriterion("card_type <", value, "cardType");
            return (Criteria) this;
        }

        public Criteria andCardTypeLessThanOrEqualTo(String value) {
            addCriterion("card_type <=", value, "cardType");
            return (Criteria) this;
        }

        public Criteria andCardTypeLike(String value) {
            addCriterion("card_type like", value, "cardType");
            return (Criteria) this;
        }

        public Criteria andCardTypeNotLike(String value) {
            addCriterion("card_type not like", value, "cardType");
            return (Criteria) this;
        }

        public Criteria andCardTypeIn(List<String> values) {
            addCriterion("card_type in", values, "cardType");
            return (Criteria) this;
        }

        public Criteria andCardTypeNotIn(List<String> values) {
            addCriterion("card_type not in", values, "cardType");
            return (Criteria) this;
        }

        public Criteria andCardTypeBetween(String value1, String value2) {
            addCriterion("card_type between", value1, value2, "cardType");
            return (Criteria) this;
        }

        public Criteria andCardTypeNotBetween(String value1, String value2) {
            addCriterion("card_type not between", value1, value2, "cardType");
            return (Criteria) this;
        }

        public Criteria andCardNumIsNull() {
            addCriterion("card_num is null");
            return (Criteria) this;
        }

        public Criteria andCardNumIsNotNull() {
            addCriterion("card_num is not null");
            return (Criteria) this;
        }

        public Criteria andCardNumEqualTo(String value) {
            addCriterion("card_num =", value, "cardNum");
            return (Criteria) this;
        }

        public Criteria andCardNumNotEqualTo(String value) {
            addCriterion("card_num <>", value, "cardNum");
            return (Criteria) this;
        }

        public Criteria andCardNumGreaterThan(String value) {
            addCriterion("card_num >", value, "cardNum");
            return (Criteria) this;
        }

        public Criteria andCardNumGreaterThanOrEqualTo(String value) {
            addCriterion("card_num >=", value, "cardNum");
            return (Criteria) this;
        }

        public Criteria andCardNumLessThan(String value) {
            addCriterion("card_num <", value, "cardNum");
            return (Criteria) this;
        }

        public Criteria andCardNumLessThanOrEqualTo(String value) {
            addCriterion("card_num <=", value, "cardNum");
            return (Criteria) this;
        }

        public Criteria andCardNumLike(String value) {
            addCriterion("card_num like", value, "cardNum");
            return (Criteria) this;
        }

        public Criteria andCardNumNotLike(String value) {
            addCriterion("card_num not like", value, "cardNum");
            return (Criteria) this;
        }

        public Criteria andCardNumIn(List<String> values) {
            addCriterion("card_num in", values, "cardNum");
            return (Criteria) this;
        }

        public Criteria andCardNumNotIn(List<String> values) {
            addCriterion("card_num not in", values, "cardNum");
            return (Criteria) this;
        }

        public Criteria andCardNumBetween(String value1, String value2) {
            addCriterion("card_num between", value1, value2, "cardNum");
            return (Criteria) this;
        }

        public Criteria andCardNumNotBetween(String value1, String value2) {
            addCriterion("card_num not between", value1, value2, "cardNum");
            return (Criteria) this;
        }

        public Criteria andBankCodeIsNull() {
            addCriterion("bank_code is null");
            return (Criteria) this;
        }

        public Criteria andBankCodeIsNotNull() {
            addCriterion("bank_code is not null");
            return (Criteria) this;
        }

        public Criteria andBankCodeEqualTo(String value) {
            addCriterion("bank_code =", value, "bankCode");
            return (Criteria) this;
        }

        public Criteria andBankCodeNotEqualTo(String value) {
            addCriterion("bank_code <>", value, "bankCode");
            return (Criteria) this;
        }

        public Criteria andBankCodeGreaterThan(String value) {
            addCriterion("bank_code >", value, "bankCode");
            return (Criteria) this;
        }

        public Criteria andBankCodeGreaterThanOrEqualTo(String value) {
            addCriterion("bank_code >=", value, "bankCode");
            return (Criteria) this;
        }

        public Criteria andBankCodeLessThan(String value) {
            addCriterion("bank_code <", value, "bankCode");
            return (Criteria) this;
        }

        public Criteria andBankCodeLessThanOrEqualTo(String value) {
            addCriterion("bank_code <=", value, "bankCode");
            return (Criteria) this;
        }

        public Criteria andBankCodeLike(String value) {
            addCriterion("bank_code like", value, "bankCode");
            return (Criteria) this;
        }

        public Criteria andBankCodeNotLike(String value) {
            addCriterion("bank_code not like", value, "bankCode");
            return (Criteria) this;
        }

        public Criteria andBankCodeIn(List<String> values) {
            addCriterion("bank_code in", values, "bankCode");
            return (Criteria) this;
        }

        public Criteria andBankCodeNotIn(List<String> values) {
            addCriterion("bank_code not in", values, "bankCode");
            return (Criteria) this;
        }

        public Criteria andBankCodeBetween(String value1, String value2) {
            addCriterion("bank_code between", value1, value2, "bankCode");
            return (Criteria) this;
        }

        public Criteria andBankCodeNotBetween(String value1, String value2) {
            addCriterion("bank_code not between", value1, value2, "bankCode");
            return (Criteria) this;
        }

        public Criteria andCvvIsNull() {
            addCriterion("cvv is null");
            return (Criteria) this;
        }

        public Criteria andCvvIsNotNull() {
            addCriterion("cvv is not null");
            return (Criteria) this;
        }

        public Criteria andCvvEqualTo(String value) {
            addCriterion("cvv =", value, "cvv");
            return (Criteria) this;
        }

        public Criteria andCvvNotEqualTo(String value) {
            addCriterion("cvv <>", value, "cvv");
            return (Criteria) this;
        }

        public Criteria andCvvGreaterThan(String value) {
            addCriterion("cvv >", value, "cvv");
            return (Criteria) this;
        }

        public Criteria andCvvGreaterThanOrEqualTo(String value) {
            addCriterion("cvv >=", value, "cvv");
            return (Criteria) this;
        }

        public Criteria andCvvLessThan(String value) {
            addCriterion("cvv <", value, "cvv");
            return (Criteria) this;
        }

        public Criteria andCvvLessThanOrEqualTo(String value) {
            addCriterion("cvv <=", value, "cvv");
            return (Criteria) this;
        }

        public Criteria andCvvLike(String value) {
            addCriterion("cvv like", value, "cvv");
            return (Criteria) this;
        }

        public Criteria andCvvNotLike(String value) {
            addCriterion("cvv not like", value, "cvv");
            return (Criteria) this;
        }

        public Criteria andCvvIn(List<String> values) {
            addCriterion("cvv in", values, "cvv");
            return (Criteria) this;
        }

        public Criteria andCvvNotIn(List<String> values) {
            addCriterion("cvv not in", values, "cvv");
            return (Criteria) this;
        }

        public Criteria andCvvBetween(String value1, String value2) {
            addCriterion("cvv between", value1, value2, "cvv");
            return (Criteria) this;
        }

        public Criteria andCvvNotBetween(String value1, String value2) {
            addCriterion("cvv not between", value1, value2, "cvv");
            return (Criteria) this;
        }

        public Criteria andExpireDateIsNull() {
            addCriterion("expire_date is null");
            return (Criteria) this;
        }

        public Criteria andExpireDateIsNotNull() {
            addCriterion("expire_date is not null");
            return (Criteria) this;
        }

        public Criteria andExpireDateEqualTo(String value) {
            addCriterion("expire_date =", value, "expireDate");
            return (Criteria) this;
        }

        public Criteria andExpireDateNotEqualTo(String value) {
            addCriterion("expire_date <>", value, "expireDate");
            return (Criteria) this;
        }

        public Criteria andExpireDateGreaterThan(String value) {
            addCriterion("expire_date >", value, "expireDate");
            return (Criteria) this;
        }

        public Criteria andExpireDateGreaterThanOrEqualTo(String value) {
            addCriterion("expire_date >=", value, "expireDate");
            return (Criteria) this;
        }

        public Criteria andExpireDateLessThan(String value) {
            addCriterion("expire_date <", value, "expireDate");
            return (Criteria) this;
        }

        public Criteria andExpireDateLessThanOrEqualTo(String value) {
            addCriterion("expire_date <=", value, "expireDate");
            return (Criteria) this;
        }

        public Criteria andExpireDateLike(String value) {
            addCriterion("expire_date like", value, "expireDate");
            return (Criteria) this;
        }

        public Criteria andExpireDateNotLike(String value) {
            addCriterion("expire_date not like", value, "expireDate");
            return (Criteria) this;
        }

        public Criteria andExpireDateIn(List<String> values) {
            addCriterion("expire_date in", values, "expireDate");
            return (Criteria) this;
        }

        public Criteria andExpireDateNotIn(List<String> values) {
            addCriterion("expire_date not in", values, "expireDate");
            return (Criteria) this;
        }

        public Criteria andExpireDateBetween(String value1, String value2) {
            addCriterion("expire_date between", value1, value2, "expireDate");
            return (Criteria) this;
        }

        public Criteria andExpireDateNotBetween(String value1, String value2) {
            addCriterion("expire_date not between", value1, value2, "expireDate");
            return (Criteria) this;
        }

        public Criteria andExtraChannelIdIsNull() {
            addCriterion("extra_channel_id is null");
            return (Criteria) this;
        }

        public Criteria andExtraChannelIdIsNotNull() {
            addCriterion("extra_channel_id is not null");
            return (Criteria) this;
        }

        public Criteria andExtraChannelIdEqualTo(String value) {
            addCriterion("extra_channel_id =", value, "extraChannelId");
            return (Criteria) this;
        }

        public Criteria andExtraChannelIdNotEqualTo(String value) {
            addCriterion("extra_channel_id <>", value, "extraChannelId");
            return (Criteria) this;
        }

        public Criteria andExtraChannelIdGreaterThan(String value) {
            addCriterion("extra_channel_id >", value, "extraChannelId");
            return (Criteria) this;
        }

        public Criteria andExtraChannelIdGreaterThanOrEqualTo(String value) {
            addCriterion("extra_channel_id >=", value, "extraChannelId");
            return (Criteria) this;
        }

        public Criteria andExtraChannelIdLessThan(String value) {
            addCriterion("extra_channel_id <", value, "extraChannelId");
            return (Criteria) this;
        }

        public Criteria andExtraChannelIdLessThanOrEqualTo(String value) {
            addCriterion("extra_channel_id <=", value, "extraChannelId");
            return (Criteria) this;
        }

        public Criteria andExtraChannelIdLike(String value) {
            addCriterion("extra_channel_id like", value, "extraChannelId");
            return (Criteria) this;
        }

        public Criteria andExtraChannelIdNotLike(String value) {
            addCriterion("extra_channel_id not like", value, "extraChannelId");
            return (Criteria) this;
        }

        public Criteria andExtraChannelIdIn(List<String> values) {
            addCriterion("extra_channel_id in", values, "extraChannelId");
            return (Criteria) this;
        }

        public Criteria andExtraChannelIdNotIn(List<String> values) {
            addCriterion("extra_channel_id not in", values, "extraChannelId");
            return (Criteria) this;
        }

        public Criteria andExtraChannelIdBetween(String value1, String value2) {
            addCriterion("extra_channel_id between", value1, value2, "extraChannelId");
            return (Criteria) this;
        }

        public Criteria andExtraChannelIdNotBetween(String value1, String value2) {
            addCriterion("extra_channel_id not between", value1, value2, "extraChannelId");
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

        public Criteria andPayTypeEqualTo(Integer value) {
            addCriterion("pay_type =", value, "payType");
            return (Criteria) this;
        }

        public Criteria andPayTypeNotEqualTo(Integer value) {
            addCriterion("pay_type <>", value, "payType");
            return (Criteria) this;
        }

        public Criteria andPayTypeGreaterThan(Integer value) {
            addCriterion("pay_type >", value, "payType");
            return (Criteria) this;
        }

        public Criteria andPayTypeGreaterThanOrEqualTo(Integer value) {
            addCriterion("pay_type >=", value, "payType");
            return (Criteria) this;
        }

        public Criteria andPayTypeLessThan(Integer value) {
            addCriterion("pay_type <", value, "payType");
            return (Criteria) this;
        }

        public Criteria andPayTypeLessThanOrEqualTo(Integer value) {
            addCriterion("pay_type <=", value, "payType");
            return (Criteria) this;
        }

        public Criteria andPayTypeIn(List<Integer> values) {
            addCriterion("pay_type in", values, "payType");
            return (Criteria) this;
        }

        public Criteria andPayTypeNotIn(List<Integer> values) {
            addCriterion("pay_type not in", values, "payType");
            return (Criteria) this;
        }

        public Criteria andPayTypeBetween(Integer value1, Integer value2) {
            addCriterion("pay_type between", value1, value2, "payType");
            return (Criteria) this;
        }

        public Criteria andPayTypeNotBetween(Integer value1, Integer value2) {
            addCriterion("pay_type not between", value1, value2, "payType");
            return (Criteria) this;
        }

        public Criteria andBackDataIsNull() {
            addCriterion("back_data is null");
            return (Criteria) this;
        }

        public Criteria andBackDataIsNotNull() {
            addCriterion("back_data is not null");
            return (Criteria) this;
        }

        public Criteria andBackDataEqualTo(String value) {
            addCriterion("back_data =", value, "backData");
            return (Criteria) this;
        }

        public Criteria andBackDataNotEqualTo(String value) {
            addCriterion("back_data <>", value, "backData");
            return (Criteria) this;
        }

        public Criteria andBackDataGreaterThan(String value) {
            addCriterion("back_data >", value, "backData");
            return (Criteria) this;
        }

        public Criteria andBackDataGreaterThanOrEqualTo(String value) {
            addCriterion("back_data >=", value, "backData");
            return (Criteria) this;
        }

        public Criteria andBackDataLessThan(String value) {
            addCriterion("back_data <", value, "backData");
            return (Criteria) this;
        }

        public Criteria andBackDataLessThanOrEqualTo(String value) {
            addCriterion("back_data <=", value, "backData");
            return (Criteria) this;
        }

        public Criteria andBackDataLike(String value) {
            addCriterion("back_data like", value, "backData");
            return (Criteria) this;
        }

        public Criteria andBackDataNotLike(String value) {
            addCriterion("back_data not like", value, "backData");
            return (Criteria) this;
        }

        public Criteria andBackDataIn(List<String> values) {
            addCriterion("back_data in", values, "backData");
            return (Criteria) this;
        }

        public Criteria andBackDataNotIn(List<String> values) {
            addCriterion("back_data not in", values, "backData");
            return (Criteria) this;
        }

        public Criteria andBackDataBetween(String value1, String value2) {
            addCriterion("back_data between", value1, value2, "backData");
            return (Criteria) this;
        }

        public Criteria andBackDataNotBetween(String value1, String value2) {
            addCriterion("back_data not between", value1, value2, "backData");
            return (Criteria) this;
        }

        public Criteria andResultIsNull() {
            addCriterion("result is null");
            return (Criteria) this;
        }

        public Criteria andResultIsNotNull() {
            addCriterion("result is not null");
            return (Criteria) this;
        }

        public Criteria andResultEqualTo(String value) {
            addCriterion("result =", value, "result");
            return (Criteria) this;
        }

        public Criteria andResultNotEqualTo(String value) {
            addCriterion("result <>", value, "result");
            return (Criteria) this;
        }

        public Criteria andResultGreaterThan(String value) {
            addCriterion("result >", value, "result");
            return (Criteria) this;
        }

        public Criteria andResultGreaterThanOrEqualTo(String value) {
            addCriterion("result >=", value, "result");
            return (Criteria) this;
        }

        public Criteria andResultLessThan(String value) {
            addCriterion("result <", value, "result");
            return (Criteria) this;
        }

        public Criteria andResultLessThanOrEqualTo(String value) {
            addCriterion("result <=", value, "result");
            return (Criteria) this;
        }

        public Criteria andResultLike(String value) {
            addCriterion("result like", value, "result");
            return (Criteria) this;
        }

        public Criteria andResultNotLike(String value) {
            addCriterion("result not like", value, "result");
            return (Criteria) this;
        }

        public Criteria andResultIn(List<String> values) {
            addCriterion("result in", values, "result");
            return (Criteria) this;
        }

        public Criteria andResultNotIn(List<String> values) {
            addCriterion("result not in", values, "result");
            return (Criteria) this;
        }

        public Criteria andResultBetween(String value1, String value2) {
            addCriterion("result between", value1, value2, "result");
            return (Criteria) this;
        }

        public Criteria andResultNotBetween(String value1, String value2) {
            addCriterion("result not between", value1, value2, "result");
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

        public Criteria andTimeIsNull() {
            addCriterion("time is null");
            return (Criteria) this;
        }

        public Criteria andTimeIsNotNull() {
            addCriterion("time is not null");
            return (Criteria) this;
        }

        public Criteria andTimeEqualTo(Date value) {
            addCriterion("time =", value, "time");
            return (Criteria) this;
        }

        public Criteria andTimeNotEqualTo(Date value) {
            addCriterion("time <>", value, "time");
            return (Criteria) this;
        }

        public Criteria andTimeGreaterThan(Date value) {
            addCriterion("time >", value, "time");
            return (Criteria) this;
        }

        public Criteria andTimeGreaterThanOrEqualTo(Date value) {
            addCriterion("time >=", value, "time");
            return (Criteria) this;
        }

        public Criteria andTimeLessThan(Date value) {
            addCriterion("time <", value, "time");
            return (Criteria) this;
        }

        public Criteria andTimeLessThanOrEqualTo(Date value) {
            addCriterion("time <=", value, "time");
            return (Criteria) this;
        }

        public Criteria andTimeIn(List<Date> values) {
            addCriterion("time in", values, "time");
            return (Criteria) this;
        }

        public Criteria andTimeNotIn(List<Date> values) {
            addCriterion("time not in", values, "time");
            return (Criteria) this;
        }

        public Criteria andTimeBetween(Date value1, Date value2) {
            addCriterion("time between", value1, value2, "time");
            return (Criteria) this;
        }

        public Criteria andTimeNotBetween(Date value1, Date value2) {
            addCriterion("time not between", value1, value2, "time");
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