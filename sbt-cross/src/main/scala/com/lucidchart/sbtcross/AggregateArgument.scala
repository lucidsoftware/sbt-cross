package com.lucidchart.sbtcross

import scala.language.implicitConversions
import sbt._

sealed trait AggregateArgument

final case class ProjectAggregateArgument(project: Project) extends AggregateArgument

object ProjectAggregateArgument {
  implicit def toArgument(project: Project): ProjectAggregateArgument = apply(project)
}

final case class VersionAggregateArgument(version: String) extends AggregateArgument

object VersionAggregateArgument {
  implicit def toArgument(version: String): VersionAggregateArgument = apply(version)
}
