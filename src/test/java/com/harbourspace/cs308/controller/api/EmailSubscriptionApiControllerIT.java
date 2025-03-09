package com.harbourspace.cs308.controller.api;

import com.harbourspace.cs308.model.EmailSubscriber;
import com.harbourspace.cs308.repository.EmailSubscriberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class EmailSubscriptionApiControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private EmailSubscriberRepository repository;

    @BeforeEach
    public void setUp() {
        repository.deleteAll();
    }

    @Test
    public void testSubscribeApi_Success() throws Exception {
        String jsonPayload = "{\"email\":\"test@integration.com\"}";

        mockMvc.perform(post("/api/email-subscribe")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonPayload))
                .andExpect(status().isOk());

        EmailSubscriber subscriber = repository.findByEmail("test@integration.com");
        assertThat(subscriber).isNotNull();
        assertThat(subscriber.getIpAddress()).isEqualTo("127.0.0.1");
    }

    @Test
    public void testSubscribeApi_DuplicateEmail() throws Exception {
        EmailSubscriber existingSubscriber = new EmailSubscriber();
        existingSubscriber.setEmail("duplicate@integration.com");
        repository.save(existingSubscriber);

        String jsonPayload = "{\"email\":\"duplicate@integration.com\"}";

        mockMvc.perform(post("/api/email-subscribe")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonPayload))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("already been registered")));
    }
}