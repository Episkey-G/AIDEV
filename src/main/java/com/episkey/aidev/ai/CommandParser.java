package com.episkey.aidev.ai;

import lombok.extern.slf4j.Slf4j;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 解析AI响应
 */
@Slf4j
public class CommandParser {
    private static final Pattern COMMAND_PATTERN = Pattern.compile("```(?:shell|bash)?\\s*([\\s\\S]*?)```|`([^`]+)`");
    
    public List<String> parseCommands(String aiResponse) {
        List<String> commands = new ArrayList<>();
        Matcher matcher = COMMAND_PATTERN.matcher(aiResponse);
        
        while (matcher.find()) {
            String command = matcher.group(1) != null ? matcher.group(1) : matcher.group(2);
            if (command != null && !command.trim().isEmpty()) {
                commands.add(command.trim());
            }
        }
        
        // 如果没有找到代码块格式的命令，尝试直接解析整个响应
        if (commands.isEmpty() && !aiResponse.trim().isEmpty()) {
            commands.add(aiResponse.trim());
        }
        
        return commands;
    }
}
