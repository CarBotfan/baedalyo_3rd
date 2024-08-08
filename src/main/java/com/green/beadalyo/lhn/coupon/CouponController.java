package com.green.beadalyo.lhn.coupon;

import com.green.beadalyo.lhn.coupon.dto.CouponResponseDto;
import com.green.beadalyo.lhn.coupon.dto.CouponUserResponseDto;
import com.green.beadalyo.lhn.coupon.entity.Coupon;
import com.green.beadalyo.lhn.coupon.entity.CouponUser;
import com.green.beadalyo.common.model.ResultDto;
import com.green.beadalyo.lhn.coupon.model.CouponPostReq;
import com.green.beadalyo.lhn.coupon.repository.CouponRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/coupons")
@RequiredArgsConstructor
@Tag(name = "쿠폰 컨트롤러")
public class CouponController {

    private final CouponService couponService;

    // 쿠폰 생성
    @PostMapping
    @Operation(summary = "쿠폰 생성")
    public ResponseEntity<ResultDto<CouponResponseDto>> createCoupon(@RequestBody CouponPostReq p) {
        try {
            Coupon createdCoupon = couponService.createCoupon(p);
            return ResponseEntity.ok(ResultDto.<CouponResponseDto>builder()
                    .statusCode(1)
                    .resultMsg("쿠폰 생성 완료")
                    .resultData(convertToDto(createdCoupon))
                    .build());
        } catch (Exception e) {
            return ResponseEntity.ok(ResultDto.<CouponResponseDto>builder()
                    .statusCode(-1)
                    .resultMsg(e.getMessage())
                    .build());
        }
    }

    // 가게별 쿠폰 조회
    @GetMapping("/{restaurantId}")
    @Operation(summary = "가게별 쿠폰 조회")
    public ResponseEntity<ResultDto<List<CouponResponseDto>>> getCouponsByRestaurant(@PathVariable Long restaurantId) {
        try {
            List<Integer> state = new ArrayList<>();
            state.add(1);
            List<Coupon> coupons = couponService.getCouponsByRestaurant(restaurantId , state);
            List<CouponResponseDto> couponDtos = coupons.stream().map(this::convertToDto).collect(Collectors.toList());
            return ResponseEntity.ok(ResultDto.<List<CouponResponseDto>>builder()
                    .statusCode(1)
                    .resultMsg("쿠폰 조회 완료")
                    .resultData(couponDtos)
                    .build());
        } catch (Exception e) {
            return ResponseEntity.ok(ResultDto.<List<CouponResponseDto>>builder()
                    .statusCode(-1)
                    .resultMsg(e.getMessage())
                    .build());
        }
    }
    //유저가 발급 받은 쿠폰 목록 조회
    @GetMapping("/user")
    @Operation(summary = "유저 발급 쿠폰 목록조회")
    public ResultDto<List<CouponUserResponseDto>> getCouponsByUser() {
        List<CouponUserResponseDto> coupons = couponService.getCouponByUser();

        return ResultDto.<List<CouponUserResponseDto>>builder()
                .statusCode(1)
                .resultMsg("쿠폰 목록조회 완료")
                .resultData(coupons)
                .build();
    }

    // 가게 주인이 생성한 쿠폰 목록 조회
    @GetMapping("/owner")
    @Operation(summary = "가게 주인 쿠폰생성 목록조회")
    public ResultDto<List<CouponResponseDto>> getCouponsByOwner() {
        List<CouponResponseDto> coupons = couponService.getCouponsByOwner();
//        List<CouponResponseDto> couponResponseDtos = coupons.stream()
//                .map(CouponResponseDto::new)
//                .collect(Collectors.toList());



        return ResultDto.<List<CouponResponseDto>>builder()
                .statusCode(1)
                .resultMsg("쿠폰 목록 조회 완료")
                .resultData(coupons)
                .build();
    }

    // 쿠폰 발급
    @PostMapping("/{couponId}")
    @PreAuthorize("hasAnyRole('USER')")
    @Operation(summary = "쿠폰 발급")
    public ResultDto<Long> issueCoupon(@PathVariable Long couponId) {
        Long issuedCoupon = couponService.issueCoupon(couponId);

        return ResultDto.<Long>builder()
                .statusCode(1)
                .resultMsg("쿠폰 발급 성공")
                .resultData(issuedCoupon)
                .build();
    }


    // 쿠폰 상태 변경
    @PatchMapping
    @Operation(summary = "사용자 쿠폰 상태 변경")
    public ResponseEntity<ResultDto<CouponUserResponseDto>> updateCouponStatus(@RequestParam Long couponUserId) {
        try {
            CouponUser updatedCouponUser = couponService.updateCouponStatus(couponUserId);
            return ResponseEntity.ok(ResultDto.<CouponUserResponseDto>builder()
                    .statusCode(1)
                    .resultMsg("쿠폰 상태 변경 완료")
                    .resultData(convertToCouponUserDto(updatedCouponUser))
                    .build());
        } catch (Exception e) {
            return ResponseEntity.ok(ResultDto.<CouponUserResponseDto>builder()
                    .statusCode(-1)
                    .resultMsg(e.getMessage())
                    .build());
        }
    }

    // 쿠폰 삭제
    @DeleteMapping("/{couponId}")
    @Operation(summary = "가게 쿠폰 삭제")
    public ResponseEntity<ResultDto<CouponResponseDto>> deleteCoupon(@RequestParam Long couponId) {
        try {
            couponService.deleteCoupon(couponId , 2);
            return ResponseEntity.ok(ResultDto.<CouponResponseDto>builder()
                    .statusCode(1)
                    .resultMsg("쿠폰 삭제 완료")
                    .build());
        } catch (Exception e) {
            return ResponseEntity.ok(ResultDto.<CouponResponseDto>builder()
                    .statusCode(-1)
                    .resultMsg(e.getMessage())
                    .build());
        }
    }

    // 쿠폰 엔티티를 DTO로 변환
    private CouponResponseDto convertToDto(Coupon coupon) {
        return new CouponResponseDto(
                coupon.getId(),
                coupon.getName(),
                coupon.getContent(),
                coupon.getPrice(),
                coupon.getMinOrderAmount(),
                coupon.getCreatedAt()
        );
    }

    // 쿠폰 사용자 엔티티를 DTO로 변환
    private CouponUserResponseDto convertToCouponUserDto(CouponUser couponUser) {
        return new CouponUserResponseDto(
                couponUser.getId(),
                couponUser.getCoupon().getId(),
                couponUser.getCoupon().getContent(),
                couponUser.getCoupon().getPrice(),
                couponUser.getCoupon().getMinOrderAmount(),
                couponUser.getCoupon().getName()
        );

    }
}