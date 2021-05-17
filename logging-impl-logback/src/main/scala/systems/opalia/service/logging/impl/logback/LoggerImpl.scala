package systems.opalia.service.logging.impl.logback

import org.slf4j.{Logger => Slf4jLogger}
import systems.opalia.interfaces.logging._


class LoggerImpl(private[logback] val underlying: Slf4jLogger, level: LogLevel)
  extends Logger {

  val name: String =
    underlying.getName

  val logLevel: LogLevel =
    level

  protected def internal(logLevel: LogLevel, message: String, throwable: Throwable): Unit = {

    if (throwable == null) {

      logLevel match {
        case LogLevel.OFF =>
        case LogLevel.ERROR => underlying.error(message)
        case LogLevel.WARNING => underlying.warn(message)
        case LogLevel.INFO => underlying.info(message)
        case LogLevel.DEBUG => underlying.debug(message)
        case LogLevel.TRACE => underlying.trace(message)
      }

    } else {

      logLevel match {
        case LogLevel.OFF =>
        case LogLevel.ERROR => underlying.error(message, throwable)
        case LogLevel.WARNING => underlying.warn(message, throwable)
        case LogLevel.INFO => underlying.info(message, throwable)
        case LogLevel.DEBUG => underlying.debug(message, throwable)
        case LogLevel.TRACE => underlying.trace(message, throwable)
      }
    }
  }
}
