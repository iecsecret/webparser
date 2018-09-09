#!/usr/bin/env bash
nohup java  -Xms512m -Xmx1024m -Dspring.profiles.active=test -jar webparser.jar > /dev/null 2>&1 &