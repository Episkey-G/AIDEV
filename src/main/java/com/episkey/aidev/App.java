package com.episkey.aidev;

import com.episkey.aidev.cli.CommandHandler;
import com.episkey.aidev.core.AIShellOrchestrator;
import lombok.extern.slf4j.Slf4j;

/**
 * 主入口
 */
@Slf4j
public class App {
    public static void main(String[] args) {
        try {
            if (args.length == 0 || args[0].equals("shell")) {
                // 进入交互式shell模式
                new AIShellOrchestrator().startInteractiveShell();
            } else {
                // 处理命令行参数
                new CommandHandler().handleCommand(args);
            }
        } catch (Exception e) {
            log.error("程序执行出错: ", e);
            System.exit(1);
        }
    }
}
