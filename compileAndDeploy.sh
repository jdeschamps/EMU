#!/bin/bash
exit_required=false

# get MM2 home
MM2_HOME=$1
if ! [ -d "$MM2_HOME" ]; then
	echo "[$MM2_HOME] is not a directory."
	exit_required=true
fi

# tests if mvn is installed
command -v mvn >/dev/null 2>&1 || { echo >&2 "Could not call mvn, are you sure Maven is installed?";}

# tests if the Micro-Manager jars are present and deploy them
MMJ="$MM2_HOME\plugins\Micro-Manager\MMJ_.jar"
MMAcqEngine="$MM2_HOME\plugins\Micro-Manager\MMAcqEngine.jar"
MMCoreJ="$MM2_HOME\plugins\Micro-Manager\MMCoreJ.jar"
MM2_PLUGINS_HOME="$MM2_HOME\mmplugins"

if [ "$exit_required" = false ]; then
	if [ -f "$MMJ" ] && [ -f "$MMAcqEngine" ] && [ -f "$MMCoreJ" ]; then
		# deploy MM2 jars
		mvn install:install-file -Dfile="$MMJ" -DgroupId=org.micromanager  -DartifactId=MMJ_ -Dversion=2.0.0-SNAPSHOT -Dpackaging=jar
		mvn install:install-file -Dfile="$MMAcqEngine" -DgroupId=org.micromanager  -DartifactId=MMAcqEngine -Dversion=2.0.0-SNAPSHOT -Dpackaging=jar
		mvn install:install-file -Dfile="$MMCoreJ" -DgroupId=org.micromanager  -DartifactId=MMCoreJ -Dversion=2.0.0-SNAPSHOT -Dpackaging=jar
		
		# compile EMU
		mvn clean package shade:shade -Dmaven.test.skip=true

		# then deploy it to be used for plugins
		mvn install:install-file -Dfile="target\EMU-1.0-SNAPSHOT.jar" -DgroupId=de.embl.rieslab  -DartifactId=EMU -Dversion=1.0-SNAPSHOT -Dpackaging=jar
		
		# finally copy the jar to MM
		cp -i "target\EMU-1.0-SNAPSHOT.jar" "$MM2_PLUGINS_HOME\EMU-1.0-SNAPSHOT.jar"
		
	else
		echo "Could not find MMJ_.jar, MMAcqEngine.jar or MMCoreJ.jar. Did you input the correct directory?"
		exit_required=true
	fi
fi