export default [
  // {
  //   path: '/colony',
  //   layout: true,
  //   icon: 'smile',
  //   name: '集群管理',
  //   component: './colony',
  //   routes: [
  //     {
  //       name: 'colony',
  //       path: '/colony/colonyMg',
  //       component: './colony',
  //       // menuRender: false, // 当前路由不展示菜单
  //       // hideInMenu: true,
  //     },
  //     {
  //       name: '节点列表',
  //       icon: 'smile',
  //       path: '/colony/nodeList',
  //       component: './colony/nodeList',
  //     },
  //     {
  //       component: './404',
  //     },
  //   ],
  // },
  {
    name: 'colony',
    icon: 'smile',
    path: '/colony/colonyMg',
    component: './colony/colonyMg',
    menuRender: false, // 当前路由不展示菜单
    hideInMenu: true,
  },
  {
    name: 'nodelist',
    icon: 'smile',
    path: '/colony/nodeList',
    component: './colony/nodeList',
  },
  {
    name: 'servicelist',
    icon: 'smile',
    path: '/colony/serviceList',
    component: './colony/serviceList',
  },
  {
    name: 'addService',
    icon: 'smile',
    path: '/colony/serviceList/addService',
    component: './colony/serviceList/addService',
    menuRender: false, // 当前路由不展示菜单
    footerRender: false, // 不展示页脚
    hideInMenu: true,
  },
  {
    name: 'serviceListDetail',
    icon: 'smile',
    hideInMenu: true,
    path: '/colony/serviceList/detail',
    component: './colony/serviceList/detail',
  },
  {
    name: 'actionlist',
    icon: 'smile',
    path: '/colony/actionList',
    component: './colony/actionList',
  },
  {
    name: 'actionDetail',
    icon: 'smile',
    hideInMenu: true,
    path: '/colony/actionList/detail',
    component: './colony/actionList/detail',
  },
  {
    path: '/user',
    layout: false,
    routes: [
      {
        name: 'login',
        path: '/user/login',
        component: './user/Login',
      },
      {
        component: './404',
      },
    ],
  },
  // {
  //   path: '/welcome',
  //   name: 'welcome',
  //   icon: 'smile',
  //   component: './Welcome',
  // },
  // {
  //   path: '/admin',
  //   name: 'admin',
  //   icon: 'crown',
  //   access: 'canAdmin',
  //   routes: [
  //     {
  //       path: '/admin/sub-page',
  //       name: 'sub-page',
  //       icon: 'smile',
  //       component: './Welcome',
  //     },
  //     {
  //       component: './404',
  //     },
  //   ],
  // },
  // {
  //   name: 'list.table-list',
  //   icon: 'table',
  //   path: '/list',
  //   component: './TableList',
  // },
  {
    path: '/',
    redirect: '/colony/colonyMg',
  },
  {
    component: './404',
  },
];
