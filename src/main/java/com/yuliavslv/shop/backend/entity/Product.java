package com.yuliavslv.shop.backend.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "product", schema = "shop")
@Getter
@Setter
@NoArgsConstructor
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
}
