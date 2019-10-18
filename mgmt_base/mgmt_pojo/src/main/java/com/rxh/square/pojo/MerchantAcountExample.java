package com.rxh.square.pojo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MerchantAcountExample  implements Serializable {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public MerchantAcountExample() {
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

        public Criteria andBenefitNameIsNull() {
            addCriterion("benefit_name is null");
            return (Criteria) this;
        }

        public Criteria andBenefitNameIsNotNull() {
            addCriterion("benefit_name is not null");
            return (Criteria) this;
        }

        public Criteria andBenefitNameEqualTo(String value) {
            addCriterion("benefit_name =", value, "benefitName");
            return (Criteria) this;
        }

        public Criteria andBenefitNameNotEqualTo(String value) {
            addCriterion("benefit_name <>", value, "benefitName");
            return (Criteria) this;
        }

        public Criteria andBenefitNameGreaterThan(String value) {
            addCriterion("benefit_name >", value, "benefitName");
            return (Criteria) this;
        }

        public Criteria andBenefitNameGreaterThanOrEqualTo(String value) {
            addCriterion("benefit_name >=", value, "benefitName");
            return (Criteria) this;
        }

        public Criteria andBenefitNameLessThan(String value) {
            addCriterion("benefit_name <", value, "benefitName");
            return (Criteria) this;
        }

        public Criteria andBenefitNameLessThanOrEqualTo(String value) {
            addCriterion("benefit_name <=", value, "benefitName");
            return (Criteria) this;
        }

        public Criteria andBenefitNameLike(String value) {
            addCriterion("benefit_name like", value, "benefitName");
            return (Criteria) this;
        }

        public Criteria andBenefitNameNotLike(String value) {
            addCriterion("benefit_name not like", value, "benefitName");
            return (Criteria) this;
        }

        public Criteria andBenefitNameIn(List<String> values) {
            addCriterion("benefit_name in", values, "benefitName");
            return (Criteria) this;
        }

        public Criteria andBenefitNameNotIn(List<String> values) {
            addCriterion("benefit_name not in", values, "benefitName");
            return (Criteria) this;
        }

        public Criteria andBenefitNameBetween(String value1, String value2) {
            addCriterion("benefit_name between", value1, value2, "benefitName");
            return (Criteria) this;
        }

        public Criteria andBenefitNameNotBetween(String value1, String value2) {
            addCriterion("benefit_name not between", value1, value2, "benefitName");
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

        public Criteria andIdentityUrl1IsNull() {
            addCriterion("identity_url1 is null");
            return (Criteria) this;
        }

        public Criteria andIdentityUrl1IsNotNull() {
            addCriterion("identity_url1 is not null");
            return (Criteria) this;
        }

        public Criteria andIdentityUrl1EqualTo(String value) {
            addCriterion("identity_url1 =", value, "identityUrl1");
            return (Criteria) this;
        }

        public Criteria andIdentityUrl1NotEqualTo(String value) {
            addCriterion("identity_url1 <>", value, "identityUrl1");
            return (Criteria) this;
        }

        public Criteria andIdentityUrl1GreaterThan(String value) {
            addCriterion("identity_url1 >", value, "identityUrl1");
            return (Criteria) this;
        }

        public Criteria andIdentityUrl1GreaterThanOrEqualTo(String value) {
            addCriterion("identity_url1 >=", value, "identityUrl1");
            return (Criteria) this;
        }

        public Criteria andIdentityUrl1LessThan(String value) {
            addCriterion("identity_url1 <", value, "identityUrl1");
            return (Criteria) this;
        }

        public Criteria andIdentityUrl1LessThanOrEqualTo(String value) {
            addCriterion("identity_url1 <=", value, "identityUrl1");
            return (Criteria) this;
        }

        public Criteria andIdentityUrl1Like(String value) {
            addCriterion("identity_url1 like", value, "identityUrl1");
            return (Criteria) this;
        }

        public Criteria andIdentityUrl1NotLike(String value) {
            addCriterion("identity_url1 not like", value, "identityUrl1");
            return (Criteria) this;
        }

        public Criteria andIdentityUrl1In(List<String> values) {
            addCriterion("identity_url1 in", values, "identityUrl1");
            return (Criteria) this;
        }

        public Criteria andIdentityUrl1NotIn(List<String> values) {
            addCriterion("identity_url1 not in", values, "identityUrl1");
            return (Criteria) this;
        }

        public Criteria andIdentityUrl1Between(String value1, String value2) {
            addCriterion("identity_url1 between", value1, value2, "identityUrl1");
            return (Criteria) this;
        }

        public Criteria andIdentityUrl1NotBetween(String value1, String value2) {
            addCriterion("identity_url1 not between", value1, value2, "identityUrl1");
            return (Criteria) this;
        }

        public Criteria andIdentityUrl2IsNull() {
            addCriterion("identity_url2 is null");
            return (Criteria) this;
        }

        public Criteria andIdentityUrl2IsNotNull() {
            addCriterion("identity_url2 is not null");
            return (Criteria) this;
        }

        public Criteria andIdentityUrl2EqualTo(String value) {
            addCriterion("identity_url2 =", value, "identityUrl2");
            return (Criteria) this;
        }

        public Criteria andIdentityUrl2NotEqualTo(String value) {
            addCriterion("identity_url2 <>", value, "identityUrl2");
            return (Criteria) this;
        }

        public Criteria andIdentityUrl2GreaterThan(String value) {
            addCriterion("identity_url2 >", value, "identityUrl2");
            return (Criteria) this;
        }

        public Criteria andIdentityUrl2GreaterThanOrEqualTo(String value) {
            addCriterion("identity_url2 >=", value, "identityUrl2");
            return (Criteria) this;
        }

        public Criteria andIdentityUrl2LessThan(String value) {
            addCriterion("identity_url2 <", value, "identityUrl2");
            return (Criteria) this;
        }

        public Criteria andIdentityUrl2LessThanOrEqualTo(String value) {
            addCriterion("identity_url2 <=", value, "identityUrl2");
            return (Criteria) this;
        }

        public Criteria andIdentityUrl2Like(String value) {
            addCriterion("identity_url2 like", value, "identityUrl2");
            return (Criteria) this;
        }

        public Criteria andIdentityUrl2NotLike(String value) {
            addCriterion("identity_url2 not like", value, "identityUrl2");
            return (Criteria) this;
        }

        public Criteria andIdentityUrl2In(List<String> values) {
            addCriterion("identity_url2 in", values, "identityUrl2");
            return (Criteria) this;
        }

        public Criteria andIdentityUrl2NotIn(List<String> values) {
            addCriterion("identity_url2 not in", values, "identityUrl2");
            return (Criteria) this;
        }

        public Criteria andIdentityUrl2Between(String value1, String value2) {
            addCriterion("identity_url2 between", value1, value2, "identityUrl2");
            return (Criteria) this;
        }

        public Criteria andIdentityUrl2NotBetween(String value1, String value2) {
            addCriterion("identity_url2 not between", value1, value2, "identityUrl2");
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