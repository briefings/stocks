FROM almondsh/almond:latest

USER root

RUN apt-get update && apt-get install -y \
    openjdk-8-jdk \
    ca-certificates-java \
    graphviz \
    curl && \
    apt-get clean

RUN wget -q https://git.io/coursier-cli && \
    chmod +x coursier && \
    ./coursier launch --fork almond:0.10.9 --scala 2.12.12 -- --install && \
    rm -f coursier

ENV DEFAULT_KERNEL_NAME "scala"

USER $NB_UID

RUN jupyter labextension install @jupyterlab/plotly-extension \
    @almond-sh/scalafmt \
    @almond-sh/jupyterlab_variableinspector \
    @jupyterlab/toc

COPY --chown=1000:100 notebooks/ $HOME