

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

historyserver.web.tmpdir: /data/1/tmp


env.java.opts: -Dlog4j2.formatMsgNoLookups=true
env.pid.dir: /workspace/pid
env.log.dir: /workspace/logs

<#list confFiles['flink-conf.yaml']?keys as key>
${key}: ${confFiles['flink-conf.yaml'][key]}
</#list>
