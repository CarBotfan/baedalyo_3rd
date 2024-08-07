package com.green.beadalyo.kdh.stat.owner.model;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.beans.ConstructorProperties;

@Getter
@Setter
public class GetReviewStatReq {
    @Schema(name = "res_pk")
    private long resPk;

    @ConstructorProperties({"res_pk"})
    public GetReviewStatReq(long resPk) {
        this.resPk = resPk;
    }
}
