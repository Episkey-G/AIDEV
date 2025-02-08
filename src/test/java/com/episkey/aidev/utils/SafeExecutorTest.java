package com.episkey.aidev.utils;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class SafeExecutorTest {
    private SafeExecutor executor;

    @BeforeEach
    void setUp() {
        executor = new SafeExecutor();
    }

    @Test
    void executeSimpleCommand() {
        SafeExecutor.ExecutionResult result = executor.execute("echo 'hello world'");
        assertEquals(0, result.getExitCode());
        assertEquals("hello world", result.getOutput().trim());
        assertTrue(result.getError().isEmpty());
        assertFalse(result.isTimeout());
    }

    @Test
    void handleCommandNotFound() {
        SafeExecutor.ExecutionResult result = executor.execute("nonexistentcommand");
        assertNotEquals(0, result.getExitCode());
        assertFalse(result.getError().isEmpty());
    }

    @Test
    void handleTimeout() {
        SafeExecutor.ExecutionResult result = executor.execute("sleep 5", 1);
        assertTrue(result.isTimeout());
        assertEquals(-1, result.getExitCode());
    }

    @Test
    void handleMultilineOutput() {
        SafeExecutor.ExecutionResult result = executor.execute("echo 'line1\nline2'");
        assertEquals(0, result.getExitCode());
        assertEquals("line1\nline2", result.getOutput().trim());
    }
} 