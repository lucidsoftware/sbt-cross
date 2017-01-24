package com.lucidchart.sbtcross

import sbt.LocalProject

trait Axis {
  def apply[A <: CrossableProject[A]](delegate: A, version: String): A
  def apply(project: LocalProject, version: String): LocalProject
}
