package com.green.beadalyo.lmy.doneorder.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.beans.ConstructorProperties;

@Data
public class Paging {
    private Integer size;
    private Integer page;

    @ConstructorProperties({"size", "page"})
    public Paging(Integer size, Integer page) {
        if (size == null || size < 1) {
            this.size = 20;
        } else {
            this.size = size;
        }

        if (page == null || page < 1) {
            this.page = 0;
        } else {
            this.page = page;
        }
    }
}
