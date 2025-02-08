package com.episkey.aidev.core;

import lombok.extern.slf4j.Slf4j;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * 命令安全检查
 */
@Slf4j
public class SecurityValidator {
    private static final Set<String> DANGEROUS_COMMANDS = new HashSet<>(Arrays.asList(
        "rm", "chmod", "chown", "dd", "mkfs", "sudo", "su"
    ));
    
    private static final Set<String> DANGEROUS_PATTERNS = new HashSet<>(Arrays.asList(
        "&&", "||", "|", ">", ">>", "<", "<<", ";", "`"
    ));
    
    private static final Pattern PATH_TRAVERSAL = Pattern.compile("\\.\\./|/\\.\\.");
    
    public boolean validateCommand(String command) {
        if (command == null || command.trim().isEmpty()) {
            return false;
        }
        
        String[] parts = command.split("\\s+");
        String baseCommand = parts[0];
        
        // 检查危险命令
        if (DANGEROUS_COMMANDS.contains(baseCommand)) {
            log.warn("检测到危险命令: {}", baseCommand);
            return false;
        }
        
        // 检查危险模式
        for (String pattern : DANGEROUS_PATTERNS) {
            if (command.contains(pattern)) {
                log.warn("检测到危险字符: {}", pattern);
                return false;
            }
        }
        
        // 检查路径穿越
        if (PATH_TRAVERSAL.matcher(command).find()) {
            log.warn("检测到路径穿越尝试");
            return false;
        }
        
        return true;
    }
}
