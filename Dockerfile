FROM openjdk:21

ENV LANGUAGE='en_US:en'

COPY target/quarkus-app /deployments/

EXPOSE 8080
USER 185

ENTRYPOINT [ "java", "-jar", "/deployments/quarkus-run.jar" ]
