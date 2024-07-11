package com.green.beadalyo.kdh.menu.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public class PutMenuReq {
    @Schema(example = "1" , description = "메뉴 PK", requiredMode = Schema.RequiredMode.REQUIRED)
    private long menuPk;

    @Schema(example = "후라이드 치킨" , description = "메뉴 이름", requiredMode = Schema.RequiredMode.REQUIRED)
    private String menuName;

    @Schema(example = "바삭바삭한 후라이드" , description = "메뉴 설명", requiredMode = Schema.RequiredMode.REQUIRED)
    private String menuContent;

    @Schema(example = "10000" , description = "메뉴 가격", requiredMode = Schema.RequiredMode.REQUIRED)
    private int menuPrice;

    @JsonIgnore
    @Schema(example = "asdkfmlksad.jpg" , description = "메뉴 사진", requiredMode = Schema.RequiredMode.REQUIRED)
    private String menuPic;

    @Schema(example = "1" , description = "메뉴 상태 ex) 1:판매 중 2: 품절", requiredMode = Schema.RequiredMode.REQUIRED)
    private int menuState;

    @JsonIgnore
    private long resUserPk;

}
