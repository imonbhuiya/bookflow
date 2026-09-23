package com.bookflow.auth.controller;

import com.bookflow.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class AuthControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
    }

    @Test
    void register_shouldCreateNewUser() throws Exception {

        String requestBody = """
                {
                    "firstName": "Imon",
                    "lastName": "Bhuiya",
                    "email": "integration@bookflow.com",
                    "password": "Password123",
                    "phone": "015123456789",
                    "dateOfBirth": "1997-05-15",
                    "gender": "MALE",
                    "street": "Test Street 10",
                    "city": "Frankfurt",
                    "postalCode": "60311",
                    "country": "Germany"
                }
                """;

        mockMvc.perform(
                        post("/api/auth/register")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestBody)
                )
                .andExpect(status().isCreated())
                .andExpect(content().contentTypeCompatibleWith(
                        MediaType.APPLICATION_JSON
                ))
                .andExpect(jsonPath("$.id").value(notNullValue()))
                .andExpect(jsonPath("$.firstName").value("Imon"))
                .andExpect(jsonPath("$.lastName").value("Bhuiya"))
                .andExpect(jsonPath("$.email")
                        .value("integration@bookflow.com"))
                .andExpect(jsonPath("$.phone")
                        .value("015123456789"))
                .andExpect(jsonPath("$.dateOfBirth")
                        .value("1997-05-15"))
                .andExpect(jsonPath("$.gender").value("MALE"))
                .andExpect(jsonPath("$.street")
                        .value("Test Street 10"))
                .andExpect(jsonPath("$.city").value("Frankfurt"))
                .andExpect(jsonPath("$.postalCode").value("60311"))
                .andExpect(jsonPath("$.country").value("Germany"))
                .andExpect(jsonPath("$.role").value("USER"))
                .andExpect(jsonPath("$.enabled").value(true))
                .andExpect(jsonPath("$.createdAt").value(notNullValue()));
    }

    @Test
    void register_shouldRejectDuplicateEmail() throws Exception {

        String requestBody = """
                {
                    "firstName": "Imon",
                    "lastName": "Bhuiya",
                    "email": "duplicate@bookflow.com",
                    "password": "Password123",
                    "phone": "015123456789",
                    "dateOfBirth": "1997-05-15",
                    "gender": "MALE",
                    "street": "Test Street 10",
                    "city": "Frankfurt",
                    "postalCode": "60311",
                    "country": "Germany"
                }
                """;

        mockMvc.perform(
                        post("/api/auth/register")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestBody)
                )
                .andExpect(status().isCreated());

        mockMvc.perform(
                        post("/api/auth/register")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestBody)
                )
                .andExpect(status().isConflict());
    }

    @Test
    void register_shouldRejectInvalidRequest() throws Exception {

        String requestBody = """
                {
                    "firstName": "",
                    "lastName": "",
                    "email": "invalid-email",
                    "password": "123",
                    "phone": "",
                    "dateOfBirth": "2030-01-01",
                    "gender": null,
                    "street": "",
                    "city": "",
                    "postalCode": "",
                    "country": ""
                }
                """;

        mockMvc.perform(
                        post("/api/auth/register")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestBody)
                )
                .andExpect(status().isBadRequest());
    }

    @Test
    void login_shouldReturnTokenForValidCredentials() throws Exception {

        registerUser(
                "login@bookflow.com",
                "Password123"
        );

        String loginRequest = """
                {
                    "email": "login@bookflow.com",
                    "password": "Password123"
                }
                """;

        mockMvc.perform(
                        post("/api/auth/login")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(loginRequest)
                )
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(
                        MediaType.APPLICATION_JSON
                ))
                .andExpect(jsonPath("$.token").value(notNullValue()))
                .andExpect(jsonPath("$.user.id").value(notNullValue()))
                .andExpect(jsonPath("$.user.firstName").value("Imon"))
                .andExpect(jsonPath("$.user.lastName").value("Bhuiya"))
                .andExpect(jsonPath("$.user.email")
                        .value("login@bookflow.com"))
                .andExpect(jsonPath("$.user.role").value("USER"))
                .andExpect(jsonPath("$.user.enabled").value(true));
    }

    @Test
    void login_shouldRejectWrongPassword() throws Exception {

        registerUser(
                "wrongpassword@bookflow.com",
                "Password123"
        );

        String loginRequest = """
                {
                    "email": "wrongpassword@bookflow.com",
                    "password": "WrongPassword123"
                }
                """;

        mockMvc.perform(
                        post("/api/auth/login")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(loginRequest)
                )
                .andExpect(status().isUnauthorized());
    }

    private void registerUser(
            String email,
            String password
    ) throws Exception {

        String requestBody = """
                {
                    "firstName": "Imon",
                    "lastName": "Bhuiya",
                    "email": "%s",
                    "password": "%s",
                    "phone": "015123456789",
                    "dateOfBirth": "1997-05-15",
                    "gender": "MALE",
                    "street": "Test Street 10",
                    "city": "Frankfurt",
                    "postalCode": "60311",
                    "country": "Germany"
                }
                """.formatted(email, password);

        mockMvc.perform(
                        post("/api/auth/register")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestBody)
                )
                .andExpect(status().isCreated());
    }
}