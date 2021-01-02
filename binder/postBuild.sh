#!/usr/bin/env bash

# https://mybinder.readthedocs.io/en/latest/using/config_files.html#postbuild-run-code-after-installing-the-environment
set -e
jupyter lab build

# Install coursier
curl -Lo coursier https://git.io/coursier-cli && chmod +x coursier


# Install almond for Scala 2.12
./coursier launch --fork almond:0.10.9 --scala 2.12.12 -- \
  --install \
  --id scala212 \
  --display-name "Scala 2.12.12"


# Install almond for Scala 2.11
./coursier launch --fork almond --scala 2.11.12 --
    --install \
    --id scala211 \
    --display-name "Scala 2.11.12"


# Install required Jupyter/JupyterLab extensions
jupyter labextension install \
    @almond-sh/scalafmt \
    @almond-sh/jupyterlab_variableinspector \
    @jupyterlab/toc-extension \
    --minimize=False
