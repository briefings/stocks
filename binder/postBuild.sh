#!/usr/bin/env bash

# https://mybinder.readthedocs.io/en/latest/using/config_files.html#postbuild-run-code-after-installing-the-environment
set -e


# Install coursier
curl -Lo coursier https://git.io/coursier-cli && chmod +x coursier


# Install almond for Scala 2.12
./coursier launch --fork almond:0.10.9 --scala 2.12.12 -- \
  --install \
  --id scala212 \
  --display-name "Scala 2.12.12" \
  --env "JAVA_OPTS=-XX:MaxRAMPercentage=80.0" \
  --variable-inspector \
  </dev/null 2>&1 | grep -v '^Download'


# Install almond for Scala 2.11
./coursier launch --fork almond --scala 2.11.12 --
    --install \
    --id scala211 \
    --display-name "Scala 2.11.12" \
    --env "JAVA_OPTS=-XX:MaxRAMPercentage=80.0" \
    --variable-inspector \
    </dev/null 2>&1 | grep -v '^Download'


# Install required Jupyter/JupyterLab extensions
jupyter labextension install \
    @almond-sh/scalafmt \
    @almond-sh/jupyterlab_variableinspector \
    @jupyterlab/toc-extension \
    --minimize=False
