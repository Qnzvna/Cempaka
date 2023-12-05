FROM amazoncorretto:17.0.9-alpine3.18

RUN mkdir /opt/cyclone /opt/cyclone/parcels /etc/cyclone /var/log/cyclone

EXPOSE 8000
VOLUME /etc/cyclone
VOLUME /opt/cyclone/parsels
VOLUME /var/log/cyclone

COPY target/dependency/guava.jar /opt/cyclone/
COPY target/cyclone-daemon.jar /opt/cyclone/
COPY docker/conf/configuration.yml /etc/cyclone/

CMD java -jar /opt/cyclone/cyclone-daemon.jar server /etc/cyclone/configuration.yml