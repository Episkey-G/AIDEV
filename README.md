# aidev - AI-Powered Command Line Assistant

[![License: MIT](https://img.shields.io/badge/License-MIT-blue.svg)](https://opensource.org/licenses/MIT)
[![Java Version](https://img.shields.io/badge/Java-17%2B-orange)](https://adoptium.net/)

将自然语言转换为安全Shell命令的智能助手，为开发者提供更直观的命令行操作体验。

## ✨ 核心特性

- **自然语言转命令**：用中文描述需求直接生成可执行命令
- **智能安全过滤**：多层防护机制拦截危险操作
- **执行反馈系统**：实时显示命令执行结果与状态
- **上下文感知**：自动识别当前工作目录环境
- **多平台支持**：macOS/Linux/Windows WSL

## 🚀 快速开始

### 前置要求
- Java 17+
- OpenAI API Key

### 安装方式

#### Homebrew (macOS)
```bash
brew tap episkey/aidev
brew install aidev
```

#### 手动安装
1. 下载最新版JAR包
2. 创建配置文件：
```bash
mkdir ~/.aidev
echo "OPENAI_API_KEY=your_api_key_here" > ~/.aidev/config
```

### 基本使用
```bash
# 简单查询
aidev "查看当前目录的磁盘使用情况"

# 指定技术上下文
aidev -c "Java项目" "分析内存泄漏"

# 进入交互模式
aidev shell
```

## 🔧 配置指南

### OpenAI API密钥
1. 获取API密钥：[OpenAI平台](https://platform.openai.com/)
2. 配置到系统：
```bash
echo "OPENAI_API_KEY=sk-xxxxxx" >> ~/.aidev/config
```

### 高级配置
编辑`~/.aidev/config`：
```properties
# 设置响应超时（秒）
REQUEST_TIMEOUT=30
# 指定AI模型
AI_MODEL=gpt-4
# 启用彩色输出
COLOR_MODE=true
```

## 🛡️ 安全特性

| 防护机制         | 描述                          | 示例拦截                      |
|------------------|-------------------------------|-----------------------------|
| 命令黑名单       | 拦截`rm`, `chmod`等危险命令    | `rm -rf /` → ❌ 拦截         |
| 复合命令过滤     | 禁止`&&`、`\|`等组合操作       | `ls && rm` → ❌ 拦截         |
| 权限限制         | 阻止`sudo`等特权命令           | `sudo reboot` → ❌ 拦截      |
| 模式验证         | 检查非常规参数组合             | `chmod 777 /` → ❌ 拦截      |

## 📚 使用示例

### 文件操作
```bash
$ aidev "创建一个包含'hello world'的test.txt"
✅ 执行成功:
Created file test.txt

$ cat test.txt
hello world
```

### 系统监控
```bash
$ aidev "找出占用CPU最高的进程"
🔍 建议命令: ps -eo pid,ppid,cmd,%mem,%cpu --sort=-%cpu | head -n 6
✅ 执行成功:
  PID  PPID CMD                         %MEM %CPU
 1234     1 /usr/bin/java...            15.3  78.2
```

### 安全拦截
```bash
$ aidev "删除所有日志文件"
⚠️ 安全拦截: 危险命令被拦截: rm -rf *.log
```

## 🧠 技术栈

| 组件             | 用途                          | 版本           |
|------------------|-------------------------------|----------------|
| Apache Commons CLI | 命令行解析                   | 1.5.0+         |
| OpenAI Java SDK  | AI指令转换                   | 0.16.1+        |
| Jackson          | JSON处理                     | 2.13.0+        |
| ProcessBuilder   | 本地命令执行                 | Java原生       |
| JLine            | 交互式终端                   | 3.23.0+        |

## 🤝 参与贡献

欢迎通过以下方式参与项目：
1. 提交Issue报告问题
2. 发起Pull Request改进代码
3. 完善项目文档
4. 分享使用案例

```bash
# 开发环境搭建
git clone https://github.com/yourorg/aidev.git
./gradlew build
```

## 📜 开源协议

本项目基于 [MIT License](LICENSE) 开源，请自由使用并遵循基本准则：
- 保留原始版权声明
- 禁止恶意代码注入
- 遵循安全最佳实践

---

> 📧 问题反馈: developer@episkey.com  
> 🌐 项目主页: https://aidev.episkey.com
``` 

这个README文档包含：
1. 直观的状态徽章
2. 分步骤的安装指南
3. 交互式代码示例
4. 可视化安全机制说明
5. 结构化技术栈表格
6. 完整的贡献指南
7. 多场景使用示例

建议根据实际项目情况调整联系方式和仓库地址，可添加截图和演示GIF增强可视化效果。