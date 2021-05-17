package systems.opalia.service.logging.impl.logback

import com.typesafe.config.{Config, ConfigException, ConfigUtil}
import java.nio.charset.Charset
import java.nio.file.Path
import scala.collection.JavaConverters._
import systems.opalia.commons.configuration.ConfigHelper._
import systems.opalia.commons.configuration.Reader._
import systems.opalia.interfaces.logging.LogLevel
import systems.opalia.interfaces.rendering.Renderer


final class BundleConfig(config: Config) {

  val logLevel: LogLevel = config.as[LogLevel]("log.level")

  val deploymentPath: Path = config.as[Path]("log.deployment-path").normalize()

  val stdoutCapturing: Boolean = config.as[Option[Boolean]]("log.capture-stdout").getOrElse(false)
  val stderrCapturing: Boolean = config.as[Option[Boolean]]("log.capture-stderr").getOrElse(false)

  val sinks: List[BundleConfig.Sink] =
    config.as[List[Config]]("log.sinks")
      .map {
        sink =>

          val name = sink.as[String]("name")
          val filePath = sink.as[Path]("file-path")
          val charset1 = sink.as[Option[Charset]]("charset")

          val charset2 = charset1.getOrElse(Renderer.osDefaultCharset)

          BundleConfig.Sink(
            name,
            filePath,
            charset2
          )
      }

  private val loggerSinkMappingRaw = config.getConfig("log.logger-sink-mapping")

  val loggerSinkMapping: List[BundleConfig.LoggerSinksPair] =
    loggerSinkMappingRaw.entrySet().asScala.toList
      .map(_.getKey)
      .map {
        key =>

          val loggerName = ConfigUtil.splitPath(key).asScala.head

          val sinkList =
            loggerSinkMappingRaw.getStringList(key).asScala.toList
              .map {
                sinkName =>

                  sinks.find(_.name == sinkName)
                    .getOrElse(throw new ConfigException.BadValue(loggerSinkMappingRaw.origin(), sinkName,
                      s"Cannot find sink with name $sinkName."))
              }

          BundleConfig.LoggerSinksPair(loggerName, sinkList)
      }

  val loggerSinkMappingDefault: List[BundleConfig.Sink] =
    config.getStringList("log.logger-sink-mapping-default").asScala.toList
      .map {
        sinkName =>

          sinks.find(_.name == sinkName)
            .getOrElse(throw new ConfigException.BadValue(loggerSinkMappingRaw.origin(), sinkName,
              s"Cannot find sink with name $sinkName."))
      }
}

object BundleConfig {

  case class Sink(name: String, filePath: Path, charset: Charset)

  case class LoggerSinksPair(loggerName: String, sinks: List[Sink])

}
