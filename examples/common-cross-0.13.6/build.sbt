lazy val all = (project in file(".")).aggregate(foo, bar)

lazy val foo = (project in file("foo")).dependsOn(common_2_10).settings(
  scalaVersion := "2.10.4"
)

lazy val bar = (project in file("bar")).dependsOn(common_2_11).settings(
  scalaVersion := "2.11.5"
)

lazy val (common_2_10, common_2_11) = Project("common", file("common")).cross
  .forVersions("2.10.4", "2.11.5")
