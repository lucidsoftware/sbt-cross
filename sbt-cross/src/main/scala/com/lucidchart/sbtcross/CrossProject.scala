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
      .addSbtFiles(projectsSbtFiles: _*)
      .dependsOn(
        dependencies.map(_(version)): _*
      )
  }

  private lazy val projectsSbtFiles: Seq[File] =
    project
      .base
      .getAbsoluteFile
      .*(ExistsFileFilter && NameFilter.fnToNameFilter(_.endsWith(".sbt")))
      .get

  def forVersions(v0: String) = Tuple1(apply(v0))

  def forVersions(v0: String, v1: String): (Project, Project) = (apply(v0), apply(v1))

  def forVersions(v0: String, v1: String, v2: String) = (apply(v0), apply(v1), apply(v2))

  def forVersions(v0: String, v1: String, v2: String, v3: String) = (apply(v0), apply(v1), apply(v2), apply(v3))

  def forVersions(v0: String, v1: String, v2: String, v3: String, v4: String) =
    (apply(v0), apply(v1), apply(v2), apply(v3), apply(v4))

  def forVersions(v0: String, v1: String, v2: String, v3: String, v4: String, v5: String) =
    (apply(v0), apply(v1), apply(v2), apply(v3), apply(v4), apply(v5))

  def forVersions(v0: String, v1: String, v2: String, v3: String, v4: String, v5: String, v6: String) =
    (apply(v0), apply(v1), apply(v2), apply(v3), apply(v4), apply(v5), apply(v6))

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
