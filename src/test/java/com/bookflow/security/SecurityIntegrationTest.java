package com.bookflow.security;

import com.bookflow.user.entity.User;
import com.bookflow.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class SecurityIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
    }

    @Test
    void getAllUsers_withoutToken_shouldReturnUnauthorized() throws Exception {

        mockMvc.perform(
                        get("/api/users")
                )
                .andExpect(status().isUnauthorized());
    }

    @Test
    void getAllUsers_withUserToken_shouldReturnForbidden() throws Exception {

        String email = "user@bookflow.com";
        String password = "Password123";

        registerUser(email, password);

        String token = loginAndGetToken(email, password);

        mockMvc.perform(
                        get("/api/users")
                                .header(
                                        "Authorization",
                                        "Bearer " + token
                                )
                )
                .andExpect(status().isForbidden());
    }

    @Test
    void getAllUsers_withAdminToken_shouldReturnOk() throws Exception {

        String email = "admin@bookflow.com";
        String password = "Password123";

        registerUser(email, password);

        User user = userRepository.findByEmail(email)
                .orElseThrow();

        user.promoteToAdmin();

        userRepository.save(user);

        String token = loginAndGetToken(email, password);

        mockMvc.perform(
                        get("/api/users")
                                .header(
                                        "Authorization",
                                        "Bearer " + token
                                )
                )
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(
                        MediaType.APPLICATION_JSON
                ))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].email").value(email))
                .andExpect(jsonPath("$[0].role").value("ADMIN"));
    }

    private void registerUser(
            String email,
            String password
    ) throws Exception {

        String requestBody = """
                {
                    "firstName": "Security",
                    "lastName": "Test",
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

    private String loginAndGetToken(
            String email,
            String password
    ) throws Exception {

        String requestBody = """
                {
                    "email": "%s",
                    "password": "%s"
                }
                """.formatted(email, password);

        MvcResult result = mockMvc.perform(
                        post("/api/auth/login")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestBody)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").isNotEmpty())
                .andReturn();

        String responseBody = result
                .getResponse()
                .getContentAsString();

        int tokenStart = responseBody.indexOf("\"token\":\"")
                + "\"token\":\"".length();

        int tokenEnd = responseBody.indexOf(
                "\"",
                tokenStart
        );

        return responseBody.substring(
                tokenStart,
                tokenEnd
        );
    }
}