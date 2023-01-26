package com.yuliavslv.shop.backend.entity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "product", schema = "shop")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "prodSeq")
    @SequenceGenerator(name = "prodSeq", sequenceName = "prod_seq", allocationSize = 1)
    private Integer id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "brand_id")
    @NotNull
    private Brand brand;

    @Column(name = "name")
    @NotNull
    private String name;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "type_id")
    @NotNull
    private ProductType type;

    @Column(name = "price")
    @NotNull
    private Double price;

    @Column(name = "discount")
    @NotNull
    private Integer discount;

    @Column(name = "amount")
    @NotNull
    private Integer amount;

    public Product() {

    }

    public Product(
            Brand brand,
            String name,
            ProductType type,
            Double price,
            Integer discount,
            Integer amount) {
        this.brand = brand;
        this.name = name;
        this.type = type;
        this.price = price;
        this.discount = discount;
        this.amount = amount;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Brand getBrand() {
        return brand;
    }

    public void setBrand(Brand brand) {
        this.brand = brand;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ProductType getType() {
        return type;
    }

    public void setType(ProductType type) {
        this.type = type;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Integer getDiscount() {
        return discount;
    }

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
