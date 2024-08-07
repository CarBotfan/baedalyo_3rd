package com.green.beadalyo.kdh.stat.admin;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/admin/stat")
@RequiredArgsConstructor
@Tag(name = "어드민 통계 컨트롤러입니다.")
public class StatControllerForAdmin {
    private final StatServiceForAdmin service;
}
