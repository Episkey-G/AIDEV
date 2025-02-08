package com.episkey.aidev.ai;

import com.episkey.aidev.utils.ConfigManager;
import com.theokanning.openai.completion.chat.*;
import com.theokanning.openai.service.OpenAiService;
import lombok.extern.slf4j.Slf4j;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

/**
 * 封装AI请求
 */
@Slf4j
public class AIService {
    private final OpenAiService openAiService;
    private final String model;
    private final List<ChatMessage> conversationHistory;
    
    public AIService() {
        ConfigManager config = ConfigManager.getInstance();
        this.openAiService = new OpenAiService(config.getOpenAiApiKey(), 
            Duration.ofSeconds(config.getRequestTimeout()));
        this.model = config.getAiModel();
        this.conversationHistory = new ArrayList<>();
        
        // 添加系统提示
        addSystemPrompt();
    }
    
    private void addSystemPrompt() {
        String systemPrompt = """
            你是一个命令行助手，帮助用户将自然语言转换为shell命令。
            请遵循以下规则：
            1. 只输出实际的shell命令，不要包含解释
            2. 确保命令安全，避免危险操作
            3. 优先使用通用的POSIX兼容命令
            4. 如果需要多个命令，用分号分隔
            """;
        
        conversationHistory.add(new ChatMessage("system", systemPrompt));
    }
    
    public String getCommand(String userInput) {
        try {
            conversationHistory.add(new ChatMessage("user", userInput));
            
            ChatCompletionRequest request = ChatCompletionRequest.builder()
                .model(model)
                .messages(conversationHistory)
                .temperature(0.7)
                .build();
            
            ChatMessage response = openAiService.createChatCompletion(request)
                .getChoices().get(0).getMessage();
            
            conversationHistory.add(response);
            return response.getContent().trim();
            
        } catch (Exception e) {
            log.error("AI服务调用失败: ", e);
            throw new RuntimeException("AI服务调用失败", e);
        }
    }
    
    public void clearHistory() {
        conversationHistory.clear();
        addSystemPrompt();
    }
}
