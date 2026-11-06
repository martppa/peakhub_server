FROM public.ecr.aws/amazoncorretto/amazoncorretto:17
WORKDIR /app
COPY build/libs/PeakHubServer.jar app.jar
EXPOSE 5801
ENTRYPOINT ["java", "-Xms256m", "-Xmx2048m", "-jar", "app.jar"]
