FROM openjdk:8-jdk-stretch
EXPOSE 8080
ADD target/autobuildreport.jar autobuildreport.jar
ENTRYPOINT ["java","-jar","autobuildreport.jar"]
