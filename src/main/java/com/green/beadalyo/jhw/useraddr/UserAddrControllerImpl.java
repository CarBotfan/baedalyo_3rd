package com.green.beadalyo.jhw.useraddr;

import com.green.beadalyo.jhw.common.model.ResultDto;
import com.green.beadalyo.jhw.useraddr.model.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/api/address")
@RequiredArgsConstructor
public class UserAddrControllerImpl implements UserAddrController{
    private final UserAddrServiceImpl service;
    @Override
    @PostMapping
    public ResultDto<Long> postUserAddr(@RequestBody UserAddrPostReq p) {
        long result = service.postUserAddr(p);
        return ResultDto.<Long>builder()
                .statusCode(2)
                .resultMsg("")
                .result(result).build();
    }

    @Override
    @GetMapping
    public ResultDto<List<UserAddrGetRes>> getUserAddrList() {
        List<UserAddrGetRes> result = service.getUserAddrList();
        return ResultDto.<List<UserAddrGetRes>>builder()
                .statusCode(2)
                .resultMsg("")
                .result(result).build();
    }

    @Override
    @GetMapping("/main-address")
    public ResultDto<UserAddrGetRes> getMainUserAddr() {
        UserAddrGetRes result = service.getMainUserAddr();
        return ResultDto.<UserAddrGetRes>builder()
                .statusCode(2)
                .resultMsg("")
                .result(result).build();
    }

    @Override
    @PatchMapping
    public ResultDto<Integer> patchUserAddr(@RequestBody UserAddrPatchReq p) {
        int result = service.patchUserAddr(p);
        return ResultDto.<Integer>builder()
                .statusCode(2)
                .resultMsg("")
                .result(result).build();
    }

    @Override
    @PatchMapping("/main-address")
    public ResultDto<Integer> patchMainUserAddr(@RequestBody MainUserAddrPatchReq p) {
        int result = service.patchMainUserAddr(p);
        return ResultDto.<Integer>builder()
                .statusCode(2)
                .resultMsg("")
                .result(result).build();
    }

    @Override
    @DeleteMapping
    public ResultDto<Integer> deleteUserAddr(@ModelAttribute @ParameterObject UserAddrDelReq p) {
        int result = service.deleteUserAddr(p);
        return ResultDto.<Integer>builder()
                .statusCode(2)
                .resultMsg("")
                .result(result).build();
    }
}
