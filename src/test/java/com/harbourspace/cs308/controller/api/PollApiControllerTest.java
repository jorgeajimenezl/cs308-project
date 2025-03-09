package com.harbourspace.cs308.controller.api;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.harbourspace.cs308.dto.PollDto;
import com.harbourspace.cs308.model.Poll;
import com.harbourspace.cs308.services.PollService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.net.URI;
import java.util.Arrays;

public class PollApiControllerTest {

    @Mock
    private PollService pollService;

    @InjectMocks
    private PollApiController pollApiController;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetPolls() {
        // Arrange
        PollDto pollDto = new PollDto("Test question", false, true, Arrays.asList("Option1", "Option2"));
        Page<PollDto> page = new PageImpl<>(Arrays.asList(pollDto), PageRequest.of(0, 10), 1);
        when(pollService.getPolls(any())).thenReturn(page);

        // Act
        ResponseEntity<Page<PollDto>> response = pollApiController.getPolls(PageRequest.of(0, 10));

        // Assert
        assertNotNull(response);
        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().getTotalElements());
        verify(pollService).getPolls(any());
    }

    @Test
    void testCreatePoll() {
        // Arrange
        PollDto pollDto = new PollDto("Test poll", false, true, Arrays.asList("Option1", "Option2"));
        Poll poll = new Poll();
        poll.setSlug("testSlug");
        when(pollService.createPoll(any(PollDto.class))).thenReturn(poll);

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRequestURI("/api/polls");
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
        
        // Act
        ResponseEntity<Void> response = pollApiController.createPoll(pollDto);

        // Assert
        assertNotNull(response);
        assertEquals(201, response.getStatusCode().value());
        URI location = response.getHeaders().getLocation();
        assertNotNull(location);
        assertTrue(location.toString().endsWith("/testSlug"));
        verify(pollService).createPoll(any(PollDto.class));

        RequestContextHolder.resetRequestAttributes();
    }

    @Test
    void testGetPoll() {
        // Arrange
        String slug = "testSlug";
        PollDto pollDto = new PollDto("Test poll", false, true, Arrays.asList("Option1", "Option2"));
        when(pollService.getPoll(slug)).thenReturn(pollDto);

        // Act
        ResponseEntity<PollDto> response = pollApiController.getPoll(slug);

        // Assert
        assertNotNull(response);
        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals("Test poll", response.getBody().getQuestion());
        verify(pollService).getPoll(slug);
    }
}