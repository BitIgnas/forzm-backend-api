package org.forzm.demo.controller;

import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
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
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.oauth2.resource.OAuth2ResourceServerProperties;
import org.springframework.boot.test.autoconfigure.OverrideAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import static org.mockito.Mockito.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import java.util.*;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
public class ForumControllerTest {

    @MockBean
    private ForumService forumService;

    @Autowired
    protected MockMvc mockMvc;

    @Test
    void createForum() throws Exception {
        ForumRequestDto forumRequestDto = new ForumRequestDto("FORUM_TEST", ForumGameType.FPS, "FORUM_DESC");
        ForumResponseDto forumResponseDto = new ForumResponseDto("FORUM_TEST", "FORUM_DESC", ForumGameType.FPS, null, null);

        when(forumService.createForum(forumRequestDto)).thenReturn(forumResponseDto);

        mockMvc.perform(post("/api/forum/save", forumRequestDto)
                .accept(APPLICATION_JSON)
                .contentType(APPLICATION_JSON)
                .content("{\"name\": \"FORUM_TEST\", \"forumGameType\":\"FPS\", \"description\": \"FORUM_DESC\"}"))
                .andExpect(status().isCreated());

        verify(forumService, times(1)).createForum(any(ForumRequestDto.class));
        verifyNoMoreInteractions(forumService);
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
    void getForumByName() throws Exception {
        ForumResponseDto forumResponseDto = new ForumResponseDto("FORUM_TEST", "FORUM DESC", ForumGameType.ADVENTURE, null, null);


        when(forumService.findForumByName(anyString())).thenReturn(forumResponseDto);

        mockMvc.perform(get("/api/forum/{forumName}", "ANY_FORUM")
                .accept(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(forumResponseDto.getName()))
                .andExpect(jsonPath("$.description").value(forumResponseDto.getDescription()))
                .andExpect(jsonPath("$.forumGameType").value(forumResponseDto.getForumGameType().name()));

        verify(forumService, times(1)).findForumByName(anyString());
        verifyNoMoreInteractions(forumService);

    }

    @Test
    void searchForumsByName() throws Exception {
        ForumResponseDto forumResponseDto1 = new ForumResponseDto("FORUM_TEST", "FORUM DESC", ForumGameType.FPS, null, null);
        ForumResponseDto forumResponseDto2 = new ForumResponseDto("FORUM_TEST2", "FORUM DESC2", ForumGameType.ADVENTURE, null, null);
        List<ForumResponseDto> forums = Arrays.asList(forumResponseDto1, forumResponseDto2);

        when(forumService.findForumByNameIgnoreCase(anyString())).thenReturn(forums);

        mockMvc.perform(get("/api/forum/{forumName}/search", "ANY_FORUM"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$[0].name", Matchers.is("FORUM_TEST")))
                .andExpect(jsonPath("$[0].description", Matchers.is("FORUM DESC")))
                .andExpect(jsonPath("$[0].forumGameType", Matchers.is(ForumGameType.FPS.name())))
                .andExpect(jsonPath("$[1].name", Matchers.is("FORUM_TEST2")))
                .andExpect(jsonPath("$[1].description", Matchers.is("FORUM DESC2")))
                .andExpect(jsonPath("$[1].forumGameType", Matchers.is(ForumGameType.ADVENTURE.name())));

        verify(forumService, times(1)).findForumByNameIgnoreCase(anyString());
        verifyNoMoreInteractions(forumService);

    }

    @Test
    void findUserForumsByUsername() throws Exception {
        ForumResponseDto forumResponseDto1 = new ForumResponseDto("FORUM_TEST", "FORUM DESC", ForumGameType.FPS, null, null);
        ForumResponseDto forumResponseDto2 = new ForumResponseDto("FORUM_TEST2", "FORUM DESC2", ForumGameType.ADVENTURE, null, null);
        List<ForumResponseDto> forums = Arrays.asList(forumResponseDto1, forumResponseDto2);

        when(forumService.findUserForumsByUsername(anyString())).thenReturn(forums);

        mockMvc.perform(get("/api/forum/{username}/forums", "USERNAME"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$[0].name", Matchers.is("FORUM_TEST")))
                .andExpect(jsonPath("$[0].description", Matchers.is("FORUM DESC")))
                .andExpect(jsonPath("$[0].forumGameType", Matchers.is(ForumGameType.FPS.name())))
                .andExpect(jsonPath("$[1].name", Matchers.is("FORUM_TEST2")))
                .andExpect(jsonPath("$[1].description", Matchers.is("FORUM DESC2")))
                .andExpect(jsonPath("$[1].forumGameType", Matchers.is(ForumGameType.ADVENTURE.name())));

        verify(forumService, times(1)).findUserForumsByUsername(anyString());
        verifyNoMoreInteractions(forumService);
    }

    @Test
    void deleteForum() throws Exception {
        doNothing().when(forumService).deleteForum(anyString());

        mockMvc.perform(delete("/api/forum/{forumName}/delete", "FORUM_NAME")
                .contentType(APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(forumService, times(1)).deleteForum(anyString());
        verifyNoMoreInteractions(forumService);
    }
}