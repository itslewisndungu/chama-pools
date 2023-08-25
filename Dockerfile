from maven:3.8.5-openjdk-17 AS build
copy . .
RUN mvn clean package -DskipsTests -Dspring.profiles.active=prod

from openjdk:22-slim
COPY --from=build /target/chamapool-0.0.1-SNAPSHOT.jar chamapool.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","chamapool.jar"]
