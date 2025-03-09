package com.harbourspace.cs308.controller.api;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class PollApiControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testGetPolls() throws Exception {
        String pollJson = "{\"question\":\"Test Poll?\", \"options\":[\"Option1\", \"Option2\"], \"isPublic\":true, \"multipleChoice\":false}";

        mockMvc.perform(post("/api/polls")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(pollJson))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", notNullValue()));

        mockMvc.perform(get("/api/polls"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content", hasSize(1)));
    }

    @Test
    public void testCreateAndGetPoll() throws Exception {
        String pollJson = "{\"question\":\"Another Test Poll?\", \"options\":[\"Yes\", \"No\"], \"isPublic\":true, \"multipleChoice\":false}";

        MvcResult result = mockMvc.perform(post("/api/polls")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(pollJson))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andReturn();

        String location = result.getResponse().getHeader("Location");
        assertThat(location).isNotNull();

        String[] segments = location.split("/");
        String slug = segments[segments.length - 1];
        assertThat(slug).isNotEmpty();

        mockMvc.perform(get("/api/polls/" + slug))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.question").value("Another Test Poll?"));
                // .andExpect(jsonPath("$.options", hasSize(2)))
                // .andExpect(jsonPath("$.options[0]").value("Yes"))
                // .andExpect(jsonPath("$.options[1]").value("No"));
    }
}