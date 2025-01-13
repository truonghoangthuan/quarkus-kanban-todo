# Build stage
FROM registry.access.redhat.com/ubi8/openjdk-21:latest AS build

# Set the working directory
WORKDIR /workspace/app

# Copy the Maven wrapper and POM
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .
COPY .mvn/wrapper/maven-wrapper.jar .
COPY .mvn/wrapper/maven-wrapper.properties .

# Copy the source code
COPY src src

# Build the application
RUN ./mvnw package -DskipTests

# Runtime stage
FROM registry.access.redhat.com/ubi8/openjdk-21-runtime:latest

# Set environment variables
ENV LANGUAGE='en_US:en'

# Copy the built artifact from build stage
COPY --from=build /workspace/app/target/quarkus-app/lib/ /deployments/lib/
COPY --from=build /workspace/app/target/quarkus-app/*.jar /deployments/
COPY --from=build /workspace/app/target/quarkus-app/app/ /deployments/app/
COPY --from=build /workspace/app/target/quarkus-app/quarkus/ /deployments/quarkus/

# Create a non-root user
USER 185

# Expose the application port
EXPOSE 8080

# Set the entrypoint
ENTRYPOINT [ "java", "-jar", "/deployments/quarkus-run.jar" ]