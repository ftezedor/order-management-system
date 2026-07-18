package com.acme.order.bootstrap.spring.exception;

import com.acme.order.bootstrap.spring.rest.handler.GlobalExceptionHandler;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class MethodArgumentNotValidExceptionTest
{

    private MockMvc mockMvc;

    @BeforeEach
    void setUp()
    {
        // Initialize the standard JSR-380 Validator
        LocalValidatorFactoryBean validator = new LocalValidatorFactoryBean();
        validator.afterPropertiesSet();

        mockMvc = MockMvcBuilders.standaloneSetup(new TestValidationController())
                .setControllerAdvice(new GlobalExceptionHandler()).setValidator(validator) // Pass validator for bean
                                                                                           // validation
                .build();
    }

    @Test
    void shouldHandleMethodArgumentNotValidException() throws Exception
    {
        // Given an invalid JSON payload (empty name)
        String invalidPayload = "{\"name\":\"\"}";

        // When & Then
        mockMvc.perform(post("/test/validate").contentType(MediaType.APPLICATION_JSON).content(invalidPayload))
                .andExpect(status().isBadRequest()).andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message").value("name: must not be blank"));
    }

    // Dummy controller and DTO to trigger validation failures
    @RestController
    public static class TestValidationController
    {

        @PostMapping("/test/validate")
        public void validateBody(@Valid @RequestBody TestRequest request)
        {
            // No implementation needed; validation exception is thrown before entering the
            // method
        }
    }

    // DTO Record to validate
    public record TestRequest(@NotBlank String name)
    {
    }
}