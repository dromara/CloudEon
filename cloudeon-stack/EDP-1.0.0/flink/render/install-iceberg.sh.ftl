#!/bin/bash


# 定义软连接路径和目标路径
iceberg_jar=iceberg-flink-runtime-1.15-1.2.1.jar
link_path=/home/flink/apache-flink/lib/$iceberg_jar
target_path=/home/flink/$iceberg_jar

<#if conf['plugin.iceberg'] =='true'>

# 检查软连接是否存在
if [ ! -L "$link_path" ]; then
  echo "软连接不存在，创建软连接..."
  ln -s "$target_path" "$link_path"
else
  echo "软连接已存在，跳过创建步骤。"
fi
<#else >
# 检查软连接是否存在
if [ -L "$link_path" ]; then
  echo "软连接已存在，删除软连接..."
  rm "$link_path"
else
  echo "软连接不存在，忽略删除步骤。"
fi
</#if>







