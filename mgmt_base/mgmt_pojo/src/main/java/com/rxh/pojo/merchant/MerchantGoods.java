package com.rxh.pojo.merchant;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class MerchantGoods implements Serializable {
    // 主键
    private String id;

    // 域名表主键
    private Integer siteId;

    // 商家产品id
    private String productId;

    // 商家产品Url
    private String productUrl;

    // 商品名称
    private String name;

    // 商品价格
    private BigDecimal price;

    // 常量组Currency提供
    private String currency;

    // 0: 旧 1:新
    private Short currentFlag;

    // 最后修改时间
    private Date modifyTime;

    /**
     * 主键
     *
     * @return id 主键
     */
    public String getId() {
        return id;
    }

    /**
     * 主键
     *
     * @param id 主键
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * 域名表主键
     *
     * @return site_id 域名表主键
     */
    public Integer getSiteId() {
        return siteId;
    }

    /**
     * 域名表主键
     *
     * @param siteId 域名表主键
     */
    public void setSiteId(Integer siteId) {
        this.siteId = siteId;
    }

    /**
     * 商家产品id
     *
     * @return product_id 商家产品id
     */
    public String getProductId() {
        return productId;
    }

    /**
     * 商家产品id
     *
     * @param productId 商家产品id
     */
    public void setProductId(String productId) {
        this.productId = productId == null ? null : productId.trim();
    }

    public String getProductUrl() {
        return productUrl;
    }

    public void setProductUrl(String productUrl) {
        this.productUrl = productUrl == null ? null : productUrl.trim();
    }

    /**
     * 商品名称
     *
     * @return name 商品名称
     */
    public String getName() {
        return name;
    }

    /**
     * 商品名称
     *
     * @param name 商品名称
     */
    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    /**
     * 商品价格
     *
     * @return price 商品价格
     */
    public BigDecimal getPrice() {
        return price;
    }

    /**
     * 商品价格
     *
     * @param price 商品价格
     */
    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    /**
     * 常量组Currency提供
     *
     * @return currency 常量组Currency提供
     */
    public String getCurrency() {
        return currency;
    }

    /**
     * 常量组Currency提供
     *
     * @param currency 常量组Currency提供
     */
    public void setCurrency(String currency) {
        this.currency = currency == null ? null : currency.trim();
    }

    /**
     * 0: 旧 1:新
     *
     * @return current_flag 0: 旧 1:新
     */
    public Short getCurrentFlag() {
        return currentFlag;
    }

    /**
     * 0: 旧 1:新
     *
     * @param currentFlag 0: 旧 1:新
     */
    public void setCurrentFlag(Short currentFlag) {
        this.currentFlag = currentFlag;
    }

    /**
     * 最后修改时间
     *
     * @return modify_time 最后修改时间
     */
    public Date getModifyTime() {
        return modifyTime;
    }

    /**
     * 最后修改时间
     *
     * @param modifyTime 最后修改时间
     */
    public void setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
    }
}