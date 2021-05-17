package systems.opalia.service.logging.impl.logback

import org.osgi.framework.{Bundle, BundleContext}
import org.osgi.service.component.annotations._
import org.osgi.service.log._
import scala.concurrent.Await
import scala.concurrent.duration.Duration
import systems.opalia.interfaces.soa.ConfigurationService
import systems.opalia.interfaces.soa.osgi.ServiceManager


@Component(service = Array(classOf[LoggerFactory]), immediate = false)
class OsgiLoggerFactoryServiceImpl
  extends LoggerFactory {

  private val serviceManager: ServiceManager = new ServiceManager()
  private var bootable: LoggingServiceBootable = _

  @Activate
  def start(bundleContext: BundleContext): Unit = {

    val configurationService = serviceManager.getService(bundleContext, classOf[ConfigurationService])
    val config = new BundleConfig(configurationService.getConfiguration)

    bootable = new LoggingServiceBootable(config, mainService = false)

    bootable.setup()
    Await.result(bootable.awaitUp(), Duration.Inf)
  }

  @Deactivate
  def stop(bundleContext: BundleContext): Unit = {

    bootable.shutdown()
    Await.result(bootable.awaitUp(), Duration.Inf)

    serviceManager.ungetServices(bundleContext)

    bootable = null
  }

  def getLogger(name: String): Logger =
    new OsgiLoggerImpl(bootable.newLogger(name).asInstanceOf[LoggerImpl])

  def getLogger(clazz: Class[_]): Logger =
    getLogger(clazz.getName)

  def getLogger[L <: Logger](name: String, loggerType: Class[L]): L = {

    if (loggerType.getName == classOf[FormatterLogger].getName)
      new OsgiFormatterLoggerImpl(bootable.newLogger(name).asInstanceOf[LoggerImpl]).asInstanceOf[L]
    else if (loggerType.getName == classOf[Logger].getName)
      new OsgiLoggerImpl(bootable.newLogger(name).asInstanceOf[LoggerImpl]).asInstanceOf[L]
    else
      throw new IllegalArgumentException("The specified logger type is not supported.")
  }

  def getLogger[L <: Logger](clazz: Class[_], loggerType: Class[L]): L =
    getLogger(clazz.getName, loggerType)

  def getLogger[L <: Logger](bundle: Bundle, name: String, loggerType: Class[L]): L =
    getLogger(name, loggerType)
}
