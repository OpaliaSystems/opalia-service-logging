package systems.opalia.service.logging.impl.logback

import org.slf4j.{Logger => Slf4jLogger}
import systems.opalia.interfaces.logging._


final class LoggerImpl(logger: Slf4jLogger, level: LogLevel)
  extends Logger {

  val name: String =
    logger.getName

  val logLevel: LogLevel =
    level

  protected def internal(logLevel: LogLevel, message: String, throwable: Throwable): Unit = {

    if (throwable == null) {

      logLevel match {
        case LogLevel.OFF =>
        case LogLevel.ERROR => logger.error(message)
        case LogLevel.WARNING => logger.warn(message)
        case LogLevel.INFO => logger.info(message)
        case LogLevel.DEBUG => logger.debug(message)
      }

    } else {

      logLevel match {
        case LogLevel.OFF =>
        case LogLevel.ERROR => logger.error(message, throwable)
        case LogLevel.WARNING => logger.warn(message, throwable)
        case LogLevel.INFO => logger.info(message, throwable)
        case LogLevel.DEBUG => logger.debug(message, throwable)
      }
    }
  }
}
