#!/bin/sh
# Generated wrapper launcher. GitHub Actions does not depend on this file;
# it uses the Gradle setup action directly.
APP_HOME=$(CDPATH= cd -- "$(dirname -- "$0")" && pwd)
exec gradle "$@"
