FROM almondsh/almond:0.10.9-scala-2.12.12


# pip install
RUN pip install --no-cache-dir jupyterlab==3.0.0 nbdime==2.1.0


# Variables
ARG NB_USER=greyhypotheses
ARG NB_UID=1000
ENV USER ${NB_USER}
ENV NB_UID ${NB_UID}
ENV HOME /home/${NB_USER}


# User
RUN adduser --disabled-password \
    --gecos "Default user" \
    --uid ${NB_UID} \
    ${NB_USER}


# Make sure the contents of our repo are in ${HOME}
COPY . ${HOME}
USER root
RUN chown -R ${NB_UID} ${HOME}
USER ${NB_USER}


# Prepare
RUN apt-get update && apt-get install -y \
    openjdk-8-jdk \
    ca-certificates-java \
    graphviz \
    curl && \
    apt-get clean


# Scala
ARG SCALA_VERSION=2.12.12
ARG SCALA_BINARY_ARCHIVE_NAME=scala-${SCALA_VERSION}
ARG SCALA_BINARY_DOWNLOAD_URL=https://downloads.lightbend.com/scala/${SCALA_VERSION}/${SCALA_BINARY_ARCHIVE_NAME}.tgz
ENV SCALA_HOME  /usr/local/scala

RUN wget -q ${SCALA_BINARY_DOWNLOAD_URL} && \
    tar -zxvf ${SCALA_BINARY_ARCHIVE_NAME}.tgz && \
    cp -R ${SCALA_BINARY_ARCHIVE_NAME} /usr/local/scala && \
    rm -R scala*


# Cousier
RUN cd /usr/local/ && wget -q https://git.io/coursier-cli && \
    chmod +x coursier && \
    ./coursier launch --fork almond:0.10.9 --scala 2.12.12 -- --install && \
    rm -f coursier


# Extensions
RUN jupyter labextension install \
    @almond-sh/scalafmt \
    @almond-sh/jupyterlab_variableinspector \
    @jupyterlab/toc-extension

COPY --chown=1000:100 notebooks/ $HOME