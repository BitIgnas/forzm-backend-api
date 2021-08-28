package org.forzm.demo.controller;

import org.forzm.demo.service.VerificationService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
public class VerificationControllerTest {

    @MockBean
    private VerificationService verificationService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void verifyAccount() throws Exception {
        doNothing().when(verificationService).verifyUser(anyString());

        verificationService.verifyUser("TOKEN");

        MvcResult result = mockMvc.perform(get("/api/verify/{token}", "TOKEN")
                .accept(APPLICATION_JSON)
                .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        verify(verificationService, times(2)).verifyUser(anyString());
        verifyNoMoreInteractions(verificationService);

        assertThat(result).isNotNull();
        assertThat(result.getResponse().getContentAsString()).isEqualTo("User activated");
    }
}