<#assign
coordinator_hostname=serviceRoles['TRINO_COORDINATOR'][0].hostname
>
coordinator=false
query.max-memory=${conf['query.maxMemory']}GB
query.max-memory-per-node=${conf['worker.query.maxMemoryPerNode']}GB
http-server.http.port=${conf['worker.http.port']}
discovery.uri=http://${coordinator_hostname}:${conf['coordinator.http.port']}
