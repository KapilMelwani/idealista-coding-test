package com.idealista;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class IntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithMockUser(username = "qualityAdmin",password = "1234")
    public void calculateScores() throws Exception {
        this.mockMvc
            .perform(put("/api/ads/calculateScores"))
            .andDo(print()).andExpect(status().isOk());
        this.mockMvc
            .perform(get("/api/ads/quality"))
            .andDo(print()).andExpect(status().isOk());
        this.mockMvc
            .perform(get("/api/ads/public"))
            .andDo(print()).andExpect(status().isOk());
    }
}