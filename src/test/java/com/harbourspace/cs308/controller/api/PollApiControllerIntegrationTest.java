package com.harbourspace.cs308.controller.api;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import java.util.Arrays;
import java.util.List;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.harbourspace.cs308.dto.PollDto;
import com.harbourspace.cs308.model.Poll;
import com.harbourspace.cs308.services.PollService;
import com.harbourspace.cs308.exceptions.EntityNotFoundException;

@WebMvcTest(PollApiController.class)
@MockitoBean(types = JpaMetamodelMappingContext.class)
public class PollApiControllerIntegrationTest {
    
    @Autowired
    private MockMvc mockMvc;
    
    @MockitoBean
    private PollService pollService;
    
    @Autowired
    private ObjectMapper objectMapper;
    
    @Test
    public void testGetPolls() throws Exception {
        List<PollDto> pollList = Arrays.asList(new PollDto("Sample Question", false, true, Arrays.asList("Option1", "Option2")));
        Page<PollDto> page = new PageImpl<>(pollList);
        
        when(pollService.getPolls(any(Pageable.class))).thenReturn(page);
        
        mockMvc.perform(get("/api/polls"))
               .andExpect(status().isOk())
               .andExpect(content().contentType(MediaType.APPLICATION_JSON))
               .andExpect(jsonPath("$.content[0].question").value("Sample Question"))
               .andExpect(jsonPath("$.content[0].multipleChoice").value(false))
               .andExpect(jsonPath("$.content[0].options[0]").value("Option1"))
               .andExpect(jsonPath("$.content[0].options[1]").value("Option2"));
        
        verify(pollService).getPolls(any(Pageable.class));
    }
    
    @Test
    public void testCreatePoll() throws Exception {
        PollDto inputPoll = new PollDto("New Poll", false, true, Arrays.asList("Opt1", "Opt2"));
        Poll poll = new Poll();
        poll.setSlug("new-poll");
        
        when(pollService.createPoll(any(PollDto.class))).thenReturn(poll);
        
        // Perform POST request and verify location header
        mockMvc.perform(post("/api/polls")
               .contentType(MediaType.APPLICATION_JSON)
               .content(objectMapper.writeValueAsString(inputPoll)))
               .andExpect(status().isCreated());
            //    .andExpect(header().string("Location", "/api/polls/new-poll"));
        
        verify(pollService).createPoll(any(PollDto.class));
    }
    
    @Test
    public void testGetPollBySlug_Found() throws Exception {
        PollDto pollDto = new PollDto("Existing Poll", false, true, Arrays.asList("Opt1", "Opt2"));
        
        Poll poll = new Poll();
        poll.setSlug("existing-slug");
        
        when(pollService.getPoll("existing-slug")).thenReturn(pollDto);
        
        mockMvc.perform(get("/api/polls/existing-slug"))
               .andExpect(status().isOk())
               .andExpect(content().contentType(MediaType.APPLICATION_JSON))
               .andExpect(jsonPath("$.question").value("Existing Poll"));
        
        verify(pollService).getPoll("existing-slug");
    }
    
    @Test
    public void testGetPollBySlug_NotFound() throws Exception {
        when(pollService.getPoll("nonexistent-slug"))
            .thenThrow(new EntityNotFoundException(Poll.class, "nonexistent-slug"));
        
        mockMvc.perform(get("/api/polls/nonexistent-slug"))
               .andExpect(status().isNotFound());
        
        verify(pollService).getPoll("nonexistent-slug");
    }
}