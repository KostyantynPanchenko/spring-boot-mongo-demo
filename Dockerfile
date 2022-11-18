FROM eclipse-temurin:17-jre-alpine@sha256:ddcde24217dc1a9df56c7dd206ee1f4dc89f6988c9364968cd02c6cbeb21b1de

RUN addgroup --system mongogroup && adduser --system mongo --ingroup mongogroup
USER mongo

ARG APP_VERSION
ARG JAR_FILE=/build/libs/spring-boot-mongo-${APP_VERSION}.jar

COPY ${JAR_FILE} /mongo-app.jar

USER mongo:mongogroup

ENTRYPOINT java -Xms128M -Xmx1G -jar /mongo-app.jar
