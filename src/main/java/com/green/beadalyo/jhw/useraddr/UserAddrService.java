package com.green.beadalyo.jhw.useraddr;

import com.green.beadalyo.jhw.useraddr.model.*;

import java.util.List;

public interface UserAddrService {
    long postUserAddr(UserAddrPostReq p) throws Exception;
    List<UserAddrGetRes> getUserAddrList() throws Exception;
    UserAddrGetRes getMainUserAddr() throws Exception;
    int patchUserAddr(UserAddrPatchReq p) throws Exception;
    int deleteUserAddr(UserAddrDelReq p) throws Exception;
    int patchMainUserAddr(MainUserAddrPatchReq p) throws Exception;
}
