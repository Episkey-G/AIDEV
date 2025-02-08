package com.episkey.aidev.cli;

import com.episkey.aidev.core.AIShellOrchestrator;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.cli.*;

/**
 * 整合Apache Commons CLI
 */
@Slf4j
public class CommandHandler {
    private final Options options;
    private final AIShellOrchestrator orchestrator;

    public CommandHandler() {
        this.options = createOptions();
        this.orchestrator = new AIShellOrchestrator();
    }

    private Options createOptions() {
        Options options = new Options();

        options.addOption(Option.builder("h")
            .longOpt("help")
            .desc("显示帮助信息")
            .build());

        options.addOption(Option.builder("v")
            .longOpt("version")
            .desc("显示版本信息")
            .build());

        options.addOption(Option.builder("c")
            .longOpt("context")
            .hasArg()
            .argName("技术上下文")
            .desc("指定技术上下文，如：Java项目、Python项目等")
            .build());

        options.addOption(Option.builder("s")
            .longOpt("shell")
            .desc("进入交互式shell模式")
            .build());

        return options;
    }

    public void handleCommand(String[] args) {
        CommandLineParser parser = new DefaultParser();
        HelpFormatter formatter = new HelpFormatter();

        try {
            CommandLine cmd = parser.parse(options, args);

            if (cmd.hasOption("help")) {
                formatter.printHelp("aidev", options, true);
                return;
            }

            if (cmd.hasOption("version")) {
                System.out.println("aidev version 1.0.0");
                return;
            }

            if (cmd.hasOption("shell")) {
                orchestrator.startInteractiveShell();
                return;
            }

            // 获取剩余的参数作为查询内容
            String[] queries = cmd.getArgs();
            if (queries.length == 0) {
                formatter.printHelp("aidev", options, true);
                return;
            }

            // 构建完整的查询字符串
            StringBuilder query = new StringBuilder();
            if (cmd.hasOption("context")) {
                query.append("在").append(cmd.getOptionValue("context")).append("的上下文中，");
            }
            query.append(String.join(" ", queries));

            // 处理单次查询
            orchestrator.processUserInput(query.toString());

        } catch (ParseException e) {
            log.error("参数解析错误: ", e);
            formatter.printHelp("aidev", options, true);
        }
    }
}
