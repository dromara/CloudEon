import Footer from '@/components/Footer';
import RightContent from '@/components/RightContent';
import { BookOutlined, LinkOutlined } from '@ant-design/icons';
import type { Settings as LayoutSettings } from '@ant-design/pro-components';
import { PageLoading, SettingDrawer } from '@ant-design/pro-components';
import type { RunTimeLayoutConfig } from 'umi';
import { history, RequestConfig } from 'umi';
import { message, Image } from 'antd';
import defaultSettings from '../config/defaultSettings';
import { currentUser as queryCurrentUser } from './services/ant-design-pro/api';
import logoImg from '../src/assets/images/ic_launcher.png';
import userImg from '../src/assets/images/user.png'
import React from 'react';
import * as Icon from '@ant-design/icons';
import {
  RobotOutlined,
} from '@ant-design/icons';


const isDev = process.env.NODE_ENV === 'development';
const loginPath = '/user/login';


const menuIcons = {
  '节点':'RobotOutlined',
  '服务':'CloudServerOutlined',
  '指令':'AimOutlined'
}
// antd4中动态创建icon
const createIcon = (key: string) => {
  // console.log('--key:', key);
  
  const icon = React.createElement(
    Icon[menuIcons[key]],
    {
      style:{ fontSize: '24px'}
    }
  )
  return icon
}


// 接口请求全局配置
export const request: RequestConfig = {
  timeout: 10000,
  errorHandler:(error)  => {
    if(error && error.name==="BizError"){
      const { response } = error;
      if ('success' in response && !response.success) {
        message.error(`请求错误: ${('message' in response) ? response.message : '' }`, 3);
        return {
          success:false,
          data:[],
          message:''
        }
      }
    }
  },
  errorConfig: {
  },
  // 自定义端口规范
  // errorConfig: {
  //   adaptor: res => {
  //     return {
  //       success: res.code ==config.successCode,
  //       data:res.data,
  //       errorCode:res.code,
  //       errorMessage: res.msg,
  //     };
  //   },
  middlewares: [],
  requestInterceptors: [],
  responseInterceptors: [
    (response, options) => {
      const codeMaps = {
        502: '网关错误。',
        503: '服务不可用，服务器暂时过载或维护。',
        504: '网关超时。',
      };
      codeMaps[response.status] && message.error(codeMaps[response.status]);
      
      return response;
    }
  ],
};

/** 获取用户信息比较慢的时候会展示一个 loading */
export const initialStateConfig = {
  loading: <PageLoading />,
};

/**
 * @see  https://umijs.org/zh-CN/plugins/plugin-initial-state
 * */
export async function getInitialState(): Promise<{
  settings?: Partial<LayoutSettings>;
  currentUser?: API.CurrentUser;
  loading?: boolean;
  fetchUserInfo?: () => Promise<API.CurrentUser | undefined>;
}> {
  const fetchUserInfo = async () => {
    try {
      const msg = await queryCurrentUser();
      return msg.data;
    } catch (error) {
      history.push(loginPath);
    }
    return undefined;
  };
  // 如果不是登录页面，执行
  if (history.location.pathname !== loginPath) {
    const currentUser = await fetchUserInfo();
    return {
      fetchUserInfo,
      currentUser,
      settings: defaultSettings,
    };
  }
  return {
    fetchUserInfo,
    settings: defaultSettings,
  };
}

// ProLayout 支持的api https://procomponents.ant.design/components/layout
export const layout: RunTimeLayoutConfig = ({ initialState, setInitialState }) => {
  return {
    rightContentRender: () => <RightContent />,
    disableContentMargin: false,
    waterMarkProps: {
      // 水印
      // content: initialState?.currentUser?.name,
    },
    footerRender: () => <Footer />,
    onPageChange: () => {
      const { location } = history;
      // 如果没有登录，重定向到 login
      if (!initialState?.currentUser && location.pathname !== loginPath) {
        history.push(loginPath);
      }
    },
    links: isDev
      ? [
          // <Link key="openapi" to="/umi/plugin/openapi" target="_blank">
          //   <LinkOutlined />
          //   <span>OpenAPI 文档</span>
          // </Link>,
          // <Link to="/~docs" key="docs">
          //   <BookOutlined />
          //   <span>业务组件文档</span>
          // </Link>,
        ]
      : [],
    menuHeaderRender: undefined,
    // 自定义 403 页面
    // unAccessible: <div>unAccessible</div>,
    // 增加一个 loading 的状态
    childrenRender: (children: any, props: { location: { pathname: string | string[]; }; }) => {
      if (initialState?.loading) return <PageLoading />;
      return (
        <>
          {children}
          {!props.location?.pathname?.includes('/login') && (
            <SettingDrawer
              disableUrlParams
              enableDarkTheme
              settings={initialState?.settings}
              onSettingChange={(settings) => {
                setInitialState((preInitialState) => ({
                  ...preInitialState,
                  settings,
                }));
              }}
            />
          )}
        </>
      );
    },
    ...initialState?.settings,
    logo:<><Image src={logoImg}/></>,
    // layout: 'side',
    breakpoint: true,
    fixedHeader:true,
    collapsed:false,
    collapsedButtonRender: false,
    menuFooterRender:()=><></>,
    menuItemRender: (itemProps: any, defaultDom: any, props: any) => (

      <div style={{height:'100%',display:'flex',justifyContent: 'center',flexDirection:'column', alignItems:'center',width:'100%'}} 
        onClick={() => {
          history.push(itemProps.path);
        }}>
            <div style={{display:'inline-flex',lineHeight:'30px', fontSize: '24px'}}>
              {/* {itemProps.icon} */}
              {createIcon(itemProps.name)}
            </div>
            <div style={{lineHeight:'20px',fontSize:'12px'}}>{itemProps.name}</div>
      </div>
    )
  };
};
