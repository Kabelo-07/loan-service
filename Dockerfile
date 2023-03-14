FROM adoptopenjdk:11-jre-hotspot

ENV JAVA_OPTS="-Xmx512m -Xms256m"

ADD target/loan-service-*.jar /app/loan-service.jar

ENTRYPOINT ["java","-jar","/app/loan-service.jar"]