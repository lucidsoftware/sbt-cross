package com.lucidchart.sbtcross

import sbt.{ ClasspathDependency, LocalProject }

case class LocalProjectDependency(project: LocalProject, configurations: Option[String] = None) {
  def classpathDependency = ClasspathDependency(project, configurations)
}
