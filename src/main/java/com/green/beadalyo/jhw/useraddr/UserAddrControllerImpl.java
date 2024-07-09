package com.green.beadalyo.jhw.useraddr;

import com.green.beadalyo.common.model.ResultDto;
import com.green.beadalyo.jhw.useraddr.model.*;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("/api/address")
@RequiredArgsConstructor
public class UserAddrControllerImpl implements UserAddrController{
    private final UserAddrServiceImpl service;
    @Override
    @PostMapping
    @Operation(description = "유저 주소 등록")
    public ResultDto<Long> postUserAddr(@RequestBody UserAddrPostReq p) {
        long result = 0;
        String msg = "등록 완료";
        int statusCode = 100;
        try { result = service.postUserAddr(p); }
        catch (Exception e) {
            e.printStackTrace();
            msg = e.getMessage();
            statusCode = -100;
        }
        return ResultDto.<Long>builder()
                .statusCode(100)
                .resultMsg(msg)
                .resultData(result).build();
    }

    @Override
    @GetMapping
    @Operation(description = "유저 주소목록 조회")
    public ResultDto<List<UserAddrGetRes>> getUserAddrList() {
        List<UserAddrGetRes> result = new ArrayList<>();
        String msg = "조회 성공";
        int statusCode = 100;
        try { result = service.getUserAddrList(); }
        catch (Exception e) {
            e.printStackTrace();
            msg = e.getMessage();
            statusCode = -100;
        }
        return ResultDto.<List<UserAddrGetRes>>builder()
                .statusCode(statusCode)
                .resultMsg(msg)
                .resultData(result).build();
    }

    @Override
    @GetMapping("/main-address")
    @Operation(description = "유저 메인 주소 조회")
    public ResultDto<UserAddrGetRes> getMainUserAddr() {
        UserAddrGetRes result = new UserAddrGetRes();
        String msg = "조회 성공";
        int statusCode = 100;
        try { result = service.getMainUserAddr(); }
        catch (Exception e) {
            e.printStackTrace();
            msg = e.getMessage();
            statusCode = -100;
        }
        return ResultDto.<UserAddrGetRes>builder()
                .statusCode(statusCode)
                .resultMsg(msg)
                .resultData(result).build();
    }

    @Override
    @PatchMapping
    @Operation(description = "유저 주소 수정")
    public ResultDto<Integer> patchUserAddr(@RequestBody UserAddrPatchReq p) {
        int result = 0;
        String msg = "수정 완료";
        int statusCode = 100;
        try { result = service.patchUserAddr(p); }
        catch (Exception e) {
            e.printStackTrace();
            msg = e.getMessage();
            statusCode = -100;
        }
        return ResultDto.<Integer>builder()
                .statusCode(statusCode)
                .resultMsg(msg)
                .resultData(result).build();
    }

    @Override
    @PatchMapping("/main-address")
    @Operation(description = "유저 메인 주소 변경")
    public ResultDto<Integer> patchMainUserAddr(@RequestBody MainUserAddrPatchReq p) {
        int result = 0;
        String msg = "등록 완료";
        int statusCode = 100;
        try { result = service.patchMainUserAddr(p); }
        catch (Exception e) {
            e.printStackTrace();
            msg = e.getMessage();
            statusCode = -100;
        }
        return ResultDto.<Integer>builder()
                .statusCode(statusCode)
                .resultMsg(msg)
                .resultData(result).build();
    }

    @Override
    @DeleteMapping
    @Operation(description = "유저 주소 삭제")
    public ResultDto<Integer> deleteUserAddr(@ModelAttribute @ParameterObject UserAddrDelReq p) {
        int result = 0;
        String msg = "등록 완료";
        int statusCode = 100;
        try { result = service.deleteUserAddr(p); }
        catch (Exception e) {
            e.printStackTrace();
            msg = e.getMessage();
            statusCode = -100;
        }
        return ResultDto.<Integer>builder()
                .statusCode(result)
                .resultMsg(msg)
                .resultData(result).build();
    }
}
