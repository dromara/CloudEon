## 一 获取cloudeon安装包

执行以下命令，在cloudeon-assembly的target下找到tar.gz/zip的安装包

```shell
clean package -Dmaven.test.skip
```

## 二 构建并推送镜像

先构建

```shell
file_path="../cloudeon-assembly/target/cloudeon-assembly-v2.0.0-beta-release.tar.gz"
# 提取文件名部分
filename="${file_path##*/}"
cp $file_path ./
# 去掉前缀和后缀
version="${filename#cloudeon-assembly-}"
version="${version%-release*}"

docker build --build-arg CLOUDEON_VERSION=$version \
             -t registry.cn-guangzhou.aliyuncs.com/bigdata200/cloudeon:$version .

rm -f ./$filename

```

推送镜像

```shell
docker push registry.cn-guangzhou.aliyuncs.com/bigdata200/cloudeon:$version

```

## 三 运行

简单运行可以使用如下指令

```shell
image=registry.cn-guangzhou.aliyuncs.com/bigdata200/cloudeon:v2.0.0-beta

docker run -d --name cloudeon2 -p 7800:7700 $image

```

自定义配置文件则可参考如下指令

```shell
image=registry.cn-guangzhou.aliyuncs.com/bigdata200/cloudeon:v2.0.0-beta
conf_path_dir=/opt/cloudeon
# 运行临时容器把配置文件复制到外部，如果已有配置文件则此步骤可以跳过
docker run --rm --entrypoint /bin/bash -v $conf_path_dir:/data/conf $image -c "cp  /opt/cloudeon/conf/application.properties /data/conf/"

# 根据需求修改配置文件

# 正式运行
docker run -d --name cloudeon -v $conf_path_dir/application.properties:/opt/cloudeon/conf/application.properties -p 7700:7700 $image

```