package com.rxh.square.pojo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

public class ChannelInfoExample implements Serializable {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public ChannelInfoExample() {
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

        protected void addCriterionForJDBCDate(String condition, Date value, String property) {
            if (value == null) {
                throw new RuntimeException("Value for " + property + " cannot be null");
            }
            addCriterion(condition, new java.sql.Date(value.getTime()), property);
        }

        protected void addCriterionForJDBCDate(String condition, List<Date> values, String property) {
            if (values == null || values.size() == 0) {
                throw new RuntimeException("Value list for " + property + " cannot be null or empty");
            }
            List<java.sql.Date> dateList = new ArrayList<java.sql.Date>();
            Iterator<Date> iter = values.iterator();
            while (iter.hasNext()) {
                dateList.add(new java.sql.Date(iter.next().getTime()));
            }
            addCriterion(condition, dateList, property);
        }

        protected void addCriterionForJDBCDate(String condition, Date value1, Date value2, String property) {
            if (value1 == null || value2 == null) {
                throw new RuntimeException("Between values for " + property + " cannot be null");
            }
            addCriterion(condition, new java.sql.Date(value1.getTime()), new java.sql.Date(value2.getTime()), property);
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

        public Criteria andChannelNameIsNull() {
            addCriterion("channel_name is null");
            return (Criteria) this;
        }

        public Criteria andChannelNameIsNotNull() {
            addCriterion("channel_name is not null");
            return (Criteria) this;
        }

        public Criteria andChannelNameEqualTo(String value) {
            addCriterion("channel_name =", value, "channelName");
            return (Criteria) this;
        }

        public Criteria andChannelNameNotEqualTo(String value) {
            addCriterion("channel_name <>", value, "channelName");
            return (Criteria) this;
        }

        public Criteria andChannelNameGreaterThan(String value) {
            addCriterion("channel_name >", value, "channelName");
            return (Criteria) this;
        }

        public Criteria andChannelNameGreaterThanOrEqualTo(String value) {
            addCriterion("channel_name >=", value, "channelName");
            return (Criteria) this;
        }

        public Criteria andChannelNameLessThan(String value) {
            addCriterion("channel_name <", value, "channelName");
            return (Criteria) this;
        }

        public Criteria andChannelNameLessThanOrEqualTo(String value) {
            addCriterion("channel_name <=", value, "channelName");
            return (Criteria) this;
        }

        public Criteria andChannelNameLike(String value) {
            addCriterion("channel_name like", value, "channelName");
            return (Criteria) this;
        }

        public Criteria andChannelNameNotLike(String value) {
            addCriterion("channel_name not like", value, "channelName");
            return (Criteria) this;
        }

        public Criteria andChannelNameIn(List<String> values) {
            addCriterion("channel_name in", values, "channelName");
            return (Criteria) this;
        }

        public Criteria andChannelNameNotIn(List<String> values) {
            addCriterion("channel_name not in", values, "channelName");
            return (Criteria) this;
        }

        public Criteria andChannelNameBetween(String value1, String value2) {
            addCriterion("channel_name between", value1, value2, "channelName");
            return (Criteria) this;
        }

        public Criteria andChannelNameNotBetween(String value1, String value2) {
            addCriterion("channel_name not between", value1, value2, "channelName");
            return (Criteria) this;
        }

        public Criteria andChannelLevelIsNull() {
            addCriterion("channel_level is null");
            return (Criteria) this;
        }

        public Criteria andChannelLevelIsNotNull() {
            addCriterion("channel_level is not null");
            return (Criteria) this;
        }

        public Criteria andChannelLevelEqualTo(Integer value) {
            addCriterion("channel_level =", value, "channelLevel");
            return (Criteria) this;
        }

        public Criteria andChannelLevelNotEqualTo(Integer value) {
            addCriterion("channel_level <>", value, "channelLevel");
            return (Criteria) this;
        }

        public Criteria andChannelLevelGreaterThan(Integer value) {
            addCriterion("channel_level >", value, "channelLevel");
            return (Criteria) this;
        }

        public Criteria andChannelLevelGreaterThanOrEqualTo(Integer value) {
            addCriterion("channel_level >=", value, "channelLevel");
            return (Criteria) this;
        }

        public Criteria andChannelLevelLessThan(Integer value) {
            addCriterion("channel_level <", value, "channelLevel");
            return (Criteria) this;
        }

        public Criteria andChannelLevelLessThanOrEqualTo(Integer value) {
            addCriterion("channel_level <=", value, "channelLevel");
            return (Criteria) this;
        }

        public Criteria andChannelLevelIn(List<Integer> values) {
            addCriterion("channel_level in", values, "channelLevel");
            return (Criteria) this;
        }

        public Criteria andChannelLevelNotIn(List<Integer> values) {
            addCriterion("channel_level not in", values, "channelLevel");
            return (Criteria) this;
        }

        public Criteria andChannelLevelBetween(Integer value1, Integer value2) {
            addCriterion("channel_level between", value1, value2, "channelLevel");
            return (Criteria) this;
        }

        public Criteria andChannelLevelNotBetween(Integer value1, Integer value2) {
            addCriterion("channel_level not between", value1, value2, "channelLevel");
            return (Criteria) this;
        }

        public Criteria andTypeIsNull() {
            addCriterion("type is null");
            return (Criteria) this;
        }

        public Criteria andTypeIsNotNull() {
            addCriterion("type is not null");
            return (Criteria) this;
        }

        public Criteria andTypeEqualTo(Integer value) {
            addCriterion("type =", value, "type");
            return (Criteria) this;
        }

        public Criteria andTypeNotEqualTo(Integer value) {
            addCriterion("type <>", value, "type");
            return (Criteria) this;
        }

        public Criteria andTypeGreaterThan(Integer value) {
            addCriterion("type >", value, "type");
            return (Criteria) this;
        }

        public Criteria andTypeGreaterThanOrEqualTo(Integer value) {
            addCriterion("type >=", value, "type");
            return (Criteria) this;
        }

        public Criteria andTypeLessThan(Integer value) {
            addCriterion("type <", value, "type");
            return (Criteria) this;
        }

        public Criteria andTypeLessThanOrEqualTo(Integer value) {
            addCriterion("type <=", value, "type");
            return (Criteria) this;
        }

        public Criteria andTypeIn(List<Integer> values) {
            addCriterion("type in", values, "type");
            return (Criteria) this;
        }

        public Criteria andTypeNotIn(List<Integer> values) {
            addCriterion("type not in", values, "type");
            return (Criteria) this;
        }

        public Criteria andTypeBetween(Integer value1, Integer value2) {
            addCriterion("type between", value1, value2, "type");
            return (Criteria) this;
        }

        public Criteria andTypeNotBetween(Integer value1, Integer value2) {
            addCriterion("type not between", value1, value2, "type");
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

        public Criteria andPayUrlIsNull() {
            addCriterion("pay_url is null");
            return (Criteria) this;
        }

        public Criteria andPayUrlIsNotNull() {
            addCriterion("pay_url is not null");
            return (Criteria) this;
        }

        public Criteria andPayUrlEqualTo(String value) {
            addCriterion("pay_url =", value, "payUrl");
            return (Criteria) this;
        }

        public Criteria andPayUrlNotEqualTo(String value) {
            addCriterion("pay_url <>", value, "payUrl");
            return (Criteria) this;
        }

        public Criteria andPayUrlGreaterThan(String value) {
            addCriterion("pay_url >", value, "payUrl");
            return (Criteria) this;
        }

        public Criteria andPayUrlGreaterThanOrEqualTo(String value) {
            addCriterion("pay_url >=", value, "payUrl");
            return (Criteria) this;
        }

        public Criteria andPayUrlLessThan(String value) {
            addCriterion("pay_url <", value, "payUrl");
            return (Criteria) this;
        }

        public Criteria andPayUrlLessThanOrEqualTo(String value) {
            addCriterion("pay_url <=", value, "payUrl");
            return (Criteria) this;
        }

        public Criteria andPayUrlLike(String value) {
            addCriterion("pay_url like", value, "payUrl");
            return (Criteria) this;
        }

        public Criteria andPayUrlNotLike(String value) {
            addCriterion("pay_url not like", value, "payUrl");
            return (Criteria) this;
        }

        public Criteria andPayUrlIn(List<String> values) {
            addCriterion("pay_url in", values, "payUrl");
            return (Criteria) this;
        }

        public Criteria andPayUrlNotIn(List<String> values) {
            addCriterion("pay_url not in", values, "payUrl");
            return (Criteria) this;
        }

        public Criteria andPayUrlBetween(String value1, String value2) {
            addCriterion("pay_url between", value1, value2, "payUrl");
            return (Criteria) this;
        }

        public Criteria andPayUrlNotBetween(String value1, String value2) {
            addCriterion("pay_url not between", value1, value2, "payUrl");
            return (Criteria) this;
        }

        public Criteria andOthersIsNull() {
            addCriterion("others is null");
            return (Criteria) this;
        }

        public Criteria andOthersIsNotNull() {
            addCriterion("others is not null");
            return (Criteria) this;
        }

        public Criteria andOthersEqualTo(String value) {
            addCriterion("others =", value, "others");
            return (Criteria) this;
        }

        public Criteria andOthersNotEqualTo(String value) {
            addCriterion("others <>", value, "others");
            return (Criteria) this;
        }

        public Criteria andOthersGreaterThan(String value) {
            addCriterion("others >", value, "others");
            return (Criteria) this;
        }

        public Criteria andOthersGreaterThanOrEqualTo(String value) {
            addCriterion("others >=", value, "others");
            return (Criteria) this;
        }

        public Criteria andOthersLessThan(String value) {
            addCriterion("others <", value, "others");
            return (Criteria) this;
        }

        public Criteria andOthersLessThanOrEqualTo(String value) {
            addCriterion("others <=", value, "others");
            return (Criteria) this;
        }

        public Criteria andOthersLike(String value) {
            addCriterion("others like", value, "others");
            return (Criteria) this;
        }

        public Criteria andOthersNotLike(String value) {
            addCriterion("others not like", value, "others");
            return (Criteria) this;
        }

        public Criteria andOthersIn(List<String> values) {
            addCriterion("others in", values, "others");
            return (Criteria) this;
        }

        public Criteria andOthersNotIn(List<String> values) {
            addCriterion("others not in", values, "others");
            return (Criteria) this;
        }

        public Criteria andOthersBetween(String value1, String value2) {
            addCriterion("others between", value1, value2, "others");
            return (Criteria) this;
        }

        public Criteria andOthersNotBetween(String value1, String value2) {
            addCriterion("others not between", value1, value2, "others");
            return (Criteria) this;
        }

        public Criteria andChannelSingleFeeIsNull() {
            addCriterion("channel_single_fee is null");
            return (Criteria) this;
        }

        public Criteria andChannelSingleFeeIsNotNull() {
            addCriterion("channel_single_fee is not null");
            return (Criteria) this;
        }

        public Criteria andChannelSingleFeeEqualTo(Integer value) {
            addCriterion("channel_single_fee =", value, "channelSingleFee");
            return (Criteria) this;
        }

        public Criteria andChannelSingleFeeNotEqualTo(Integer value) {
            addCriterion("channel_single_fee <>", value, "channelSingleFee");
            return (Criteria) this;
        }

        public Criteria andChannelSingleFeeGreaterThan(Integer value) {
            addCriterion("channel_single_fee >", value, "channelSingleFee");
            return (Criteria) this;
        }

        public Criteria andChannelSingleFeeGreaterThanOrEqualTo(Integer value) {
            addCriterion("channel_single_fee >=", value, "channelSingleFee");
            return (Criteria) this;
        }

        public Criteria andChannelSingleFeeLessThan(Integer value) {
            addCriterion("channel_single_fee <", value, "channelSingleFee");
            return (Criteria) this;
        }

        public Criteria andChannelSingleFeeLessThanOrEqualTo(Integer value) {
            addCriterion("channel_single_fee <=", value, "channelSingleFee");
            return (Criteria) this;
        }

        public Criteria andChannelSingleFeeIn(List<Integer> values) {
            addCriterion("channel_single_fee in", values, "channelSingleFee");
            return (Criteria) this;
        }

        public Criteria andChannelSingleFeeNotIn(List<Integer> values) {
            addCriterion("channel_single_fee not in", values, "channelSingleFee");
            return (Criteria) this;
        }

        public Criteria andChannelSingleFeeBetween(Integer value1, Integer value2) {
            addCriterion("channel_single_fee between", value1, value2, "channelSingleFee");
            return (Criteria) this;
        }

        public Criteria andChannelSingleFeeNotBetween(Integer value1, Integer value2) {
            addCriterion("channel_single_fee not between", value1, value2, "channelSingleFee");
            return (Criteria) this;
        }

        public Criteria andChannelRateFeeIsNull() {
            addCriterion("channel_rate_fee is null");
            return (Criteria) this;
        }

        public Criteria andChannelRateFeeIsNotNull() {
            addCriterion("channel_rate_fee is not null");
            return (Criteria) this;
        }

        public Criteria andChannelRateFeeEqualTo(BigDecimal value) {
            addCriterion("channel_rate_fee =", value, "channelRateFee");
            return (Criteria) this;
        }

        public Criteria andChannelRateFeeNotEqualTo(BigDecimal value) {
            addCriterion("channel_rate_fee <>", value, "channelRateFee");
            return (Criteria) this;
        }

        public Criteria andChannelRateFeeGreaterThan(BigDecimal value) {
            addCriterion("channel_rate_fee >", value, "channelRateFee");
            return (Criteria) this;
        }

        public Criteria andChannelRateFeeGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("channel_rate_fee >=", value, "channelRateFee");
            return (Criteria) this;
        }

        public Criteria andChannelRateFeeLessThan(BigDecimal value) {
            addCriterion("channel_rate_fee <", value, "channelRateFee");
            return (Criteria) this;
        }

        public Criteria andChannelRateFeeLessThanOrEqualTo(BigDecimal value) {
            addCriterion("channel_rate_fee <=", value, "channelRateFee");
            return (Criteria) this;
        }

        public Criteria andChannelRateFeeIn(List<BigDecimal> values) {
            addCriterion("channel_rate_fee in", values, "channelRateFee");
            return (Criteria) this;
        }

        public Criteria andChannelRateFeeNotIn(List<BigDecimal> values) {
            addCriterion("channel_rate_fee not in", values, "channelRateFee");
            return (Criteria) this;
        }

        public Criteria andChannelRateFeeBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("channel_rate_fee between", value1, value2, "channelRateFee");
            return (Criteria) this;
        }

        public Criteria andChannelRateFeeNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("channel_rate_fee not between", value1, value2, "channelRateFee");
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

        public Criteria andSingleMinAmountIsNull() {
            addCriterion("single_min_amount is null");
            return (Criteria) this;
        }

        public Criteria andSingleMinAmountIsNotNull() {
            addCriterion("single_min_amount is not null");
            return (Criteria) this;
        }

        public Criteria andSingleMinAmountEqualTo(BigDecimal value) {
            addCriterion("single_min_amount =", value, "singleMinAmount");
            return (Criteria) this;
        }

        public Criteria andSingleMinAmountNotEqualTo(BigDecimal value) {
            addCriterion("single_min_amount <>", value, "singleMinAmount");
            return (Criteria) this;
        }

        public Criteria andSingleMinAmountGreaterThan(BigDecimal value) {
            addCriterion("single_min_amount >", value, "singleMinAmount");
            return (Criteria) this;
        }

        public Criteria andSingleMinAmountGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("single_min_amount >=", value, "singleMinAmount");
            return (Criteria) this;
        }

        public Criteria andSingleMinAmountLessThan(BigDecimal value) {
            addCriterion("single_min_amount <", value, "singleMinAmount");
            return (Criteria) this;
        }

        public Criteria andSingleMinAmountLessThanOrEqualTo(BigDecimal value) {
            addCriterion("single_min_amount <=", value, "singleMinAmount");
            return (Criteria) this;
        }

        public Criteria andSingleMinAmountIn(List<BigDecimal> values) {
            addCriterion("single_min_amount in", values, "singleMinAmount");
            return (Criteria) this;
        }

        public Criteria andSingleMinAmountNotIn(List<BigDecimal> values) {
            addCriterion("single_min_amount not in", values, "singleMinAmount");
            return (Criteria) this;
        }

        public Criteria andSingleMinAmountBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("single_min_amount between", value1, value2, "singleMinAmount");
            return (Criteria) this;
        }

        public Criteria andSingleMinAmountNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("single_min_amount not between", value1, value2, "singleMinAmount");
            return (Criteria) this;
        }

        public Criteria andSingleMaxAmountIsNull() {
            addCriterion("single_max_amount is null");
            return (Criteria) this;
        }

        public Criteria andSingleMaxAmountIsNotNull() {
            addCriterion("single_max_amount is not null");
            return (Criteria) this;
        }

        public Criteria andSingleMaxAmountEqualTo(BigDecimal value) {
            addCriterion("single_max_amount =", value, "singleMaxAmount");
            return (Criteria) this;
        }

        public Criteria andSingleMaxAmountNotEqualTo(BigDecimal value) {
            addCriterion("single_max_amount <>", value, "singleMaxAmount");
            return (Criteria) this;
        }

        public Criteria andSingleMaxAmountGreaterThan(BigDecimal value) {
            addCriterion("single_max_amount >", value, "singleMaxAmount");
            return (Criteria) this;
        }

        public Criteria andSingleMaxAmountGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("single_max_amount >=", value, "singleMaxAmount");
            return (Criteria) this;
        }

        public Criteria andSingleMaxAmountLessThan(BigDecimal value) {
            addCriterion("single_max_amount <", value, "singleMaxAmount");
            return (Criteria) this;
        }

        public Criteria andSingleMaxAmountLessThanOrEqualTo(BigDecimal value) {
            addCriterion("single_max_amount <=", value, "singleMaxAmount");
            return (Criteria) this;
        }

        public Criteria andSingleMaxAmountIn(List<BigDecimal> values) {
            addCriterion("single_max_amount in", values, "singleMaxAmount");
            return (Criteria) this;
        }

        public Criteria andSingleMaxAmountNotIn(List<BigDecimal> values) {
            addCriterion("single_max_amount not in", values, "singleMaxAmount");
            return (Criteria) this;
        }

        public Criteria andSingleMaxAmountBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("single_max_amount between", value1, value2, "singleMaxAmount");
            return (Criteria) this;
        }

        public Criteria andSingleMaxAmountNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("single_max_amount not between", value1, value2, "singleMaxAmount");
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

        public Criteria andOutChannelIdIsNull() {
            addCriterion("out_channel_id is null");
            return (Criteria) this;
        }

        public Criteria andOutChannelIdIsNotNull() {
            addCriterion("out_channel_id is not null");
            return (Criteria) this;
        }

        public Criteria andOutChannelIdEqualTo(String value) {
            addCriterion("out_channel_id =", value, "outChannelId");
            return (Criteria) this;
        }

        public Criteria andOutChannelIdNotEqualTo(String value) {
            addCriterion("out_channel_id <>", value, "outChannelId");
            return (Criteria) this;
        }

        public Criteria andOutChannelIdGreaterThan(String value) {
            addCriterion("out_channel_id >", value, "outChannelId");
            return (Criteria) this;
        }

        public Criteria andOutChannelIdGreaterThanOrEqualTo(String value) {
            addCriterion("out_channel_id >=", value, "outChannelId");
            return (Criteria) this;
        }

        public Criteria andOutChannelIdLessThan(String value) {
            addCriterion("out_channel_id <", value, "outChannelId");
            return (Criteria) this;
        }

        public Criteria andOutChannelIdLessThanOrEqualTo(String value) {
            addCriterion("out_channel_id <=", value, "outChannelId");
            return (Criteria) this;
        }

        public Criteria andOutChannelIdLike(String value) {
            addCriterion("out_channel_id like", value, "outChannelId");
            return (Criteria) this;
        }

        public Criteria andOutChannelIdNotLike(String value) {
            addCriterion("out_channel_id not like", value, "outChannelId");
            return (Criteria) this;
        }

        public Criteria andOutChannelIdIn(List<String> values) {
            addCriterion("out_channel_id in", values, "outChannelId");
            return (Criteria) this;
        }

        public Criteria andOutChannelIdNotIn(List<String> values) {
            addCriterion("out_channel_id not in", values, "outChannelId");
            return (Criteria) this;
        }

        public Criteria andOutChannelIdBetween(String value1, String value2) {
            addCriterion("out_channel_id between", value1, value2, "outChannelId");
            return (Criteria) this;
        }

        public Criteria andOutChannelIdNotBetween(String value1, String value2) {
            addCriterion("out_channel_id not between", value1, value2, "outChannelId");
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

        public Criteria andCreateTimeIsNull() {
            addCriterion("create_time is null");
            return (Criteria) this;
        }

        public Criteria andCreateTimeIsNotNull() {
            addCriterion("create_time is not null");
            return (Criteria) this;
        }

        public Criteria andCreateTimeEqualTo(Date value) {
            addCriterionForJDBCDate("create_time =", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeNotEqualTo(Date value) {
            addCriterionForJDBCDate("create_time <>", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeGreaterThan(Date value) {
            addCriterionForJDBCDate("create_time >", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeGreaterThanOrEqualTo(Date value) {
            addCriterionForJDBCDate("create_time >=", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeLessThan(Date value) {
            addCriterionForJDBCDate("create_time <", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeLessThanOrEqualTo(Date value) {
            addCriterionForJDBCDate("create_time <=", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeIn(List<Date> values) {
            addCriterionForJDBCDate("create_time in", values, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeNotIn(List<Date> values) {
            addCriterionForJDBCDate("create_time not in", values, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeBetween(Date value1, Date value2) {
            addCriterionForJDBCDate("create_time between", value1, value2, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeNotBetween(Date value1, Date value2) {
            addCriterionForJDBCDate("create_time not between", value1, value2, "createTime");
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