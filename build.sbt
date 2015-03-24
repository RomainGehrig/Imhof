organization := "ch.epfl.imhof"

name := "Imhof ftw"

version := "0.1-SNAPSHOT"

autoScalaLibrary := false

javaSource in Compile := baseDirectory.value / "src"

javaSource in Test := baseDirectory.value / "test"

resourceDirectory := baseDirectory.value / "data"

unmanagedResourceDirectories in Test += baseDirectory.value / "data"

libraryDependencies += "com.novocode" % "junit-interface" % "0.8" % "test->default"

