package com.lucidchart.sbtcross

import sbt._

case class CrossProjectModuleId(crossProject: CrossProject, configurations: Option[String] = None) {

  def apply(version: String) = {
    val projectRef = crossProject.ref(version)
    configurations.fold(projectRef: ClasspathDependency)(projectRef % _)
  }

}
