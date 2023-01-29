import { request } from 'umi';

export async function getListService(params: {
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
