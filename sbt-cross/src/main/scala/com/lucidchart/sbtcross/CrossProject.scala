package com.lucidchart.sbtcross

import sbt._
import sbt.Keys._
import sbt.cross.CrossVersionUtil

class CrossProject(project: Project, dependencies: Seq[CrossProjectModuleId] = Seq()) {

  def aggregate(parts: AggregateArgument*): Project = {
    val projects = parts.map {
      case ProjectAggregateArgument(project) => project
      case VersionAggregateArgument(version) => ref(version)
    }
    Project(project.id, project.base / ".cross" / "aggregate").aggregate(projects: _*)
  }

  def apply(version: String) = {
    project.copy(id = idForVersion(version), base = baseForVersion(version))
      .settings(
        baseDirectory := project.base.getAbsoluteFile,
        scalaVersion := version,
        target := {
          val oldTarget = target.value
          val binaryVersion = CrossVersionUtil.binaryScalaVersion(scalaVersion.value)
          oldTarget.getParentFile / s"${oldTarget.name}-$binaryVersion"
        }
      )
      .dependsOn(
        dependencies.map(_(version)): _*
      )
  }

  private def base = project.base / ".cross"

  /**
   * Need unique base due to https://github.com/sbt/sbt/issues/1861
   */
  private def baseForVersion(version: String) = {
    val binaryVersion = CrossVersionUtil.binaryScalaVersion(version)
    base / binaryVersion
  }

  def dependsOn(crossProjects: CrossProjectModuleId*): CrossProject =
    new CrossProject(project, dependencies ++ crossProjects)

  private def idForVersion(version: String) = {
    val binaryVersion = CrossVersionUtil.binaryScalaVersion(version)
    s"${project.id}-${binaryVersion.replace(".", "_")}"
  }

  implicit def moduleId: CrossProjectModuleId = CrossProjectModuleId(this)

  def ref(version: String): ProjectReference =
    Project(idForVersion(version), baseForVersion(version))

  def %(configurations: String) = CrossProjectModuleId(this, Some(configurations))

}
