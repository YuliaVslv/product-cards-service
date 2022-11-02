package com.yuliavslv.shop.backend.entity;

import javax.persistence.*;

@Entity
@Table(name = "brand", schema = "shop")
public class Brand {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "brandSeq")
    @SequenceGenerator(name = "brandSeq", sequenceName = "brand_seq", allocationSize = 1)
    private Integer id;

    @Column(name = "name")
    private String name;

    public Brand() {
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
