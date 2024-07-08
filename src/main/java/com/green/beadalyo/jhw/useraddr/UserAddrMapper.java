package com.green.beadalyo.jhw.useraddr;

import com.green.beadalyo.jhw.useraddr.model.*;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface UserAddrMapper {
    long postUserAddr(UserAddrPostReq p);
    List<UserAddrGetRes> getUserAddrList(long signedUserPk);
    UserAddrGetRes getMainUserAddr(long signedUserPk);
    int patchCurrentMainUserAddr(long signedUserPk);
    int patchMainUserAddr(MainUserAddrPatchReq p);
    int updUserAddr(UserAddrPatchReq p);
    int deleteUserAddr(UserAddrDelReq p);
}

