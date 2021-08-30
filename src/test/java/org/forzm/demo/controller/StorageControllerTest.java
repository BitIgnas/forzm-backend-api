package org.forzm.demo.controller;

import org.forzm.demo.service.StorageService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
public class StorageControllerTest {

    @MockBean
    private StorageService storageService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void uploadUserFile() throws Exception {
        MockMultipartFile jpegFile = new MockMultipartFile("test.jpeg", "", "image/jpeg", "{\"key1\": \"value1\"}".getBytes());
        doNothing().when(storageService).saveUserProfile(any(MultipartFile.class), anyString());

        storageService.saveUserProfile(jpegFile, "USER");

        MvcResult mockMvcResult = mockMvc.perform(multipart("/api/storage/user/{username}/upload", "USER")
                .file("file", jpegFile.getBytes())
                .contentType(MediaType.IMAGE_JPEG))
                .andExpect(status().isCreated())
                .andReturn();

        verify(storageService, times(2)).saveUserProfile(any(MultipartFile.class), anyString());
        verifyNoMoreInteractions(storageService);

        assertThat(mockMvcResult).isNotNull();
        assertThat(mockMvcResult.getResponse().getContentAsString()).isEqualTo("Image saved");
    }

    @Test
    void uploadForumFile() throws Exception {
        MockMultipartFile jpegFile = new MockMultipartFile("test.jpeg", "", "image/jpeg", "{\"key1\": \"value1\"}".getBytes());
        doNothing().when(storageService).saveForumImage(any(MultipartFile.class), anyString());

        storageService.saveForumImage(jpegFile, "USER");

        MvcResult mockMvcResult = mockMvc.perform(multipart("/api/storage/forum/{forumName}/upload", "USER_FORUM")
                .file("file", jpegFile.getBytes())
                .contentType(MediaType.IMAGE_JPEG))
                .andExpect(status().isCreated())
                .andReturn();

        verify(storageService, times(2)).saveForumImage(any(MultipartFile.class), anyString());
        verifyNoMoreInteractions(storageService);

        assertThat(mockMvcResult).isNotNull();
        assertThat(mockMvcResult.getResponse().getContentAsString()).isEqualTo("Image saved");
    }
}