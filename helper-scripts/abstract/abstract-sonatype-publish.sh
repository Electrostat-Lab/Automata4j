#!/bin/bash

function generateGenericPom() {
    local groupId=$1
    local artifactId=$2
    local version=$3
    local output=$4

    config="<project xmlns=\"http://maven.apache.org/POM/4.0.0\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd\"> \
    <modelVersion>4.0.0</modelVersion> \
    <groupId>${groupId}</groupId> \
    <artifactId>${artifactId}</artifactId> \
    <version>${version}</version> \
    <name>The Automata4j Framework</name> \
    <packaging>jar</packaging> \
    <description>A classic finite state automaton framework for JVM Applications (Android and jMonkeyEngine Applications).</description> \
    <url>https://github.com/Software-Hardware-Codesign/Automata4j</url> \
    <licenses>  \
      <license> \
          <name>The AvrSandbox Project, Automata4j BSD-3 Clause License</name> \
          <url>https://github.com/Software-Hardware-Codesign/Automata4j/blob/master/LICENSE</url> \
      </license> \
    </licenses> \
    <developers> \
      <developer> \
        <name>Pavly Gerges aka. pavl_g</name> \
        <email>pepogerges33@gmail.com</email> \
        <organization>AvrSandbox Project</organization> \
        <organizationUrl>https://github.com/Software-Hardware-Codesign</organizationUrl> \
      </developer> \
    </developers> \
    <scm> \
      <connection>scm:git://github.com/Software-Hardware-Codesign/Automata4j.git</connection> \
      <developerConnection>scm:git:ssh://github.com/Software-Hardware-Codesign/Automata4j.git</developerConnection> \
      <url>https://github.com/Software-Hardware-Codesign/Automata4j/tree/master</url> \
    </scm> \
</project> \
"
    echo $config > ${output}
}

function publishBuild() {
    local artifactId=$1
    local artifact=$2
    local version=$3
    local javadoc_jar=$4
    local sources_jar=$5
    local pomFile=$6

    ${maven_bin} gpg:sign-and-deploy-file -s ${settings} -Durl=${sonatype_url} \
                                                         -DartifactId=${artifactId} \
                                                         -DrepositoryId=${repository} \
                                                         -Dversion=${version} \
                                                         -DpomFile=${pomFile} \
                                                         -Dgpg.passphrase=${passphrase} \
                                                         -Dfile=${artifact} \
                                                         -Djavadoc=${javadoc_jar} \
                                                         -Dsources=${sources_jar} 


    return $?
}
