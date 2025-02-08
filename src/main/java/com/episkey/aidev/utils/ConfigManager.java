package com.episkey.aidev.utils;

import lombok.Getter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * 配置加载
 */
@Getter
public class ConfigManager {
    private static final String CONFIG_DIR = System.getProperty("user.home") + "/.aidev";
    private static final String CONFIG_FILE = CONFIG_DIR + "/config";
    
    private String openAiApiKey;
    private String aiModel = "gpt-4";
    private int requestTimeout = 30;
    private boolean colorMode = true;
    
    private static ConfigManager instance;
    
    public static ConfigManager getInstance() {
        if (instance == null) {
            instance = new ConfigManager();
            instance.loadConfig();
        }
        return instance;
    }
    
    private void loadConfig() {
        try {
            File configFile = new File(CONFIG_FILE);
            if (!configFile.exists()) {
                createDefaultConfig();
            }
            
            Properties props = new Properties();
            props.load(new FileInputStream(configFile));
            
            openAiApiKey = props.getProperty("OPENAI_API_KEY");
            aiModel = props.getProperty("AI_MODEL", aiModel);
            requestTimeout = Integer.parseInt(props.getProperty("REQUEST_TIMEOUT", "30"));
            colorMode = Boolean.parseBoolean(props.getProperty("COLOR_MODE", "true"));
        } catch (Exception e) {
            throw new RuntimeException("配置加载失败", e);
        }
    }
    
    private void createDefaultConfig() {
        try {
            // 创建配置目录
            File configDir = new File(CONFIG_DIR);
            if (!configDir.exists()) {
                configDir.mkdirs();
            }
            
            // 创建日志目录
            File logDir = new File(CONFIG_DIR + "/logs");
            if (!logDir.exists()) {
                logDir.mkdirs();
            }
            
            // 创建默认配置文件
            File configFile = new File(CONFIG_FILE);
            if (!configFile.exists()) {
                Properties defaultProps = new Properties();
                defaultProps.setProperty("OPENAI_API_KEY", "your-api-key-here");
                defaultProps.setProperty("AI_MODEL", "gpt-4o-mini");
                defaultProps.setProperty("REQUEST_TIMEOUT", "30");
                defaultProps.setProperty("COLOR_MODE", "true");
                
                try (FileOutputStream out = new FileOutputStream(configFile)) {
                    defaultProps.store(out, "AIdev Default Configuration");
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("无法创建默认配置", e);
        }
    }
}
