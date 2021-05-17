
val mScalaVersion = "2.12.13"
val mInterfacesVersion = "1.0.0"
val mCommonsVersion = "1.0.0"
val mCrossVersion = """^(\d+\.\d+)""".r.findFirstIn(mScalaVersion).get

val exclusionRules = Seq(
  ExclusionRule("org.scala-lang", "scala-library"),
  ExclusionRule("org.scala-lang", "scala-reflect"),
  ExclusionRule("org.scala-lang", "scala-compiler"),
  ExclusionRule("com.typesafe", "config"),
  ExclusionRule("systems.opalia", s"interfaces_$mCrossVersion"),
  ExclusionRule("org.osgi", "org.osgi.core"),
  ExclusionRule("org.osgi", "org.osgi.service.component"),
  ExclusionRule("org.osgi", "org.osgi.compendium")
)

def commonSettings: Seq[Setting[_]] = {

  Seq(
    organizationName := "Opalia Systems",
    organizationHomepage := Some(url("https://opalia.systems")),
    organization := "systems.opalia",
    homepage := Some(url("https://github.com/OpaliaSystems/opalia-service-logging")),
    version := "1.0.0",
    scalaVersion := mScalaVersion
  )
}

lazy val `logging-impl-logback` =
  (project in file("logging-impl-logback"))
    .settings(

      name := "logging-impl-logback",

      description := "The project provides an implementation for logging based on logback.",

      commonSettings,

      bundleSettings,

      OsgiKeys.privatePackage ++= Seq(
        "systems.opalia.service.logging.impl.*"
      ),

      OsgiKeys.importPackage ++= Seq(
        "scala.*",
        "com.typesafe.config.*",
        "systems.opalia.interfaces.*",
        "org.osgi.service.log.*"
      ),

      libraryDependencies ++= Seq(
        "org.osgi" % "osgi.core" % "8.0.0" % "provided",
        "org.osgi" % "org.osgi.service.log" % "1.5.0" % "provided",
        "org.osgi" % "org.osgi.service.component.annotations" % "1.4.0",
        "systems.opalia" %% "interfaces" % mInterfacesVersion % "provided",
        "systems.opalia" %% "commons" % mCommonsVersion excludeAll (exclusionRules: _*),
        "ch.qos.logback" % "logback-core" % "1.2.3",
        "ch.qos.logback" % "logback-classic" % "1.2.3",
        "org.slf4j" % "slf4j-api" % "1.7.30"
      )
    )
