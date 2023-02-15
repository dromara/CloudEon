// @ts-ignore
/* eslint-disable */

declare namespace API {
  type CurrentUser = {
    name?: string;
    avatar?: string;
    userid?: string;
    email?: string;
    signature?: string;
    title?: string;
    group?: string;
    tags?: { key?: string; label?: string }[];
    notifyCount?: number;
    unreadCount?: number;
    country?: string;
    access?: string;
    geographic?: {
      province?: { label?: string; key?: string };
      city?: { label?: string; key?: string };
    };
    address?: string;
    phone?: string;
  };

  type LoginResult = {
    status?: string;
    type?: string;
    currentAuthority?: string;
  };

  type PageParams = {
    current?: number;
    pageSize?: number;
  };

  type RuleListItem = {
    key?: number;
    disabled?: boolean;
    href?: string;
    avatar?: string;
    name?: string;
    owner?: string;
    desc?: string;
    callNo?: number;
    status?: number;
    updatedAt?: string;
    createdAt?: string;
    progress?: number;
  };

  type RuleList = {
    data?: RuleListItem[];
    /** 列表的内容总数 */
    total?: number;
    success?: boolean;
  };

  type FakeCaptcha = {
    code?: number;
    status?: string;
  };

  type LoginParams = {
    username?: string;
    password?: string;
    autoLogin?: boolean;
    type?: string;
  };

  type ErrorResponse = {
    /** 业务约定的错误码 */
    errorCode: string;
    /** 业务上的错误信息 */
    errorMessage?: string;
    /** 业务上的请求是否成功 */
    success?: boolean;
  };

  type NoticeIconList = {
    data?: NoticeIconItem[];
    /** 列表的内容总数 */
    total?: number;
    success?: boolean;
  };

  type NoticeIconItemType = 'notification' | 'message' | 'event';

  type NoticeIconItem = {
    id?: string;
    extra?: string;
    key?: string;
    read?: boolean;
    avatar?: string;
    title?: string;
    status?: string;
    datetime?: string;
    description?: string;
    type?: NoticeIconItemType;
  };

  type ServiceItem = {
    label?: string;
    name?: string;
    id: number;
    version?: string;
    description?: string;
    dockerImage?: string;
    roles?: Array<string>;
  };

  type ServiceList = {
    success?: boolean;
    message?: string;
    data?: ServiceItem[];
  };

  type ColonyData = {
    clusterId?: number,
  }

  type ColonyItem = {
    id?: number,
    createBy?: string,
    createTime?: string,
    clusterName?: string,
    clusterCode?: string,
    stackId?: number
  }

  type ColonyList = {
    success?: boolean;
    message?: string;
    data?: ColonyItem[];
  };

  type NodeItem = {
    id?: number,
    createTime?: string,
    hostname?: string,
    ip?: string,
    rack?: string,
    coreNum?: number,
    totalMem?: number,
    totalDisk?: number,
    sshUser?: string,
    sshPort?: number,
    clusterId?: number,
    cpuArchitecture?: string,
    nodeLabel?: string,
    serviceRoleNum?: string
  }

  type NodeList = {
    success?: boolean;
    message?: string;
    data?: NodeItem[];
  };

  type StackItem = {
    id?: number,
    stackCode?: string
  }

  type StackList = {
    success?: boolean;
    message?: string;
    data?: StackItem[];
  };

  type ConfItem = {
    name?: string,
    description?: string,
    label?: string,
    recommendExpression?: string,
    valueType?: string,
    configurableInWizard?: boolean,
    groups?: string[]
  }

  type ConfList = {
    success?: boolean;
    message?: string;
    data?: {
      confs?: ConfItem[],
      customFileNames?: string[]
    };
  };

  type RolesItem = {
    stackRoleName?: string,
    nodeIds?: number[]
  }

  type PresetConfListItem = {
    name?: string,
    value?: string,
    recommendedValue?: string
  }

  type ServiceInfosItem = {
    stackServiceId?: number,
    stackServiceName?: string,
    stackServiceLabel?: string,
    roles?: RolesItem[],
    presetConfList?: PresetConfListItem[]
  }

  type SubmitServicesParams = {
    stackId?: number,
    enableKerberos?: boolean,
    clusterId?: number,
    serviceInfos?: ServiceInfosItem[]
  }

  type normalResult = {
    success?: boolean;
    message?: string;
    data?: any[];
  }

  type commandResult = {
    success?: boolean;
    message?: string;
    data?: commandType;
  }

  type commandType = {
    id: number;
    name: string;
    "type": string,
    "commandState": string,
    "submitTime": string,
    "startTime": string,
    "endTime": string,
    "currentProgress": number,
    "operateUserId": number,
    "clusterId": number,
    // "tasksMap":
  }

}
