package com.green.beadalyo.kdh.report.admin.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.green.beadalyo.jhw.user.entity.User;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PatchAccountSuspensionReq {
    @Schema(example = "7", description = "정지 하고싶은 만큼의 날짜를 입력해주시면 됩니다")
    private Integer userBlockDate;

    @Schema(example = "1", description = "정지 줄 유저의 pk(리뷰 작성자의 pk)")
    private Long userPk;

    private Long reportPk;
}
