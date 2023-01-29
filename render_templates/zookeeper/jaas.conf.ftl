<#if service.auth = "kerberos">
Server {
  com.sun.security.auth.module.Krb5LoginModule required
  useKeyTab=true
  keyTab="${service.keytab}"
  storeKey=true
  useTicketCache=false
  principal="zookeeper/${localhostname?lower_case}@${service.realm}";
};
Client {
  com.sun.security.auth.module.Krb5LoginModule required
  useKeyTab=true
  storeKey=true
  useTicketCache=false
  keyTab="${service.keytab}"
  principal="zookeeper/${localhostname?lower_case}@${service.realm}";
};
</#if>
