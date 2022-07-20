#!/bin/bash
set -euox pipefail
IFS=$'\n\t'

git reset --hard
git pull
cd docker-eles
docker-compose down
cd ..
./mvnw clean package -DskipTests=true
cd docker-eles
docker-compose up --build --detach