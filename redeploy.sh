#!/bin/bash
docker kill $(docker ps -aq)
docker rmi -f $(docker images -q)
docker system prune -f
docker volume rm $(docker volume ls -qf dangling=true)
docker network rm $(docker network ls -q)
docker pull reevoo2020/backend:latest
docker pull reevoo2020/redirect-service:latest
docker-compose up -d
