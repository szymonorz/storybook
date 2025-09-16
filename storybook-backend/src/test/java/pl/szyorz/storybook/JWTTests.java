package pl.szyorz.storybook;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import pl.szyorz.storybook.entity.user.User;
import pl.szyorz.storybook.entity.user.UserRepository;
import pl.szyorz.storybook.entity.user.data.LoginRequest;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
class JWTTests {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserRepository userRepository;


    @MockitoBean
    private PasswordEncoder passwordEncoder;

    private final ObjectMapper mapper = new ObjectMapper();

    private User user;
    private UUID userId;

    @BeforeEach
    void prepareMocks() {
        user = new User();
        userId = UUID.randomUUID();
        user.setId(userId);
        user.setUsername("adam");
        user.setPassword("ENCODED-PASS");

        when(userRepository.findByUsername("adam")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(eq("1234"), anyString())).thenReturn(true);
    }

    @Test
    void shouldIssueTokensOnValidLogin() throws Exception {
        LoginRequest req = new LoginRequest("adam", "1234");

        mockMvc.perform(
                        post("/auth")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(req))
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.access_token").exists())
                .andExpect(jsonPath("$.refresh_token").exists());
    }

    @Test
    void shouldRejectInvalidLogin() throws Exception {
        when(passwordEncoder.matches(eq("bad"), anyString())).thenReturn(false);
        LoginRequest req = new LoginRequest("adam", "bad");

        mockMvc.perform(
                        post("/auth")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(req))
                )
                .andDo(print())
                .andExpect(status().is4xxClientError());
    }
}
