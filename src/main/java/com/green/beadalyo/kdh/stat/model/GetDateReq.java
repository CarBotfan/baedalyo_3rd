package com.green.beadalyo.kdh.stat.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.beans.ConstructorProperties;

@Getter
@Setter

public class GetDateReq {
    private String date;

    @Schema(name = "res_pk")
    private long resPk;

    @JsonIgnore
    private long resUserPk;

    @ConstructorProperties({"res_pk"})
    public GetDateReq(long resPk) {
        this.resPk = resPk;
    }
}
