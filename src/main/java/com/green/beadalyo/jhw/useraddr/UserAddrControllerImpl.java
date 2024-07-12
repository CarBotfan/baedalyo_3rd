package com.green.beadalyo.jhw.useraddr;

import com.green.beadalyo.common.model.ResultDto;
import com.green.beadalyo.jhw.useraddr.model.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "유저 주소 컨트롤러")
public class UserAddrControllerImpl implements UserAddrController{
    private final UserAddrServiceImpl service;
    @Override
    @PostMapping
    @Operation(summary = "유저 주소 등록")
    @ApiResponse(
            description =
                    "<p> 1 : 성공 </p>"+
                            "<p> -1 : 오류 </p>"
    )
    public ResultDto<Long> postUserAddr(@RequestBody UserAddrPostReq p) {
        long result = 0;
        String msg = "등록 완료";
        int statusCode = 1;
        try { result = service.postUserAddr(p); }
        catch (Exception e) {
            e.printStackTrace();
            msg = e.getMessage();
            statusCode = -1;
        }
        return ResultDto.<Long>builder()
                .statusCode(statusCode)
                .resultMsg(msg)
                .resultData(result).build();
    }

    @Override
    @GetMapping("/list")
    @Operation(summary = "유저 주소목록 조회")
    @ApiResponse(
            description =
                    "<p> 1 : 성공 </p>"+
                            "<p> -1 : 오류 </p>"
    )
    public ResultDto<List<UserAddrGetRes>> getUserAddrList() {
        List<UserAddrGetRes> result = new ArrayList<>();
        String msg = "조회 성공";
        int statusCode = 1;
        try { result = service.getUserAddrList(); }
        catch (Exception e) {
            e.printStackTrace();
            msg = e.getMessage();
            statusCode = -1;
        }
        return ResultDto.<List<UserAddrGetRes>>builder()
                .statusCode(statusCode)
                .resultMsg(msg)
                .resultData(result).build();
    }

    @Override
    @GetMapping("/list/{addrPk}")
    @Operation(summary = "유저 주소 조회")
    @ApiResponse(
            description =
                    "<p> 1 : 성공 </p>"+
                            "<p> -1 : 오류 </p>"
    )
    public ResultDto<UserAddrGetRes> getUserAddr(@PathVariable long addrPk) {
        UserAddrGetRes result = new UserAddrGetRes();
        String msg = "조회 성공";
        int statusCode = 1;
        try { result = service.getUserAddr(addrPk); }
        catch (Exception e) {
            e.printStackTrace();
            msg = e.getMessage();
            statusCode = -1;
        }
        return ResultDto.<UserAddrGetRes>builder()
                .statusCode(statusCode)
                .resultMsg(msg)
                .resultData(result).build();
    }

    @Override
    @GetMapping("/main-address")
    @Operation(summary = "유저 메인 주소 조회")
    @ApiResponse(
            description =
                    "<p> 1 : 성공 </p>"+
                            "<p> -1 : 오류 </p>"
    )
    public ResultDto<UserAddrGetRes> getMainUserAddr() {
        UserAddrGetRes result = new UserAddrGetRes();
        String msg = "조회 성공";
        int statusCode = 1;
        try { result = service.getMainUserAddr(); }
        catch (Exception e) {
            e.printStackTrace();
            msg = e.getMessage();
            statusCode = -1;
        }
        return ResultDto.<UserAddrGetRes>builder()
                .statusCode(statusCode)
                .resultMsg(msg)
                .resultData(result).build();
    }

    @Override
    @PatchMapping
    @Operation(summary = "유저 주소 수정")
    @ApiResponse(
            description =
                    "<p> 1 : 성공 </p>"+
                            "<p> -1 : 오류 </p>"
    )
    public ResultDto<Integer> patchUserAddr(@RequestBody UserAddrPatchReq p) {
        int result = 0;
        String msg = "수정 완료";
        int statusCode = 1;
        try { result = service.patchUserAddr(p); }
        catch (Exception e) {
            e.printStackTrace();
            msg = e.getMessage();
            statusCode = -1;
        }
        return ResultDto.<Integer>builder()
                .statusCode(statusCode)
                .resultMsg(msg)
                .resultData(result).build();
    }

    @Override
    @PatchMapping("/main-address")
    @Operation(summary = "유저 메인 주소 변경")
    @ApiResponse(
            description =
                    "<p> 1 : 성공 </p>"+
                            "<p> -1 : 오류 </p>"
    )
    public ResultDto<Integer> patchMainUserAddr(@RequestBody MainUserAddrPatchReq p) {
        int result = 0;
        String msg = "등록 완료";
        int statusCode = 1;
        try { result = service.patchMainUserAddr(p); }
        catch (Exception e) {
            e.printStackTrace();
            msg = e.getMessage();
            statusCode = -1;
        }
        return ResultDto.<Integer>builder()
                .statusCode(statusCode)
                .resultMsg(msg)
                .resultData(result).build();
    }

    @Override
    @DeleteMapping
    @Operation(summary = "유저 주소 삭제")
    @ApiResponse(
            description =
                    "<p> 1 : 성공 </p>"+
                            "<p> -1 : 오류 </p>"
    )
    public ResultDto<Integer> deleteUserAddr(@ModelAttribute @ParameterObject UserAddrDelReq p) {
        int result = 0;
        String msg = "등록 완료";
        int statusCode = 1;
        try { result = service.deleteUserAddr(p); }
        catch (Exception e) {
            e.printStackTrace();
            msg = e.getMessage();
            statusCode = -1;
        }
        return ResultDto.<Integer>builder()
                .statusCode(statusCode)
                .resultMsg(msg)
                .resultData(result).build();
    }
}
