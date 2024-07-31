package com.green.beadalyo.jhw.useraddr;

import com.green.beadalyo.jhw.security.AuthenticationFacade;
import com.green.beadalyo.jhw.user.repository.UserRepository;
import com.green.beadalyo.jhw.useraddr.Entity.UserAddr;
import com.green.beadalyo.jhw.useraddr.model.*;
import com.green.beadalyo.jhw.useraddr.repository.UserAddrRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class UserAddrServiceImpl implements UserAddrService{
    private final UserAddrMapper mapper;
    private final AuthenticationFacade authenticationFacade;
    private final UserAddrRepository repository;
    private final UserRepository userRepository;

    @Override
    public long postUserAddr(UserAddrPostReq p) throws Exception{
        p.setSignedUserId(authenticationFacade.getLoginUserPk());
        UserAddr userAddr = new UserAddr(p);
        userAddr.setUser(userRepository.findByUserPk(authenticationFacade.getLoginUserPk()));
        repository.save(userAddr);
        if(repository.findAllByUserPkOrderByAddrDefaultDesc(p.getSignedUserId()).size() == 1) {
            repository.setMainUserAddr(userAddr.getAddrPk(), p.getSignedUserId());
        }
        return userAddr.getAddrPk();
    }

    @Override
    public List<UserAddrGetRes> getUserAddrList() throws Exception{
        long signedUserPk = authenticationFacade.getLoginUserPk();
        List<UserAddr> list = repository.findAllByUserPkOrderByAddrDefaultDesc(signedUserPk);
        List<UserAddrGetRes> result = new ArrayList<>();
        for(UserAddr userAddr : list) {
            UserAddrGetRes addrGetRes = new UserAddrGetRes(userAddr);
            result.add(addrGetRes);
        }

        return result;

    }

    @Override
    public UserAddrGetRes getUserAddr(long addrPk) throws Exception {

        UserAddrGetRes result = new UserAddrGetRes(repository.findUserAddrByUserPkAnAndAddrPk(addrPk, authenticationFacade.getLoginUserPk()));
        if(result.getAddrPk() == 0) {
            throw new RuntimeException("존재하지 않는 데이터");
        }
        return result;
    }

    @Override
    public UserAddrGetRes getMainUserAddr() throws Exception{
        UserAddr addr = repository.findMainUserAddr(authenticationFacade.getLoginUserPk());
        return new UserAddrGetRes(addr);
    }

    @Override
    public int patchUserAddr(UserAddrPatchReq p) throws Exception{
        p.setSignedUserPk(authenticationFacade.getLoginUserPk());
        return mapper.updUserAddr(p);
    }

    @Override
    public int patchMainUserAddr(MainUserAddrPatchReq p) throws Exception{
        p.setSignedUserPk(authenticationFacade.getLoginUserPk());
        repository.removeMainUserAddr(p.getSignedUserPk());
        return repository.setMainUserAddr(p.getChangeAddrPk(), p.getSignedUserPk());
    }

    @Override
    public int deleteUserAddr(UserAddrDelReq p) throws Exception{
        p.setSignedUserPk(authenticationFacade.getLoginUserPk());
        int result =  mapper.deleteUserAddr(p);
        if(getMainUserAddr() == null) {
            List<UserAddrGetRes> list = getUserAddrList();
            if(!list.isEmpty()) {
                MainUserAddrPatchReq req = new MainUserAddrPatchReq();
                req.setSignedUserPk(authenticationFacade.getLoginUserPk());
                req.setChangeAddrPk(list.get(0).getAddrPk());
                patchMainUserAddr(req);
            }
        }
        return result;
    }


}
