package org.dromara.cloudeon.crd.helmchart;

import javax.annotation.Nullable;

@com.fasterxml.jackson.annotation.JsonInclude(com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL)
@com.fasterxml.jackson.annotation.JsonPropertyOrder({"authPassCredentials", "authSecret", "backOffLimit", "bootstrap", "chart", "chartContent", "createNamespace", "dockerRegistrySecret", "failurePolicy", "helmVersion", "jobImage", "repo", "repoCA", "repoCAConfigMap", "set", "targetNamespace", "timeout", "valuesContent", "version"})
@com.fasterxml.jackson.databind.annotation.JsonDeserialize(using = com.fasterxml.jackson.databind.JsonDeserializer.None.class)
public class HelmChartSpec implements io.fabric8.kubernetes.api.model.KubernetesResource {

    @com.fasterxml.jackson.annotation.JsonProperty("authPassCredentials")
    @com.fasterxml.jackson.annotation.JsonSetter(nulls = com.fasterxml.jackson.annotation.Nulls.SKIP)
    private Boolean authPassCredentials;

    public Boolean getAuthPassCredentials() {
        return authPassCredentials;
    }

    public void setAuthPassCredentials(Boolean authPassCredentials) {
        this.authPassCredentials = authPassCredentials;
    }

    @com.fasterxml.jackson.annotation.JsonProperty("authSecret")
    @com.fasterxml.jackson.annotation.JsonSetter(nulls = com.fasterxml.jackson.annotation.Nulls.SET)
    @Nullable
    private org.dromara.cloudeon.crd.helmchart.helmchartspec.AuthSecret authSecret;

    public org.dromara.cloudeon.crd.helmchart.helmchartspec.AuthSecret getAuthSecret() {
        return authSecret;
    }

    public void setAuthSecret(org.dromara.cloudeon.crd.helmchart.helmchartspec.AuthSecret authSecret) {
        this.authSecret = authSecret;
    }

    @com.fasterxml.jackson.annotation.JsonProperty("backOffLimit")
    @com.fasterxml.jackson.annotation.JsonSetter(nulls = com.fasterxml.jackson.annotation.Nulls.SET)
    @Nullable
    private Long backOffLimit;

    public Long getBackOffLimit() {
        return backOffLimit;
    }

    public void setBackOffLimit(Long backOffLimit) {
        this.backOffLimit = backOffLimit;
    }

    @com.fasterxml.jackson.annotation.JsonProperty("bootstrap")
    @com.fasterxml.jackson.annotation.JsonSetter(nulls = com.fasterxml.jackson.annotation.Nulls.SKIP)
    private Boolean bootstrap;

    public Boolean getBootstrap() {
        return bootstrap;
    }

    public void setBootstrap(Boolean bootstrap) {
        this.bootstrap = bootstrap;
    }

    @com.fasterxml.jackson.annotation.JsonProperty("chart")
    @com.fasterxml.jackson.annotation.JsonSetter(nulls = com.fasterxml.jackson.annotation.Nulls.SET)
    @Nullable
    private String chart;

    public String getChart() {
        return chart;
    }

    public void setChart(String chart) {
        this.chart = chart;
    }

    @com.fasterxml.jackson.annotation.JsonProperty("chartContent")
    @com.fasterxml.jackson.annotation.JsonSetter(nulls = com.fasterxml.jackson.annotation.Nulls.SET)
    @Nullable
    private String chartContent;

    public String getChartContent() {
        return chartContent;
    }

    public void setChartContent(String chartContent) {
        this.chartContent = chartContent;
    }

    @com.fasterxml.jackson.annotation.JsonProperty("createNamespace")
    @com.fasterxml.jackson.annotation.JsonSetter(nulls = com.fasterxml.jackson.annotation.Nulls.SKIP)
    private Boolean createNamespace;

    public Boolean getCreateNamespace() {
        return createNamespace;
    }

    public void setCreateNamespace(Boolean createNamespace) {
        this.createNamespace = createNamespace;
    }

    @com.fasterxml.jackson.annotation.JsonProperty("dockerRegistrySecret")
    @com.fasterxml.jackson.annotation.JsonSetter(nulls = com.fasterxml.jackson.annotation.Nulls.SET)
    @Nullable
    private org.dromara.cloudeon.crd.helmchart.helmchartspec.DockerRegistrySecret dockerRegistrySecret;

    public org.dromara.cloudeon.crd.helmchart.helmchartspec.DockerRegistrySecret getDockerRegistrySecret() {
        return dockerRegistrySecret;
    }

    public void setDockerRegistrySecret(org.dromara.cloudeon.crd.helmchart.helmchartspec.DockerRegistrySecret dockerRegistrySecret) {
        this.dockerRegistrySecret = dockerRegistrySecret;
    }

    @com.fasterxml.jackson.annotation.JsonProperty("failurePolicy")
    @com.fasterxml.jackson.annotation.JsonSetter(nulls = com.fasterxml.jackson.annotation.Nulls.SET)
    @Nullable
    private String failurePolicy;

    public String getFailurePolicy() {
        return failurePolicy;
    }

    public void setFailurePolicy(String failurePolicy) {
        this.failurePolicy = failurePolicy;
    }

    @com.fasterxml.jackson.annotation.JsonProperty("helmVersion")
    @com.fasterxml.jackson.annotation.JsonSetter(nulls = com.fasterxml.jackson.annotation.Nulls.SET)
    @Nullable
    private String helmVersion;

    public String getHelmVersion() {
        return helmVersion;
    }

    public void setHelmVersion(String helmVersion) {
        this.helmVersion = helmVersion;
    }

    @com.fasterxml.jackson.annotation.JsonProperty("jobImage")
    @com.fasterxml.jackson.annotation.JsonSetter(nulls = com.fasterxml.jackson.annotation.Nulls.SET)
    @Nullable
    private String jobImage;

    public String getJobImage() {
        return jobImage;
    }

    public void setJobImage(String jobImage) {
        this.jobImage = jobImage;
    }

    @com.fasterxml.jackson.annotation.JsonProperty("repo")
    @com.fasterxml.jackson.annotation.JsonSetter(nulls = com.fasterxml.jackson.annotation.Nulls.SET)
    @Nullable
    private String repo;

    public String getRepo() {
        return repo;
    }

    public void setRepo(String repo) {
        this.repo = repo;
    }

    @com.fasterxml.jackson.annotation.JsonProperty("repoCA")
    @com.fasterxml.jackson.annotation.JsonSetter(nulls = com.fasterxml.jackson.annotation.Nulls.SET)
    @Nullable
    private String repoCA;

    public String getRepoCA() {
        return repoCA;
    }

    public void setRepoCA(String repoCA) {
        this.repoCA = repoCA;
    }

    @com.fasterxml.jackson.annotation.JsonProperty("repoCAConfigMap")
    @com.fasterxml.jackson.annotation.JsonSetter(nulls = com.fasterxml.jackson.annotation.Nulls.SET)
    @Nullable
    private org.dromara.cloudeon.crd.helmchart.helmchartspec.RepoCAConfigMap repoCAConfigMap;

    public org.dromara.cloudeon.crd.helmchart.helmchartspec.RepoCAConfigMap getRepoCAConfigMap() {
        return repoCAConfigMap;
    }

    public void setRepoCAConfigMap(org.dromara.cloudeon.crd.helmchart.helmchartspec.RepoCAConfigMap repoCAConfigMap) {
        this.repoCAConfigMap = repoCAConfigMap;
    }

    @com.fasterxml.jackson.annotation.JsonProperty("set")
    @com.fasterxml.jackson.annotation.JsonSetter(nulls = com.fasterxml.jackson.annotation.Nulls.SET)
    @Nullable
    private java.util.Map<String, io.fabric8.kubernetes.api.model.IntOrString> set;

    public java.util.Map<String, io.fabric8.kubernetes.api.model.IntOrString> getSet() {
        return set;
    }

    public void setSet(java.util.Map<String, io.fabric8.kubernetes.api.model.IntOrString> set) {
        this.set = set;
    }

    @com.fasterxml.jackson.annotation.JsonProperty("targetNamespace")
    @com.fasterxml.jackson.annotation.JsonSetter(nulls = com.fasterxml.jackson.annotation.Nulls.SET)
    @Nullable
    private String targetNamespace;

    public String getTargetNamespace() {
        return targetNamespace;
    }

    public void setTargetNamespace(String targetNamespace) {
        this.targetNamespace = targetNamespace;
    }

    @com.fasterxml.jackson.annotation.JsonProperty("timeout")
    @com.fasterxml.jackson.annotation.JsonSetter(nulls = com.fasterxml.jackson.annotation.Nulls.SET)
    @Nullable
    private String timeout;

    public String getTimeout() {
        return timeout;
    }

    public void setTimeout(String timeout) {
        this.timeout = timeout;
    }

    @com.fasterxml.jackson.annotation.JsonProperty("valuesContent")
    @com.fasterxml.jackson.annotation.JsonSetter(nulls = com.fasterxml.jackson.annotation.Nulls.SET)
    @Nullable
    private String valuesContent;

    public String getValuesContent() {
        return valuesContent;
    }

    public void setValuesContent(String valuesContent) {
        this.valuesContent = valuesContent;
    }

    @com.fasterxml.jackson.annotation.JsonProperty("version")
    @com.fasterxml.jackson.annotation.JsonSetter(nulls = com.fasterxml.jackson.annotation.Nulls.SET)
    @Nullable
    private String version;

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }
}

