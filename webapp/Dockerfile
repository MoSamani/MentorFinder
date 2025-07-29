FROM openjdk:8-jdk-alpine as build-env
WORKDIR /app
COPY app ./
RUN ./gradlew bootRepackage


FROM openjdk:8-jre-alpine
ENV TZ=Europe/Berlin
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone
WORKDIR /app
COPY --from=build-env /app/build/libs/app*.jar app.jar
ENTRYPOINT ["java", "-Dspring.profiles.active=container", "-jar", "app.jar"]
#CMD ["java", "-jar", "app.jar"]
