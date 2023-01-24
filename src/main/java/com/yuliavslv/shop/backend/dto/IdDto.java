package com.yuliavslv.shop.backend.dto;

import javax.validation.constraints.NotNull;

public class IdDto {
    @NotNull
    private Integer id;

    public IdDto() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
