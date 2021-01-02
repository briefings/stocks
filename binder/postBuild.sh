#!/bin/bash

# https://mybinder.readthedocs.io/en/latest/using/config_files.html#postbuild-run-code-after-installing-the-environment
set -e


# Variables
ALMOND_VERSION=0.10.9
SCALA_VERSION_PREV=2.11.12
ALMOND_VERSION_PREV=0.6.0
JUPYTER_CONFIG_DIR=$(jupyter --config-dir)


# Install coursier
curl -fLo coursier https://github.com/coursier/coursier/releases/download/v2.0.3/cs-x86_64-pc-linux
chmod +x coursier


# Install almond for Scala 2.11
./coursier launch --fork almond --scala 2.11.12 -- --install \
    --id scala211 \
    --display-name "Scala (2.11)" \
    --env "JAVA_OPTS=-XX:MaxRAMPercentage=80.0"


# Install almond for Scala 2.13
./coursier launch --fork almond --scala 2.13.3 -- --install \
    --id scala213 \
    --display-name "Scala (2.13)" \
    --env "JAVA_OPTS=-XX:MaxRAMPercentage=80.0"


# Install almond for Scala 2.12
./coursier launch --fork almond --scala 2.12.12 -- --install \
    --id scala212 \
    --display-name "Scala (2.12)" \
    --env "JAVA_OPTS=-XX:MaxRAMPercentage=80.0"


# Hence
rm -f coursier


# Install required Jupyter/JupyterLab extensions
jupyter labextension install --minimize=False jupyterlab-plotly \
    @almond-sh/scalafmt \
    @almond-sh/jupyterlab_variableinspector \
    @jupyterlab/toc


# Classic notebook
mkdir -p ${JUPYTER_CONFIG_DIR}/nbconfig/
cat > ${JUPYTER_CONFIG_DIR}/nbconfig/notebook.json <<- EOF
{
  "CodeCell": {
    "cm_config": {
      "indentUnit": 2
    }
  }
}
EOF


# Jupyter Lab notebook
mkdir -p ${JUPYTER_CONFIG_DIR}/lab/user-settings/@jupyterlab/notebook-extension/
cat > ${JUPYTER_CONFIG_DIR}/lab/user-settings/@jupyterlab/notebook-extension/tracker.jupyterlab-settings <<- EOF
{
    "codeCellConfig": {
      "tabSize": 2
    }
}
EOF


# Jupyter Lab editor
mkdir -p ${JUPYTER_CONFIG_DIR}/lab/user-settings/@jupyterlab/fileeditor-extension/
cat > ${JUPYTER_CONFIG_DIR}/lab/user-settings/@jupyterlab/fileeditor-extension/plugin.jupyterlab-settings <<- EOF
{
    "editorConfig": {
      "tabSize": 2,
    }
}
EOF