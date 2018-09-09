package com.volantx.webparser.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.VendorExtension;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by kamuran YILMAZ
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig {
    @Bean
    public Docket api() {

        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.volantx.webparser"))
                .paths(PathSelectors.any())
                .build()
                .apiInfo(apiInfo());

    }

    private ApiInfo apiInfo() {
        Contact contact = new Contact("Volantx", "https://volantx.com", "developer@volantxs.com");
        String title = "Volantx Rest API's";
        String description = "Rest API's provided down below. Can be used and tried";
        String version = "1.0.0.RELEASE";
        String termsOfServiceUrl = " Can not be use without permission";
        String license = "License";
        String licenseUrl = "URL";
        Collection<VendorExtension> vendorExtensions = new ArrayList<>();
        return new ApiInfo(title, description, version, termsOfServiceUrl, contact, license, licenseUrl, vendorExtensions);
    }
}
