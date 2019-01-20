package ru.vlad.loyalty.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@ComponentScan(value = {
        "ru.vlad.loyalty.controller"
})
@Import({SwaggerUIConfig.class})
public class ControllerConfig {
}
