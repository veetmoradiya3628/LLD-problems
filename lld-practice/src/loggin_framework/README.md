### Logging Framework
Design and implement a flexible and extensible logging framework that can be used by applications to log messages at different levels (INFO, DEBUG, ERROR, etc.), support multiple output destinations (console, file, etc.), and allow for custom formatting of log messages.

#### 1. Requirement Gathering

- Functional Requirement
  - The logging framework should support different log levels, such as DEBUG, INFO, WARNING, ERROR, and FATAL.
  - It should allow logging messages with a timestamp, log level, and message content.
  - The framework should support multiple output destinations, such as console, file, and database.
  - It should provide a configuration mechanism to set the log level and output destination.
  - Ability to configure loggers and appenders.
- Non-Functional Requirement
  - The logging framework should be thread-safe to handle concurrent logging from multiple threads.
  - It should be extensible to accommodate new log levels and output destinations in the future.

#### 2. Core Identity
- Logger - Main class used by clients to log messages
- LogLevel - Enum representing different log levels
- LogMessage - Encapsulates the details of a log event
- LogFormatter - Interface for formatting log messages
- DefaultFormatter - Default implementation of LogFormatter
- LoggerConfig - Holds configuration for the logger (appenders, formatters, etc.).
- LogAppender - Interface and implementations for output destinations (e.g., ConsoleAppender, FileAppender).

#### 3. Design class & relationships
- LogMessage - class
  - LocalDateTime timestamp
  - LogLevel level
  - String loggerName
  - String theadName
  - String message
  - getter methods
- LogLevel - enum
  - DEBUG(1), INFO(2), WARN(3), ERROR(4), FATAL(5)
- LogAppender - interface
  - defines methods supporting different operations for log appender
  - append(LogMessage logMessage)
  - close()
  - getFormatter()
  - setFormatter(LogFormatter formatter)
- FileAppender - class implements LogAppender
  - overrides methods as per the implementation
- ConsoleAppender - class implements LogAppender
  - overrides methods as per the implementation
- LogFormatter - interface
  - String format(LogMessage logMessage);
- SimpleTextFormatter - class implements LogFormatter
  - Overrides format method accordingly
- Logger - class
  - name
  - level - LogLevel
  - parent - Logger
  - appenders - list of appenders
  - additivity - bool
  - addAppender(LogAppender)
  - getAppenders()
  - setLevel(LogLevel)
  - setAdditivity(bool)
  - getEffectiveLevel() - recursive traverse till parent is not null then apply / return that effective log level
  - log(Loglevel, message) - messageLevel >= effectiveLogLevel() then create LogMessage and call Appenders
  - callAppenders(LogMessage)
  - debug()
  - info()
  - warn()
  - error()
  - fatal()
- LogManager - singleton class
  - LogManager instance
  - loggers - Map<String, logger>
  - Logger rootLogger
  - AsyncLogProcessor processor
  - getInstance()
  - getLogger(String name)
  - createLogger(String name)
  - getRootLogger()
  - getProcessor()
  - shutdown()
- AsyncLogProcessor - class
  - executor ExecutorService
  - process(logMessage, List of Appenders)
  - stop()

#### 4. Code Impl, Run & Test
- UML TODO...

#### 5. Concurrency & Thread Safety

#### 6. Extensions

#### Design patterns & Principles

#### Open issues
