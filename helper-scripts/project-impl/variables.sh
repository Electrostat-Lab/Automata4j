#!/bin/sh

# Maven sonatype stuff
# ---------------------
sonatype_url="https://s01.oss.sonatype.org/service/local/staging/deploy/maven2/"
repository="ossrh"
groupId="io.github.software-hardware-codesign"
maven_version="3.9.3"
maven_bin="./apache-maven-$maven_version/bin/mvn"
desktop_pomFile="./helper-scripts/project-impl/publishing/automata4j.pom"
passphrase="avrsandbox"

desktop_artifactId_release="automata4j"

desktop_artifactId_debug="automata4j-debug"

settings="./helper-scripts/project-impl/publishing/maven-settings.xml"
build_dir="./automata4j/build/libs"
# ---------------------
