package org.hstack.vmeta.videoMetadata;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI openAPI() {
        Info info = new Info()
                .title("VideoMetadata API")
                .description("VideoMetadata API Docs")
                .version("1.0");
        return new OpenAPI()
                .components(new Components())
                .info(info);
    }

}
