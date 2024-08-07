package com.green.beadalyo.kdh.menu.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public class PostMenuReq {
    @JsonIgnore
    private long menuPk;

    @JsonIgnore
    private long resUserPk;

    @JsonIgnore
    private long resPk;

    @JsonIgnore
    private long menuResPk;

    @Schema(example = "후라이드 치킨" , description = "메뉴 이름", requiredMode = Schema.RequiredMode.REQUIRED)
    private String menuName;

    @Schema(example = "바삭바삭한 후라이드" , description = "메뉴 설명", requiredMode = Schema.RequiredMode.REQUIRED)
    private String menuContent;

    @Schema(example = "10000" , description = "메뉴 가격", requiredMode = Schema.RequiredMode.REQUIRED)
    private int menuPrice;

    @JsonIgnore
    private String menuPic;

    @Schema(example = "1" , description = "메뉴 상태 ex) 1:판매 중 2: 품절", requiredMode = Schema.RequiredMode.REQUIRED)
    private int menuState;

    @Schema(description = "메뉴 카테고리 Pk")
    private long menuCatPk;


}
