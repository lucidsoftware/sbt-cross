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
    val project: ProjectDefinition[ProjectReference] = delegate.project
    // https://github.com/sbt/sbt/issues/1861
    // this isn't a complete fix, but most things aren't
    val newId = id(project.id, version)
    val newBase =
      if (project.base == file(".")) file(".cross") / newId
      else project.base
    val newProject = Project(newId, newBase)
      .aggregate(project.aggregate: _*)
      .dependsOn(project.dependencies: _*)
      .enablePlugins(project.plugins)
      .configs(project.configurations: _*)
      .settings(project.settings)
      .settings(
        baseDirectory := project.base.getAbsoluteFile,
        originalName := originalName.?.value.getOrElse(project.id),
        target := target.value / s"$name-${major(version)}")
    delegate.withProject(newProject)
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
    name := originalName.?.value.getOrElse(name.value))

}
