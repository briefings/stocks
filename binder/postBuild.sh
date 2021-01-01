#!/usr/bin/env bash
set -e

# Install Coursier
curl -fLo cs https://github.com/coursier/coursier/releases/download/v2.0.3/cs-x86_64-pc-linux
chmod +x cs


SCALA_VERSION=2.11.12
ALMOND_VERSION=0.6.0
JUPYTER_CONFIG_DIR=$(jupyter --config-dir)


# Install almond 0.6.0 for Scala 2.11
./cs bootstrap \
  -r jitpack \
  -i user -I user:sh.almond:scala-kernel-api_${SCALA_VERSION}:${ALMOND_VERSION} \
  sh.almond:scala-kernel_${SCALA_VERSION}:${ALMOND_VERSION} \
  --sources --default=true \
  -o almond-scala-2.11 \
  </dev/null 2>&1 | grep -v '^Download'
./almond-scala-2.11 --install --id scala211 --display-name "Scala (2.11)" \
  --command "java -XX:MaxRAMPercentage=80.0 -jar almond-scala-2.11 --id scala211 --display-name 'Scala (2.11)'" \
  --copy-launcher \
  --metabrowse
rm -f almond-scala-2.11


# Install required Jupyter/JupyterLab extensions
jupyter labextension install --minimize=False \
  jupyterlab/toc \
  jupyterlab-plotly \
  @almond-sh/scalafmt \
  @almond-sh/jupyterlab_variableinspector


# Jupyter Lab Notebook
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