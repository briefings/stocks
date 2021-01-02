#!/bin/bash

# https://mybinder.readthedocs.io/en/latest/using/config_files.html#postbuild-run-code-after-installing-the-environment
set -e


# Variables
SCALA_VERSION=2.11.12
ALMOND_VERSION=0.6.0


# Install coursier
curl -fLo coursier https://github.com/coursier/coursier/releases/download/v2.0.3/cs-x86_64-pc-linux
chmod +x coursier


# Install almond for Scala 2.11
coursier bootstrap \
    -r jitpack \
    -i user -I user:sh.almond:scala-kernel-api_${SCALA_VERSION}:${ALMOND_VERSION} \
    sh.almond:scala-kernel_${SCALA_VERSION}:${ALMOND_VERSION} \
    -o almond
./almond --install


# Install required Jupyter/JupyterLab extensions
jupyter contrib nbextension install --user
jupyter nbextension enable --py widgetsnbextension

jupyter labextension install \
    @jupyter-widgets/jupyterlab-manager@0.38 \
    @almond-sh/scalafmt \
    @almond-sh/jupyterlab_variableinspector \
    @jupyterlab/toc \
    --minimize=False
