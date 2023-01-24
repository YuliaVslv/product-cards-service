package com.yuliavslv.shop.backend.entity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "product_type", schema = "shop")
public class ProductType {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "typeSeq")
    @SequenceGenerator(name = "typeSeq", sequenceName = "type_seq", allocationSize = 1)
    private Integer id;

    @Column(name = "name")
    @NotNull
    private String name;

    public ProductType() {
    }

    public ProductType(String name) {
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
