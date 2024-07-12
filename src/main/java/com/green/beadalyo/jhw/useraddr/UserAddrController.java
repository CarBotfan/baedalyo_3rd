package com.green.beadalyo.jhw.useraddr;

import com.green.beadalyo.common.model.ResultDto;
import com.green.beadalyo.jhw.useraddr.model.*;

import java.util.List;

public interface UserAddrController {
    ResultDto<Long> postUserAddr(UserAddrPostReq p);
    ResultDto<List<UserAddrGetRes>> getUserAddrList();
    ResultDto<UserAddrGetRes> getMainUserAddr();
    ResultDto<UserAddrGetRes> getUserAddr(long addrPk);
    ResultDto<Integer> patchUserAddr(UserAddrPatchReq p);
    ResultDto<Integer> patchMainUserAddr(MainUserAddrPatchReq p);
    ResultDto<Integer> deleteUserAddr(UserAddrDelReq p);
}
