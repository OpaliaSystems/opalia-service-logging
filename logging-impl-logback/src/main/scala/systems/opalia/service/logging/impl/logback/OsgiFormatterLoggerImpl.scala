package systems.opalia.service.logging.impl.logback

import org.osgi.service.log._


class OsgiFormatterLoggerImpl(logger: LoggerImpl)
  extends FormatterLogger {

  def getName: String =
    logger.name

  def isTraceEnabled: Boolean =
    logger.traceEnabled

  def trace(message: String): Unit = {

    if (logger.traceEnabled)
      logger.underlying.trace(message)
  }

  def trace(format: String, argument: AnyRef): Unit = {

    if (logger.traceEnabled)
      logger.underlying.trace(String.format(format, argument))
  }

  def trace(format: String, argument1: AnyRef, argument2: AnyRef): Unit = {

    if (logger.traceEnabled)
      logger.underlying.trace(String.format(format, argument1, argument2))
  }

  def trace(format: String, arguments: AnyRef*): Unit = {

    if (logger.traceEnabled)
      logger.underlying.trace(String.format(format, arguments: _*))
  }

  def trace[E <: Exception](consumer: LoggerConsumer[E]): Unit = {

    if (logger.traceEnabled)
      consumer.accept(this)
  }

  def isDebugEnabled: Boolean =
    logger.debugEnabled

  def debug(message: String): Unit = {

    if (logger.debugEnabled)
      logger.underlying.debug(message)
  }

  def debug(format: String, argument: AnyRef): Unit = {

    if (logger.debugEnabled)
      logger.underlying.debug(String.format(format, argument))
  }

  def debug(format: String, argument1: AnyRef, argument2: AnyRef): Unit = {

    if (logger.debugEnabled)
      logger.underlying.debug(String.format(format, argument1, argument2))
  }

  def debug(format: String, arguments: AnyRef*): Unit = {

    if (logger.debugEnabled)
      logger.underlying.debug(String.format(format, arguments: _*))
  }

  def debug[E <: Exception](consumer: LoggerConsumer[E]): Unit = {

    if (logger.debugEnabled)
      consumer.accept(this)
  }

  def isInfoEnabled: Boolean =
    logger.infoEnabled

  def info(message: String): Unit = {

    if (logger.infoEnabled)
      logger.underlying.info(message)
  }

  def info(format: String, argument: AnyRef): Unit = {

    if (logger.infoEnabled)
      logger.underlying.info(String.format(format, argument))
  }

  def info(format: String, argument1: AnyRef, argument2: AnyRef): Unit = {

    if (logger.infoEnabled)
      logger.underlying.info(String.format(format, argument1, argument2))
  }

  def info(format: String, arguments: AnyRef*): Unit = {

    if (logger.infoEnabled)
      logger.underlying.info(String.format(format, arguments: _*))
  }

  def info[E <: Exception](consumer: LoggerConsumer[E]): Unit = {

    if (logger.infoEnabled)
      consumer.accept(this)
  }

  def isWarnEnabled: Boolean =
    logger.warningEnabled

  def warn(message: String): Unit = {

    if (logger.warningEnabled)
      logger.underlying.warn(message)
  }

  def warn(format: String, argument: AnyRef): Unit = {

    if (logger.warningEnabled)
      logger.underlying.warn(String.format(format, argument))
  }

  def warn(format: String, argument1: AnyRef, argument2: AnyRef): Unit = {

    if (logger.warningEnabled)
      logger.underlying.warn(String.format(format, argument1, argument2))
  }

  def warn(format: String, arguments: AnyRef*): Unit = {

    if (logger.warningEnabled)
      logger.underlying.warn(String.format(format, arguments: _*))
  }

  def warn[E <: Exception](consumer: LoggerConsumer[E]): Unit = {

    if (logger.warningEnabled)
      consumer.accept(this)
  }

  def isErrorEnabled: Boolean =
    logger.errorEnabled

  def error(message: String): Unit = {

    if (logger.errorEnabled)
      logger.underlying.error(message)
  }

  def error(format: String, argument: AnyRef): Unit = {

    if (logger.errorEnabled)
      logger.underlying.error(String.format(format, argument))
  }

  def error(format: String, argument1: AnyRef, argument2: AnyRef): Unit = {

    if (logger.errorEnabled)
      logger.underlying.error(String.format(format, argument1, argument2))
  }

  def error(format: String, arguments: AnyRef*): Unit = {

    if (logger.errorEnabled)
      logger.underlying.error(String.format(format, arguments: _*))
  }

  def error[E <: Exception](consumer: LoggerConsumer[E]): Unit = {

    if (logger.errorEnabled)
      consumer.accept(this)
  }

  def audit(message: String): Unit = {

    println(message)
  }

  def audit(format: String, argument: AnyRef): Unit = {

    println(String.format(format, argument))
  }

  def audit(format: String, argument1: AnyRef, argument2: AnyRef): Unit = {

    println(String.format(format, argument1, argument2))
  }

  def audit(format: String, arguments: AnyRef*): Unit = {

    println(String.format(format, arguments: _*))
  }
}
