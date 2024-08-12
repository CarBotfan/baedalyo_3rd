package com.green.beadalyo.jhw;

import com.green.beadalyo.gyb.category.CategoryService;
import com.green.beadalyo.jhw.user.UserService;
import com.green.beadalyo.jhw.user.UserServiceImpl;
import com.green.beadalyo.jhw.user.entity.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@Import(UserServiceImpl.class)
@ExtendWith(SpringExtension.class)
class
UserServiceTest {
    @Autowired
    private UserServiceImpl service;
    @Test
    public void getUserTest()
    {
        User user = service.getUser(999L);
    }


}
