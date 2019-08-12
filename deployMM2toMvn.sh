#!/bin/bash
# Simple bash script to add the main Micro-manager jars to Maven.
# Set your Micro-manager path here:
MM2_PLUGINS_HOME="D:\Micromanager\Micro-Manager-2.0gammaNB190725\plugins\Micro-Manager"

mvn install:install-file -Dfile="$MM2_PLUGINS_HOME\MMJ_.jar" -DgroupId=org.micromanager  -DartifactId=MMJ_ -Dversion=2.0.0-SNAPSHOT -Dpackaging=jar
mvn install:install-file -Dfile="$MM2_PLUGINS_HOME\MMAcqEngine.jar" -DgroupId=org.micromanager  -DartifactId=MMAcqEngine -Dversion=2.0.0-SNAPSHOT -Dpackaging=jar
mvn install:install-file -Dfile="$MM2_PLUGINS_HOME\MMCoreJ.jar" -DgroupId=org.micromanager  -DartifactId=MMCoreJ -Dversion=2.0.0-SNAPSHOT -Dpackaging=jar
