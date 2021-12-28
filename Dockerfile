FROM openjdk:8u282-jre-slim

MAINTAINER Hanjian
ENV TZ "Asia/Shanghai"
ENV LANG C.UTF-8

RUN mkdir -p /harley/server
RUN mkdir -p /harley/server/logs
WORKDIR /harley/server

EXPOSE 8080

ADD ./harley-admin/target/harley-admin.jar ./harley-admin.jar

CMD ["java","-jar","harley-admin.jar"]
