package com.lucidchart.sbtcross

import sbt.Keys
import sbt.Keys._
import sbt.cross.CrossVersionUtil

class ScalaAxis extends DefaultAxis {
  protected[this] val name = "scala"
  protected[this] def major(version: String) = CrossVersionUtil.binaryScalaVersion(version)
  override def apply[A <: CrossableProject[A]](delegate: A, version: String) = {
    val newDelegate = super.apply(delegate, version)
    newDelegate.withProject(newDelegate.project.settings(
      crossTarget := target.value,
      scalaVersion := version
    ))
  }
}

object ScalaAxis extends ScalaAxis
