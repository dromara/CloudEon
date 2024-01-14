package org.dromara.cloudeon.crd.helmchart;

@io.fabric8.kubernetes.model.annotation.Version(value = "v1", storage = true, served = true)
@io.fabric8.kubernetes.model.annotation.Group("helm.cattle.io")
@io.fabric8.kubernetes.model.annotation.Singular("helmchart")
@io.fabric8.kubernetes.model.annotation.Plural("helmcharts")
public class HelmChart extends io.fabric8.kubernetes.client.CustomResource<org.dromara.cloudeon.crd.helmchart.HelmChartSpec, org.dromara.cloudeon.crd.helmchart.HelmChartStatus> implements io.fabric8.kubernetes.api.model.Namespaced {
}

