#!/bin/bash
docker kill $(docker ps -aq)
docker rmi -f $(docker images -q)
docker system prune -f
docker volume rm $(docker volume ls -qf dangling=true)
docker pull reevoo2020/backend:latest
docker pull reevoo2020/redirect-service:1.2
docker pull reevoo2020/visitLog-service:1.0
docker network rm $(docker network ls -q)
docker-compose up -d --scale whoami=2
