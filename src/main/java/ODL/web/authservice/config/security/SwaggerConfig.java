package ODL.web.authservice.config.security;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.Contact;
import springfox.documentation.service.OAuth;
import springfox.documentation.service.ResourceOwnerPasswordCredentialsGrant;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;

@Configuration
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SwaggerConfig {

    @Value("${server.port}")
    String port;

    @Value("${token.auth.URL}")
    String authURL;

    private static String HOST = "http://localhost:";

    private static final String CONTOLLER_PACKAGE = "ODL.web.authservice.controller";

    @Bean
    public Docket productApi() {

        return new Docket(DocumentationType.SWAGGER_2).select()
                .apis(RequestHandlerSelectors.basePackage(CONTOLLER_PACKAGE)).paths(PathSelectors.any()).build()
                .securityContexts(Collections.singletonList(securityContext()))
                .securitySchemes(Arrays.asList(securitySchema())).apiInfo(apiInfo());

    }

    private OAuth securitySchema() {
        return new OAuth("oauth2", new ArrayList(),
                Arrays.asList(new ResourceOwnerPasswordCredentialsGrant(HOST.concat(port).concat(authURL))));
    }

    private SecurityContext securityContext() {
        return SecurityContext.builder().securityReferences(defaultAuth()).build();
    }

    private List<SecurityReference> defaultAuth() {
        final AuthorizationScope[] authorizationScopes = new AuthorizationScope[2];
        authorizationScopes[0] = new AuthorizationScope("read", "read all");
        authorizationScopes[1] = new AuthorizationScope("write", "write all");
        return Collections.singletonList(new SecurityReference("oauth2", authorizationScopes));
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder().title("Auth service API").description("").version("1.0.0")
                .contact(new Contact("Developers", "https://cit.vstu.by/", "")).build();

    }
}
