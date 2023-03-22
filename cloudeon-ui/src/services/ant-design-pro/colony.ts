import { request } from 'umi';

// 集群列表
export async function getClusterListAPI(params: {
  // query
  /** 集群id */
  clusterId?: number;
}) {
  return request<API.ServiceList>('/apiPre/cluster/list', {
    method: 'GET',
    params: {
      ...params,
    },
  });
}

// 节点
export async function getNodeListAPI(params: {
  // query
  /** 集群id */
  clusterId?: number;
}) {
  return request<API.ServiceList>('/apiPre/node/list', {
    method: 'GET',
    params: {
      ...params,
    },
  });
}

// 服务
export async function getServiceListAPI(params: {
  // query
  /** 集群id */
  clusterId?: number;
}) {
  return request<API.ServiceList>('/apiPre/stack/listService', {
    method: 'GET',
    params: {
      ...params,
    },
  });
}

// 框架列表
export async function getStackListAPI(params: {
  // query
  /** 集群id */
  clusterId?: number;
}) {
  return request<API.StackList>('/apiPre/stack/list', {
    method: 'GET',
    params: {
      ...params,
    },
  });
}

//新增集群
export async function createClusterAPI(options?: { [key: string]: any }) {
  return request<API.normalResult>('/apiPre/cluster/save', {
    method: 'POST',
    data: {...(options || {})},
  });
}


/** 获取k8s节点列表 */
export async function getListK8sNodeAPI(options?: { [key: string]: any }) {
  return request<API.nodeIpListResult>('/apiPre/node/listK8sNode', {
    method: 'GET',
    params: {
      ...options,
    },
  });
}

//新增节点
export async function createNodeAPI(options?: { [key: string]: any }) {  
  return request<API.normalResult>('/apiPre/node/add', {
    method: 'POST',
    data: {...(options || {})},
  });
}

// 安装服务校验
export async function checkServiceAPI(options?: { [key: string]: any }) {
  return request<API.StackList>('/apiPre/stack/validInstallServicesDeps', {
    method: 'POST',
    data: {...(options || {})},
  });
}

// 服务可配置参数
export async function getServiceConfAPI(params: {
  serviceId?: number;
  inWizard?: boolean;
}) {
  return request<API.ConfList>('/apiPre/stack/listServiceConf', {
    method: 'GET',
    params: {
      ...params,
    },
  });
}

// 安装服务校验
export async function checkKerberosAPI(options?: number[]) {
  return request<API.normalResult>('/apiPre/service/validInstallServiceHasKerberos', {
    method: 'POST',
    data: options,
  });
}

// 添加服务校验
export async function initServiceAPI(options?: API.SubmitServicesParams) {
  return request<API.normalResult>('/apiPre/service/initService', {
    method: 'POST',
    data: options,
  });
}

// 服务
export async function serviceListAPI(params: {
  clusterId?: number;
}) {
  return request<API.normalResult>('/apiPre/service/listServiceInstance', {
    method: 'GET',
    params: {
      ...params,
    },
  });
}

// /command/list?clusterId=1
/** 指令 */
export async function getCommandListAPI(options?: { [key: string]: any }) {
  return request<API.normalResult>('/apiPre/command/list', {
    method: 'GET',
    params: {
      ...options,
    },
  });
}

/** 指令明细 */
export async function getCommandDetailAPI(options?: { [key: string]: any }) {
  return request<API.commandResult>('/apiPre/command/detail', {
    method: 'GET',
    params: {
      ...options,
    },
  });
}

/** 删除服务 */
export async function deleteServiceAPI(options?: { [key: string]: any }) {
  return request<API.normalResult>('/apiPre/service/deleteServiceInstance', {
    method: 'GET',
    params: {
      ...options,
    },
  });
}

/** 日志详情 */
export async function getTaskLogAPI(options?: { [key: string]: any }) {
  return request<API.logResult>('/apiPre/log/task', {
    method: 'GET',
    params: {
      ...options,
    },
  });
}

// 启动服务
export async function startServiceAPI(options?: { [key: string]: any}) {
  return request<API.normalResult>('/apiPre/service/startService', {
    method: 'POST',
    params: options,
  });
}

// 停止服务
export async function stopServiceAPI(options?: { [key: string]: any}) {
  return request<API.normalResult>('/apiPre/service/stopService', {
    method: 'POST',
    params: options,
  });
}

// 重启服务
export async function restartServiceAPI(options?: { [key: string]: any}) {
  return request<API.normalResult>('/apiPre/service/restartService', {
    method: 'POST',
    params: options,
  });
}

// 更新配置
export async function upgradeServiceAPI(options?: { [key: string]: any}) {
  return request<API.normalResult>('/apiPre/service/upgradeServiceConfig', {
    method: 'POST',
    params: options,
  });
}

/** 服务实例详情 */
export async function getServiceInfoAPI(options?: { [key: string]: any }) {
  return request<API.serviceInfosResult>('/apiPre/service/serviceInstanceInfo', {
    method: 'GET',
    params: {
      ...options,
    },
  });
}

/** 服务实例角色列表 */
export async function getServiceRolesAPI(options?: { [key: string]: any }) {
  return request<API.serviceRolesResult>('/apiPre/service/serviceInstanceRoles', {
    method: 'GET',
    params: {
      ...options,
    },
  });
}

/** 自动分配角色绑定节点 */
export async function getRolesAllocationAPI(options?: { [key: string]: any }) {
  return request<API.rolesValidResult>('/apiPre/stack/getRolesAllocation', {
    method: 'POST',
    data: options,
  });
}

/** 查询服务实例配置 */
export async function getListConfsAPI(options?: { [key: string]: any }) {
  return request<API.ConfList>('/apiPre/service/listConfs', {
    method: 'GET',
    params: {
      ...options,
    },
  });
}



/** 保存服务实例配置 */
export async function saveServiceConfAPI(options?: { [key: string]: any }) {
  return request<API.normalResult>('/apiPre/service/serviceInstanceSaveConf', {
    method: 'POST',
    data: options,
  });
}

/** 查询正在运行的指令书 */
export async function getCountActiveAPI(options?: { [key: string]: any }) {
  return request<API.stringResult>('/apiPre/command/countActive', {
    method: 'GET',
    params: {
      ...options,
    },
  });
}


/** 查询服务实例相关web ui地址 */
export async function getListWebURLsAPI(options?: { [key: string]: any }) {
  return request<API.webUrlsListResult>('/apiPre/service/listWebURLs', {
    method: 'GET',
    params: {
      ...options,
    },
  });
}


