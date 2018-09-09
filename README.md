# WebParser

This project taking screenshots from given urls.


## Deploy

mvn clean install -Dspring.profiles.active=test
or SPRING_PROFILES_ACTIVE as os variable

## Install 


##run
##dev
nohup java  -Xms512m -Xmx1024m -Dspring.profiles.active=dev -jar webparser.jar > /dev/null 2>&1 &

##test
nohup java  -Xms512m -Xmx1024m -Dspring.profiles.active=test -jar webparser.jar > /dev/null 2>&1 &

##prod
nohup java  -Xms512m -Xmx1024m -Dspring.profiles.active=prod -jar webparser.jar > /dev/null 2>&1 &