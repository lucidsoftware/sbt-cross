#sbt-cross

A better solution for building multiple Scala versions (cross compiling) in SBT.

## Purpose

Since compilation outputs are not binary compatible across all versions of Scala, SBT has functionality to make this
less painful. One of these is `crossScalaVersions`.

However, `crossScalaVersions` is problematic:

1. Performance - Each Scala version is handled sequentially (not in parallel).
1. Subprojects - Project classpath dependencies can be tricky.
1. Aggregation - Aggregation doesn't take into account `crossScalaVersions` of subprojects, hence
  [sbt-doge](https://github.com/sbt/sbt-doge).
1. Cross paths - Very many tasks don't play nicely. E.g. if you use sbt-native-packager and `+debian:package`, you'll
  build two debs on top of each other.
1. General funniness - For example, [sbt-sonatype](https://github.com/xerial/sbt-sonatype#using-with-sbt-release-plugin) requires
  extra wrangling to work with `crossScalaVersions`.

Ultimately, `crossScalaVersions` is a hack that tries to reuse projects and tasks by mutating settings.

sbt-cross solves all by simply splitting projects: one for each version fo Scala. Moreover, it
generalizes this approach to other any cross-compilation-like use.

## Install

Requires SBT 0.13+.

In project/plugins.sbt, add

```scala
addSbtPlugin("com.lucidchart" % "sbt-cross" % "3.0")
```

For the latest development version,

```scala
resolvers += Resolver.sonatypeRepo("snapshots")

addSbtPlugin("com.lucidchart" % "sbt-cross" % "master-SNAPSHOT")
```

## Example

Suppose there is a project `pipers` that uses Scala 2.11, a project `drummers` that uses Scala 2.12, and they depend on a
project `common` that compiles with both versions.

This cannot be done with `crossScalaVersions`, but it can with sbt-cross.

```scala
lazy val pipers = project.dependsOn(common_2_10)
  .settings(scalaVersion := "2.11.8")

lazy val drummers = project.dependsOn(common_2_11)
  .settings(scalaVersion := "2.12.1")

lazy val common = project.cross
lazy val common_2_10 = common("2.11.8")
lazy val common_2_11 = common("2.12.1")
```

This defines four projects: `pipers`, `drummers`, `common-2_11`, and `common-2_12`.

SBT will concurrently compile the right ones in the right order.

* `sbt pipers/compile` will compile `common-2_11`  then `pipers`.

* `sbt drummers/compile` will compile `common-2_12`  then `drummers`.

* `sbt compile` will compile `common-2_11` then `pipers` and, *in parallel*, `common-2_12` then `drummers`.

See [examples](examples) for more.

### Additional crossing

You can cross by more than just Scala versions, and even chaining cross versions. There is currently no documentation
for this, but `LibraryVersionAxis` is an example.

## Contributions

We welcome issues, questions, and contributions on our GitHub project
([https://github.com/lucidsoftware/sbt-cross](github.com/lucidsoftware/sbt-cross)).

[![Build Status](https://travis-ci.org/lucidsoftware/sbt-cross.svg?branch=master)](https://travis-ci.org/lucidsoftware/sbt-cross)
