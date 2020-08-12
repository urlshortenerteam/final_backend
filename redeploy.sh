#!bin/bash
docker pull https://hub.docker.com/repository/docker/reevoo2020/backend:latest
docker stop reevoo:latest
docker system prune -f
docker run -p 80:8080 -dit reevoo:latest --JASYPT_ENCRYPTOR_PASSWORD=SXSYYDS --IP2REGION=./ip2region.db