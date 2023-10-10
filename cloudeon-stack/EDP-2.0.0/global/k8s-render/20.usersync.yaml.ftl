apiVersion: v1
kind: ConfigMap
metadata:
  name: global-usersync-config
  labels:
    app: global
data:
  user.conf: |
    hdfs,supergroup
    yarn,supergroup
    jhs,supergroup
    mapred,supergroup
    flink,supergroup
    spark,supergroup
    hive,supergroup
    hbase,supergroupp
<#if conf["global.user.list"]??>
<#assign ugList = conf["global.user.list"]?split(";")>
<#list ugList as ug>
    ${ug?trim}
</#list>
</#if>

<#noparse>
  user-sync.sh: |
    #!/bin/bash
    set +e
    source /etc/default/locale

    ARRAY=($(cat ${USER_SYNC_CONF}/user.conf))
    for userGroup in ${ARRAY[@]} ; do
        userGroupArr=(`echo $userGroup | tr ',' ' '`)
        user=${userGroupArr[0]}
        group=${userGroupArr[1]}
        echo "user: ${user} group: ${group}"
        #create group if not exists
        egrep "^$group:" /etc/group >& /dev/null
        if [ $? -ne 0 ]
        then
            groupadd $group
        fi
        egrep "^$user:" /etc/group >& /dev/null
        if [ $? -ne 0 ]
        then
            groupadd $user
        fi
        #create user if not exists
        egrep "^$user:" /etc/passwd >& /dev/null
        if [ $? -ne 0 ]
        then
            useradd -g $group $user
        fi
        if [ "$ENABLE_SUDO" != "" ];then
          # grant sudo permission if not exists
          egrep "^$user" /etc/sudoers >& /dev/null
          if [ $? -ne 0 ]
          then
            echo "$user ALL=(ALL) NOPASSWD:ALL " >> /etc/sudoers
          fi
        fi
        
        #if ! id $user ;then
        #   useradd  $user
        #fi
    done
  user-sync.crontab: |
    SHELL=/bin/bash
    PATH=/sbin:/bin:/usr/sbin:/usr/bin
    MAILTO=root
    HOME=/

  bootstrap.sh: |
    #!/bin/bash
    set -e

    basepath=$(cd `dirname $0`; pwd)

    echo "执行user-sync模块"
    if [ "$ENABLE_SUDO" != "" ];then
      # 设置不重置环境变量
      sed -i "s/env_reset/\!env_reset/g"  /etc/sudoers
    fi

    export USER_SYNC_CONF=$basepath
    export USER_SYNC_DIR=/opt/my/global/user-sync
    env >> /etc/default/locale

    mkdir -p $USER_SYNC_DIR
    cp $basepath/user-sync.sh $USER_SYNC_DIR
    chmod 777 $USER_SYNC_DIR/user-sync.sh
    $USER_SYNC_DIR/user-sync.sh >> $USER_SYNC_DIR/user-sync.log  2>&1

    cp $basepath/user-sync.crontab $USER_SYNC_DIR
    echo "0 0 * * * $USER_SYNC_DIR/user-sync.sh > $USER_SYNC_DIR/user-sync.log  ">>$USER_SYNC_DIR/user-sync.crontab
    echo "*/1 * * * * $USER_SYNC_DIR/user-sync.sh >> $USER_SYNC_DIR/user-sync.log  2>&1">>$USER_SYNC_DIR/user-sync.crontab
    echo "" >> $USER_SYNC_DIR/user-sync.crontab
    crond
    crontab $USER_SYNC_DIR/user-sync.crontab
    crontab -l
</#noparse>