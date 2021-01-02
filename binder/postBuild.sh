#!/bin/bash

# https://mybinder.readthedocs.io/en/latest/using/config_files.html#postbuild-run-code-after-installing-the-environment
set -e


# Variables
SCALA_VERSION=2.11.12
ALMOND_VERSION=0.6.0


# Install coursier
curl -Lo coursier https://git.io/coursier-cli && chmod +x coursier


# Install almond for Scala 2.11
./coursier bootstrap \
    -r jitpack \
    -i user -I user:sh.almond:scala-kernel-api_${SCALA_VERSION}:${ALMOND_VERSION} \
    sh.almond:scala-kernel_${SCALA_VERSION}:${ALMOND_VERSION} \
    -o almond

./almond --install --id scala_2_11_12  --display-name "Scala 2.11.12"  \
    --command "java -XX:MaxRAMPercentage=80.0 -jar almond --id scala_2_11_12 --display-name 'Scala 2.11.12'" \
    --copy-launcher \
    --metabrowse


# Install required Jupyter/JupyterLab extensions
jupyter labextension install \
    @almond-sh/scalafmt \
    @almond-sh/jupyterlab_variableinspector \
    @jupyterlab/toc \
    --minimize=False
