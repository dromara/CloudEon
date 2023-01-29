<#if service.auth = "kerberos">
export JVMFLAGS="-Djava.security.auth.login.config=/etc/${service.sid}/conf/jaas.conf"
</#if>
