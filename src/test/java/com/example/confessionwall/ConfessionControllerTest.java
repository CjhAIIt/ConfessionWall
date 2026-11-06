package com.example.confessionwall;
import com.example.confessionwall.Controller.ConfessionController;
import com.example.confessionwall.Service.ConfessionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
class ConfessionControllerTest {
    ConfessionService service;
    ConfessionController controller;
    @BeforeEach void setUp() {
        service = mock(ConfessionService.class);
        controller = new ConfessionController();
        ReflectionTestUtils.setField(controller, "confessionService", service);
    }
    @Test void blankAndOversizedInputIsRejectedBeforePersistence() {
        assertEquals(400, controller.addConfession(" ", "message").getStatusCode().value());
        assertEquals(400, controller.addConfession("a".repeat(101), "message").getStatusCode().value());
        assertEquals(400, controller.addConfession("user", "a".repeat(2001)).getStatusCode().value());
        verifyNoInteractions(service);
    }
    @Test void validInputIsPublished() {
        assertEquals(200, controller.addConfession("user", "message").getStatusCode().value());
        verify(service).addConfession(any());
    }
    @Test void invalidQueryLimitIsRejected() {
        assertEquals(400, controller.getRecentConfessions(0).getStatusCode().value());
        assertEquals(400, controller.getRecentConfessions(101).getStatusCode().value());
        verifyNoInteractions(service);
    }
    @Test void databaseExceptionsDoNotLeakThroughResponses() {
        doThrow(new RuntimeException("private-database-detail")).when(service).addConfession(any());
        when(service.getRecentConfessions(10)).thenThrow(new RuntimeException("private-database-detail"));
        var post = controller.addConfession("user", "message");
        var get = controller.getRecentConfessions(10);
        assertEquals(500, post.getStatusCode().value());
        assertEquals(500, get.getStatusCode().value());
        assertFalse(post.getBody().toString().contains("private-database-detail"));
        assertFalse(get.getBody().toString().contains("private-database-detail"));
    }
}
