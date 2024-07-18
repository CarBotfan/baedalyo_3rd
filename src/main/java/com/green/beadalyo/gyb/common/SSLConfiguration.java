package com.green.beadalyo.gyb.common;

import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Hidden
@RestController
public class SSLConfiguration
{

    @GetMapping("/.well-known/pki-validation/E294CC5CE7D911F481E08B0D2705C54F.txt")
    public Resource getValidationFile() {
        return new FileSystemResource("/root/jar/E294CC5CE7D911F481E08B0D2705C54F.txt");
    }

}
