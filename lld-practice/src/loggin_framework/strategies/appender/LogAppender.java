package loggin_framework.strategies.appender;

import loggin_framework.entities.LogMessage;
import loggin_framework.strategies.formatter.LogFormatter;

public interface LogAppender {
    void append(LogMessage logMessage);
    void close();
    LogFormatter getFormatter();
    void setFormatter(LogFormatter formatter);
}
