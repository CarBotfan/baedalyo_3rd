package com.green.beadalyo.gyb.common;

import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SSLConfiguration
{

    @GetMapping("/.well-known/pki-validation/78601C36A0D31B69288ADD01B62149E7.txt")
    public Resource getValidationFile() {
        return new FileSystemResource("/root/jar/78601C36A0D31B69288ADD01B62149E7.txt");
    }

}
