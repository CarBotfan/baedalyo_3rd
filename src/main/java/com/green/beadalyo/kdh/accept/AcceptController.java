//package com.green.beadalyo.kdh.accept;
//
//import com.green.beadalyo.gyb.common.ResultDto;
//import io.swagger.v3.oas.annotations.Operation;
//import io.swagger.v3.oas.annotations.tags.Tag;
//import lombok.RequiredArgsConstructor;
//import org.springframework.security.access.prepost.PreAuthorize;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import java.util.List;
//
//@RestController
//@RequestMapping("api/admin/accept")
//@RequiredArgsConstructor
//@Tag(name = "어드민 승인 관련 컨트롤러입니다.")
//public class AcceptController {
//    private final AcceptService service;
//
//    @GetMapping("unAccept_restaurant")
//    @PreAuthorize("hasRole(ADMIN)")
//    @Operation(summary = "승인 안된 음식점 불러오기(어드민용)", description = "음식점들을 불러옵니다.")
//    public ResultDto<Integer> acceptRestaurant(){
//
////        List<>
//
//    }
//}
