package de.ait.os.config;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import de.ait.os.dto.StandardResponseDto;
import de.ait.os.security.config.SecurityConfig;
import freemarker.cache.ClassTemplateLoader;
import freemarker.template.Configuration;
import io.swagger.v3.core.converter.AnnotatedType;
import io.swagger.v3.core.converter.ModelConverters;
import io.swagger.v3.core.converter.ResolvedSchema;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static de.ait.os.documentation.OpenApiDocumentation.*;

/**
 * 7/5/2024
 * education-center
 *
 * @author Chechkina (AIT TR)
 */

@org.springframework.context.annotation.Configuration //указываем полный путь
public class ServiceConfiguration {

    @Bean
    public AmazonS3 amazonS3(S3ConfigurationProperties properties) {
        // прописываем данные для аутентификации нашего приложения в DigitalOcean
        AWSCredentials credentials = new BasicAWSCredentials(
                properties.getAccessKey(), properties.getSecretKey()
        );

        // настраиваем точку подключения к нашему региону
        AwsClientBuilder.EndpointConfiguration endpointConfiguration = new AwsClientBuilder.EndpointConfiguration(
                properties.getEndpoint(), properties.getRegion()
        );

        // создадим клиента для загрузки файлов
        AmazonS3ClientBuilder amazonS3ClientBuilder = AmazonS3ClientBuilder
                .standard()
                .withCredentials(new AWSStaticCredentialsProvider(credentials)); // указываем права доступа
        amazonS3ClientBuilder.setEndpointConfiguration(endpointConfiguration); // указываем куда

        return amazonS3ClientBuilder.build(); // клиент для амазона/digital ocean
    }

    @Bean
    ModelMapper getModelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration()
                .setFieldMatchingEnabled(true)
                .setFieldAccessLevel(org.modelmapper.config.Configuration.AccessLevel.PRIVATE)
                .setMatchingStrategy(MatchingStrategies.STRICT);
        return modelMapper;
    }


    @Bean
    PasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder();

    }

    //создаем бин openAPI, чтобы Swagger содержал пути для login и logout
    @Bean
    public OpenAPI openAPI(@Value("${base.url}") String baseUrl) {

        ResolvedSchema resolvedSchema = ModelConverters.getInstance()
                .resolveAsResolvedSchema(
                        new AnnotatedType(StandardResponseDto.class).resolveAsRef(false));

        return new OpenAPI()
                .servers(Arrays.asList(
                         new Server().url("http://localhost:8080"),
                         new Server().url(baseUrl)
            ))
                .components(new Components()
                        .addSchemas("EmailAndPassword", emailAndPassword())
                        .addSecuritySchemes("cookieAuth", securityScheme())
                        .addSchemas("StandardResponseDto", resolvedSchema.schema.description("StandardResponseDto")))
                .addSecurityItem(buildSecurity())
                .paths(buildAuthenticationPath())
                .info(new Info().title("Education Service API").version("0.1"));
    }

    @Bean
    public WebMvcConfigurer cors() { // чтобы можно было посылать запросы с других доменов
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**").allowedOrigins("*");
            }
        };
    }

    @Bean // а здесь конфигурация будет в импортах
    public Configuration freemarkerConfiguration() {
        Configuration configuration = new Configuration(Configuration.VERSION_2_3_21);
        configuration.setDefaultEncoding("UTF-8");
        configuration.setTemplateLoader(new ClassTemplateLoader(SecurityConfig.class, "/mails/"));

        return configuration;
    }
}
