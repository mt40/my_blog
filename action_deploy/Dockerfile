#FROM ubuntu:18.04
FROM debian:9.5-slim

LABEL "com.github.actions.name"="Deploy ScalaJS app"
LABEL "com.github.actions.description"="Build and deploy ScalaJS app using sbt & npm"
LABEL "com.github.actions.icon"="cloud-lightning"
LABEL "com.github.actions.color"="red"

ADD entrypoint.sh /entrypoint.sh
ENTRYPOINT ["/entrypoint.sh"]