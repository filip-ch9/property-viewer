lazy val root = (project in file("."))
  .enablePlugins(PlayJava)
  .settings(
    name := """property-viewer""",
    version := "1.0.5-SNAPSHOT",
    scalaVersion := "2.13.6",
    libraryDependencies ++= Seq(
      guice,
      javaJpa,
      "mysql" % "mysql-connector-java" % "5.1.36",
      "org.hibernate" % "hibernate-core" % "5.4.9.Final",
      "com.squareup.okhttp3" % "okhttp" % "4.6.0",
      "org.json" % "json" % "20211205",
      javaWs % "test",
      "org.awaitility" % "awaitility" % "4.0.1" % "test",
      "org.assertj" % "assertj-core" % "3.14.0" % "test",
      "org.mockito" % "mockito-core" % "3.1.0" % "test",
    ),
    Test / testOptions += Tests.Argument(TestFrameworks.JUnit, "-a", "-v"),
    scalacOptions ++= List("-encoding", "utf8", "-deprecation", "-feature", "-unchecked"),
    javacOptions ++= List("-Xlint:unchecked", "-Xlint:deprecation", "-Werror"),
    PlayKeys.externalizeResourcesExcludes += baseDirectory.value / "conf" / "META-INF" / "persistence.xml"
  )
