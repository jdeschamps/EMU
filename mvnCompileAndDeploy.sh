#!/bin/bash
# compile project
mvn clean package shade:shade

# then deploy it to be used for plugins
mvn install:install-file -Dfile="target\EMU-1.0-SNAPSHOT.jar" -DgroupId=de.embl.rieslab  -DartifactId=EMU -Dversion=1.0-SNAPSHOT -Dpackaging=jar