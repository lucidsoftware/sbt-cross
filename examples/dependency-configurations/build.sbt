lazy val mainCross = Project("main", file("main")).cross
  .dependsOn(testCross % "test")

lazy val mainCross_2_10 = mainCross("2.10.5")

lazy val mainCross_2_11 = mainCross("2.11.6")

lazy val testCross = Project("common", file("common")).cross

lazy val testCross_2_10 = testCross("2.10.5")

lazy val testCross_2_11 = testCross("2.11.6")
