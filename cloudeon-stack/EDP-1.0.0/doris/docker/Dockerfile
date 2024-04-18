FROM registry.cn-hangzhou.aliyuncs.com/udh/jdk:1.8.141


USER doris

WORKDIR /home/doris/

RUN wget https://archive.apache.org/dist/doris/1.2/1.2.4.1-rc01/apache-doris-fe-1.2.4.1-bin-x86_64.tar.xz && tar -xvf apache-doris-fe-1.2.4.1-bin-x86_64.tar.xz -C /home/doris/ \
    && rm -f apache-doris-fe-1.2.4.1-bin-x86_64.tar.xz && mv apache-doris-fe-1.2.4.1-bin-x86_64  doris-fe  \
     && wget https://archive.apache.org/dist/doris/1.2/1.2.4.1-rc01/apache-doris-be-1.2.4.1-bin-x86_64.tar.xz \
    && tar -xvf apache-doris-be-1.2.4.1-bin-x86_64.tar.xz -C /home/doris/   && rm -f apache-doris-be-1.2.4.1-bin-x86_64.tar.xz && mv apache-doris-be-1.2.4.1-bin-x86_64  doris-be

RUN wget https://archive.apache.org/dist/doris/1.2/1.2.4.1-rc01/apache-doris-dependencies-1.2.4.1-bin-x86_64.tar.xz && tar -xvf apache-doris-dependencies-1.2.4.1-bin-x86_64.tar.xz -C /home/doris/   \
    && rm -f apache-doris-dependencies-1.2.4.1-bin-x86_64.tar.xz && cp -r apache-doris-dependencies-1.2.4.1-bin-x86_64/*  /home/doris/doris-be/lib/



ENV DORIS_HOME=/home/doris/
ENV PATH=$PATH:$DORIS_HOME/doris-fe/bin:$DORIS_HOME/doris-be/bin







