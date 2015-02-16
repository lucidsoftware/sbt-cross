lazy val base = Project("foo", file(".")).cross

lazy val project_2_10 = base("2.10.4")

lazy val project_2_11 = base("2.11.5")

lazy val all = base.aggregate(project_2_10, project_2_11)

