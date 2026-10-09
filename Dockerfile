FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /workspace

COPY pom.xml ./
COPY src ./src
RUN mvn -B -DskipTests package

FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

RUN addgroup -S baraigad && adduser -S baraigad -G baraigad
COPY --from=build /workspace/target/baraigadWebApp-*.jar /app/baraigad-api.jar
RUN mkdir -p /app/uploads /app/logs && chown -R baraigad:baraigad /app

USER baraigad
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/baraigad-api.jar"]
