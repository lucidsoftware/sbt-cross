scalariformSettings

sonatypeSettings

val checkFormat = TaskKey[Unit]("check-format")

checkFormat := {
  (ScalariformKeys.format in Compile).value
  val directory = baseDirectory.value.absolutePath
  val logger = (streams in Compile).value.log
  val paths = stringToProcess(s"git diff --name-only $directory").lines
  if (paths.nonEmpty) {
    logger.error("The following files are not corrected formatted:")
    paths.foreach { path =>
      logger.error(s"  $path")
    }
    logger.error("Run `sbt scalariform-format`")
    throw new RuntimeException(s"${paths.size} files need formatting")
  }
}

credentials += Credentials(
  "Sonatype Nexus Repository Manager",
  "oss.sonatype.org",
  System.getenv("SONATYPE_USERNAME"),
  System.getenv("SONATYPE_PASSWORD")
)

name := "sbt-cross"

organization := "com.lucidchart"

pomExtra := {
  <developers>
    <developer>
      <name>Lucid Software</name>
      <email>github@lucidchart.com</email>
      <organization>Lucid Software, Inc.</organization>
      <organizationUrl>https://www.golucid.co/</organizationUrl>
    </developer>
  </developers>
  <licenses>
    <license>
      <name>Apache 2</name>
      <url>http://www.apache.org/licenses/LICENSE-2.0.txt</url>
    </license>
  </licenses>
  <scm>
    <connection>scm:git:github.com/lucidsoftware/sbt-cross</connection>
    <developerConnection>scm:git:git@github.com:lucidsoftware/sbt-cross</developerConnection>
    <url>https://github.com/lucidsoftware/sbt-cross</url>
  </scm>
  <url>https://github.com/lucidsoftware/sbt-cross</url>
}

sbtPlugin := true

scalacOptions ++= Seq("-deprecation", "-language")

version := "2.0-SNAPSHOT"
