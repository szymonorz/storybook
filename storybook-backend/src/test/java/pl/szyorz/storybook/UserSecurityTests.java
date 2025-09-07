package pl.szyorz.storybook;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import pl.szyorz.storybook.config.JWTConfig;
import pl.szyorz.storybook.entity.book.Book;
import pl.szyorz.storybook.entity.book.BookRepository;
import pl.szyorz.storybook.entity.book.data.UpdateBookRequest;
import pl.szyorz.storybook.entity.chapter.ChapterRepository;
import pl.szyorz.storybook.entity.user.DetailsService;
import pl.szyorz.storybook.entity.user.User;
import pl.szyorz.storybook.entity.user.UserRepository;
import pl.szyorz.storybook.entity.user.data.UpdateUserRequest;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static pl.szyorz.storybook.SecurityTestUtils.jwtFor;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc(addFilters = true)
class UserSecurityTests {

    @Autowired
    MockMvc mockMvc;
    @MockitoBean
    UserRepository userRepository;
    @MockitoBean
    BookRepository bookRepository;
    @MockitoBean
    ChapterRepository chapterRepository;
    @MockitoBean
    private DetailsService detailsService;
    @Autowired
    private JWTConfig jwtConfig;

    private final ObjectMapper mapper = new ObjectMapper();

    @Test
    void selfUpdateAllowed() throws Exception {
        UUID id = UUID.randomUUID();

        User dbUser = new User();
        dbUser.setId(id);
        dbUser.setUsername("adam");
        dbUser.setEmail("old@mail.tld");

        when(userRepository.findById(eq(id))).thenReturn(Optional.of(dbUser));
        when(userRepository.existsByUsername("adam2")).thenReturn(false);
        when(userRepository.existsByEmail("new@mail.tld")).thenReturn(false);
        when(userRepository.save(any(User.class))).thenAnswer(inv -> inv.getArgument(0));

        UserDetails authUser = new org.springframework.security.core.userdetails.User(
                "adam", "N/A", List.of());
        when(detailsService.loadUserByUsername("adam")).thenReturn(authUser);

        UpdateUserRequest body = new UpdateUserRequest("adam2", "new@mail.tld", null);

        mockMvc.perform(
                        patch("/api/user/{id}", id)
                                .header("Authorization", jwtConfig.getPrefix() + jwtFor(jwtConfig,"adam"))
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(body))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value(id.toString()))
                .andExpect(jsonPath("$.username").value("adam2"));
    }

    @Test
    void otherUserForbidden() throws Exception {
        UUID id = UUID.randomUUID();

        User dbUser = new User();
        dbUser.setId(id);
        dbUser.setUsername("owner");
        when(userRepository.findById(eq(id))).thenReturn(Optional.of(dbUser));

        UpdateUserRequest body = new UpdateUserRequest("xxx", null, null);

        UserDetails intruder = new org.springframework.security.core.userdetails.User(
                "intruder", "N/A", List.of(new SimpleGrantedAuthority("VIEW")));
        when(detailsService.loadUserByUsername("intruder")).thenReturn(intruder);

        mockMvc.perform(
                        patch("/api/user/{id}", id)
                                .header("Authorization", jwtConfig.getPrefix() + jwtFor(jwtConfig, "intruder"))
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(body))
                )
                .andExpect(status().isForbidden());
    }

    @Test
    void superuserAllowed() throws Exception {
        UUID id = UUID.randomUUID();

        User dbUser = new User();
        dbUser.setId(id);
        dbUser.setUsername("owner");
        when(userRepository.findById(eq(id))).thenReturn(Optional.of(dbUser));
        when(userRepository.existsByUsername("bossed")).thenReturn(false);
        when(userRepository.save(any(User.class))).thenAnswer(inv -> inv.getArgument(0));

        UserDetails boss = new org.springframework.security.core.userdetails.User(
                "boss", "N/A", List.of(new SimpleGrantedAuthority("SUPERUSER")));
        when(detailsService.loadUserByUsername("boss")).thenReturn(boss);

        UpdateUserRequest body = new UpdateUserRequest("bossed", null, null);

        mockMvc.perform(
                        patch("/api/user/{id}", id)
                                .header("Authorization", jwtConfig.getPrefix() + jwtFor(jwtConfig,"boss"))
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(body))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value(id.toString()))
                .andExpect(jsonPath("$.username").value("bossed"));
    }

    @Test
    void authorCanUpdate() throws Exception {
        UUID id = UUID.randomUUID();

        User author = new User();
        author.setUsername("adam");

        Book b = new Book();
        b.setId(id);
        b.setTitle("Old");
        b.setDescription("Old desc");
        b.setAuthor(author);

        when(bookRepository.findById(eq(id))).thenReturn(Optional.of(b));
        when(bookRepository.save(any(Book.class))).thenAnswer(inv -> inv.getArgument(0));

        UserDetails authUser = new org.springframework.security.core.userdetails.User(
                "adam", "N/A", List.of());
        when(detailsService.loadUserByUsername("adam")).thenReturn(authUser);

        UpdateBookRequest body = new UpdateBookRequest("New", "New desc");

        mockMvc.perform(patch("/api/book/{id}", id)
                        .header("Authorization", jwtConfig.getPrefix() + jwtFor(jwtConfig,"adam"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(body)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id.toString()))
                .andExpect(jsonPath("$.title").value("New"))
                .andExpect(jsonPath("$.description").value("New desc"));
    }
}
