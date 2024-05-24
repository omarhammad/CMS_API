FROM gradle:jdk21-alpine
RUN apk add --no-cache nodejs npm
WORKDIR /home/gradle/project
USER gradle
