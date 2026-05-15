#!/bin/sh
# Wrapper bootstrap minimal. Android Studio regenerará el binario gradle-wrapper.jar
# la primera vez que abras el proyecto. Si compilás por terminal sin Android Studio,
# corré `gradle wrapper` una vez para generar el jar (necesita Gradle instalado),
# o copialo desde cualquier otro proyecto Android.
DIR="$(cd "$(dirname "$0")" && pwd)"
exec java -jar "$DIR/gradle/wrapper/gradle-wrapper.jar" "$@"
