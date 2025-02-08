package com.episkey.aidev.utils;

import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.concurrent.*;

/**
 * 安全执行封装
 */
@Slf4j
public class SafeExecutor {
    private static final int DEFAULT_TIMEOUT = 30;
    
    @Data
    @Builder
    public static class ExecutionResult {
        private int exitCode;
        private String output;
        private String error;
        private boolean timeout;
    }
    
    public ExecutionResult execute(String command) {
        return execute(command, DEFAULT_TIMEOUT);
    }
    
    public ExecutionResult execute(String command, int timeoutSeconds) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Process process = null;
        
        try {
            ProcessBuilder processBuilder = new ProcessBuilder();
            if (System.getProperty("os.name").toLowerCase().contains("windows")) {
                processBuilder.command("cmd.exe", "/c", command);
            } else {
                processBuilder.command("sh", "-c", command);
            }
            
            process = processBuilder.start();

            Process finalProcess = process;
            Future<ExecutionResult> future = executor.submit(() -> {
                StringBuilder output = new StringBuilder();
                StringBuilder error = new StringBuilder();
                
                try (BufferedReader stdInput = new BufferedReader(new InputStreamReader(finalProcess.getInputStream()));
                     BufferedReader stdError = new BufferedReader(new InputStreamReader(finalProcess.getErrorStream()))) {
                    
                    String line;
                    while ((line = stdInput.readLine()) != null) {
                        output.append(line).append("\n");
                    }
                    while ((line = stdError.readLine()) != null) {
                        error.append(line).append("\n");
                    }
                }
                
                int exitCode = finalProcess.waitFor();
                return ExecutionResult.builder()
                    .exitCode(exitCode)
                    .output(output.toString().trim())
                    .error(error.toString().trim())
                    .timeout(false)
                    .build();
            });
            
            return future.get(timeoutSeconds, TimeUnit.SECONDS);
            
        } catch (TimeoutException e) {
            log.warn("命令执行超时: {}", command);
            return ExecutionResult.builder()
                .exitCode(-1)
                .output("")
                .error("执行超时")
                .timeout(true)
                .build();
        } catch (Exception e) {
            log.error("命令执行失败: ", e);
            return ExecutionResult.builder()
                .exitCode(-1)
                .output("")
                .error(e.getMessage())
                .timeout(false)
                .build();
        } finally {
            if (process != null) {
                process.destroyForcibly();
            }
            executor.shutdownNow();
        }
    }
}
