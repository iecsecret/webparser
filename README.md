# WebParser

This project taking screenhots from given urls.


## Deploy

mvn clean install -Dspring.profiles.active=test
or SPRING_PROFILES_ACTIVE as os variable

## Install 


##run
nohup java  -Xms512m -Xmx1024m -Dspring.profiles.active=test -jar webparser.jar > /dev/null 2>&1 &