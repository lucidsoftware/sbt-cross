package com.lucidchart.sbtcross

import sbt._
import scala.language.implicitConversions

object SbtCrossImport {

  implicit class SbtCrossable(project: Project) {
    def cross = new CrossProject(project)
  }

  implicit def stringToAggregateArgument(version: String): VersionAggregateArgument =
    VersionAggregateArgument(version)

  implicit def projectToAggregateArgument[A](project: A)(implicit evidence: A => ProjectReference): ProjectAggregateArgument =
    ProjectAggregateArgument(project)

}
