package com.yuliavslv.shop.backend.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "brand", schema = "product_cards")
@Getter
@Setter
@NoArgsConstructor
public class Brand {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "brandSeq")
    @SequenceGenerator(name = "brandSeq", sequenceName = "brand_seq", allocationSize = 1)
    private Integer id;

    @Column(name = "name")
    @NotNull
    private String name;
}
