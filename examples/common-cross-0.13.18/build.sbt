lazy val foo = (project in file("foo")).dependsOn(common_2_11).settings(
  scalaVersion := "2.11.12"
)

lazy val bar = (project in file("bar")).dependsOn(common_2_12).settings(
  scalaVersion := "2.12.10"
)

// unfortunately, pattern matching doesn't work in val assignment,
// see: https://github.com/sbt/sbt/issues/2290
lazy val common = Project("common", file("common")).cross
  .forVersions("2.11.12", "2.12.10")

lazy val common_2_11 = common._1
lazy val common_2_12 = common._2
