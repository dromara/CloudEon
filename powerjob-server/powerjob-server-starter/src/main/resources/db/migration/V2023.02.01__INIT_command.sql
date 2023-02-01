create table udh_command
(
    id                            int auto_increment
        primary key,
    cluster_id                    int          null,
    command_state                 varchar(255) null,
    end_time                      datetime(6)  null,
    name                          varchar(255) null,
    operate_user_id               int          null,
    powerjob_app_id               bigint       null,
    powerjob_workflow_instance_id bigint       null,
    start_time                    datetime(6)  null,
    submit_time                   datetime(6)  null,
    total_progress                int          null,
    type                          varchar(255) null
);

create table udh_command_task_group
(
    id                    int auto_increment
        primary key,
    command_id            int          null,
    service_instance_id   int          null,
    service_instance_name varchar(255) null,
    stack_service_id      int          null,
    stack_service_name    varchar(255) null,
    step_name             varchar(255) null,
    step_sort_num         int          null
);

create table udh_command_task
(
    id                    int auto_increment
        primary key,
    command_state         varchar(255) null,
    command_task_group_id int          null,
    end_time              datetime(6)  null,
    powerjob_instance_id  bigint       null,
    progress              int          null,
    start_time            datetime(6)  null,
    task_name             varchar(255) null,
    task_show_sort_num    int          null
);

-- init command
INSERT INTO udh_command (id, cluster_id, command_state, end_time, name, operate_user_id, powerjob_app_id, powerjob_workflow_instance_id, start_time, submit_time, total_progress, type) VALUES (1, 1, '运行中', '2023-02-01 18:52:06', '添加服务', null, null, null, '2023-02-01 17:48:19', '2023-02-01 17:47:30', 90, 'installService');

-- init command task group
INSERT INTO udh_command_task_group (id, command_id, service_instance_id, service_instance_name, stack_service_id, stack_service_name, step_name, step_sort_num) VALUES (1, 1, 1, 'ZOOKEEPER1', 2, 'ZOOKEEPER', '安装ZOOKEEPER', 1);
INSERT INTO udh_command_task_group (id, command_id, service_instance_id, service_instance_name, stack_service_id, stack_service_name, step_name, step_sort_num) VALUES (2, 1, 1, 'ZOOKEEPER1', 2, 'ZOOKEEPER', '配置ZOOKEEPER', 2);
INSERT INTO udh_command_task_group (id, command_id, service_instance_id, service_instance_name, stack_service_id, stack_service_name, step_name, step_sort_num) VALUES (3, 1, 1, 'ZOOKEEPER1', 2, 'ZOOKEEPER', '启动ZOOKEEPER', 3);
INSERT INTO udh_command_task_group (id, command_id, service_instance_id, service_instance_name, stack_service_id, stack_service_name, step_name, step_sort_num) VALUES (4, 1, 1, 'ZOOKEEPER1', 2, 'ZOOKEEPER', '初始化ZOOKEEPER预定义监控信息', 4);


-- init command task
INSERT INTO udh_command_task (id, command_state, command_task_group_id, end_time, powerjob_instance_id, progress, start_time, task_name, task_show_sort_num) VALUES (1, '成功', 1, '2023-02-01 18:06:15', 1, 100, '2023-02-01 18:06:15', '从registry拉ZooKeeper Server (node002)镜像', 1);
INSERT INTO udh_command_task (id, command_state, command_task_group_id, end_time, powerjob_instance_id, progress, start_time, task_name, task_show_sort_num) VALUES (2, '成功', 1, '2023-02-01 18:06:15', 2, 100, '2023-02-01 18:06:15', '从registry拉ZooKeeper Server (node001)镜像', 2);
INSERT INTO udh_command_task (id, command_state, command_task_group_id, end_time, powerjob_instance_id, progress, start_time, task_name, task_show_sort_num) VALUES (3, '成功', 1, '2023-02-01 18:06:15', 3, 100, '2023-02-01 18:06:15', '从registry拉ZooKeeper Server (node003)镜像', 3);
INSERT INTO udh_command_task (id, command_state, command_task_group_id, end_time, powerjob_instance_id, progress, start_time, task_name, task_show_sort_num) VALUES (4, '成功', 1, '2023-02-01 18:06:15', 4, 100, '2023-02-01 18:06:15', '安装ZooKeeper Server(node002)', 4);
INSERT INTO udh_command_task (id, command_state, command_task_group_id, end_time, powerjob_instance_id, progress, start_time, task_name, task_show_sort_num) VALUES (5, '成功', 1, '2023-02-01 18:06:15', 5, 100, '2023-02-01 18:06:15', '安装ZooKeeper Server(node001)', 5);
INSERT INTO udh_command_task (id, command_state, command_task_group_id, end_time, powerjob_instance_id, progress, start_time, task_name, task_show_sort_num) VALUES (6, '成功', 1, '2023-02-01 18:06:15', 6, 100, '2023-02-01 18:06:15', '安装ZooKeeper Server(node003)', 6);
INSERT INTO udh_command_task (id, command_state, command_task_group_id, end_time, powerjob_instance_id, progress, start_time, task_name, task_show_sort_num) VALUES (7, '成功', 2, '2023-02-01 18:06:15', 7, 100, '2023-02-01 18:06:15', '配置ZooKeeper Server(node002)', 7);
INSERT INTO udh_command_task (id, command_state, command_task_group_id, end_time, powerjob_instance_id, progress, start_time, task_name, task_show_sort_num) VALUES (8, '成功', 2, '2023-02-01 18:06:15', 8, 100, '2023-02-01 18:06:15', '配置ZooKeeper Server(node001)', 8);
INSERT INTO udh_command_task (id, command_state, command_task_group_id, end_time, powerjob_instance_id, progress, start_time, task_name, task_show_sort_num) VALUES (9, '成功', 2, '2023-02-01 18:06:15', 9, 100, '2023-02-01 18:06:15', '配置ZooKeeper Server(node003)', 9);
INSERT INTO udh_command_task (id, command_state, command_task_group_id, end_time, powerjob_instance_id, progress, start_time, task_name, task_show_sort_num) VALUES (10, '成功', 3, '2023-02-01 18:06:15', 10, 100, '2023-02-01 18:06:15', '为ZooKeeper Server(node002)添加TOS标签', 10);
INSERT INTO udh_command_task (id, command_state, command_task_group_id, end_time, powerjob_instance_id, progress, start_time, task_name, task_show_sort_num) VALUES (11, '成功', 3, '2023-02-01 18:06:15', 11, 100, '2023-02-01 18:06:15', '为ZooKeeper Server(node001)添加TOS标签', 11);
INSERT INTO udh_command_task (id, command_state, command_task_group_id, end_time, powerjob_instance_id, progress, start_time, task_name, task_show_sort_num) VALUES (12, '成功', 3, '2023-02-01 18:06:15', 12, 100, '2023-02-01 18:06:15', '为ZooKeeper Server(node003)添加TOS标签', 12);
INSERT INTO udh_command_task (id, command_state, command_task_group_id, end_time, powerjob_instance_id, progress, start_time, task_name, task_show_sort_num) VALUES (13, '成功', 3, '2023-02-01 18:06:15', 13, 100, '2023-02-01 18:06:15', '通过TOS启动ZooKeeper Server', 13);
