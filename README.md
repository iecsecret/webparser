# WebParser

This project taking screenshots from given urls.


## Deploy

mvn clean install -Dspring.profiles.active=test
or SPRING_PROFILES_ACTIVE as os variable

## Install  Driver
debian:
wget -O /tmp/chromedriver.zip http://chromedriver.storage.googleapis.com/2.11/chromedriver_linux64.zip && sudo unzip /tmp/chromedriver.zip chromedriver -d /usr/local/bin/;


##run
##dev
nohup java  -Xms512m -Xmx1024m -Dspring.profiles.active=dev -jar webparser.jar > /dev/null 2>&1 &

##test
nohup java  -Xms512m -Xmx1024m -Dspring.profiles.active=test -jar webparser.jar > /dev/null 2>&1 &

##prod
nohup java  -Xms512m -Xmx1024m -Dspring.profiles.active=prod -jar webparser.jar > /dev/null 2>&1 &


##API TEST

 curl -X POST \
  http://localhost:8099/api/takeSnapshotSync \
  -H 'content-type: application/json' \
  -d '["http://www.amazon.com","http://www.google.com”]'
  
##About Kafka
Place proper Kafka(AMQP) settings instead of embeded Kafka in order to benefit from MQ and concurrent execution.
 