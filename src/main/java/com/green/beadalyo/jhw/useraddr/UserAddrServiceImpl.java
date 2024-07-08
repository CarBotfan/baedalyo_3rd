package com.green.beadalyo.jhw.useraddr;

import com.green.beadalyo.jhw.security.AuthenticationFacade;
import com.green.beadalyo.jhw.useraddr.model.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserAddrServiceImpl implements UserAddrService{
    private final UserAddrMapper mapper;
    private final AuthenticationFacade authenticationFacade;

    @Override
    public long postUserAddr(UserAddrPostReq p) {
        p.setSignedUserId(authenticationFacade.getLoginUserPk());
        return mapper.postUserAddr(p);
    }

    @Override
    public List<UserAddrGetRes> getUserAddrList() {
        long signedUserPk = authenticationFacade.getLoginUserPk();
        return mapper.getUserAddrList(signedUserPk);

    }

    @Override
    public UserAddrGetRes getMainUserAddr() {
        long signedUserPk = authenticationFacade.getLoginUserPk();
        return mapper.getMainUserAddr(signedUserPk);
    }

    @Override
    public int patchUserAddr(UserAddrPatchReq p) {
        p.setSignedUserPk(authenticationFacade.getLoginUserPk());
        return mapper.updUserAddr(p);
    }

    @Override
    public int patchMainUserAddr(MainUserAddrPatchReq p) {
        p.setSignedUserPk(authenticationFacade.getLoginUserPk());
        mapper.patchCurrentMainUserAddr(p.getSignedUserPk());
        return mapper.patchMainUserAddr(p);
    }

    @Override
    public int deleteUserAddr(UserAddrDelReq p) {
        p.setSignedUserPk(authenticationFacade.getLoginUserPk());
        return mapper.deleteUserAddr(p);
    }


}
