FROM openjdk:latest
COPY ./target/sem-scrum-team-2-0.1.3-jar-with-dependencies.jar /tmp
WORKDIR /tmp
ENTRYPOINT ["java", "-jar", "sem-scrum-team-2-0.1.3-jar-with-dependencies.jar"]