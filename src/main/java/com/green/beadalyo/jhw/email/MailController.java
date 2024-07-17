package com.green.beadalyo.jhw.email;

import com.green.beadalyo.common.model.ResultDto;
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
    public ResultDto<String> mailSend(@RequestBody @Valid EmailRequestDto emailDto) {
        String result = mailService.joinEmail(emailDto.getEmail());
        return ResultDto.<String>builder()
                .statusCode(1)
                .resultMsg("")
                .resultData(result).build();
    }

    @PostMapping("/auth_check")
    public ResultDto<Boolean> AuthCheck(@RequestBody @Valid EmailCheckDto emailCheckDto) {
        Boolean checked = mailService.CheckAuthNum(emailCheckDto.getEmail(), emailCheckDto.getAuthNum());
        int statusCode = 1;
        String msg = "인증 성공";
        if(checked) {

        } else {
            msg = "인증번호가 잘못되었거나 만료되었습니다.";
            statusCode = -1;
        }
        return ResultDto.<Boolean>builder()
                .statusCode(statusCode)
                .resultMsg(msg)
                .resultData(checked).build();
    }
}
