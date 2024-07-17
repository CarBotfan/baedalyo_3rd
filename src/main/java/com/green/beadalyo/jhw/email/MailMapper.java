package com.green.beadalyo.jhw.email;

import com.green.beadalyo.jhw.email.model.UserEntityEmail;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MailMapper {
    UserEntityEmail getUserByEmail(String email);
}
