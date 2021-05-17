package systems.opalia.service.logging.impl.logback

import ch.qos.logback.classic.LoggerContext
import ch.qos.logback.classic.joran.JoranConfigurator
import java.nio.file.{Path, Paths}
import org.slf4j.LoggerFactory
import systems.opalia.commons.io.FileUtils
import systems.opalia.interfaces.logging._
import systems.opalia.interfaces.rendering.Renderer
import systems.opalia.interfaces.soa.Bootable


final class LoggingServiceBootable(config: BundleConfig)
  extends LoggingService
    with Bootable[Unit, Unit] {

  private val loggerContext = LoggerFactory.getILoggerFactory.asInstanceOf[LoggerContext]

  def newLogger(name: String): Logger =
    new LoggerImpl(loggerContext.getLogger(name), config.logLevel)

  protected def setupTask(): Unit = {

    val logbackConfigFile = config.deploymentPath.resolve("logback.xml")
    val configurator = new JoranConfigurator()

    configurator.setContext(loggerContext)
    loggerContext.reset()
    createConfigurationFile(logbackConfigFile)
    configurator.doConfigure(logbackConfigFile.toFile)

    val stdoutLogger =
      new LoggerImpl(loggerContext.getLogger("STDOUT"), LogLevel.TRACE).subLogger(LogLevel.INFO)

    val stderrLogger =
      new LoggerImpl(loggerContext.getLogger("STDERR"), LogLevel.TRACE).subLogger(LogLevel.WARNING)

    PrintStreams.stdoutBind(new LoggingOutputStream(x => stdoutLogger(x)).createPrintStream())
    PrintStreams.stderrBind(new LoggingOutputStream(x => stderrLogger(x)).createPrintStream())
  }

  protected def shutdownTask(): Unit = {

    PrintStreams.stdoutUnbind()
    PrintStreams.stderrUnbind()

    loggerContext.stop()
  }

  private def createConfigurationFile(logbackConfigFile: Path): Unit = {

    val pattern =
      """[%level] [%date{"yyyy-MM-dd'T'HH:mm:ss.SSSXXX", UTC}] [%thread] [%logger] - %message%n%throwable{full}"""

    FileUtils.using(new java.io.PrintWriter(logbackConfigFile.toFile, Renderer.appDefaultCharset.toString)) {
      writer =>

        writer.println("""<?xml version="1.0" encoding="UTF-8"?>""")
        writer.println("""<Configuration>""")
        writer.println()
        writer.println(s"""  <statusListener class="${classOf[OnPrintStreamStatusListener].getName}"/>""")
        writer.println()

        config.sinks.foreach {
          sink =>

            if (sink.filePath == Paths.get("//console")) {

              writer.println(
                s"""  <appender name="${sink.name}_stdout" class="${classOf[StdoutPrintStreamAppender].getName}">
                   |    <encoder>
                   |      <charset>${sink.charset}</charset>
                   |      <pattern>$pattern</pattern>
                   |    </encoder>
                   |  </appender>
                   |
                   |  <appender name="${sink.name}_stderr" class="${classOf[StderrPrintStreamAppender].getName}">
                   |    <encoder>
                   |      <charset>${sink.charset}</charset>
                   |      <pattern>$pattern</pattern>
                   |    </encoder>
                   |  </appender>
                 """.stripMargin)

            } else {

              writer.println(
                s"""  <appender name="${sink.name}_file" class="ch.qos.logback.core.FileAppender">
                   |    <file>${sink.filePath}</file>
                   |    <append>true</append>
                   |    <encoder>
                   |      <charset>${sink.charset}</charset>
                   |      <pattern>$pattern</pattern>
                   |    </encoder>
                   |  </appender>
                 """.stripMargin)
            }
        }

        config.loggerSinkMapping.foreach {
          map =>

            writer.println(s"""  <logger name="${map.loggerName}" level="debug" additivity="false">""")

            map.sinks.foreach {
              sink =>

                if (sink.filePath == Paths.get("//console")) {

                  writer.println(s"""    <appender-ref ref="${sink.name}_stdout"/>""")
                  writer.println(s"""    <appender-ref ref="${sink.name}_stderr"/>""")

                } else {

                  writer.println(s"""    <appender-ref ref="${sink.name}_file"/>""")
                }
            }

            writer.println("""  </logger>""")
            writer.println()
        }

        writer.println(s"""  <root level="debug">""")

        config.loggerSinkMappingDefault.foreach {
          sink =>

            if (sink.filePath == Paths.get("//console")) {

              writer.println(s"""    <appender-ref ref="${sink.name}_stdout"/>""")
              writer.println(s"""    <appender-ref ref="${sink.name}_stderr"/>""")

            } else {

              writer.println(s"""    <appender-ref ref="${sink.name}_file"/>""")
            }
        }

        writer.println(s"""  </root>""")
        writer.println()
        writer.println(s"""</Configuration>""")
    }
  }
}
