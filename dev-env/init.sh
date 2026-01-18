#!/bin/bash

mkdir -p ./mysql/data
mkdir -p ./mysql/conf

mkdir -p ./redis/data

mkdir -p ./mongodb-data

mkdir -p ./elasticsearch/data
mkdir -p ./elasticsearch/plugins
mkdir -p ./elasticsearch/logs

# 下载ik分词器
curl -o ./ik.zip https://release.infinilabs.com/analysis-ik/stable/elasticsearch-analysis-ik-8.10.4.zip
mkdir ./elasticsearch/plugins/ik
unzip ./ik.zip -d ./elasticsearch/plugins/ik
rm ./ik.zip

mkdir -p ./minio/data
echo "文件已创建"
