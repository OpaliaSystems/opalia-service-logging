package systems.opalia.service.logging.impl.logback

import org.osgi.framework.BundleContext
import org.osgi.service.component.annotations._
import scala.concurrent.Await
import scala.concurrent.duration.Duration
import systems.opalia.interfaces.logging.{Logger, LoggingService}
import systems.opalia.interfaces.soa.ConfigurationService
import systems.opalia.interfaces.soa.osgi.ServiceManager


@Component(service = Array(classOf[LoggingService]), immediate = true)
class LoggingServiceImpl
  extends LoggingService {

  private val serviceManager: ServiceManager = new ServiceManager()
  private var bootable: LoggingServiceBootable = _

  @Activate
  def start(bundleContext: BundleContext): Unit = {

    val configurationService = serviceManager.getService(bundleContext, classOf[ConfigurationService])
    val config = new BundleConfig(configurationService.getConfiguration)

    bootable = new LoggingServiceBootable(config, mainService = true)

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

  def newLogger(name: String): Logger =
    bootable.newLogger(name)
}
