package com.green.beadalyo.jhw.useraddr;

import com.green.beadalyo.jhw.useraddr.model.*;

import java.util.List;

public interface UserAddrService {
    long postUserAddr(UserAddrPostReq p);
    List<UserAddrGetRes> getUserAddrList();
    UserAddrGetRes getMainUserAddr();
    int patchUserAddr(UserAddrPatchReq p);
    int deleteUserAddr(UserAddrDelReq p);
    int patchMainUserAddr(MainUserAddrPatchReq p);
}
