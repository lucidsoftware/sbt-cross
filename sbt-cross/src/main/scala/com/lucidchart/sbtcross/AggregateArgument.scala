package com.lucidchart.sbtcross

import sbt._

sealed trait AggregateArgument

case class ProjectAggregateArgument(project: ProjectReference) extends AggregateArgument

case class VersionAggregateArgument(version: String) extends AggregateArgument
