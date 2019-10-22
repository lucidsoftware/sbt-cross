package com.lucidchart.sbtcross

import org.apache.commons.lang3.StringUtils
import sbt.Keys._
import sbt._
import sbt.plugins.JvmPlugin

class LibraryVersionAxis(protected[this] val name: String, settingKey: SettingKey[String], versionFn: String => String) extends DefaultAxis {
  import LibraryVersionAxis._

  protected[this] def major(version: String) = versionFn(version)

  override def apply[A <: CrossableProject[A]](delegate: A, version: String) = {
    val newDelegate = super.apply(delegate, version)
    newDelegate.withProject(newDelegate.project.settings(
      extraDirectories ++= (sourceDirectory.value +: extraDirectories.value).map(_ / s"$name-${major(version)}"),
      settingKey := version,
      Keys.version := {
        if (Keys.version.value.endsWith("-SNAPSHOT")) {
          s"${Keys.version.value.replace("-SNAPSHOT", "")}-${major(version)}-SNAPSHOT"
        } else {
          s"${Keys.version.value}-${major(version)}"
        }
      }))
  }
}

object LibraryVersionAxis {
  val majorVersion = (version: String) => version.indexOf('.') match {
    case StringUtils.INDEX_NOT_FOUND => version
    case i => version.take(i)
  }

  val minorVersion = (version: String) => StringUtils.ordinalIndexOf(version, ".", 2) match {
    case StringUtils.INDEX_NOT_FOUND => version
    case i => version.take(i)
  }

  val extraDirectories = SettingKey[Seq[File]]("extra-directories")
}

object LibraryVersionPlugin extends AutoPlugin {
  object autoImport {
    val extraDirectories = LibraryVersionAxis.extraDirectories
  }
  import autoImport._

  override val trigger = allRequirements

  override val requires = JvmPlugin

  override val globalSettings = Seq(
    extraDirectories := Nil)

  override val projectSettings = Seq(Compile, Test).flatMap { config =>
    Seq(
      unmanagedSourceDirectories in config ++= extraDirectories.value.flatMap { directory =>
        (unmanagedSourceDirectories in config).value.flatMap(Path.rebase(sourceDirectory.value, directory)(_))
      },
      unmanagedResourceDirectories in config ++= extraDirectories.value.flatMap { directory =>
        (unmanagedResourceDirectories in config).value.flatMap(Path.rebase(sourceDirectory.value, directory)(_))
      })
  }
}