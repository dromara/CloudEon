alter table ce_alert_rule_define
    add constraint ce_cluster_alert_rule_pk
        primary key (id);

alter table ce_alert_rule_define
    modify id int auto_increment;