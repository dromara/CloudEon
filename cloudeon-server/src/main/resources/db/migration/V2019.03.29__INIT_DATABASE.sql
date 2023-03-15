create table app_info
(
    id             bigint auto_increment comment '应用ID'
        primary key,
    app_name       varchar(128) not null comment '应用名称',
    current_server varchar(255) null comment 'Server地址,用于负责调度应用的ActorSystem地址',
    gmt_create     datetime     not null comment '创建时间',
    gmt_modified   datetime     not null comment '更新时间',
    password       varchar(255) not null comment '应用密码',
    constraint appNameUK
        unique (app_name),
    constraint uidx01_app_info
        unique (app_name)
)
    comment '应用表';

create table container_info
(
    id               bigint auto_increment comment '容器ID'
        primary key,
    app_id           bigint       not null comment '应用ID',
    container_name   varchar(128) not null comment '容器名称',
    gmt_create       datetime     not null comment '创建时间',
    gmt_modified     datetime     not null comment '更新时间',
    last_deploy_time datetime     null comment '上次部署时间',
    source_info      varchar(255) null comment '资源信息,内容取决于source_type
1、FatJar -> String
2、Git -> JSON，{"repo”:””仓库,”branch”:”分支”,”username”:”账号,”password”:”密码”}',
    source_type      int          not null comment '资源类型,1:FatJar/2:Git',
    status           int          not null comment '状态,1:正常ENABLE/2:已禁用DISABLE/99:已删除DELETED',
    version          varchar(255) null comment '版本'
)
    comment '容器表';

create index IDX8hixyaktlnwil2w9up6b0p898
    on container_info (app_id);

create index idx01_container_info
    on container_info (app_id);



create table hibernate_sequence
(
    next_val bigint null
);

create table instance_info
(
    id                    bigint auto_increment comment '任务实例ID'
        primary key,
    app_id                bigint       not null comment '应用ID',
    instance_id           bigint       not null comment '任务实例ID',
    type                  int          not null comment '任务实例类型,1:普通NORMAL/2:工作流WORKFLOW',
    job_id                bigint       not null comment '任务ID',
    instance_params       longtext     null comment '任务动态参数',
    job_params            longtext     null comment '任务静态参数',
    actual_trigger_time   bigint       null comment '实际触发时间',
    expected_trigger_time bigint       null comment '计划触发时间',
    finished_time         bigint       null comment '执行结束时间',
    last_report_time      bigint       null comment '最后上报时间',
    result                longtext     null comment '执行结果',
    running_times         bigint       null comment '总执行次数,用于重试判断',
    status                int          not null comment '任务状态,1:等待派发WAITING_DISPATCH/2:等待Worker接收WAITING_WORKER_RECEIVE/3:运行中RUNNING/4:失败FAILED/5:成功SUCCEED/9:取消CANCELED/10:手动停止STOPPED',
    task_tracker_address  varchar(255) null comment 'TaskTracker地址',
    wf_instance_id        bigint       null comment '工作流实例ID',
    additional_data       longtext     null comment '附加信息 (JSON)',
    gmt_create            datetime     not null comment '创建时间',
    gmt_modified          datetime     not null comment '更新时间'
)
    comment '任务实例表';

create index IDX5b1nhpe5je7gc5s1ur200njr7
    on instance_info (job_id);

create index IDXa98hq3yu0l863wuotdjl7noum
    on instance_info (instance_id);

create index IDXjnji5lrr195kswk6f7mfhinrs
    on instance_info (app_id);

create index idx01_instance_info
    on instance_info (job_id);

create index idx02_instance_info
    on instance_info (app_id);

create index idx03_instance_info
    on instance_info (instance_id);

create index idx04_instance_info
    on instance_info (wf_instance_id);

create index idx05_instance_info
    on instance_info (expected_trigger_time);

create table job_info
(
    id                   bigint auto_increment
        primary key,
    app_id               bigint           null comment '应用ID',
    job_name             varchar(128)     null comment '任务名称',
    job_description      varchar(255)     null comment '任务描述',
    job_params           text             null comment '任务默认参数',
    concurrency          int              null comment '并发度,同时执行某个任务的最大线程数量',
    designated_workers   varchar(255)     null comment '运行节点,空:不限(多值逗号分割)',
    dispatch_strategy    int              null comment '投递策略,1:健康优先/2:随机',
    execute_type         int              not null comment '执行类型,1:单机STANDALONE/2:广播BROADCAST/3:MAP_REDUCE/4:MAP',
    instance_retry_num   int    default 0 not null comment 'Instance重试次数',
    instance_time_limit  bigint default 0 not null comment '任务整体超时时间',
    lifecycle            varchar(255)     null comment '生命周期',
    max_instance_num     int    default 1 not null comment '最大同时运行任务数,默认 1',
    max_worker_count     int    default 0 not null comment '最大运行节点数量',
    min_cpu_cores        double default 0 not null comment '最低CPU核心数量,0:不限',
    min_disk_space       double default 0 not null comment '最低磁盘空间(GB),0:不限',
    min_memory_space     double default 0 not null comment '最低内存空间(GB),0:不限',
    next_trigger_time    bigint           null comment '下一次调度时间',
    notify_user_ids      varchar(255)     null comment '报警用户(多值逗号分割)',
    processor_info       varchar(255)     null comment '执行器信息',
    processor_type       int              not null comment '执行器类型,1:内建处理器BUILT_IN/2:SHELL/3:PYTHON/4:外部处理器（动态加载）EXTERNAL',
    status               int              not null comment '状态,1:正常ENABLE/2:已禁用DISABLE/99:已删除DELETED',
    task_retry_num       int    default 0 not null comment 'Task重试次数',
    time_expression      varchar(255)     null comment '时间表达式,内容取决于time_expression_type,1:CRON/2:NULL/3:LONG/4:LONG',
    time_expression_type int              not null comment '时间表达式类型,1:CRON/2:API/3:FIX_RATE/4:FIX_DELAY,5:WORKFLOW
）',
    tag                  varchar(255)     null comment 'TAG',
    log_config           varchar(255)     null comment '日志配置',
    extra                varchar(255)     null comment '扩展字段',
    gmt_create           datetime         not null comment '创建时间',
    gmt_modified         datetime         not null comment '更新时间',
    alarm_config         varchar(255)     null
)
    comment '任务表';

create index IDXk2xprmn3lldmlcb52i36udll1
    on job_info (app_id);

create index idx01_job_info
    on job_info (app_id);

create index idx02_job_info
    on job_info (job_name);

create index idx03_job_info
    on job_info (next_trigger_time);

create table oms_lock
(
    id            bigint auto_increment comment '序号ID'
        primary key,
    lock_name     varchar(128) null comment '名称',
    max_lock_time bigint       null comment '最长持锁时间',
    ownerip       varchar(255) null comment '拥有者IP',
    gmt_create    datetime     not null comment '创建时间',
    gmt_modified  datetime     not null comment '更新时间',
    constraint lockNameUK
        unique (lock_name),
    constraint uidx01_oms_lock
        unique (lock_name)
)
    comment '数据库锁';

create table server_info
(
    id           bigint auto_increment comment '服务器ID'
        primary key,
    gmt_create   datetime     null comment '创建时间',
    gmt_modified datetime     null comment '更新时间',
    ip           varchar(128) null comment '服务器IP地址',
    constraint UKtk8ytgpl7mpukhnvhbl82kgvy
        unique (ip),
    constraint uidx01_server_info
        unique (ip)
)
    comment '服务器表';



create table user_info
(
    id           bigint auto_increment comment '用户ID'
        primary key,
    username     varchar(128) not null comment '用户名',
    password     varchar(255) null comment '密码',
    phone        varchar(255) null comment '手机号',
    email        varchar(128) not null comment '邮箱',
    extra        varchar(255) null comment '扩展字段',
    web_hook     varchar(255) null comment 'webhook地址',
    gmt_create   datetime     not null comment '创建时间',
    gmt_modified datetime     not null comment '更新时间',
    constraint uidx01_user_info
        unique (username),
    constraint uidx02_user_info
        unique (email)
)
    comment '用户表';

create table workflow_info
(
    id                   bigint auto_increment comment '工作流ID'
        primary key,
    app_id               bigint        not null comment '应用ID',
    wf_name              varchar(128)  not null comment '工作流名称',
    wf_description       varchar(255)  null comment '工作流描述',
    extra                varchar(255)  null comment '扩展字段',
    lifecycle            varchar(255)  null comment '生命周期',
    max_wf_instance_num  int default 1 not null comment '最大运行工作流数量,默认 1',
    next_trigger_time    bigint        null comment '下次调度时间',
    notify_user_ids      varchar(255)  null comment '报警用户(多值逗号分割)',
    pedag                text          null comment 'DAG信息(JSON)',
    status               int           not null comment '状态,1:正常ENABLE/2:已禁用DISABLE/99:已删除DELETED',
    time_expression      varchar(255)  null comment '时间表达式,内容取决于time_expression_type,1:CRON/2:NULL/3:LONG/4:LONG',
    time_expression_type int           not null comment '时间表达式类型,1:CRON/2:API/3:FIX_RATE/4:FIX_DELAY,5:WORKFLOW
）',
    gmt_create           datetime      null comment '创建时间',
    gmt_modified         datetime      null comment '更新时间'
)
    comment '工作流表';

create index IDX7uo5w0e3beeho3fnx9t7eiol3
    on workflow_info (app_id);

create index idx01_workflow_info
    on workflow_info (app_id);

create table workflow_instance_info
(
    id                    bigint auto_increment comment '工作流实例ID'
        primary key,
    wf_instance_id        bigint   null comment '工作流实例ID',
    workflow_id           bigint   null comment '工作流ID',
    actual_trigger_time   bigint   null comment '实际触发时间',
    app_id                bigint   null comment '应用ID',
    dag                   text     null comment 'DAG信息(JSON)',
    expected_trigger_time bigint   null comment '计划触发时间',
    finished_time         bigint   null comment '执行结束时间',
    result                text     null comment '执行结果',
    status                int      null comment '工作流实例状态,1:等待调度WAITING/2:运行中RUNNING/3:失败FAILED/4:成功SUCCEED/10:手动停止STOPPED',
    wf_context            text     null comment '工作流上下文',
    wf_init_params        text     null comment '工作流启动参数',
    gmt_create            datetime null comment '创建时间',
    gmt_modified          datetime null comment '更新时间',
    parent_wf_instance_id bigint   null,
    constraint uidx01_wf_instance
        unique (wf_instance_id)
)
    comment '工作流实例表';

create index idx01_wf_instance
    on workflow_instance_info (workflow_id);

create index idx02_wf_instance
    on workflow_instance_info (app_id, status);

create table workflow_node_info
(
    id               bigint auto_increment comment '节点ID'
        primary key,
    app_id           bigint       not null comment '应用ID',
    enable           bit          not null comment '是否启动,0:否/1:是',
    extra            text         null comment '扩展字段',
    gmt_create       datetime     not null comment '创建时间',
    gmt_modified     datetime     not null comment '更新时间',
    job_id           bigint       null comment '任务ID',
    node_name        varchar(255) null comment '节点名称',
    node_params      text         null comment '节点参数',
    skip_when_failed bit          not null comment '是否允许失败跳过,0:否/1:是',
    type             int          null comment '节点类型,1:任务JOB',
    workflow_id      bigint       null comment '工作流ID'
)
    comment '工作流节点表';

create index IDX36t7rhj4mkg2a5pb4ttorscta
    on workflow_node_info (app_id);

create index IDXacr0i6my8jr002ou8i1gmygju
    on workflow_node_info (workflow_id);

create index idx01_workflow_node_info
    on workflow_node_info (app_id);

create index idx02_workflow_node_info
    on workflow_node_info (workflow_id);

create index idx03_workflow_node_info
    on workflow_node_info (job_id);

