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
        if(list.isEmpty()) {
            return result;
        }
        for(UserAddr userAddr : list) {
            UserAddrGetRes addrGetRes = new UserAddrGetRes(userAddr);
            result.add(addrGetRes);
        }

        return result;

    }

    @Override
    public UserAddrGetRes getUserAddr(long addrPk) throws Exception {
        if(repository.existsById(addrPk)) {
            throw new RuntimeException("존재하지 않는 데이터");
        }
        UserAddrGetRes result = new UserAddrGetRes(repository.findUserAddrByUserPkAnAndAddrPk(addrPk, authenticationFacade.getLoginUserPk()));

        return result;
    }

    @Override
    public UserAddrGetRes getMainUserAddr() throws Exception{
        if(repository.existsUserAddrByUser(userRepository.findByUserPk(authenticationFacade.getLoginUserPk())) == null) {
            return null;
        }
        UserAddr addr = repository.findMainUserAddr(authenticationFacade.getLoginUserPk());
        return new UserAddrGetRes(addr);
    }

    @Override
    public int patchUserAddr(UserAddrPatchReq p) throws Exception{
        UserAddr addr = repository.getReferenceById(p.getAddrPk());
        addr.update(p);
        repository.save(addr);
        return 1;
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
        UserAddr userAddr = repository.getReferenceById(p.getAddrPk());
        repository.delete(userAddr);
        if(getMainUserAddr() == null) {
            List<UserAddrGetRes> list = getUserAddrList();
            if(!list.isEmpty()) {
                MainUserAddrPatchReq req = new MainUserAddrPatchReq();
                req.setSignedUserPk(authenticationFacade.getLoginUserPk());
                req.setChangeAddrPk(list.get(0).getAddrPk());
                patchMainUserAddr(req);
            }
        }
        return 1;
    }


}
