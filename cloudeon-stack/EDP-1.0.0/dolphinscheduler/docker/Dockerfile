FROM registry.cn-hangzhou.aliyuncs.com/udh/jdk:1.8.141


USER root

WORKDIR /home/root/

RUN wget https://archive.apache.org/dist/dolphinscheduler/3.0.5/apache-dolphinscheduler-3.0.5-bin.tar.gz && tar -zxvf apache-dolphinscheduler-3.0.5-bin.tar.gz -C /home/root/ \
    && rm -f apache-dolphinscheduler-3.0.5-bin.tar.gz && mv apache-dolphinscheduler-3.0.5-bin  apache-dolphinscheduler

ENV DOLPHINSCHEDULER_HOME=/home/root/apache-dolphinscheduler

RUN   wget https://repo1.maven.org/maven2/mysql/mysql-connector-java/8.0.28/mysql-connector-java-8.0.28.jar \
    && ln -s /home/root/mysql-connector-java-8.0.28.jar $DOLPHINSCHEDULER_HOME/alert-server/libs/mysql-connector-java-8.0.28.jar \
    && ln -s /home/root/mysql-connector-java-8.0.28.jar $DOLPHINSCHEDULER_HOME/api-server/libs/mysql-connector-java-8.0.28.jar \
    && ln -s /home/root/mysql-connector-java-8.0.28.jar $DOLPHINSCHEDULER_HOME/master-server/libs/mysql-connector-java-8.0.28.jar \
    && ln -s /home/root/mysql-connector-java-8.0.28.jar $DOLPHINSCHEDULER_HOME/tools/libs/mysql-connector-java-8.0.28.jar \
    && ln -s /home/root/mysql-connector-java-8.0.28.jar $DOLPHINSCHEDULER_HOME/worker-server/libs/mysql-connector-java-8.0.28.jar

RUN yum install -y sudo && yum clean all && rm -rf /var/lib/yum/history/*.sqlite  && rm -rf /var/cache/yum




