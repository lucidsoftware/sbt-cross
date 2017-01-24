package com.lucidchart.sbtcross

import sbt._
import scala.language.implicitConversions

sealed trait CrossableProject[A <: CrossableProject[A]] {
  self: A =>
  protected[this] type P
  final def %(configurations: String) = (this: LocalProjectDependency).copy(configurations = Some(configurations))
  final def cross(axis: Axis) = new CrossedProject(axis, this)
  final def empty = Project(project.id, file(".cross") / project.id)
  final def localProject = LocalProject(project.id)
  def dependsOn(dependencies: LocalProjectDependency*): A
  def resolve: P
  def project: Project
  def withProject(project: Project): A
}

object CrossableProject {
  implicit def toDependency(project: CrossableProject[_]): LocalProjectDependency = LocalProjectDependency(project.localProject)
}

final case class BaseProject(project: Project) extends CrossableProject[BaseProject] {
  protected[this] type P = Project
  def cross: CrossedProject[BaseProject] = cross(ScalaAxis)
  def dependsOn(dependencies: LocalProjectDependency*) = copy(project.dependsOn(dependencies.map(_.classpathDependency): _*))
  def resolve = project
  def withProject(project: Project) = copy(project)
}

final case class CrossedProject[A <: CrossableProject[A]](axis: Axis, delegate: A, dependencies: Seq[LocalProjectDependency] = Nil) extends CrossableProject[CrossedProject[A]] {
  protected[this] type P = this.type
  def aggregate(aggregates: AggregateArgument*) = empty.aggregate(aggregates.map {
    case ProjectAggregateArgument(project) => project: ProjectReference
    case VersionAggregateArgument(version) => axis(LocalProject(delegate.project.id), version)
  }: _*)
  def apply(version: String) =
    axis(delegate, version)
      .dependsOn(dependencies.map(d => d.copy(axis(d.project, version))): _*)
      .resolve
  def dependsOn(dependencies: LocalProjectDependency*) = copy(dependencies = this.dependencies ++ dependencies)
  def forVersions(version1: String) = Tuple1(apply(version1))
  def forVersions(version1: String, version2: String) = (apply(version1), apply(version2))
  def forVersions(version1: String, version2: String, version3: String) = (apply(version1), apply(version2), apply(version3))
  def forVersions(version1: String, version2: String, version3: String, version4: String) = (apply(version1), apply(version2), apply(version3), apply(version4))
  def forVersions(version1: String, version2: String, version3: String, version4: String, version5: String) = (apply(version1), apply(version2), apply(version3), apply(version4), apply(version5))
  def project = delegate.project
  def resolve = this
  def withProject(project: Project) = copy(delegate = delegate.withProject(project))
}
