package com.harbourspace.cs308.controller.api;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import com.harbourspace.cs308.exception.FieldValidationException;
import com.harbourspace.cs308.model.EmailSubscriber;
import com.harbourspace.cs308.repository.EmailSubscriberRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

public class EmailSubscriptionApiControllerTest {

    private EmailSubscriberRepository repository;
    private EmailSubscriptionApiController controller;

    @BeforeEach
    public void setUp() {
        repository = mock(EmailSubscriberRepository.class);
        controller = new EmailSubscriptionApiController(repository);
    }

    @Test
    public void testSubscribeApi_Success() {
        // Arrange
        EmailSubscriber subscriber = new EmailSubscriber();
        subscriber.setEmail("test@example.com");
        when(repository.existsByEmail("test@example.com")).thenReturn(false);

        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getRemoteAddr()).thenReturn("127.0.0.1");

        // Act
        ResponseEntity<?> response = controller.subscribeApi(subscriber, request);

        // Assert
        assertEquals(200, response.getStatusCode().value());
        verify(repository).save(subscriber);
        assertEquals("127.0.0.1", subscriber.getIpAddress());
    }

    @Test
    public void testSubscribeApi_DuplicateEmail() {
        // Arrange
        EmailSubscriber subscriber = new EmailSubscriber();
        subscriber.setEmail("duplicate@example.com");
        when(repository.existsByEmail("duplicate@example.com")).thenReturn(true);

        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getRemoteAddr()).thenReturn("127.0.0.1");

        // Act & Assert
        FieldValidationException exception = assertThrows(FieldValidationException.class, () -> {
            controller.subscribeApi(subscriber, request);
        });
        assertEquals("This email has already been registered.", exception.getMessage());
    }
}