#!/bin/bash


<#if serviceRoles['HBASE_MASTER'][0].hostname == localhostname>
hbase-daemon.sh --config /opt/udh/${service.serviceName}/conf start master
<#else >
hbase-daemon.sh --config /opt/udh/${service.serviceName}/conf start master --backup
</#if>


tail -f /dev/null