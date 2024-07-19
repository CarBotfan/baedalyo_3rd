package com.green.beadalyo.jhw.email;

import com.green.beadalyo.common.model.ResultDto;
import com.green.beadalyo.jhw.email.model.EmailCheckDto;
import com.green.beadalyo.jhw.email.model.EmailRequestDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "메일 컨트롤러")
public class MailController {
    private final MailService mailService;
    @PostMapping("/send")
    @Operation(summary = "인증번호 메일 발송")
    public ResultDto<String> mailSend(@RequestBody @Valid EmailRequestDto emailDto) {

        String result = mailService.joinEmail(emailDto.getEmail());
        return ResultDto.<String>builder()
                .statusCode(1)
                .resultMsg(result)
                .resultData(result).build();
    }

    @PostMapping("/auth_check")
    @Operation(summary = "인증번호 체크")
    @ApiResponse(
            description =
                    "<p> 1 : 인증 성공 </p>"+
                            "<p> -1 : 인증 실패 </p>"
    )
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
