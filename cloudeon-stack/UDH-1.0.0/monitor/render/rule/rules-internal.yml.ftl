groups:
  - name: ${serviceName}
    # rules：定义规则
    rules:
        # alert：告警规则的名称
        <#list itemList as item>
        -   alert: ${item.alertName}
            expr: ${item.alertExpr}
            labels:
                # severity: 指定告警级别。有三种等级，分别为warning、critical和emergency。严重等级依次递增。
                severity: ${item.alertLevel}
                clusterId: ${item.clusterId}
                serviceRoleName: ${item.serviceRoleName}
            annotations:
                # summary描述告警的概要信息
                # description用于描述告警的详细信息。
                summary: ${item.alertAdvice}
                description: "{{ $labels.job }}的{{ $labels.instance }}实例产生告警"
        </#list>