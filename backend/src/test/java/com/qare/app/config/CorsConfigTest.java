package com.qare.app.config;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.options;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = {
        CorsConfig.class},
        properties = {
        "server.servlet.context-path=",
        "spring.mvc.servlet.path="
})
@Import(CorsConfig.class)
class CorsConfigTest {

    private static final String ALLOWED_ORIGIN = "http://localhost:5173";

    @Autowired
    MockMvc mockMvc;

    @Test
    void preflight_allowedOrigin_underApi_includesAllowHeaders() throws Exception {
        mockMvc.perform(options("/api/ping")
                        .header("Origin", ALLOWED_ORIGIN)
                        .header("Access-Control-Request-Method", "POST"))
                .andExpect(status().isOk())
                .andExpect(header().string("Access-Control-Allow-Origin", ALLOWED_ORIGIN))
                .andExpect(header().string("Access-Control-Allow-Methods", containsString("POST")))
                .andExpect(header().string("Access-Control-Allow-Methods", containsString("GET")))
                .andExpect(header().string("Access-Control-Allow-Methods", containsString("DELETE")))
                .andExpect(header().string("Vary", containsString("Origin"))); // typical for CORS
    }

}

