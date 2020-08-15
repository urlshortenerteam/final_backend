#!/bin/bash
docker pull reevoo2020/backend:latest
docker pull reevoo2020/redirect-service:latest
docker kill $(docker ps -aq)
docker system prune -f
docker-compose up -d
