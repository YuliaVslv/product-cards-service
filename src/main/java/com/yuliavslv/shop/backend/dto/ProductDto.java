package com.yuliavslv.shop.backend.dto;

import javax.validation.constraints.NotNull;

public class ProductDto {
    @NotNull
    private Integer brandId;
    @NotNull
    private String name;
    @NotNull
    private Integer typeId;
    @NotNull
    private Double price;
    private Integer discount;
    private Integer amount;

    public ProductDto() {
        discount = 0;
        amount = 0;
    }

    public Integer getBrandId() {
        return brandId;
    }

    public void setBrandId(Integer brandId) {
        this.brandId = brandId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getTypeId() {
        return typeId;
    }

    public void setTypeId(Integer typeId) {
        this.typeId = typeId;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Integer getDiscount() { return discount; }

    public void setDiscount(Integer discount) {
        this.discount = discount;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }
}
