

jobmanager.rpc.address: localhost


jobmanager.rpc.port: 6123


jobmanager.bind-host: localhost


jobmanager.memory.process.size: 1600m


taskmanager.bind-host: localhost

taskmanager.host: localhost


taskmanager.memory.process.size: 1728m

taskmanager.numberOfTaskSlots: 1


parallelism.default: 1


jobmanager.execution.failover-strategy: region

rest.address: localhost


rest.bind-address: localhost

jobmanager.archive.fs.dir: ${conf['flink.history.fs.logDirectory']}

historyserver.web.address: 0.0.0.0

historyserver.web.port: ${conf['flink.history.ui.port']}

historyserver.archive.fs.dir: ${conf['flink.history.fs.logDirectory']}

historyserver.web.tmpdir: /opt/edp/${service.serviceName}/data/tmp


env.java.opts: -Dlog4j2.formatMsgNoLookups=true
env.pid.dir: /opt/edp/${service.serviceName}/data
env.log.dir: /opt/edp/${service.serviceName}/log
env.yarn.conf.dir: /opt/edp/${service.serviceName}/conf
env.hadoop.conf.dir: /opt/edp/${service.serviceName}/conf
<#list confFiles['flink-conf.yaml']?keys as key>
${key}: ${confFiles['flink-conf.yaml'][key]}
</#list>
