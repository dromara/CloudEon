alter table ce_cluster_node
    add constraint ce_cluster_node_pk
        unique (ip);