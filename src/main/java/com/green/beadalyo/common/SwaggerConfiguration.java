package com.green.beadalyo.common;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.tags.Tag;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.Collections;

@OpenAPIDefinition(
        info = @Info(
                title = "배달요",
                description = "Second Project",
                version = "v1"
        ),
        security = @SecurityRequirement(name = "authorization"),
        servers = @Server(url = "/")
)
@SecurityScheme(
        type = SecuritySchemeType.HTTP
        , name = "authorization"
        , in = SecuritySchemeIn.HEADER
        , bearerFormat = "JWT"
        , scheme = "Bearer"
)
@Configuration
public class SwaggerConfiguration {
        @Bean
        public OpenAPI sortTagsAlphabetically() {
                return new OpenAPI()
                        .tags(Collections.singletonList(
                                new Tag().name("로그인")
                        ));
        }
}

