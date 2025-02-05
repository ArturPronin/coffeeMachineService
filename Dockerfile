FROM openjdk:17-alpine as builder
RUN apk update && apk add maven
WORKDIR /app
COPY pom.xml .
COPY src /app/src
RUN mvn clean package -DskipTests

FROM openjdk:17-alpine
COPY --from=builder /app/target/coffeeMachineService-0.0.1-SNAPSHOT.jar /coffee-machine-service.jar
EXPOSE 8081
ENTRYPOINT ["java", "-jar", "/coffee-machine-service.jar"]