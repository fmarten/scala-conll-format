name := "scala-conll-format"

version := "1.0-SNAPSHOT"

scalaVersion := "2.11.8"

resolvers += "Will's bintray" at "https://dl.bintray.com/willb/maven/"
resolvers += "Typesafe Repo" at "http://repo.typesafe.com/typesafe/releases/"

libraryDependencies += "com.github.tototoshi" %% "scala-csv" % "1.3.4"
libraryDependencies += "org.scalatest" %% "scalatest" % "3.0.3" % "test"