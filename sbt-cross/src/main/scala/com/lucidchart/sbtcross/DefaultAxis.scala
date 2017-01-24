package com.lucidchart.sbtcross

import sbt.Keys._
import sbt._
import sbt.plugins.CorePlugin

trait DefaultAxis extends Axis {
  import DefaultAxisPlugin.autoImport._

  protected[this] def name: String
  protected[this] def major(version: String): String
  protected[this] def id(id: String, version: String) = s"${id}_${major(version).replace(".", "_")}"
  def apply[A <: CrossableProject[A]](delegate: A, version: String) = {
    val project = delegate.project
    val newProject = project.copy(id = id(project.id, version)).settings(
      originalName := originalName.?.value.getOrElse(project.id),
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

object DefaultAxisPlugin extends AutoPlugin {
  object autoImport {
    val originalName = SettingKey[String]("original-name")
  }
  import autoImport._

  override val trigger = allRequirements

  override val requires = CorePlugin

  override val projectSettings = Seq(
    name := originalName.?.value.getOrElse(name.value)
  )

}
