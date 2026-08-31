package loggin_framework.strategies.formatter;

import loggin_framework.entities.LogMessage;

public interface LogFormatter {
    String format(LogMessage logMessage);
}
