import { PageContainer, ProCard } from '@ant-design/pro-components';
import { Button, Radio, Typography, Tabs, message, Spin } from 'antd';
import type { TabsProps } from 'antd';
import { PoweroffOutlined, PlayCircleOutlined, ReloadOutlined, ExceptionOutlined, DeleteOutlined  } from '@ant-design/icons';
import { FormattedMessage, useIntl, history } from 'umi';
import styles from './index.less'
import { useState, useEffect } from 'react';
import { restartServiceAPI, stopServiceAPI, startServiceAPI, deleteServiceAPI, getServiceInfoAPI, getServiceRolesAPI, upgradeServiceAPI } from '@/services/ant-design-pro/colony';
import StatusTab from './components/StatusTab/index';
import RoleTab from './components/RoleTab/index'
import ConfigTab from './components/ConfigTab/index'
import { dealResult } from '../../../../utils/resultUtil'

const serviceListDetail: React.FC = () => {
  // const intl = useIntl();
  const [serviceId, setServiceId] = useState<any>(0);
  const [serviceName, setServiceName] = useState<any>('');
  const [selectACT, setSelectACT] = useState<any>('');
  const [btnLoading, setBtnLoading] = useState(false);
  const [statusInfo, setStatusInfo] = useState<API.serviceInfos>();
  const [rolesInfo, setRolesInfo] = useState<API.rolesInfos[]>();
  const [apiLoading, setApiLoading] = useState(false);
  const [currentTab, setCurrentTab] = useState('StatusTab');

  const onChange = (key: string) => {
    console.log(key);
    setCurrentTab(key)
    const params = {serviceInstanceId: serviceId}
    switch(key){
      case 'StatusTab': getInfos(params);break;
      case 'RoleTab': getRoles(params) ;break;
    }
  };

  const getInfos = async (params:any) =>{
    setApiLoading(true)
    const result = await getServiceInfoAPI(params)
    setApiLoading(false)
    if(result?.success){
      setStatusInfo(result?.data)
    }
  }

  const getRoles = async (params:any) =>{
    setApiLoading(true)
    const result = await getServiceRolesAPI(params)
    setApiLoading(false)
    if(result?.success){
      setRolesInfo(result?.data)
    }
  }

  const handleACT = async (key: string) => {
    if(!serviceId) return
    setSelectACT(key)
    let result: API.normalResult
    setBtnLoading(true)
    const params = {serviceInstanceId: serviceId}
    switch(key){
      case 'start': 
        result = await startServiceAPI(params);
        dealResult(result, key)
        break;
      case 'stop': 
        result = await stopServiceAPI(params);
        dealResult(result, key)
        break;
      case 'restart': 
        result = await restartServiceAPI(params);
        dealResult(result, key)
        break;
      case 'delete':
        result = await deleteServiceAPI(params)
        dealResult(result, key);
        break;
      case 'update': 
        result = await upgradeServiceAPI(params)
        dealResult(result, key);
        break;
      default:;
    }    
    setBtnLoading(false)
  }

  const btnLoadingStatus = (key: string) => {
    return selectACT == key && btnLoading
  }

  const btnDisabledStatus = (key: string) => {
    return selectACT != key && btnLoading
  }

  const items: TabsProps['items'] = [
    {
      key: 'StatusTab',
      label: `状态`,
      children: <StatusTab statusInfo={statusInfo || {}} loading={currentTab == 'StatusTab' && apiLoading}/>,
    },
    {
      key: 'RoleTab',
      label: `角色`,
      children: <RoleTab rolesInfo={rolesInfo || []} loading={currentTab == 'RoleTab' && apiLoading}/>,
    },
    {
      key: 'ConfigTab',
      label: `配置`,
      children: <ConfigTab serviceId={serviceId}/>,
    },
    {
      key: 'WebUITab',
      label: `webUI`,
      children: <ConfigTab serviceId={serviceId}/>,
    },
  ];

  useEffect(()=>{
    const { query } = history.location;
    setServiceId(query?.id || 0)
    setServiceName(query?.serviceName || '')
    getInfos({serviceInstanceId: query?.id || 0})
  }, [])


  return (
    <div className={styles.pageContentLayout}>
      <PageContainer
      key='serviceDetailPage'
      header={ {
          title: <>{serviceName}</>,
          extra: [
            <div className={styles.btnsWrap} key="serviceDetailPageBtns" >
              <Button 
                key="start" 
                size='small'
                type="primary" ghost
                // type="primary"
                // className={styles.btnStart}
                loading={btnLoadingStatus('start')} 
                disabled={btnDisabledStatus('start')} 
                onClick={()=> handleACT('start')}
                icon={<PlayCircleOutlined key="startIcon"/>}
              >
                启动
              </Button>
              <Button 
                key="stop" 
                size='small'
                type="primary" ghost
                // type="primary" 
                // className={styles.btnStop}
                loading={btnLoadingStatus('stop')} 
                disabled={btnDisabledStatus('stop')}  
                onClick={()=> handleACT('stop')}
                icon={<PoweroffOutlined key="stopIcon" />}
              >
                停止
              </Button>
              <Button 
                key="restart" 
                size='small'
                type="primary" ghost
                loading={btnLoadingStatus('restart')} 
                disabled={btnDisabledStatus('restart')}  
                onClick={()=> handleACT('restart')}
                icon={<ReloadOutlined key="restartIcon" />}
              >
                重启
              </Button>
              <Button 
                key="update" 
                size='small'
                type="primary" ghost
                // className={styles.btnUpdate}
                loading={btnLoadingStatus('update')} 
                disabled={btnDisabledStatus('update')}  
                onClick={()=> handleACT('update')}
                icon={<ExceptionOutlined key="updateIcon"  />}
              >
                  更新配置
              </Button>
              <Button 
                key="delete" 
                size='small'
                type="primary" ghost
                // danger
                loading={btnLoadingStatus('delete')} 
                disabled={btnDisabledStatus('delete')}  
                onClick={()=> handleACT('delete')}
                icon={<DeleteOutlined key="deleteIcon" />}
              >
                删除
              </Button>
            </div> 
          ]
      }}
      >
        <Tabs
          onChange={onChange}
          type="card"
          items={items}
        />
      </PageContainer>
    </div>
  );
};

export default serviceListDetail;
