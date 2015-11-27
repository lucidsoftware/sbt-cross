#sbt-cross

A better solution for building multiple Scala versions (cross compiling) in SBT.

## Purpose

The results of Scala compilation are not always compatible across Scala versions, i.e. binary incompatibility. SBT has some built-in features to make working with Scala projects less painful, including `crossScalaVersions`.

However, cross compiling an SBT project with `crossScalaVersions` is less than ideal:

1. Performance - Each Scala version is handled sequentially (not in parallel).
1. Subprojects - Sharing a (classpath) subproject for two versions is tricky.
1. Aggregation - Aggregation doesn't take into account `crossScalaVersions` of subprojects.

## Install

Requires SBT 0.13. (Tested with 0.13.7.)

In project/plugins.sbt, add

```scala
resolvers += Resolver.sonatypeRepo("releases")

addSbtPlugin("com.lucidchart" % "sbt-cross" % "2.0-SNAPSHOT")
```

If you use Build.scala, you'll need to import `com.lucidchart.sbtcross.SbtCrossImport._`.

## Example

Suppose there is a project `foo` that uses Scala 2.10, a project `bar` that uses Scala 2.11, and they depend on a project `common` that compiles with Scala 2.10 and 2.11.

You can do this with sbt-cross like so:

```scala
lazy val foo = (project in file("foo")).dependsOn(common_2_10)
  .settings(scalaVersion := "2.10.4")

lazy val bar = (project in file("bar")).dependsOn(common_2_11)
  .settings(scalaVersion := "2.11.5")

lazy val common = (project in file("common")).cross

lazy val common_2_10 = common("2.10.4")

lazy val common_2_11 = common("2.11.5")
```

This defines four projects: `foo`, `bar`, `common-2_10`, and `common-2_11`.

SBT will concurrently compile the right ones in the right order.

* `sbt foo/compile` will compile `common-2_10`  then `foo`.

* `sbt bar/compile` will compile `common-2_11`  then `bar`.

* `sbt compile` will compile `common-2_10` then `foo` and, *in parallel*, `common-2_11` then `bar`

See [examples](examples) for more.

### Alternative syntax

The previous example can be written more succinctly as

```scala
lazy val foo = (project in file("foo")).dependsOn(common_2_10)
  .settings(scalaVersion := "2.10.4")

lazy val bar = (project in file("bar")).dependsOn(common_2_11)
  .settings(scalaVersion := "2.11.5")

lazy val (common_2_10, common_2_11) = Project("common", file("common")).cross
  .forVersions("2.10.4", "2.11.5")
```

though that only works in `.scala` files, or in `.sbt` files for SBT versions prior to 0.13.7.

## Contributions

We welcome issues, questions, and contributions on our GitHub project ([https://github.com/lucidsoftware/sbt-cross](github.com/lucidsoftware/sbt-cross)).

[![Build Status](https://travis-ci.org/lucidsoftware/sbt-cross.svg?branch=master)](https://travis-ci.org/lucidsoftware/sbt-cross)

Each major version has a branch. Development for the next major version is on `master`. If a change is needed for an
existing major version, it is cherry-picked  to that branch.
