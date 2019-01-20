package ru.vlad.loyalty.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import ru.vlad.loyalty.requests.config.RequestsConfig;

@Configuration
@Import({ControllerConfig.class,
        ServiceConfig.class,
        RequestsConfig.class})
public class ApplicationConfig {
}
