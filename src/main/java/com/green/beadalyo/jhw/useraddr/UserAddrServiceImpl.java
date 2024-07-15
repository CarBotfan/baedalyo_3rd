package com.green.beadalyo.jhw.useraddr;

import com.green.beadalyo.jhw.security.AuthenticationFacade;
import com.green.beadalyo.jhw.useraddr.model.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class UserAddrServiceImpl implements UserAddrService{
    private final UserAddrMapper mapper;
    private final AuthenticationFacade authenticationFacade;

    @Override
    public long postUserAddr(UserAddrPostReq p) throws Exception{
        p.setSignedUserId(authenticationFacade.getLoginUserPk());
        return mapper.postUserAddr(p);
    }

    @Override
    public List<UserAddrGetRes> getUserAddrList() throws Exception{
        long signedUserPk = authenticationFacade.getLoginUserPk();
        return mapper.getUserAddrList(signedUserPk);

    }

    @Override
    public UserAddrGetRes getUserAddr(long addrPk) throws Exception {

        UserAddrGetRes result = mapper.getUserAddr(authenticationFacade.getLoginUserPk(), addrPk);
        if(result == null) {
            throw new RuntimeException("존재하지 않는 데이터");
        }
        return result;
    }

    @Override
    public UserAddrGetRes getMainUserAddr() throws Exception{
        long signedUserPk = authenticationFacade.getLoginUserPk();
        return mapper.getMainUserAddr(signedUserPk);
    }

    @Override
    public int patchUserAddr(UserAddrPatchReq p) throws Exception{
        p.setSignedUserPk(authenticationFacade.getLoginUserPk());
        return mapper.updUserAddr(p);
    }

    @Override
    public int patchMainUserAddr(MainUserAddrPatchReq p) throws Exception{
        p.setSignedUserPk(authenticationFacade.getLoginUserPk());
        mapper.patchCurrentMainUserAddr(p.getSignedUserPk());
        return mapper.patchMainUserAddr(p);
    }

    @Override
    public int deleteUserAddr(UserAddrDelReq p) throws Exception{
        p.setSignedUserPk(authenticationFacade.getLoginUserPk());
        return mapper.deleteUserAddr(p);
    }


}
