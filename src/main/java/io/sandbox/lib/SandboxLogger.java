package io.sandbox.lib;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SandboxLogger {
  private Logger logger;

  public SandboxLogger(String loggerId) {
    this.logger = LoggerFactory.getLogger(loggerId);
  }

  public void info(String text) {
    this.logger.info(text);
  }

  public void warn(String text) {
    this.logger.warn(text);
  }

  public void error(String text) {
    this.logger.error(text);
  }
}
