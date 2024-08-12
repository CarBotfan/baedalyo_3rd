package com.green.beadalyo.jhw;

import com.green.beadalyo.jhw.security.AuthenticationFacade;
import com.green.beadalyo.jhw.user.UserServiceImpl;
import com.green.beadalyo.jhw.user.entity.User;
import com.green.beadalyo.jhw.useraddr.UserAddrServiceImpl;
import com.green.beadalyo.jhw.useraddr.repository.UserAddrRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@Import(UserAddrServiceImpl.class)
@ExtendWith(SpringExtension.class)
class UserAddrServiceTest {
    @Autowired
    private UserAddrServiceImpl service;
    @MockBean
    private UserAddrRepository repository;
    @MockBean
    private AuthenticationFacade facade;

}
