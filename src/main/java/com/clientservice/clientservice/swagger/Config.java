package com.clientservice.clientservice.swagger;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Tag;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.bean.validators.configuration.BeanValidatorPluginsConfiguration;

@Import(BeanValidatorPluginsConfiguration.class)
@Configuration
public class Config {

    @Bean
    public Docket swaggerConfiguration() {
        Docket docket = new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.accenture.ems"))
                .paths(PathSelectors.ant("/error").negate())
                .build()
                .apiInfo(apiInfo());
        docket.useDefaultResponseMessages(false);
        return appendTags(docket);
    }

    private Docket appendTags(Docket docket) {
        return docket.tags(
                new Tag(DescriptionVariables.CLIENT,
                        "Used to get, create, update and delete clients"));
//                new Tag(DescriptionVariables.TRAINING,
//                        "Controller used to get, create, update and delete trainings"),
//                new Tag(DescriptionVariables.TRAINING_DETAILS,
//                        "Controller used to get, create, update and delete training details")
//        );
    }
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("Client systems API")
                .description("Client management systems API")
                .version("1.0")
                .build();
    }
}