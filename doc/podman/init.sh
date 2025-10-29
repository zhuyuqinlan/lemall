#!/bin/bash
set -e

# ---------- 环境变量 ----------
REDIS_PASSWD=123456
MYSQL_PASSWD=123456
MINIO_USER=zhuyuqinlan
MINIO_PASSWD=12345678
MONGO_ROOT_USER=root
MONGO_PASSWD=123456

# ---------- 创建数据目录 ----------
mkdir -p ./mysql/data ./mysql/conf
mkdir -p ./redis/data
mkdir -p ./mongodb-data
mkdir -p ./elasticsearch/data ./elasticsearch/plugins ./elasticsearch/logs
mkdir -p ./minio/data

# 设置 Elasticsearch 目录权限 (1000:1000 用户)
chown -R 1000:1000 ./elasticsearch
chmod -R 750 ./elasticsearch

# ---------- 下载 IK 分词器 ----------
curl -o ./ik.zip https://release.infinilabs.com/analysis-ik/stable/elasticsearch-analysis-ik-8.10.4.zip
mkdir -p ./elasticsearch/plugins/ik
unzip ./ik.zip -d ./elasticsearch/plugins/ik
rm ./ik.zip

# ---------- 创建 Pod ----------
POD_NAME=lemall-pod
podman pod exists $POD_NAME && podman pod rm -f $POD_NAME
podman pod create --name $POD_NAME \
  -p 6379:6379 \
  -p 3306:3306 \
  -p 9000:9000 \
  -p 9001:9001 \
  -p 9200:9200 \
  -p 5601:5601 \
  -p 27017:27017

# ---------- Redis ----------
podman run -d --pod $POD_NAME --name lemall-redis \
  -v $(pwd)/redis/data:/data:Z \
  docker.io/redis:5.0.14 \
  redis-server --requirepass $REDIS_PASSWD

# ---------- MySQL ----------
podman run -d --pod $POD_NAME --name lemall-mysql \
  -v $(pwd)/mysql/data:/var/lib/mysql:Z \
  -v $(pwd)/mysql/conf:/etc/mysql/conf.d:Z \
  -e MYSQL_ROOT_PASSWORD=$MYSQL_PASSWD \
  docker.io/mysql:8.0

# ---------- MinIO ----------
podman run -d --pod $POD_NAME --name lemall-minio \
  -v $(pwd)/minio/data:/data:Z \
  -e MINIO_ROOT_USER=$MINIO_USER \
  -e MINIO_ROOT_PASSWORD=$MINIO_PASSWD \
  docker.io/minio/minio:RELEASE.2025-03-12T18-04-18Z-cpuv1 \
  server --console-address ":9001" /data

# ---------- Elasticsearch ----------
podman run -d --pod $POD_NAME --name lemall-elasticsearch \
  -v $(pwd)/elasticsearch/data:/usr/share/elasticsearch/data:Z \
  -v $(pwd)/elasticsearch/plugins:/usr/share/elasticsearch/plugins:Z \
  -v $(pwd)/elasticsearch/logs:/usr/share/elasticsearch/logs:Z \
  -e ES_JAVA_OPTS="-Xms512m -Xmx512m" \
  -e discovery.type=single-node \
  -e xpack.security.enabled=false \
  --user 1000:1000 \
  docker.io/elasticsearch:8.10.4

# ---------- Kibana ----------
podman run -d --pod $POD_NAME --name lemall-kibana \
  -e I18N_LOCALE=zh-CN \
  -e ELASTICSEARCH_HOSTS=http://lemall-elasticsearch:9200 \
  -e TZ=Asia/Shanghai \
  docker.io/kibana:8.10.4

# ---------- MongoDB ----------
podman run -d --pod $POD_NAME --name lemall-mongodb \
  -v $(pwd)/mongodb-data:/data/db:Z \
  -e MONGO_INITDB_ROOT_USERNAME=$MONGO_ROOT_USER \
  -e MONGO_INITDB_ROOT_PASSWORD=$MONGO_PASSWD \
  docker.io/mongo:5.0.9

# ---------- 输出状态 ----------
echo "Podman pod 部署完成！"
podman pod ps
podman ps --pod
