package com.yuliavslv.shop.backend.entity;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "type", schema = "product_cards")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class ProductType {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "typeSeq")
    @SequenceGenerator(name = "typeSeq", sequenceName = "type_seq", allocationSize = 1)
    private Integer id;

    @Column(name = "name")
    @NotNull
    private String name;
}
