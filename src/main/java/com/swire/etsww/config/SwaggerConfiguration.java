package com.swire.etsww.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StopWatch;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Date;

@Configuration
@EnableSwagger2
@Slf4j
public class SwaggerConfiguration {

    @Value("${swagger.enabled:false}")
    private boolean swaggerEnabled;
    @Value("${swagger.package:}")
    private String swaggerPackage;

    /**
     * api文档下载 http://ip:port/v2/api-docs
     * api可视化 http://ip:port//swagger-ui.html
     * @return
     */

    @Bean
    public Docket swaggerSpringfoxDocket() {
        log.debug("Starting Swagger");
        StopWatch watch = new StopWatch();
        watch.start();
        Docket docket = new Docket(DocumentationType.SWAGGER_2)
//                .apiInfo(apiInfo)
                .enable(swaggerEnabled)
                .genericModelSubstitutes(ResponseEntity.class)
                .forCodeGeneration(true)
                .genericModelSubstitutes(ResponseEntity.class)
                .directModelSubstitute(java.time.LocalDate.class, String.class)
                .directModelSubstitute(java.time.ZonedDateTime.class, Date.class)
                .directModelSubstitute(java.time.LocalDateTime.class, Date.class)
                .select()
                .apis(RequestHandlerSelectors.basePackage(swaggerPackage))
                .build();
        watch.stop();
        log.debug("Started Swagger in {} ms", watch.getTotalTimeMillis());
        return docket;
    }
}
