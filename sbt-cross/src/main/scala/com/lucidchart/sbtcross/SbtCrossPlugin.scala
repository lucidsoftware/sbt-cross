package com.lucidchart.sbtcross

import sbt._
import scala.language.implicitConversions

object SbtCrossPlugin extends AutoPlugin {

  object autoImport {
    implicit def toCrossable(project: Project): BaseProject = new BaseProject(project)
  }

}
