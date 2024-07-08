package com.green.beadalyo.lhn.common;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.beans.factory.annotation.Configurable;

@Configurable
@OpenAPIDefinition(
        info = @Info(
                title = "Postime"
                , description = "Postime 1차 프로젝트"
                , version = "응애버전"
        )
)
public class SwaggerConfiguration {

}