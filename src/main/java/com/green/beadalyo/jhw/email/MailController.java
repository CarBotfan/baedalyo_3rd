package com.green.beadalyo.jhw.email;

import com.green.beadalyo.jhw.email.model.EmailCheckDto;
import com.green.beadalyo.jhw.email.model.EmailRequestDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("api/mail")
public class MailController {
    private final MailService mailService;
    @PostMapping("/send")
    public String mailSend(@RequestBody @Valid EmailRequestDto emailDto) {
        return mailService.joinEmail(emailDto.getEmail());
    }

    @PostMapping("/auth_check")
    public Boolean AuthCheck(@RequestBody @Valid EmailCheckDto emailCheckDto) {
        Boolean checked = mailService.CheckAuthNum(emailCheckDto.getEmail(), emailCheckDto.getAuthNum());
        if(checked) {
            return checked;
        } else {
            throw new NullPointerException("인증번호를 다시 확인해주세요.");
        }
    }
}
