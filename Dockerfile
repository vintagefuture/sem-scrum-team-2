FROM openjdk:latest
COPY ./target/HumanPopulationInsights.jar /tmp
WORKDIR /tmp
ENTRYPOINT ["java", "-jar", "HumanPopulationInsights.jar", "db:3306", "10000"]