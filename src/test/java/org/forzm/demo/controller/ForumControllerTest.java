package org.forzm.demo.controller;

import org.checkerframework.checker.units.qual.A;
import org.forzm.demo.config.SecurityConfig;
import org.forzm.demo.dto.ForumRequestDto;
import org.forzm.demo.dto.ForumResponseDto;
import org.forzm.demo.model.Forum;
import org.forzm.demo.model.ForumGameType;
import org.forzm.demo.security.JwtProvider;
import org.forzm.demo.service.ForumService;
import org.forzm.demo.service.impl.ForumServiceImpl;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.OverrideAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.test.web.servlet.MockMvc;
import static org.mockito.Mockito.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Arrays;
import java.util.List;

@SpringBootTest
@AutoConfigureMockMvc
public class ForumControllerTest {

    @MockBean
    private ForumServiceImpl forumService;

    @Autowired
    protected MockMvc mockMvc;

    @MockBean
    private JwtProvider jwtProvider;

    @Test
    void createForum() throws Exception {
        ForumRequestDto forumRequestDto = new ForumRequestDto("FORUM_TEST", ForumGameType.FPS, "FORUM_DESC");
        ForumResponseDto forumResponseDto = new ForumResponseDto("FORUM_TEST", "FORUM_DESC", ForumGameType.FPS, null, null);

        when(forumService.createForum(forumRequestDto)).thenReturn(forumResponseDto);
        when(jwtProvider.generateJwt(any(Authentication.class))).thenReturn("JWT-TOKEN");

        String jwt = jwtProvider.generateJwt(new UsernamePasswordAuthenticationToken("test", "test"));
        mockMvc.perform(post("/api/forum/create", forumRequestDto)
                .header("Authorization", jwt)
                .contentType(APPLICATION_JSON)
                .content("{\"name\": \"FORUM_TEST\", \"forumGameType\":\"FPS\", \"description\": \"FORUM_DESC\"}"))
                .andExpect(status().isCreated());

    }

    @Test
    void getAllForums() throws Exception {
        ForumResponseDto forumResponseDto1 = new ForumResponseDto("FORUM_TEST", "FORUM DESC", ForumGameType.FPS, null, null);
        ForumResponseDto forumResponseDto2 = new ForumResponseDto("FORUM_TEST2", "FORUM DESC2", ForumGameType.ADVENTURE, null, null);
        List<ForumResponseDto> forums = Arrays.asList(forumResponseDto1, forumResponseDto2);

        when(forumService.getAllForums()).thenReturn(forums);

        mockMvc.perform(get("/api/forum/all"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$[0].name", Matchers.is("FORUM_TEST")))
                .andExpect(jsonPath("$[0].description", Matchers.is("FORUM DESC")))
                .andExpect(jsonPath("$[0].forumGameType", Matchers.is(ForumGameType.FPS.name())))
                .andExpect(jsonPath("$[1].name", Matchers.is("FORUM_TEST2")))
                .andExpect(jsonPath("$[1].description", Matchers.is("FORUM DESC2")))
                .andExpect(jsonPath("$[1].forumGameType", Matchers.is(ForumGameType.ADVENTURE.name())));

        verify(forumService, times(1)).getAllForums();
        verifyNoMoreInteractions(forumService);

    }

    @Test
    void getForumByName() {
    }

    @Test
    void searchForumsByName() {
    }

    @Test
    void findUserForumsByUsername() {
    }

    @Test
    void deleteForum() {
    }
}