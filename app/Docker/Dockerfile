FROM java:8 
ENV APP_VERSION="0.1.0"
ENV APP_URL="https://bintray.com/artifact/download/yetu/maven/com/yetu/app/${APP_VERSION}/app-${APP_VERSION}.jar"

RUN mkdir -p /srv/emscher/log
RUN mkdir -p /srv/emscher/config/
RUN wget $APP_URL -O /srv/emscher/emscher.jar

VOLUME ["/srv/emscher/log"]
WORKDIR /srv/emscher

COPY config.yml /srv/emscher/config/

EXPOSE 80
ENTRYPOINT ["/usr/lib/jvm/java-8-openjdk-amd64/bin/java"]
CMD ["-jar", "/srv/emscher/emscher.jar","server","/srv/emscher/config/config.yml"]  
