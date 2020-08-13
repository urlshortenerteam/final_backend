#!/bin/bash
docker pull https://hub.docker.com/repository/docker/reevoo2020/backend:latest
docker kill backend
docker kill prometheus
docker kill grafana
docker kill cadvisor
docker kill redis
docker system prune -f
docker-compose up -d
