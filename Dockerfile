FROM amazoncorretto:17-alpine3.16
ADD target/babyMonitoringAPI.jar babyMonitoringAPI.jar
ENTRYPOINT ["java", "-jar", "babyMonitoringAPI.jar"]