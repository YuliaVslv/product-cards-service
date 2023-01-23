package com.yuliavslv.shop.backend.dto;

public class ProductDto {
    private Integer brandId;
    private String name;
    private Integer typeId;
    private Double price;
    private Integer sale;
    private Integer amount;

    public ProductDto() {
    }


    public ProductDto(Integer brandId, String name, Integer typeId, Double price, Integer sale, Integer amount) {
        this.brandId = brandId;
        this.name = name;
        this.typeId = typeId;
        this.price = price;
        this.sale = sale;
        this.amount = amount;
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

    public Integer getSale() {
        return sale;
    }

    public void setSale(Integer sale) {
        this.sale = sale;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }
}
