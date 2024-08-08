package com.green.beadalyo.kdh.accept;

import com.green.beadalyo.gyb.common.ResultDto;
import com.green.beadalyo.gyb.model.Restaurant;
import com.green.beadalyo.kdh.accept.model.GetUnAcceptRestaurant;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("api/admin/accept")
@RequiredArgsConstructor
@Tag(name = "어드민 승인 관련 컨트롤러입니다.")
public class AcceptController {
    private final AcceptService service;

    @GetMapping("unAccept_restaurant_list")
    @PreAuthorize("hasAnyRole('ADMIN')")
    @Operation(summary = "승인 안된 음식점 불러오기(어드민용)", description = "음식점들을 불러옵니다.")
    public ResultDto<List<GetUnAcceptRestaurant>> getUnAcceptRestaurant(){

        List<Restaurant> list = service.getUnAcceptRestaurant();
        List<GetUnAcceptRestaurant> resData = new ArrayList<>();
        for (Restaurant i : list)
        {
            GetUnAcceptRestaurant j = new GetUnAcceptRestaurant(i) ;
            resData.add(j) ;

        }

        return ResultDto.<List<GetUnAcceptRestaurant>>builder()
                .statusCode(1)
                .resultMsg("승인 안된 음식점 불러오기 완료")
                .resultData(resData)
                .build();
    }

    @PatchMapping("{res_pk}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    @Operation(summary = "음식점 승인하기(어드민용)", description = "음식점을 승인합니다")
    public ResultDto<Integer>  acceptRestaurant(@PathVariable("res_pk") Long resPk){

        service.acceptRestaurant(resPk);

        return ResultDto.<Integer>builder()
                .statusCode(1)
                .resultMsg("음식점 승인 완료")
                .resultData(1)
                .build();
    }
}
