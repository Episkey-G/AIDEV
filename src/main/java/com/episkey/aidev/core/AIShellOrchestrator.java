package com.episkey.aidev.core;

import com.episkey.aidev.ai.AIService;
import com.episkey.aidev.ai.CommandParser;
import com.episkey.aidev.utils.SafeExecutor;
import lombok.extern.slf4j.Slf4j;
import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;

/**
 * 协调AI与命令执行
 */
@Slf4j
public class AIShellOrchestrator {
    private final AIService aiService;
    private final CommandParser commandParser;
    private final SecurityValidator securityValidator;
    private final SafeExecutor safeExecutor;
    
    public AIShellOrchestrator() {
        this.aiService = new AIService();
        this.commandParser = new CommandParser();
        this.securityValidator = new SecurityValidator();
        this.safeExecutor = new SafeExecutor();
    }
    
    public void startInteractiveShell() {
        try {
            Terminal terminal = TerminalBuilder.builder()
                .system(true)
                .build();
                
            LineReader lineReader = LineReaderBuilder.builder()
                .terminal(terminal)
                .build();
            
            while (true) {
                String input = lineReader.readLine("aidev> ");
                
                if ("exit".equalsIgnoreCase(input) || "quit".equalsIgnoreCase(input)) {
                    break;
                }
                
                processUserInput(input);
            }
        } catch (Exception e) {
            log.error("交互式Shell启动失败: ", e);
            throw new RuntimeException(e);
        }
    }
    
    public void processUserInput(String input) {
        try {
            // 获取AI响应
            String aiResponse = aiService.getCommand(input);
            
            // 解析命令
            for (String command : commandParser.parseCommands(aiResponse)) {
                // 安全检查
                if (!securityValidator.validateCommand(command)) {
                    System.out.println("⚠️ 安全警告：命令被拦截: " + command);
                    continue;
                }
                
                // 执行命令
                SafeExecutor.ExecutionResult result = safeExecutor.execute(command);
                
                // 输出结果
                if (result.getExitCode() == 0) {
                    System.out.println("✅ 执行成功:");
                    if (!result.getOutput().isEmpty()) {
                        System.out.println(result.getOutput());
                    }
                } else {
                    System.out.println("❌ 执行失败:");
                    if (!result.getError().isEmpty()) {
                        System.out.println(result.getError());
                    }
                }
            }
        } catch (Exception e) {
            log.error("命令处理失败: ", e);
            System.out.println("❌ 错误: " + e.getMessage());
        }
    }
}
