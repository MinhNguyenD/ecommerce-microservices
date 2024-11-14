### Build
# Get image with installed dependecies aka maven and java
FROM maven:3.9.9-eclipse-temurin-21-alpine AS build

# Set working dir
WORKDIR /app
# Copy pom file to build
COPY pom.xml .
# Copy src folder to build
COPY src ./src

# Build the project which creates jar file
RUN mvn package -DskipTests

### Run
# Get image with installed dependecy i.e java to run the built jar file
FROM amazoncorretto:21-alpine3.17-jdk

WORKDIR /app

#Copy the built jar file from previous layer to current layer app dir and rename it to app.jar
COPY --from=build /app/target/auth-service-0.0.1-SNAPSHOT.jar app.jar

# Run the app
ENTRYPOINT ["java", "-jar", "app.jar"]


