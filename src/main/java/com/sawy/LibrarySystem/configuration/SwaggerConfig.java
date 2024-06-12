package com.sawy.LibrarySystem.configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;

@OpenAPIDefinition(
        info = @Info(
                title = "Library System API",
                description = "Restful API to manage library",
                contact = @Contact(
                        name = "Sawy",
                        url = "https://github.com/mohamed0sawy",
                        email = "mohamed.elsawy1009@gmail.com"
                ),
                version = "v1"
        )
)
public class SwaggerConfig {

}
