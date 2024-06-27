package com.green.beadalyo.jhw.useraddr;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping("/api/user/address")
@RequiredArgsConstructor
public class UserAddrControllerImpl implements UserAddrController{
}
