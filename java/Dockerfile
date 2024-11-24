FROM maven:3.9-amazoncorretto-21 as builder

WORKDIR /app
COPY ./ /app

RUN mvn -DskipTests=true clean
RUN mvn -B -DskipTests install
RUN mkdir /app-jar-output
RUN cp /app/target/*.jar /app-jar-output

FROM openjdk:21-bullseye

ENV JAVA_OPTS ""
ENV PROJECT_NAME rinha-api
ENV TZ America/Sao_Paulo
ENV TIMEZONE America/Sao_Paulo

WORKDIR /opt/${PROJECT_NAME}

RUN apt-get update -y
RUN apt-get install -y tzdata && \
    ln -sf /bin/bash /bin/sh

COPY --from=builder /app-jar-output/ .
ENTRYPOINT java ${JAVA_OPTS} -jar $(ls *.jar)