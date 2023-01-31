package com.yuliavslv.shop.backend.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
public class ProductDto {
    @NotNull
    private Integer brandId;
    @NotNull
    private String name;
    @NotNull
    private Integer typeId;
    @NotNull
    private Double price;
    @NotNull
    private Integer discount;
    @NotNull
    private Integer amount;
}
