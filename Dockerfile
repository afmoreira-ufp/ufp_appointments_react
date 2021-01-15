FROM openjdk:11-jre-slim
COPY target/ufp_applications-1.jar /usr/local/lib/ufp_applications-1.jar
EXPOSE 9002
ENTRYPOINT ["java","-jar","/usr/local/lib/ufp_applications-1.jar"]
