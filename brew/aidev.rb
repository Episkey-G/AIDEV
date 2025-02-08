class Aidev < Formula
  desc "AI-Powered Command Line Assistant"
  homepage "https://github.com/episkey/aidev"
  url "https://github.com/episkey/aidev/releases/download/v1.0.0/aidev-1.0-SNAPSHOT-jar-with-dependencies.jar"
  version "1.0.0"
  sha256 "REPLACE_WITH_ACTUAL_SHA256" # 发布时需要更新

  depends_on "openjdk@17"

  def install
    libexec.install "aidev-#{version}-SNAPSHOT-jar-with-dependencies.jar"
    (bin/"aidev").write <<~EOS
      #!/bin/bash
      exec java -jar "#{libexec}/aidev-#{version}-SNAPSHOT-jar-with-dependencies.jar" "$@"
    EOS
  end

  test do
    assert_match "aidev version #{version}", shell_output("#{bin}/aidev --version")
  end
end
