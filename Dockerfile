FROM openjdk:latest
COPY ./target/sem-scrum-team-2-0.7.1-jar-with-dependencies.jar /tmp
WORKDIR /tmp
ENTRYPOINT ["java", "-jar", "HumanPopulationInsights.jar"]