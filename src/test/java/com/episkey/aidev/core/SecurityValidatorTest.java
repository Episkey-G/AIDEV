package com.episkey.aidev.core;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

class SecurityValidatorTest {
    private SecurityValidator validator;

    @BeforeEach
    void setUp() {
        validator = new SecurityValidator();
    }

    @Test
    void allowSafeCommands() {
        assertTrue(validator.validateCommand("ls -la"));
        assertTrue(validator.validateCommand("pwd"));
        assertTrue(validator.validateCommand("echo 'hello'"));
        assertTrue(validator.validateCommand("cat file.txt"));
    }

    @ParameterizedTest
    @ValueSource(strings = {
        "rm -rf /",
        "chmod 777 /",
        "sudo reboot",
        "dd if=/dev/zero",
        "mkfs.ext4 /dev/sda"
    })
    void blockDangerousCommands(String command) {
        assertFalse(validator.validateCommand(command));
    }

    @ParameterizedTest
    @ValueSource(strings = {
        "ls && rm",
        "echo hello | rm",
        "ls || rm",
        "ls > /etc/passwd",
        "ls >> /etc/shadow",
        "cat < /etc/shadow",
        "bash -c 'rm -rf /'"
    })
    void blockDangerousPatterns(String command) {
        assertFalse(validator.validateCommand(command));
    }

    @Test
    void blockPathTraversal() {
        assertFalse(validator.validateCommand("ls ../../../etc/passwd"));
        assertFalse(validator.validateCommand("cat /var/www/../../../etc/shadow"));
    }

    @Test
    void handleNullAndEmptyInput() {
        assertFalse(validator.validateCommand(null));
        assertFalse(validator.validateCommand(""));
        assertFalse(validator.validateCommand("   "));
    }
} 