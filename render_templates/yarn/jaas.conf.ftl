<#if service.auth = "kerberos">
Client {
  com.sun.security.auth.module.Krb5LoginModule required
  useKeyTab=true
  storeKey=true
  useTicketCache=false
  keyTab="/etc/${service.sid}/conf/yarn.keytab"
  principal="yarn/${localhostname?lower_case}@${service.realm}";
};
</#if>
