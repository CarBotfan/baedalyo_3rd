package com.green.beadalyo.lmy.resfollow;

import com.green.beadalyo.common.model.ResultDto;
import com.green.beadalyo.gyb.model.Restaurant;
import com.green.beadalyo.jhw.security.AuthenticationFacade;
import com.green.beadalyo.jhw.user.UserServiceImpl;
import com.green.beadalyo.jhw.user.entity.User;
import com.green.beadalyo.lmy.resfollow.entity.ResFollow;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("api/follow")
@Tag(name = "찜 컨트롤러")
public class ResFollowController {
    private final AuthenticationFacade authenticationFacade;
    private final ResFollowService resFollowService;
    private final UserServiceImpl userService;

    @PutMapping("toggle/{res_pk}")
    @Operation(summary = "상점 찜 토글")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @ApiResponse(
            description =
                    "<p> 1 :  </p>" +
                            "<p>  </p>" +
                            "<p>  </p>" +
                            "<p>  </p>"
    )
    @Transactional
    public ResultDto<Integer> toggleFollow(@PathVariable("res_pk") Long resPk) {
        Integer code = 1;
        String msg = null;
        Integer result = 1;
        User user = null;
        try {
            user = userService.getUser(authenticationFacade.getLoginUserPk());
        } catch(Exception e) {
            return ResultDto.<Integer>builder()
                    .statusCode(-3)
                    .resultMsg(e.getMessage())
                    .build();
        }
        Restaurant restaurant = resFollowService.getRes(resPk);
        if (restaurant == null) {
            return ResultDto.<Integer>builder()
                    .statusCode(-2)
                    .resultMsg("존재하지 않는 상점입니다.")
                    .build();
        }

        ResFollow resFollow = resFollowService.getResFollow(restaurant, user);
        if (resFollow == null) {
            result = resFollowService.saveResFollow(restaurant, user);
            return ResultDto.<Integer>builder()
                    .statusCode(1)
                    .resultMsg("좋아요 완료")
                    .resultData(result)
                    .build();
        } else {
            result = resFollowService.deleteResFollow(resFollow);
            return ResultDto.<Integer>builder()
                    .statusCode(1)
                    .resultMsg("좋아요 취소")
                    .resultData(result )
                    .build();
        }
    }
}