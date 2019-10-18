package com.rxh.pojo.merchant;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MerchantRegisterInfoExample {

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

        public Criteria andIdentityTypeEqualTo(Integer value) {
            addCriterion("identity_type =", value, "identityType");
            return (Criteria) this;
        }

        public Criteria andIdentityTypeNotEqualTo(Integer value) {
            addCriterion("identity_type <>", value, "identityType");
            return (Criteria) this;
        }

        public Criteria andIdentityTypeGreaterThan(Integer value) {
            addCriterion("identity_type >", value, "identityType");
            return (Criteria) this;
        }

        public Criteria andIdentityTypeGreaterThanOrEqualTo(Integer value) {
            addCriterion("identity_type >=", value, "identityType");
            return (Criteria) this;
        }

        public Criteria andIdentityTypeLessThan(Integer value) {
            addCriterion("identity_type <", value, "identityType");
            return (Criteria) this;
        }

        public Criteria andIdentityTypeLessThanOrEqualTo(Integer value) {
            addCriterion("identity_type <=", value, "identityType");
            return (Criteria) this;
        }

        public Criteria andIdentityTypeIn(List<Integer> values) {
            addCriterion("identity_type in", values, "identityType");
            return (Criteria) this;
        }

        public Criteria andIdentityTypeNotIn(List<Integer> values) {
            addCriterion("identity_type not in", values, "identityType");
            return (Criteria) this;
        }

        public Criteria andIdentityTypeBetween(Integer value1, Integer value2) {
            addCriterion("identity_type between", value1, value2, "identityType");
            return (Criteria) this;
        }

        public Criteria andIdentityTypeNotBetween(Integer value1, Integer value2) {
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

        public Criteria andMerchantTypeIsNull() {
            addCriterion("merchant_type is null");
            return (Criteria) this;
        }

        public Criteria andMerchantTypeIsNotNull() {
            addCriterion("merchant_type is not null");
            return (Criteria) this;
        }

        public Criteria andMerchantTypeEqualTo(String value) {
            addCriterion("merchant_type =", value, "merchantType");
            return (Criteria) this;
        }

        public Criteria andMerchantTypeNotEqualTo(String value) {
            addCriterion("merchant_type <>", value, "merchantType");
            return (Criteria) this;
        }

        public Criteria andMerchantTypeGreaterThan(String value) {
            addCriterion("merchant_type >", value, "merchantType");
            return (Criteria) this;
        }

        public Criteria andMerchantTypeGreaterThanOrEqualTo(String value) {
            addCriterion("merchant_type >=", value, "merchantType");
            return (Criteria) this;
        }

        public Criteria andMerchantTypeLessThan(String value) {
            addCriterion("merchant_type <", value, "merchantType");
            return (Criteria) this;
        }

        public Criteria andMerchantTypeLessThanOrEqualTo(String value) {
            addCriterion("merchant_type <=", value, "merchantType");
            return (Criteria) this;
        }

        public Criteria andMerchantTypeLike(String value) {
            addCriterion("merchant_type like", value, "merchantType");
            return (Criteria) this;
        }

        public Criteria andMerchantTypeNotLike(String value) {
            addCriterion("merchant_type not like", value, "merchantType");
            return (Criteria) this;
        }

        public Criteria andMerchantTypeIn(List<String> values) {
            addCriterion("merchant_type in", values, "merchantType");
            return (Criteria) this;
        }

        public Criteria andMerchantTypeNotIn(List<String> values) {
            addCriterion("merchant_type not in", values, "merchantType");
            return (Criteria) this;
        }

        public Criteria andMerchantTypeBetween(String value1, String value2) {
            addCriterion("merchant_type between", value1, value2, "merchantType");
            return (Criteria) this;
        }

        public Criteria andMerchantTypeNotBetween(String value1, String value2) {
            addCriterion("merchant_type not between", value1, value2, "merchantType");
            return (Criteria) this;
        }

        public Criteria andProvinceIsNull() {
            addCriterion("province is null");
            return (Criteria) this;
        }

        public Criteria andProvinceIsNotNull() {
            addCriterion("province is not null");
            return (Criteria) this;
        }

        public Criteria andProvinceEqualTo(String value) {
            addCriterion("province =", value, "province");
            return (Criteria) this;
        }

        public Criteria andProvinceNotEqualTo(String value) {
            addCriterion("province <>", value, "province");
            return (Criteria) this;
        }

        public Criteria andProvinceGreaterThan(String value) {
            addCriterion("province >", value, "province");
            return (Criteria) this;
        }

        public Criteria andProvinceGreaterThanOrEqualTo(String value) {
            addCriterion("province >=", value, "province");
            return (Criteria) this;
        }

        public Criteria andProvinceLessThan(String value) {
            addCriterion("province <", value, "province");
            return (Criteria) this;
        }

        public Criteria andProvinceLessThanOrEqualTo(String value) {
            addCriterion("province <=", value, "province");
            return (Criteria) this;
        }

        public Criteria andProvinceLike(String value) {
            addCriterion("province like", value, "province");
            return (Criteria) this;
        }

        public Criteria andProvinceNotLike(String value) {
            addCriterion("province not like", value, "province");
            return (Criteria) this;
        }

        public Criteria andProvinceIn(List<String> values) {
            addCriterion("province in", values, "province");
            return (Criteria) this;
        }

        public Criteria andProvinceNotIn(List<String> values) {
            addCriterion("province not in", values, "province");
            return (Criteria) this;
        }

        public Criteria andProvinceBetween(String value1, String value2) {
            addCriterion("province between", value1, value2, "province");
            return (Criteria) this;
        }

        public Criteria andProvinceNotBetween(String value1, String value2) {
            addCriterion("province not between", value1, value2, "province");
            return (Criteria) this;
        }

        public Criteria andCityIsNull() {
            addCriterion("city is null");
            return (Criteria) this;
        }

        public Criteria andCityIsNotNull() {
            addCriterion("city is not null");
            return (Criteria) this;
        }

        public Criteria andCityEqualTo(String value) {
            addCriterion("city =", value, "city");
            return (Criteria) this;
        }

        public Criteria andCityNotEqualTo(String value) {
            addCriterion("city <>", value, "city");
            return (Criteria) this;
        }

        public Criteria andCityGreaterThan(String value) {
            addCriterion("city >", value, "city");
            return (Criteria) this;
        }

        public Criteria andCityGreaterThanOrEqualTo(String value) {
            addCriterion("city >=", value, "city");
            return (Criteria) this;
        }

        public Criteria andCityLessThan(String value) {
            addCriterion("city <", value, "city");
            return (Criteria) this;
        }

        public Criteria andCityLessThanOrEqualTo(String value) {
            addCriterion("city <=", value, "city");
            return (Criteria) this;
        }

        public Criteria andCityLike(String value) {
            addCriterion("city like", value, "city");
            return (Criteria) this;
        }

        public Criteria andCityNotLike(String value) {
            addCriterion("city not like", value, "city");
            return (Criteria) this;
        }

        public Criteria andCityIn(List<String> values) {
            addCriterion("city in", values, "city");
            return (Criteria) this;
        }

        public Criteria andCityNotIn(List<String> values) {
            addCriterion("city not in", values, "city");
            return (Criteria) this;
        }

        public Criteria andCityBetween(String value1, String value2) {
            addCriterion("city between", value1, value2, "city");
            return (Criteria) this;
        }

        public Criteria andCityNotBetween(String value1, String value2) {
            addCriterion("city not between", value1, value2, "city");
            return (Criteria) this;
        }

        public Criteria andMerchantAddressIsNull() {
            addCriterion("merchant_address is null");
            return (Criteria) this;
        }

        public Criteria andMerchantAddressIsNotNull() {
            addCriterion("merchant_address is not null");
            return (Criteria) this;
        }

        public Criteria andMerchantAddressEqualTo(String value) {
            addCriterion("merchant_address =", value, "merchantAddress");
            return (Criteria) this;
        }

        public Criteria andMerchantAddressNotEqualTo(String value) {
            addCriterion("merchant_address <>", value, "merchantAddress");
            return (Criteria) this;
        }

        public Criteria andMerchantAddressGreaterThan(String value) {
            addCriterion("merchant_address >", value, "merchantAddress");
            return (Criteria) this;
        }

        public Criteria andMerchantAddressGreaterThanOrEqualTo(String value) {
            addCriterion("merchant_address >=", value, "merchantAddress");
            return (Criteria) this;
        }

        public Criteria andMerchantAddressLessThan(String value) {
            addCriterion("merchant_address <", value, "merchantAddress");
            return (Criteria) this;
        }

        public Criteria andMerchantAddressLessThanOrEqualTo(String value) {
            addCriterion("merchant_address <=", value, "merchantAddress");
            return (Criteria) this;
        }

        public Criteria andMerchantAddressLike(String value) {
            addCriterion("merchant_address like", value, "merchantAddress");
            return (Criteria) this;
        }

        public Criteria andMerchantAddressNotLike(String value) {
            addCriterion("merchant_address not like", value, "merchantAddress");
            return (Criteria) this;
        }

        public Criteria andMerchantAddressIn(List<String> values) {
            addCriterion("merchant_address in", values, "merchantAddress");
            return (Criteria) this;
        }

        public Criteria andMerchantAddressNotIn(List<String> values) {
            addCriterion("merchant_address not in", values, "merchantAddress");
            return (Criteria) this;
        }

        public Criteria andMerchantAddressBetween(String value1, String value2) {
            addCriterion("merchant_address between", value1, value2, "merchantAddress");
            return (Criteria) this;
        }

        public Criteria andMerchantAddressNotBetween(String value1, String value2) {
            addCriterion("merchant_address not between", value1, value2, "merchantAddress");
            return (Criteria) this;
        }

        public Criteria andTimeIsNull() {
            addCriterion("TIME is null");
            return (Criteria) this;
        }

        public Criteria andTimeIsNotNull() {
            addCriterion("TIME is not null");
            return (Criteria) this;
        }

        public Criteria andTimeEqualTo(Date value) {
            addCriterion("TIME =", value, "time");
            return (Criteria) this;
        }

        public Criteria andTimeNotEqualTo(Date value) {
            addCriterion("TIME <>", value, "time");
            return (Criteria) this;
        }

        public Criteria andTimeGreaterThan(Date value) {
            addCriterion("TIME >", value, "time");
            return (Criteria) this;
        }

        public Criteria andTimeGreaterThanOrEqualTo(Date value) {
            addCriterion("TIME >=", value, "time");
            return (Criteria) this;
        }

        public Criteria andTimeLessThan(Date value) {
            addCriterion("TIME <", value, "time");
            return (Criteria) this;
        }

        public Criteria andTimeLessThanOrEqualTo(Date value) {
            addCriterion("TIME <=", value, "time");
            return (Criteria) this;
        }

        public Criteria andTimeIn(List<Date> values) {
            addCriterion("TIME in", values, "time");
            return (Criteria) this;
        }

        public Criteria andTimeNotIn(List<Date> values) {
            addCriterion("TIME not in", values, "time");
            return (Criteria) this;
        }

        public Criteria andTimeBetween(Date value1, Date value2) {
            addCriterion("TIME between", value1, value2, "time");
            return (Criteria) this;
        }

        public Criteria andTimeNotBetween(Date value1, Date value2) {
            addCriterion("TIME not between", value1, value2, "time");
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