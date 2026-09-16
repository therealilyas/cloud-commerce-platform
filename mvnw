#!/bin/sh
set -eu
MVNW_VERSION=3.9.9
WRAPPER_DIR="${MAVEN_USER_HOME:-$HOME/.m2}/wrapper/dists/apache-maven-${MVNW_VERSION}"
MAVEN_HOME="$WRAPPER_DIR/apache-maven-${MVNW_VERSION}"
if [ ! -x "$MAVEN_HOME/bin/mvn" ]; then
  mkdir -p "$WRAPPER_DIR"
  ARCHIVE="$WRAPPER_DIR/apache-maven-${MVNW_VERSION}-bin.tar.gz"
  curl -fsSL "https://repo.maven.apache.org/maven2/org/apache/maven/apache-maven/${MVNW_VERSION}/apache-maven-${MVNW_VERSION}-bin.tar.gz" -o "$ARCHIVE"
  tar -xzf "$ARCHIVE" -C "$WRAPPER_DIR"
fi
exec "$MAVEN_HOME/bin/mvn" "$@"
