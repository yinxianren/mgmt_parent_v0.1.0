package com.rxh.square.pojo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class AgentMerchantInfoExample  implements Serializable {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public AgentMerchantInfoExample() {
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

        public Criteria andAgentMerchantNameIsNull() {
            addCriterion("agent_merchant_name is null");
            return (Criteria) this;
        }

        public Criteria andAgentMerchantNameIsNotNull() {
            addCriterion("agent_merchant_name is not null");
            return (Criteria) this;
        }

        public Criteria andAgentMerchantNameEqualTo(String value) {
            addCriterion("agent_merchant_name =", value, "agentMerchantName");
            return (Criteria) this;
        }

        public Criteria andAgentMerchantNameNotEqualTo(String value) {
            addCriterion("agent_merchant_name <>", value, "agentMerchantName");
            return (Criteria) this;
        }

        public Criteria andAgentMerchantNameGreaterThan(String value) {
            addCriterion("agent_merchant_name >", value, "agentMerchantName");
            return (Criteria) this;
        }

        public Criteria andAgentMerchantNameGreaterThanOrEqualTo(String value) {
            addCriterion("agent_merchant_name >=", value, "agentMerchantName");
            return (Criteria) this;
        }

        public Criteria andAgentMerchantNameLessThan(String value) {
            addCriterion("agent_merchant_name <", value, "agentMerchantName");
            return (Criteria) this;
        }

        public Criteria andAgentMerchantNameLessThanOrEqualTo(String value) {
            addCriterion("agent_merchant_name <=", value, "agentMerchantName");
            return (Criteria) this;
        }

        public Criteria andAgentMerchantNameLike(String value) {
            addCriterion("agent_merchant_name like", value, "agentMerchantName");
            return (Criteria) this;
        }

        public Criteria andAgentMerchantNameNotLike(String value) {
            addCriterion("agent_merchant_name not like", value, "agentMerchantName");
            return (Criteria) this;
        }

        public Criteria andAgentMerchantNameIn(List<String> values) {
            addCriterion("agent_merchant_name in", values, "agentMerchantName");
            return (Criteria) this;
        }

        public Criteria andAgentMerchantNameNotIn(List<String> values) {
            addCriterion("agent_merchant_name not in", values, "agentMerchantName");
            return (Criteria) this;
        }

        public Criteria andAgentMerchantNameBetween(String value1, String value2) {
            addCriterion("agent_merchant_name between", value1, value2, "agentMerchantName");
            return (Criteria) this;
        }

        public Criteria andAgentMerchantNameNotBetween(String value1, String value2) {
            addCriterion("agent_merchant_name not between", value1, value2, "agentMerchantName");
            return (Criteria) this;
        }

        public Criteria andAgentMerchantShortNameIsNull() {
            addCriterion("agent_merchant_short_name is null");
            return (Criteria) this;
        }

        public Criteria andAgentMerchantShortNameIsNotNull() {
            addCriterion("agent_merchant_short_name is not null");
            return (Criteria) this;
        }

        public Criteria andAgentMerchantShortNameEqualTo(String value) {
            addCriterion("agent_merchant_short_name =", value, "agentMerchantShortName");
            return (Criteria) this;
        }

        public Criteria andAgentMerchantShortNameNotEqualTo(String value) {
            addCriterion("agent_merchant_short_name <>", value, "agentMerchantShortName");
            return (Criteria) this;
        }

        public Criteria andAgentMerchantShortNameGreaterThan(String value) {
            addCriterion("agent_merchant_short_name >", value, "agentMerchantShortName");
            return (Criteria) this;
        }

        public Criteria andAgentMerchantShortNameGreaterThanOrEqualTo(String value) {
            addCriterion("agent_merchant_short_name >=", value, "agentMerchantShortName");
            return (Criteria) this;
        }

        public Criteria andAgentMerchantShortNameLessThan(String value) {
            addCriterion("agent_merchant_short_name <", value, "agentMerchantShortName");
            return (Criteria) this;
        }

        public Criteria andAgentMerchantShortNameLessThanOrEqualTo(String value) {
            addCriterion("agent_merchant_short_name <=", value, "agentMerchantShortName");
            return (Criteria) this;
        }

        public Criteria andAgentMerchantShortNameLike(String value) {
            addCriterion("agent_merchant_short_name like", value, "agentMerchantShortName");
            return (Criteria) this;
        }

        public Criteria andAgentMerchantShortNameNotLike(String value) {
            addCriterion("agent_merchant_short_name not like", value, "agentMerchantShortName");
            return (Criteria) this;
        }

        public Criteria andAgentMerchantShortNameIn(List<String> values) {
            addCriterion("agent_merchant_short_name in", values, "agentMerchantShortName");
            return (Criteria) this;
        }

        public Criteria andAgentMerchantShortNameNotIn(List<String> values) {
            addCriterion("agent_merchant_short_name not in", values, "agentMerchantShortName");
            return (Criteria) this;
        }

        public Criteria andAgentMerchantShortNameBetween(String value1, String value2) {
            addCriterion("agent_merchant_short_name between", value1, value2, "agentMerchantShortName");
            return (Criteria) this;
        }

        public Criteria andAgentMerchantShortNameNotBetween(String value1, String value2) {
            addCriterion("agent_merchant_short_name not between", value1, value2, "agentMerchantShortName");
            return (Criteria) this;
        }

        public Criteria andAgentIdentityTypeIsNull() {
            addCriterion("agent_identity_type is null");
            return (Criteria) this;
        }

        public Criteria andAgentIdentityTypeIsNotNull() {
            addCriterion("agent_identity_type is not null");
            return (Criteria) this;
        }

        public Criteria andAgentIdentityTypeEqualTo(Integer value) {
            addCriterion("agent_identity_type =", value, "agentIdentityType");
            return (Criteria) this;
        }

        public Criteria andAgentIdentityTypeNotEqualTo(Integer value) {
            addCriterion("agent_identity_type <>", value, "agentIdentityType");
            return (Criteria) this;
        }

        public Criteria andAgentIdentityTypeGreaterThan(Integer value) {
            addCriterion("agent_identity_type >", value, "agentIdentityType");
            return (Criteria) this;
        }

        public Criteria andAgentIdentityTypeGreaterThanOrEqualTo(Integer value) {
            addCriterion("agent_identity_type >=", value, "agentIdentityType");
            return (Criteria) this;
        }

        public Criteria andAgentIdentityTypeLessThan(Integer value) {
            addCriterion("agent_identity_type <", value, "agentIdentityType");
            return (Criteria) this;
        }

        public Criteria andAgentIdentityTypeLessThanOrEqualTo(Integer value) {
            addCriterion("agent_identity_type <=", value, "agentIdentityType");
            return (Criteria) this;
        }

        public Criteria andAgentIdentityTypeIn(List<Integer> values) {
            addCriterion("agent_identity_type in", values, "agentIdentityType");
            return (Criteria) this;
        }

        public Criteria andAgentIdentityTypeNotIn(List<Integer> values) {
            addCriterion("agent_identity_type not in", values, "agentIdentityType");
            return (Criteria) this;
        }

        public Criteria andAgentIdentityTypeBetween(Integer value1, Integer value2) {
            addCriterion("agent_identity_type between", value1, value2, "agentIdentityType");
            return (Criteria) this;
        }

        public Criteria andAgentIdentityTypeNotBetween(Integer value1, Integer value2) {
            addCriterion("agent_identity_type not between", value1, value2, "agentIdentityType");
            return (Criteria) this;
        }

        public Criteria andAgentIdentityNumIsNull() {
            addCriterion("agent_identity_num is null");
            return (Criteria) this;
        }

        public Criteria andAgentIdentityNumIsNotNull() {
            addCriterion("agent_identity_num is not null");
            return (Criteria) this;
        }

        public Criteria andAgentIdentityNumEqualTo(String value) {
            addCriterion("agent_identity_num =", value, "agentIdentityNum");
            return (Criteria) this;
        }

        public Criteria andAgentIdentityNumNotEqualTo(String value) {
            addCriterion("agent_identity_num <>", value, "agentIdentityNum");
            return (Criteria) this;
        }

        public Criteria andAgentIdentityNumGreaterThan(String value) {
            addCriterion("agent_identity_num >", value, "agentIdentityNum");
            return (Criteria) this;
        }

        public Criteria andAgentIdentityNumGreaterThanOrEqualTo(String value) {
            addCriterion("agent_identity_num >=", value, "agentIdentityNum");
            return (Criteria) this;
        }

        public Criteria andAgentIdentityNumLessThan(String value) {
            addCriterion("agent_identity_num <", value, "agentIdentityNum");
            return (Criteria) this;
        }

        public Criteria andAgentIdentityNumLessThanOrEqualTo(String value) {
            addCriterion("agent_identity_num <=", value, "agentIdentityNum");
            return (Criteria) this;
        }

        public Criteria andAgentIdentityNumLike(String value) {
            addCriterion("agent_identity_num like", value, "agentIdentityNum");
            return (Criteria) this;
        }

        public Criteria andAgentIdentityNumNotLike(String value) {
            addCriterion("agent_identity_num not like", value, "agentIdentityNum");
            return (Criteria) this;
        }

        public Criteria andAgentIdentityNumIn(List<String> values) {
            addCriterion("agent_identity_num in", values, "agentIdentityNum");
            return (Criteria) this;
        }

        public Criteria andAgentIdentityNumNotIn(List<String> values) {
            addCriterion("agent_identity_num not in", values, "agentIdentityNum");
            return (Criteria) this;
        }

        public Criteria andAgentIdentityNumBetween(String value1, String value2) {
            addCriterion("agent_identity_num between", value1, value2, "agentIdentityNum");
            return (Criteria) this;
        }

        public Criteria andAgentIdentityNumNotBetween(String value1, String value2) {
            addCriterion("agent_identity_num not between", value1, value2, "agentIdentityNum");
            return (Criteria) this;
        }

        public Criteria andAgentIdentityUrlIsNull() {
            addCriterion("agent_identity_url is null");
            return (Criteria) this;
        }

        public Criteria andAgentIdentityUrlIsNotNull() {
            addCriterion("agent_identity_url is not null");
            return (Criteria) this;
        }

        public Criteria andAgentIdentityUrlEqualTo(String value) {
            addCriterion("agent_identity_url =", value, "agentIdentityUrl");
            return (Criteria) this;
        }

        public Criteria andAgentIdentityUrlNotEqualTo(String value) {
            addCriterion("agent_identity_url <>", value, "agentIdentityUrl");
            return (Criteria) this;
        }

        public Criteria andAgentIdentityUrlGreaterThan(String value) {
            addCriterion("agent_identity_url >", value, "agentIdentityUrl");
            return (Criteria) this;
        }

        public Criteria andAgentIdentityUrlGreaterThanOrEqualTo(String value) {
            addCriterion("agent_identity_url >=", value, "agentIdentityUrl");
            return (Criteria) this;
        }

        public Criteria andAgentIdentityUrlLessThan(String value) {
            addCriterion("agent_identity_url <", value, "agentIdentityUrl");
            return (Criteria) this;
        }

        public Criteria andAgentIdentityUrlLessThanOrEqualTo(String value) {
            addCriterion("agent_identity_url <=", value, "agentIdentityUrl");
            return (Criteria) this;
        }

        public Criteria andAgentIdentityUrlLike(String value) {
            addCriterion("agent_identity_url like", value, "agentIdentityUrl");
            return (Criteria) this;
        }

        public Criteria andAgentIdentityUrlNotLike(String value) {
            addCriterion("agent_identity_url not like", value, "agentIdentityUrl");
            return (Criteria) this;
        }

        public Criteria andAgentIdentityUrlIn(List<String> values) {
            addCriterion("agent_identity_url in", values, "agentIdentityUrl");
            return (Criteria) this;
        }

        public Criteria andAgentIdentityUrlNotIn(List<String> values) {
            addCriterion("agent_identity_url not in", values, "agentIdentityUrl");
            return (Criteria) this;
        }

        public Criteria andAgentIdentityUrlBetween(String value1, String value2) {
            addCriterion("agent_identity_url between", value1, value2, "agentIdentityUrl");
            return (Criteria) this;
        }

        public Criteria andAgentIdentityUrlNotBetween(String value1, String value2) {
            addCriterion("agent_identity_url not between", value1, value2, "agentIdentityUrl");
            return (Criteria) this;
        }

        public Criteria andAgentPhoneIsNull() {
            addCriterion("agent_phone is null");
            return (Criteria) this;
        }

        public Criteria andAgentPhoneIsNotNull() {
            addCriterion("agent_phone is not null");
            return (Criteria) this;
        }

        public Criteria andAgentPhoneEqualTo(String value) {
            addCriterion("agent_phone =", value, "agentPhone");
            return (Criteria) this;
        }

        public Criteria andAgentPhoneNotEqualTo(String value) {
            addCriterion("agent_phone <>", value, "agentPhone");
            return (Criteria) this;
        }

        public Criteria andAgentPhoneGreaterThan(String value) {
            addCriterion("agent_phone >", value, "agentPhone");
            return (Criteria) this;
        }

        public Criteria andAgentPhoneGreaterThanOrEqualTo(String value) {
            addCriterion("agent_phone >=", value, "agentPhone");
            return (Criteria) this;
        }

        public Criteria andAgentPhoneLessThan(String value) {
            addCriterion("agent_phone <", value, "agentPhone");
            return (Criteria) this;
        }

        public Criteria andAgentPhoneLessThanOrEqualTo(String value) {
            addCriterion("agent_phone <=", value, "agentPhone");
            return (Criteria) this;
        }

        public Criteria andAgentPhoneLike(String value) {
            addCriterion("agent_phone like", value, "agentPhone");
            return (Criteria) this;
        }

        public Criteria andAgentPhoneNotLike(String value) {
            addCriterion("agent_phone not like", value, "agentPhone");
            return (Criteria) this;
        }

        public Criteria andAgentPhoneIn(List<String> values) {
            addCriterion("agent_phone in", values, "agentPhone");
            return (Criteria) this;
        }

        public Criteria andAgentPhoneNotIn(List<String> values) {
            addCriterion("agent_phone not in", values, "agentPhone");
            return (Criteria) this;
        }

        public Criteria andAgentPhoneBetween(String value1, String value2) {
            addCriterion("agent_phone between", value1, value2, "agentPhone");
            return (Criteria) this;
        }

        public Criteria andAgentPhoneNotBetween(String value1, String value2) {
            addCriterion("agent_phone not between", value1, value2, "agentPhone");
            return (Criteria) this;
        }

        public Criteria andAgentPhoneStatusIsNull() {
            addCriterion("agent_phone_status is null");
            return (Criteria) this;
        }

        public Criteria andAgentPhoneStatusIsNotNull() {
            addCriterion("agent_phone_status is not null");
            return (Criteria) this;
        }

        public Criteria andAgentPhoneStatusEqualTo(Integer value) {
            addCriterion("agent_phone_status =", value, "agentPhoneStatus");
            return (Criteria) this;
        }

        public Criteria andAgentPhoneStatusNotEqualTo(Integer value) {
            addCriterion("agent_phone_status <>", value, "agentPhoneStatus");
            return (Criteria) this;
        }

        public Criteria andAgentPhoneStatusGreaterThan(Integer value) {
            addCriterion("agent_phone_status >", value, "agentPhoneStatus");
            return (Criteria) this;
        }

        public Criteria andAgentPhoneStatusGreaterThanOrEqualTo(Integer value) {
            addCriterion("agent_phone_status >=", value, "agentPhoneStatus");
            return (Criteria) this;
        }

        public Criteria andAgentPhoneStatusLessThan(Integer value) {
            addCriterion("agent_phone_status <", value, "agentPhoneStatus");
            return (Criteria) this;
        }

        public Criteria andAgentPhoneStatusLessThanOrEqualTo(Integer value) {
            addCriterion("agent_phone_status <=", value, "agentPhoneStatus");
            return (Criteria) this;
        }

        public Criteria andAgentPhoneStatusIn(List<Integer> values) {
            addCriterion("agent_phone_status in", values, "agentPhoneStatus");
            return (Criteria) this;
        }

        public Criteria andAgentPhoneStatusNotIn(List<Integer> values) {
            addCriterion("agent_phone_status not in", values, "agentPhoneStatus");
            return (Criteria) this;
        }

        public Criteria andAgentPhoneStatusBetween(Integer value1, Integer value2) {
            addCriterion("agent_phone_status between", value1, value2, "agentPhoneStatus");
            return (Criteria) this;
        }

        public Criteria andAgentPhoneStatusNotBetween(Integer value1, Integer value2) {
            addCriterion("agent_phone_status not between", value1, value2, "agentPhoneStatus");
            return (Criteria) this;
        }

        public Criteria andAgentEmailIsNull() {
            addCriterion("agent_email is null");
            return (Criteria) this;
        }

        public Criteria andAgentEmailIsNotNull() {
            addCriterion("agent_email is not null");
            return (Criteria) this;
        }

        public Criteria andAgentEmailEqualTo(String value) {
            addCriterion("agent_email =", value, "agentEmail");
            return (Criteria) this;
        }

        public Criteria andAgentEmailNotEqualTo(String value) {
            addCriterion("agent_email <>", value, "agentEmail");
            return (Criteria) this;
        }

        public Criteria andAgentEmailGreaterThan(String value) {
            addCriterion("agent_email >", value, "agentEmail");
            return (Criteria) this;
        }

        public Criteria andAgentEmailGreaterThanOrEqualTo(String value) {
            addCriterion("agent_email >=", value, "agentEmail");
            return (Criteria) this;
        }

        public Criteria andAgentEmailLessThan(String value) {
            addCriterion("agent_email <", value, "agentEmail");
            return (Criteria) this;
        }

        public Criteria andAgentEmailLessThanOrEqualTo(String value) {
            addCriterion("agent_email <=", value, "agentEmail");
            return (Criteria) this;
        }

        public Criteria andAgentEmailLike(String value) {
            addCriterion("agent_email like", value, "agentEmail");
            return (Criteria) this;
        }

        public Criteria andAgentEmailNotLike(String value) {
            addCriterion("agent_email not like", value, "agentEmail");
            return (Criteria) this;
        }

        public Criteria andAgentEmailIn(List<String> values) {
            addCriterion("agent_email in", values, "agentEmail");
            return (Criteria) this;
        }

        public Criteria andAgentEmailNotIn(List<String> values) {
            addCriterion("agent_email not in", values, "agentEmail");
            return (Criteria) this;
        }

        public Criteria andAgentEmailBetween(String value1, String value2) {
            addCriterion("agent_email between", value1, value2, "agentEmail");
            return (Criteria) this;
        }

        public Criteria andAgentEmailNotBetween(String value1, String value2) {
            addCriterion("agent_email not between", value1, value2, "agentEmail");
            return (Criteria) this;
        }

        public Criteria andAgentEmailStatusIsNull() {
            addCriterion("agent_email_status is null");
            return (Criteria) this;
        }

        public Criteria andAgentEmailStatusIsNotNull() {
            addCriterion("agent_email_status is not null");
            return (Criteria) this;
        }

        public Criteria andAgentEmailStatusEqualTo(Integer value) {
            addCriterion("agent_email_status =", value, "agentEmailStatus");
            return (Criteria) this;
        }

        public Criteria andAgentEmailStatusNotEqualTo(Integer value) {
            addCriterion("agent_email_status <>", value, "agentEmailStatus");
            return (Criteria) this;
        }

        public Criteria andAgentEmailStatusGreaterThan(Integer value) {
            addCriterion("agent_email_status >", value, "agentEmailStatus");
            return (Criteria) this;
        }

        public Criteria andAgentEmailStatusGreaterThanOrEqualTo(Integer value) {
            addCriterion("agent_email_status >=", value, "agentEmailStatus");
            return (Criteria) this;
        }

        public Criteria andAgentEmailStatusLessThan(Integer value) {
            addCriterion("agent_email_status <", value, "agentEmailStatus");
            return (Criteria) this;
        }

        public Criteria andAgentEmailStatusLessThanOrEqualTo(Integer value) {
            addCriterion("agent_email_status <=", value, "agentEmailStatus");
            return (Criteria) this;
        }

        public Criteria andAgentEmailStatusIn(List<Integer> values) {
            addCriterion("agent_email_status in", values, "agentEmailStatus");
            return (Criteria) this;
        }

        public Criteria andAgentEmailStatusNotIn(List<Integer> values) {
            addCriterion("agent_email_status not in", values, "agentEmailStatus");
            return (Criteria) this;
        }

        public Criteria andAgentEmailStatusBetween(Integer value1, Integer value2) {
            addCriterion("agent_email_status between", value1, value2, "agentEmailStatus");
            return (Criteria) this;
        }

        public Criteria andAgentEmailStatusNotBetween(Integer value1, Integer value2) {
            addCriterion("agent_email_status not between", value1, value2, "agentEmailStatus");
            return (Criteria) this;
        }

        public Criteria andAgentQqIsNull() {
            addCriterion("agent_qq is null");
            return (Criteria) this;
        }

        public Criteria andAgentQqIsNotNull() {
            addCriterion("agent_qq is not null");
            return (Criteria) this;
        }

        public Criteria andAgentQqEqualTo(String value) {
            addCriterion("agent_qq =", value, "agentQq");
            return (Criteria) this;
        }

        public Criteria andAgentQqNotEqualTo(String value) {
            addCriterion("agent_qq <>", value, "agentQq");
            return (Criteria) this;
        }

        public Criteria andAgentQqGreaterThan(String value) {
            addCriterion("agent_qq >", value, "agentQq");
            return (Criteria) this;
        }

        public Criteria andAgentQqGreaterThanOrEqualTo(String value) {
            addCriterion("agent_qq >=", value, "agentQq");
            return (Criteria) this;
        }

        public Criteria andAgentQqLessThan(String value) {
            addCriterion("agent_qq <", value, "agentQq");
            return (Criteria) this;
        }

        public Criteria andAgentQqLessThanOrEqualTo(String value) {
            addCriterion("agent_qq <=", value, "agentQq");
            return (Criteria) this;
        }

        public Criteria andAgentQqLike(String value) {
            addCriterion("agent_qq like", value, "agentQq");
            return (Criteria) this;
        }

        public Criteria andAgentQqNotLike(String value) {
            addCriterion("agent_qq not like", value, "agentQq");
            return (Criteria) this;
        }

        public Criteria andAgentQqIn(List<String> values) {
            addCriterion("agent_qq in", values, "agentQq");
            return (Criteria) this;
        }

        public Criteria andAgentQqNotIn(List<String> values) {
            addCriterion("agent_qq not in", values, "agentQq");
            return (Criteria) this;
        }

        public Criteria andAgentQqBetween(String value1, String value2) {
            addCriterion("agent_qq between", value1, value2, "agentQq");
            return (Criteria) this;
        }

        public Criteria andAgentQqNotBetween(String value1, String value2) {
            addCriterion("agent_qq not between", value1, value2, "agentQq");
            return (Criteria) this;
        }

        public Criteria andAgentStatusIsNull() {
            addCriterion("agent_status is null");
            return (Criteria) this;
        }

        public Criteria andAgentStatusIsNotNull() {
            addCriterion("agent_status is not null");
            return (Criteria) this;
        }

        public Criteria andAgentStatusEqualTo(Integer value) {
            addCriterion("agent_status =", value, "agentStatus");
            return (Criteria) this;
        }

        public Criteria andAgentStatusNotEqualTo(Integer value) {
            addCriterion("agent_status <>", value, "agentStatus");
            return (Criteria) this;
        }

        public Criteria andAgentStatusGreaterThan(Integer value) {
            addCriterion("agent_status >", value, "agentStatus");
            return (Criteria) this;
        }

        public Criteria andAgentStatusGreaterThanOrEqualTo(Integer value) {
            addCriterion("agent_status >=", value, "agentStatus");
            return (Criteria) this;
        }

        public Criteria andAgentStatusLessThan(Integer value) {
            addCriterion("agent_status <", value, "agentStatus");
            return (Criteria) this;
        }

        public Criteria andAgentStatusLessThanOrEqualTo(Integer value) {
            addCriterion("agent_status <=", value, "agentStatus");
            return (Criteria) this;
        }

        public Criteria andAgentStatusIn(List<Integer> values) {
            addCriterion("agent_status in", values, "agentStatus");
            return (Criteria) this;
        }

        public Criteria andAgentStatusNotIn(List<Integer> values) {
            addCriterion("agent_status not in", values, "agentStatus");
            return (Criteria) this;
        }

        public Criteria andAgentStatusBetween(Integer value1, Integer value2) {
            addCriterion("agent_status between", value1, value2, "agentStatus");
            return (Criteria) this;
        }

        public Criteria andAgentStatusNotBetween(Integer value1, Integer value2) {
            addCriterion("agent_status not between", value1, value2, "agentStatus");
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