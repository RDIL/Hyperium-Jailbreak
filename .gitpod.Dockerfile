FROM gradle:4.10.3-jdk8

USER root

RUN sudo chown gitpod:gitpod /home/gitpod/.gradle
