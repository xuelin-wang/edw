FROM java:8-alpine
MAINTAINER Your Name <you@example.com>

ADD target/uberjar/edw.jar /edw/app.jar

EXPOSE 3000

CMD ["java", "-jar", "/edw/app.jar"]
