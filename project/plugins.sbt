logLevel := Level.Warn

addSbtPlugin("org.scala-js" % "sbt-scalajs" % "0.6.17")

addSbtPlugin("ch.epfl.scala" % "sbt-scalajs-bundler" % "0.6.0")

// Yes, this needs to be here, not in build.sbt
libraryDependencies += "org.scala-js" %% "scalajs-env-selenium" % "0.1.3"
