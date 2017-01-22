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

organizationHomepage := Some(url("https://www.golucido.co/"))

organizationName := "Lucid Software"

PgpKeys.pgpPassphrase in Global := Some(Array.emptyCharArray)

sbtPlugin := true

scalacOptions ++= Seq("-deprecation", "-language")

scmInfo := Some(ScmInfo(url("https://github.com/lucidsoftware/sbt-cross"), "scm:git:git@github.com:lucidsoftware/sbt-cross", None))

startYear := Some(2017)

version := IO.read(file("version")).trim + (if (sys.props.get("release").exists(_ == "true")) "" else "-SNAPSHOT")
