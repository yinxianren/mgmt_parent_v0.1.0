package com.rxh.square.pojo;

import java.util.ArrayList;
import java.util.List;

public class PayCardholderInfoExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public PayCardholderInfoExample() {
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

        public Criteria andCardholderNameIsNull() {
            addCriterion("cardholder_name is null");
            return (Criteria) this;
        }

        public Criteria andCardholderNameIsNotNull() {
            addCriterion("cardholder_name is not null");
            return (Criteria) this;
        }

        public Criteria andCardholderNameEqualTo(String value) {
            addCriterion("cardholder_name =", value, "cardholderName");
            return (Criteria) this;
        }

        public Criteria andCardholderNameNotEqualTo(String value) {
            addCriterion("cardholder_name <>", value, "cardholderName");
            return (Criteria) this;
        }

        public Criteria andCardholderNameGreaterThan(String value) {
            addCriterion("cardholder_name >", value, "cardholderName");
            return (Criteria) this;
        }

        public Criteria andCardholderNameGreaterThanOrEqualTo(String value) {
            addCriterion("cardholder_name >=", value, "cardholderName");
            return (Criteria) this;
        }

        public Criteria andCardholderNameLessThan(String value) {
            addCriterion("cardholder_name <", value, "cardholderName");
            return (Criteria) this;
        }

        public Criteria andCardholderNameLessThanOrEqualTo(String value) {
            addCriterion("cardholder_name <=", value, "cardholderName");
            return (Criteria) this;
        }

        public Criteria andCardholderNameLike(String value) {
            addCriterion("cardholder_name like", value, "cardholderName");
            return (Criteria) this;
        }

        public Criteria andCardholderNameNotLike(String value) {
            addCriterion("cardholder_name not like", value, "cardholderName");
            return (Criteria) this;
        }

        public Criteria andCardholderNameIn(List<String> values) {
            addCriterion("cardholder_name in", values, "cardholderName");
            return (Criteria) this;
        }

        public Criteria andCardholderNameNotIn(List<String> values) {
            addCriterion("cardholder_name not in", values, "cardholderName");
            return (Criteria) this;
        }

        public Criteria andCardholderNameBetween(String value1, String value2) {
            addCriterion("cardholder_name between", value1, value2, "cardholderName");
            return (Criteria) this;
        }

        public Criteria andCardholderNameNotBetween(String value1, String value2) {
            addCriterion("cardholder_name not between", value1, value2, "cardholderName");
            return (Criteria) this;
        }

        public Criteria andCardholderPhoneIsNull() {
            addCriterion("cardholder_phone is null");
            return (Criteria) this;
        }

        public Criteria andCardholderPhoneIsNotNull() {
            addCriterion("cardholder_phone is not null");
            return (Criteria) this;
        }

        public Criteria andCardholderPhoneEqualTo(String value) {
            addCriterion("cardholder_phone =", value, "cardholderPhone");
            return (Criteria) this;
        }

        public Criteria andCardholderPhoneNotEqualTo(String value) {
            addCriterion("cardholder_phone <>", value, "cardholderPhone");
            return (Criteria) this;
        }

        public Criteria andCardholderPhoneGreaterThan(String value) {
            addCriterion("cardholder_phone >", value, "cardholderPhone");
            return (Criteria) this;
        }

        public Criteria andCardholderPhoneGreaterThanOrEqualTo(String value) {
            addCriterion("cardholder_phone >=", value, "cardholderPhone");
            return (Criteria) this;
        }

        public Criteria andCardholderPhoneLessThan(String value) {
            addCriterion("cardholder_phone <", value, "cardholderPhone");
            return (Criteria) this;
        }

        public Criteria andCardholderPhoneLessThanOrEqualTo(String value) {
            addCriterion("cardholder_phone <=", value, "cardholderPhone");
            return (Criteria) this;
        }

        public Criteria andCardholderPhoneLike(String value) {
            addCriterion("cardholder_phone like", value, "cardholderPhone");
            return (Criteria) this;
        }

        public Criteria andCardholderPhoneNotLike(String value) {
            addCriterion("cardholder_phone not like", value, "cardholderPhone");
            return (Criteria) this;
        }

        public Criteria andCardholderPhoneIn(List<String> values) {
            addCriterion("cardholder_phone in", values, "cardholderPhone");
            return (Criteria) this;
        }

        public Criteria andCardholderPhoneNotIn(List<String> values) {
            addCriterion("cardholder_phone not in", values, "cardholderPhone");
            return (Criteria) this;
        }

        public Criteria andCardholderPhoneBetween(String value1, String value2) {
            addCriterion("cardholder_phone between", value1, value2, "cardholderPhone");
            return (Criteria) this;
        }

        public Criteria andCardholderPhoneNotBetween(String value1, String value2) {
            addCriterion("cardholder_phone not between", value1, value2, "cardholderPhone");
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

        public Criteria andBankNameIsNull() {
            addCriterion("bank_name is null");
            return (Criteria) this;
        }

        public Criteria andBankNameIsNotNull() {
            addCriterion("bank_name is not null");
            return (Criteria) this;
        }

        public Criteria andBankNameEqualTo(String value) {
            addCriterion("bank_name =", value, "bankName");
            return (Criteria) this;
        }

        public Criteria andBankNameNotEqualTo(String value) {
            addCriterion("bank_name <>", value, "bankName");
            return (Criteria) this;
        }

        public Criteria andBankNameGreaterThan(String value) {
            addCriterion("bank_name >", value, "bankName");
            return (Criteria) this;
        }

        public Criteria andBankNameGreaterThanOrEqualTo(String value) {
            addCriterion("bank_name >=", value, "bankName");
            return (Criteria) this;
        }

        public Criteria andBankNameLessThan(String value) {
            addCriterion("bank_name <", value, "bankName");
            return (Criteria) this;
        }

        public Criteria andBankNameLessThanOrEqualTo(String value) {
            addCriterion("bank_name <=", value, "bankName");
            return (Criteria) this;
        }

        public Criteria andBankNameLike(String value) {
            addCriterion("bank_name like", value, "bankName");
            return (Criteria) this;
        }

        public Criteria andBankNameNotLike(String value) {
            addCriterion("bank_name not like", value, "bankName");
            return (Criteria) this;
        }

        public Criteria andBankNameIn(List<String> values) {
            addCriterion("bank_name in", values, "bankName");
            return (Criteria) this;
        }

        public Criteria andBankNameNotIn(List<String> values) {
            addCriterion("bank_name not in", values, "bankName");
            return (Criteria) this;
        }

        public Criteria andBankNameBetween(String value1, String value2) {
            addCriterion("bank_name between", value1, value2, "bankName");
            return (Criteria) this;
        }

        public Criteria andBankNameNotBetween(String value1, String value2) {
            addCriterion("bank_name not between", value1, value2, "bankName");
            return (Criteria) this;
        }

        public Criteria andBankBranchNameIsNull() {
            addCriterion("bank_branch_name is null");
            return (Criteria) this;
        }

        public Criteria andBankBranchNameIsNotNull() {
            addCriterion("bank_branch_name is not null");
            return (Criteria) this;
        }

        public Criteria andBankBranchNameEqualTo(String value) {
            addCriterion("bank_branch_name =", value, "bankBranchName");
            return (Criteria) this;
        }

        public Criteria andBankBranchNameNotEqualTo(String value) {
            addCriterion("bank_branch_name <>", value, "bankBranchName");
            return (Criteria) this;
        }

        public Criteria andBankBranchNameGreaterThan(String value) {
            addCriterion("bank_branch_name >", value, "bankBranchName");
            return (Criteria) this;
        }

        public Criteria andBankBranchNameGreaterThanOrEqualTo(String value) {
            addCriterion("bank_branch_name >=", value, "bankBranchName");
            return (Criteria) this;
        }

        public Criteria andBankBranchNameLessThan(String value) {
            addCriterion("bank_branch_name <", value, "bankBranchName");
            return (Criteria) this;
        }

        public Criteria andBankBranchNameLessThanOrEqualTo(String value) {
            addCriterion("bank_branch_name <=", value, "bankBranchName");
            return (Criteria) this;
        }

        public Criteria andBankBranchNameLike(String value) {
            addCriterion("bank_branch_name like", value, "bankBranchName");
            return (Criteria) this;
        }

        public Criteria andBankBranchNameNotLike(String value) {
            addCriterion("bank_branch_name not like", value, "bankBranchName");
            return (Criteria) this;
        }

        public Criteria andBankBranchNameIn(List<String> values) {
            addCriterion("bank_branch_name in", values, "bankBranchName");
            return (Criteria) this;
        }

        public Criteria andBankBranchNameNotIn(List<String> values) {
            addCriterion("bank_branch_name not in", values, "bankBranchName");
            return (Criteria) this;
        }

        public Criteria andBankBranchNameBetween(String value1, String value2) {
            addCriterion("bank_branch_name between", value1, value2, "bankBranchName");
            return (Criteria) this;
        }

        public Criteria andBankBranchNameNotBetween(String value1, String value2) {
            addCriterion("bank_branch_name not between", value1, value2, "bankBranchName");
            return (Criteria) this;
        }

        public Criteria andBankBranchNumIsNull() {
            addCriterion("bank_branch_num is null");
            return (Criteria) this;
        }

        public Criteria andBankBranchNumIsNotNull() {
            addCriterion("bank_branch_num is not null");
            return (Criteria) this;
        }

        public Criteria andBankBranchNumEqualTo(String value) {
            addCriterion("bank_branch_num =", value, "bankBranchNum");
            return (Criteria) this;
        }

        public Criteria andBankBranchNumNotEqualTo(String value) {
            addCriterion("bank_branch_num <>", value, "bankBranchNum");
            return (Criteria) this;
        }

        public Criteria andBankBranchNumGreaterThan(String value) {
            addCriterion("bank_branch_num >", value, "bankBranchNum");
            return (Criteria) this;
        }

        public Criteria andBankBranchNumGreaterThanOrEqualTo(String value) {
            addCriterion("bank_branch_num >=", value, "bankBranchNum");
            return (Criteria) this;
        }

        public Criteria andBankBranchNumLessThan(String value) {
            addCriterion("bank_branch_num <", value, "bankBranchNum");
            return (Criteria) this;
        }

        public Criteria andBankBranchNumLessThanOrEqualTo(String value) {
            addCriterion("bank_branch_num <=", value, "bankBranchNum");
            return (Criteria) this;
        }

        public Criteria andBankBranchNumLike(String value) {
            addCriterion("bank_branch_num like", value, "bankBranchNum");
            return (Criteria) this;
        }

        public Criteria andBankBranchNumNotLike(String value) {
            addCriterion("bank_branch_num not like", value, "bankBranchNum");
            return (Criteria) this;
        }

        public Criteria andBankBranchNumIn(List<String> values) {
            addCriterion("bank_branch_num in", values, "bankBranchNum");
            return (Criteria) this;
        }

        public Criteria andBankBranchNumNotIn(List<String> values) {
            addCriterion("bank_branch_num not in", values, "bankBranchNum");
            return (Criteria) this;
        }

        public Criteria andBankBranchNumBetween(String value1, String value2) {
            addCriterion("bank_branch_num between", value1, value2, "bankBranchNum");
            return (Criteria) this;
        }

        public Criteria andBankBranchNumNotBetween(String value1, String value2) {
            addCriterion("bank_branch_num not between", value1, value2, "bankBranchNum");
            return (Criteria) this;
        }

        public Criteria andBankcardNumIsNull() {
            addCriterion("bankcard_num is null");
            return (Criteria) this;
        }

        public Criteria andBankcardNumIsNotNull() {
            addCriterion("bankcard_num is not null");
            return (Criteria) this;
        }

        public Criteria andBankcardNumEqualTo(String value) {
            addCriterion("bankcard_num =", value, "bankcardNum");
            return (Criteria) this;
        }

        public Criteria andBankcardNumNotEqualTo(String value) {
            addCriterion("bankcard_num <>", value, "bankcardNum");
            return (Criteria) this;
        }

        public Criteria andBankcardNumGreaterThan(String value) {
            addCriterion("bankcard_num >", value, "bankcardNum");
            return (Criteria) this;
        }

        public Criteria andBankcardNumGreaterThanOrEqualTo(String value) {
            addCriterion("bankcard_num >=", value, "bankcardNum");
            return (Criteria) this;
        }

        public Criteria andBankcardNumLessThan(String value) {
            addCriterion("bankcard_num <", value, "bankcardNum");
            return (Criteria) this;
        }

        public Criteria andBankcardNumLessThanOrEqualTo(String value) {
            addCriterion("bankcard_num <=", value, "bankcardNum");
            return (Criteria) this;
        }

        public Criteria andBankcardNumLike(String value) {
            addCriterion("bankcard_num like", value, "bankcardNum");
            return (Criteria) this;
        }

        public Criteria andBankcardNumNotLike(String value) {
            addCriterion("bankcard_num not like", value, "bankcardNum");
            return (Criteria) this;
        }

        public Criteria andBankcardNumIn(List<String> values) {
            addCriterion("bankcard_num in", values, "bankcardNum");
            return (Criteria) this;
        }

        public Criteria andBankcardNumNotIn(List<String> values) {
            addCriterion("bankcard_num not in", values, "bankcardNum");
            return (Criteria) this;
        }

        public Criteria andBankcardNumBetween(String value1, String value2) {
            addCriterion("bankcard_num between", value1, value2, "bankcardNum");
            return (Criteria) this;
        }

        public Criteria andBankcardNumNotBetween(String value1, String value2) {
            addCriterion("bankcard_num not between", value1, value2, "bankcardNum");
            return (Criteria) this;
        }

        public Criteria andBankcardTypeIsNull() {
            addCriterion("bankcard_type is null");
            return (Criteria) this;
        }

        public Criteria andBankcardTypeIsNotNull() {
            addCriterion("bankcard_type is not null");
            return (Criteria) this;
        }

        public Criteria andBankcardTypeEqualTo(Integer value) {
            addCriterion("bankcard_type =", value, "bankcardType");
            return (Criteria) this;
        }

        public Criteria andBankcardTypeNotEqualTo(Integer value) {
            addCriterion("bankcard_type <>", value, "bankcardType");
            return (Criteria) this;
        }

        public Criteria andBankcardTypeGreaterThan(Integer value) {
            addCriterion("bankcard_type >", value, "bankcardType");
            return (Criteria) this;
        }

        public Criteria andBankcardTypeGreaterThanOrEqualTo(Integer value) {
            addCriterion("bankcard_type >=", value, "bankcardType");
            return (Criteria) this;
        }

        public Criteria andBankcardTypeLessThan(Integer value) {
            addCriterion("bankcard_type <", value, "bankcardType");
            return (Criteria) this;
        }

        public Criteria andBankcardTypeLessThanOrEqualTo(Integer value) {
            addCriterion("bankcard_type <=", value, "bankcardType");
            return (Criteria) this;
        }

        public Criteria andBankcardTypeIn(List<Integer> values) {
            addCriterion("bankcard_type in", values, "bankcardType");
            return (Criteria) this;
        }

        public Criteria andBankcardTypeNotIn(List<Integer> values) {
            addCriterion("bankcard_type not in", values, "bankcardType");
            return (Criteria) this;
        }

        public Criteria andBankcardTypeBetween(Integer value1, Integer value2) {
            addCriterion("bankcard_type between", value1, value2, "bankcardType");
            return (Criteria) this;
        }

        public Criteria andBankcardTypeNotBetween(Integer value1, Integer value2) {
            addCriterion("bankcard_type not between", value1, value2, "bankcardType");
            return (Criteria) this;
        }

        public Criteria andExpiryYearIsNull() {
            addCriterion("expiry_year is null");
            return (Criteria) this;
        }

        public Criteria andExpiryYearIsNotNull() {
            addCriterion("expiry_year is not null");
            return (Criteria) this;
        }

        public Criteria andExpiryYearEqualTo(String value) {
            addCriterion("expiry_year =", value, "expiryYear");
            return (Criteria) this;
        }

        public Criteria andExpiryYearNotEqualTo(String value) {
            addCriterion("expiry_year <>", value, "expiryYear");
            return (Criteria) this;
        }

        public Criteria andExpiryYearGreaterThan(String value) {
            addCriterion("expiry_year >", value, "expiryYear");
            return (Criteria) this;
        }

        public Criteria andExpiryYearGreaterThanOrEqualTo(String value) {
            addCriterion("expiry_year >=", value, "expiryYear");
            return (Criteria) this;
        }

        public Criteria andExpiryYearLessThan(String value) {
            addCriterion("expiry_year <", value, "expiryYear");
            return (Criteria) this;
        }

        public Criteria andExpiryYearLessThanOrEqualTo(String value) {
            addCriterion("expiry_year <=", value, "expiryYear");
            return (Criteria) this;
        }

        public Criteria andExpiryYearLike(String value) {
            addCriterion("expiry_year like", value, "expiryYear");
            return (Criteria) this;
        }

        public Criteria andExpiryYearNotLike(String value) {
            addCriterion("expiry_year not like", value, "expiryYear");
            return (Criteria) this;
        }

        public Criteria andExpiryYearIn(List<String> values) {
            addCriterion("expiry_year in", values, "expiryYear");
            return (Criteria) this;
        }

        public Criteria andExpiryYearNotIn(List<String> values) {
            addCriterion("expiry_year not in", values, "expiryYear");
            return (Criteria) this;
        }

        public Criteria andExpiryYearBetween(String value1, String value2) {
            addCriterion("expiry_year between", value1, value2, "expiryYear");
            return (Criteria) this;
        }

        public Criteria andExpiryYearNotBetween(String value1, String value2) {
            addCriterion("expiry_year not between", value1, value2, "expiryYear");
            return (Criteria) this;
        }

        public Criteria andExpiryMonthIsNull() {
            addCriterion("expiry_month is null");
            return (Criteria) this;
        }

        public Criteria andExpiryMonthIsNotNull() {
            addCriterion("expiry_month is not null");
            return (Criteria) this;
        }

        public Criteria andExpiryMonthEqualTo(String value) {
            addCriterion("expiry_month =", value, "expiryMonth");
            return (Criteria) this;
        }

        public Criteria andExpiryMonthNotEqualTo(String value) {
            addCriterion("expiry_month <>", value, "expiryMonth");
            return (Criteria) this;
        }

        public Criteria andExpiryMonthGreaterThan(String value) {
            addCriterion("expiry_month >", value, "expiryMonth");
            return (Criteria) this;
        }

        public Criteria andExpiryMonthGreaterThanOrEqualTo(String value) {
            addCriterion("expiry_month >=", value, "expiryMonth");
            return (Criteria) this;
        }

        public Criteria andExpiryMonthLessThan(String value) {
            addCriterion("expiry_month <", value, "expiryMonth");
            return (Criteria) this;
        }

        public Criteria andExpiryMonthLessThanOrEqualTo(String value) {
            addCriterion("expiry_month <=", value, "expiryMonth");
            return (Criteria) this;
        }

        public Criteria andExpiryMonthLike(String value) {
            addCriterion("expiry_month like", value, "expiryMonth");
            return (Criteria) this;
        }

        public Criteria andExpiryMonthNotLike(String value) {
            addCriterion("expiry_month not like", value, "expiryMonth");
            return (Criteria) this;
        }

        public Criteria andExpiryMonthIn(List<String> values) {
            addCriterion("expiry_month in", values, "expiryMonth");
            return (Criteria) this;
        }

        public Criteria andExpiryMonthNotIn(List<String> values) {
            addCriterion("expiry_month not in", values, "expiryMonth");
            return (Criteria) this;
        }

        public Criteria andExpiryMonthBetween(String value1, String value2) {
            addCriterion("expiry_month between", value1, value2, "expiryMonth");
            return (Criteria) this;
        }

        public Criteria andExpiryMonthNotBetween(String value1, String value2) {
            addCriterion("expiry_month not between", value1, value2, "expiryMonth");
            return (Criteria) this;
        }

        public Criteria andCvvIsNull() {
            addCriterion("CVV is null");
            return (Criteria) this;
        }

        public Criteria andCvvIsNotNull() {
            addCriterion("CVV is not null");
            return (Criteria) this;
        }

        public Criteria andCvvEqualTo(String value) {
            addCriterion("CVV =", value, "cvv");
            return (Criteria) this;
        }

        public Criteria andCvvNotEqualTo(String value) {
            addCriterion("CVV <>", value, "cvv");
            return (Criteria) this;
        }

        public Criteria andCvvGreaterThan(String value) {
            addCriterion("CVV >", value, "cvv");
            return (Criteria) this;
        }

        public Criteria andCvvGreaterThanOrEqualTo(String value) {
            addCriterion("CVV >=", value, "cvv");
            return (Criteria) this;
        }

        public Criteria andCvvLessThan(String value) {
            addCriterion("CVV <", value, "cvv");
            return (Criteria) this;
        }

        public Criteria andCvvLessThanOrEqualTo(String value) {
            addCriterion("CVV <=", value, "cvv");
            return (Criteria) this;
        }

        public Criteria andCvvLike(String value) {
            addCriterion("CVV like", value, "cvv");
            return (Criteria) this;
        }

        public Criteria andCvvNotLike(String value) {
            addCriterion("CVV not like", value, "cvv");
            return (Criteria) this;
        }

        public Criteria andCvvIn(List<String> values) {
            addCriterion("CVV in", values, "cvv");
            return (Criteria) this;
        }

        public Criteria andCvvNotIn(List<String> values) {
            addCriterion("CVV not in", values, "cvv");
            return (Criteria) this;
        }

        public Criteria andCvvBetween(String value1, String value2) {
            addCriterion("CVV between", value1, value2, "cvv");
            return (Criteria) this;
        }

        public Criteria andCvvNotBetween(String value1, String value2) {
            addCriterion("CVV not between", value1, value2, "cvv");
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