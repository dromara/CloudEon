alter table ce_cluster_node
    add ssh_auth_type varchar(255) default 'PASSWORD' null;

alter table ce_cluster_node
    add private_key_path varchar(1024) null;

