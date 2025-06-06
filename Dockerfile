FROM maven:3.9.6-eclipse-temurin-17-alpine AS build

WORKDIR /app

COPY pom.xml .

COPY src ./src

RUN mvn clean install -DskipTests

FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

EXPOSE 8080

COPY --from=build /app/target/alertachuva-api-0.0.1-SNAPSHOT.jar app.jar

COPY wait-for-it.sh /usr/local/bin/wait-for-it.sh
RUN chmod +x /usr/local/bin/wait-for-it.sh

RUN addgroup -S appgroup && adduser -S appuser -G appgroup

RUN chown -R appuser:appgroup /app

USER appuser

ENTRYPOINT ["/usr/local/bin/wait-for-it.sh", "oracle-db:1521", "--", "java", "-jar", "app.jar"]

CMD ["java", "-jar", "app.jar"]