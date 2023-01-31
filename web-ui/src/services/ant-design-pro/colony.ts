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

// 节点列表
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

// 服务列表
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
  return request<API.StackList>('/validInstallServicesDeps', {
    method: 'GET',
    data: {
      ...options,
    },
  });
}


