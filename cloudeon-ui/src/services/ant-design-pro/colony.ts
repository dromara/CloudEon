import { request } from 'umi';

// 集群列表
export async function getClusterListAPI(params: {
  // query
  /** 集群id */
  clusterId?: number;
}) {
  return request<API.ServiceList>('/cluster/list', {
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
  return request<API.ServiceList>('/node/list', {
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
  return request<API.ServiceList>('/stack/listService', {
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
  return request<API.StackList>('/stack/list', {
    method: 'GET',
    params: {
      ...params,
    },
  });
}

//新增集群
export async function createClusterAPI(options?: { [key: string]: any }) {
  return request<API.normalResult>('/cluster/save', {
    method: 'POST',
    data: {...(options || {})},
  });
}

//新增节点
export async function createNodeAPI(options?: { [key: string]: any }) {  
  return request<API.normalResult>('/node/add', {
    method: 'POST',
    data: {...(options || {})},
  });
}

// 安装服务校验
export async function checkServiceAPI(options?: { [key: string]: any }) {
  return request<API.StackList>('/stack/validInstallServicesDeps', {
    method: 'POST',
    data: {...(options || {})},
  });
}

// 服务可配置参数
export async function getServiceConfAPI(params: {
  serviceId?: number;
  inWizard?: boolean;
}) {
  return request<API.ConfList>('/stack/listServiceConf', {
    method: 'GET',
    params: {
      ...params,
    },
  });
}

// 安装服务校验
export async function checkKerberosAPI(options?: number[]) {
  return request<API.normalResult>('/service/validInstallServiceHasKerberos', {
    method: 'POST',
    data: options,
  });
}

// 添加服务校验
export async function initServiceAPI(options?: API.SubmitServicesParams) {
  return request<API.normalResult>('/service/initService', {
    method: 'POST',
    data: options,
  });
}

// 服务
export async function serviceListAPI(params: {
  clusterId?: number;
}) {
  return request<API.normalResult>('/service/listServiceInstance', {
    method: 'GET',
    params: {
      ...params,
    },
  });
}

// /command/list?clusterId=1
/** 指令 */
export async function getCommandListAPI(options?: { [key: string]: any }) {
  return request<API.normalResult>('/command/list', {
    method: 'GET',
    params: {
      ...options,
    },
  });
}

/** 指令明细 */
export async function getCommandDetailAPI(options?: { [key: string]: any }) {
  return request<API.commandResult>('/command/detail', {
    method: 'GET',
    params: {
      ...options,
    },
  });
}

/** 删除服务 */
export async function deleteServiceAPI(options?: { [key: string]: any }) {
  return request<API.normalResult>('/service/deleteServiceInstance', {
    method: 'GET',
    params: {
      ...options,
    },
  });
}

/** 日志详情 */
export async function getTaskLogAPI(options?: { [key: string]: any }) {
  return request<API.logResult>('/log/task', {
    method: 'GET',
    params: {
      ...options,
    },
  });
}

// 启动服务
export async function startServiceAPI(options?: { [key: string]: any}) {
  return request<API.normalResult>('/service/startService', {
    method: 'POST',
    params: options,
  });
}

// 停止服务
export async function stopServiceAPI(options?: { [key: string]: any}) {
  return request<API.normalResult>('/service/stopService', {
    method: 'POST',
    params: options,
  });
}

// 重启服务
export async function restartServiceAPI(options?: { [key: string]: any}) {
  return request<API.normalResult>('/service/restartService', {
    method: 'POST',
    params: options,
  });
}

// 更新配置
export async function upgradeServiceAPI(options?: { [key: string]: any}) {
  return request<API.normalResult>('/service/upgradeServiceConfig', {
    method: 'POST',
    params: options,
  });
}

/** 服务实例详情 */
export async function getServiceInfoAPI(options?: { [key: string]: any }) {
  return request<API.serviceInfosResult>('/service/serviceInstanceInfo', {
    method: 'GET',
    params: {
      ...options,
    },
  });
}

/** 服务实例角色列表 */
export async function getServiceRolesAPI(options?: { [key: string]: any }) {
  return request<API.serviceRolesResult>('/service/serviceInstanceRoles', {
    method: 'GET',
    params: {
      ...options,
    },
  });
}

/** 自动分配角色绑定节点 */
export async function getRolesAllocationAPI(options?: { [key: string]: any }) {
  return request<API.rolesValidResult>('/stack/getRolesAllocation', {
    method: 'POST',
    data: options,
  });
}

/** 查询服务实例配置 */
export async function getListConfsAPI(options?: { [key: string]: any }) {
  return request<API.ConfList>('/service/listConfs', {
    method: 'GET',
    params: {
      ...options,
    },
  });
}



/** 保存服务实例配置 */
export async function saveServiceConfAPI(options?: { [key: string]: any }) {
  return request<API.normalResult>('/service/serviceInstanceSaveConf', {
    method: 'POST',
    data: options,
  });
}



