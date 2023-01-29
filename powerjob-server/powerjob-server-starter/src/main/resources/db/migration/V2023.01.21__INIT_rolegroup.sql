CREATE TABLE `udh_service_role_group_instance` (
                              `id` bigint(20) NOT NULL AUTO_INCREMENT,
                              `name` varchar(63) NOT NULL,
                              `serviceId` bigint(20) NOT NULL,
                              `label` varchar(63) NOT NULL,
                              PRIMARY KEY (`id`)

);

alter table udh_service_role_instance
    add role_group_id int null;

