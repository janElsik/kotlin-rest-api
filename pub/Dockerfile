#FROM eclipse-temurin:17-jre
#WORKDIR /app
### bootable jar
#ADD build/libs/*.jar app.jar
### entrypoint shell script for running application with custom parameters and exec mode (for proper SIGTERM propagating to JVM)
#ADD entrypoint.sh entrypoint.sh
### make entryopoint script executable
#RUN chmod +x entrypoint.shr
#
#EXPOSE 8080


# using multistage docker build
# ref: https://docs.docker.com/develop/develop-images/multistage-build/

# temp container to build using gradle
FROM gradle:7.5.1-jdk AS TEMP_BUILD_IMAGE
ENV APP_HOME=/usr/app/
WORKDIR $APP_HOME
COPY build.gradle.kts settings.gradle.kts $APP_HOME

COPY gradle $APP_HOME/gradle
COPY --chown=gradle:gradle . /home/gradle/src
USER root
RUN chown -R gradle /home/gradle/src

RUN gradle build || return 0
COPY . .
RUN gradle clean build

# actual container
FROM eclipse-temurin:17-jre
ENV ARTIFACT_NAME=pub-0.0.1.jar
ENV APP_HOME=/usr/app/

WORKDIR $APP_HOME
COPY --from=TEMP_BUILD_IMAGE $APP_HOME/build/libs/$ARTIFACT_NAME .

EXPOSE 8080
ENTRYPOINT exec java -jar ${ARTIFACT_NAME}
