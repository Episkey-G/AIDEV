package com.episkey.aidev.ai;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class CommandParserTest {
    private CommandParser parser;

    @BeforeEach
    void setUp() {
        parser = new CommandParser();
    }

    @Test
    void parseCommandsFromMarkdownCodeBlock() {
        String aiResponse = """
            ```bash
            ls -la
            ```""";
        List<String> commands = parser.parseCommands(aiResponse);
        assertEquals(1, commands.size());
        assertEquals("ls -la", commands.get(0));
    }

    @Test
    void parseCommandsFromInlineCode() {
        String aiResponse = "使用 `pwd` 命令查看当前目录";
        List<String> commands = parser.parseCommands(aiResponse);
        assertEquals(1, commands.size());
        assertEquals("pwd", commands.get(0));
    }

    @Test
    void parseMultipleCommands() {
        String aiResponse = """
            ```shell
            cd /tmp
            ls -l
            ```
            然后使用 `pwd` 确认位置""";
        List<String> commands = parser.parseCommands(aiResponse);
        assertEquals(3, commands.size());
        assertEquals("cd /tmp", commands.get(0));
        assertEquals("ls -l", commands.get(1));
        assertEquals("pwd", commands.get(2));
    }

    @Test
    void handleEmptyResponse() {
        String aiResponse = "";
        List<String> commands = parser.parseCommands(aiResponse);
        assertTrue(commands.isEmpty());
    }

    @Test
    void handlePlainTextResponse() {
        String aiResponse = "ls -la";
        List<String> commands = parser.parseCommands(aiResponse);
        assertEquals(1, commands.size());
        assertEquals("ls -la", commands.get(0));
    }
} 