package com.lucidchart.sbtcross

import sbt.Keys._
import sbt._

trait DefaultAxis extends Axis {
  protected[this] def name: String
  protected[this] def major(version: String): String
  protected[this] def id(id: String, version: String) = s"${id}_${major(version).replace(".", "_")}"
  def apply[A <: CrossableProject[A]](delegate: A, version: String) = {
    val project = delegate.project
    val newProject = project.copy(id = id(project.id, version)).settings(
      target := target.value / s"$name-${major(version)}"
    )
    if (newProject.base == file(".")) {
      // https://github.com/sbt/sbt/issues/1861
      // this isn't a complete fix, but most things aren't
      delegate.withProject(newProject.copy(base = file(".cross") / newProject.id).settings(baseDirectory := file(".")))
    } else {
      delegate.withProject(newProject)
    }
  }
  def apply(project: LocalProject, version: String) = project.copy(id(project.project, version))
}
