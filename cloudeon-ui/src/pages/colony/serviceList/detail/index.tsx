import { PageContainer, ProCard } from '@ant-design/pro-components';
import { Button, Popover, Popconfirm, Dropdown, Tabs, message, Spin } from 'antd';
import type { TabsProps } from 'antd';
import { PoweroffOutlined, PlayCircleOutlined, ReloadOutlined,LoadingOutlined, ExceptionOutlined, DeleteOutlined, DownOutlined  } from '@ant-design/icons';
import { FormattedMessage, useIntl, history } from 'umi';
import styles from './index.less'
import { useState, useEffect } from 'react';
import { 
  restartServiceAPI, 
  stopServiceAPI, 
  startServiceAPI, 
  deleteServiceAPI, 
  getServiceInfoAPI, 
  upgradeServiceAPI, 
  getListWebURLsAPI,
  getDashboardUrlAPI
} from '@/services/ant-design-pro/colony';
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
  const [webUrls, setWebUrls] = useState<API.webUrlsItem[]>();
  const [apiLoading, setApiLoading] = useState(false);
  const [currentTab, setCurrentTab] = useState('StatusTab');
  const [dashboardUrl, setDashboardUrl] = useState<any>('');
  const [dropdpwnStatus, setDropdpwnStatus] = useState(false);
  const [currentAction, setCurrentAction] = useState('');

  const onChange = (key: string) => {
    console.log(key);
    setCurrentTab(key)
    const params = {serviceInstanceId: serviceId}
    switch(key){
      case 'StatusTab': getInfos(params);break;
      default:break;
    }
  };

 // 获取webUI地址
  const getListWebURLs = async (params:any) => {
    const result = await getListWebURLsAPI(params)
    if(result?.success){
      setWebUrls(result?.data)
    }
  }

  // 获取服务详情
  const getInfos = async (params:any) =>{
    setApiLoading(true)
    const result = await getServiceInfoAPI(params)
    setApiLoading(false)
    if(result?.success){
      setStatusInfo(result?.data)
    }
  }

  // 获取实力监控地址
  const getDashboard = async (params:any) =>{
    setApiLoading(true)
    const result = await getDashboardUrlAPI(params)
    setApiLoading(false)
    if(result?.success){
       result?.data && setDashboardUrl(result.data)
    }
  }

  const handleACT = async (key: string) => {
    if(!serviceId) return
    setSelectACT(key)
    setDropdpwnStatus(false)
    let result: API.normalResult
    setBtnLoading(true)
    const params = {serviceInstanceId: serviceId}
    switch(key){
      case 'start': 
        result = await startServiceAPI(params);
        dealResult(result, key, '操作成功')
        break;
      case 'stop': 
        result = await stopServiceAPI(params);
        dealResult(result, key, '操作成功')
        break;
      case 'restart': 
        result = await restartServiceAPI(params);
        dealResult(result, key, '操作成功')
        break;
      case 'delete':
        result = await deleteServiceAPI(params)
        dealResult(result, key, '操作成功');
        break;
      case 'update': 
        result = await upgradeServiceAPI(params)
        dealResult(result, key, '操作成功');
        break;
      default:;
    }    
    currentTab === 'StatusTab' && getInfos({serviceInstanceId: serviceId})
    setBtnLoading(false)
    setCurrentAction('');
  }

  const handleOpenChange = (flag: boolean) => {
    setDropdpwnStatus(flag);
  };
  const handleCancel = () => {
    setDropdpwnStatus(false)
    setCurrentAction('');
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
      children: <StatusTab statusInfo={statusInfo || {}} dashboardUrl={dashboardUrl} loading={currentTab == 'StatusTab' && apiLoading}/>,
    },
    {
      key: 'RoleTab',
      label: `角色`,
      children: <RoleTab serviceId={serviceId}/>,
    },
    {
      key: 'ConfigTab',
      label: `配置`,
      children: <ConfigTab serviceId={serviceId}/>,
    },
    {
      key: 'WebUITab',
      label: `webUI`,
      // children: <webUITab serviceId={serviceId}/>,
    },
  ];

  useEffect(()=>{
    const { query } = history.location;
    setServiceId(query?.id || 0)
    setServiceName(query?.serviceName || '')
    const params = {serviceInstanceId: query?.id || null}
    getInfos(params)
    getDashboard(params)
    getListWebURLs(params)
  }, [])


  return (
    <div className={styles.pageContentLayout}>
      <PageContainer
      key='serviceDetailPage'
      header={ {
          title: <>{serviceName}</>,
          extra: [
            <div className={styles.btnsWrap} key="serviceDetailPageBtns" >
              <Dropdown 
                          trigger={['click']}
                          open={dropdpwnStatus}
                          onOpenChange={handleOpenChange}
                          dropdownRender={(menu) => (
                            <div className={`${styles.dropdownContent} ${styles.actionSelects}`}>
                                <Popconfirm
                                      key='startPop'
                                      placement="left"
                                      title="确定要启动吗?"
                                      onConfirm={()=>{ handleACT('start') }}
                                      onCancel={()=>{ handleCancel()} }
                                      okText="确定"
                                      cancelText="取消"
                                >
                                  <div className={
                                    `${styles.actionItem} ${currentAction=='启动' ? styles.clickedItem : ''}`
                                  } onClick={()=>{setCurrentAction('启动')}} >
                                    <PlayCircleOutlined/>&nbsp; 启动
                                  </div> 
                                </Popconfirm>
                                <Popconfirm
                                      key='stopPop'
                                      placement="left"
                                      title="确定要停止吗?"
                                      onConfirm={()=>{ handleACT('stop') }}
                                      onCancel={()=>{ handleCancel()} }
                                      okText="确定"
                                      cancelText="取消"
                                >
                                  <div 
                                    className={`${styles.actionItem} ${currentAction=='停止' ? styles.clickedItem : ''}`}
                                    onClick={()=>{setCurrentAction('停止')}}
                                  >
                                    <PoweroffOutlined/>&nbsp; 停止
                                  </div> 
                                </Popconfirm>
                                <Popconfirm
                                      key='restartPop'
                                      placement="left"
                                      title="确定要重启吗?"
                                      onConfirm={()=>{ handleACT('restart') }}
                                      onCancel={()=>{ handleCancel()} }
                                      okText="确定"
                                      cancelText="取消"
                                    >
                                    <div 
                                      className={`${styles.actionItem} ${currentAction=='重启' ? styles.clickedItem : ''}`}
                                      onClick={()=>{setCurrentAction('重启')}}
                                    >
                                      <ReloadOutlined/>&nbsp; 重启
                                    </div> 
                                </Popconfirm>
                                <Popconfirm
                                  placement="left"
                                  title="确定要更新配置吗"
                                  onConfirm={()=>{
                                    handleACT('update')
                                  }}
                                  onCancel={()=>{ handleCancel()} }
                                  okText="确定"
                                  cancelText="取消"
                                >
                                  <div 
                                    className={`${styles.actionItem} ${currentAction=='更新配置' ? styles.clickedItem : ''}`}
                                    onClick={()=>{setCurrentAction('更新配置')}}
                                  >
                                    <ExceptionOutlined/>&nbsp; 更新配置
                                  </div> 
                                </Popconfirm>
                                <Popconfirm
                                  placement="left"
                                  title="确定删除该服务吗？"
                                  onConfirm={()=>{
                                    handleACT('delete')
                                  }}
                                  onCancel={()=>{ handleCancel()} }
                                  okText="确定"
                                  cancelText="取消"
                                >
                                  <div 
                                    className={`${styles.actionItem} ${currentAction=='删除' ? styles.clickedItem : ''}`}
                                    onClick={()=>{setCurrentAction('删除')}}
                                  >
                                    <DeleteOutlined/>&nbsp; 删除
                                  </div> 
                                </Popconfirm>
                            </div>
                          )}
                        >
                        <Button type="primary"
                        className={styles.btnText}
                          onClick={()=>{ setCurrentAction('');setDropdpwnStatus(true) }}
                          loading={btnLoading}
                        >{btnLoading?<>{`${currentAction}中 `}<LoadingOutlined /></>:<>服务操作&nbsp;<DownOutlined /></>}  
                        
                        </Button>
                        </Dropdown>
              {/* <Popconfirm
                    key='startPop'
                    title="确定要启动吗?"
                    onConfirm={()=>handleACT('start')}
                    okText="确定"
                    cancelText="取消"
              >
                    <Button 
                      key="start" 
                      size='small'
                      type="primary" ghost
                      loading={btnLoadingStatus('start')} 
                      disabled={btnDisabledStatus('start')} 
                      // onClick={()=> handleACT('start')}
                      icon={<PlayCircleOutlined key="startIcon"/>}
                    >
                      启动
                    </Button>
              </Popconfirm>
              <Popconfirm
                    key='stopPop'
                    title="确定要停止吗?"
                    onConfirm={()=>handleACT('stop')}
                    okText="确定"
                    cancelText="取消"
              >
                <Button 
                  key="stop" 
                  size='small'
                  type="primary" ghost
                  loading={btnLoadingStatus('stop')} 
                  disabled={btnDisabledStatus('stop')}  
                  // onClick={()=> handleACT('stop')}
                  icon={<PoweroffOutlined key="stopIcon" />}
                >
                  停止
                </Button>
              </Popconfirm>
              <Popconfirm
                    key='restartPop'
                    title="确定要重启吗?"
                    onConfirm={()=>handleACT('restart')}
                    okText="确定"
                    cancelText="取消"
              >
                <Button 
                  key="restart" 
                  size='small'
                  type="primary" ghost
                  loading={btnLoadingStatus('restart')} 
                  disabled={btnDisabledStatus('restart')}  
                  // onClick={()=> handleACT('restart')}
                  icon={<ReloadOutlined key="restartIcon" />}
                >
                  重启
                </Button>
              </Popconfirm>
              <Popconfirm
                    key='updatePop'
                    title="确定要更新配置吗?"
                    onConfirm={()=>handleACT('update')}
                    okText="确定"
                    cancelText="取消"
              >
                <Button 
                  key="update" 
                  size='small'
                  type="primary" ghost
                  // className={styles.btnUpdate}
                  loading={btnLoadingStatus('update')} 
                  disabled={btnDisabledStatus('update')}  
                  // onClick={()=> handleACT('update')}
                  icon={<ExceptionOutlined key="updateIcon"  />}
                >
                    更新配置
                </Button>
              </Popconfirm>
              <Popconfirm
                    key='deletePop'
                    title="确定要删除吗?"
                    onConfirm={()=>handleACT('delete')}
                    okText="确定"
                    cancelText="取消"
              >
                  <Button 
                    key="delete" 
                    size='small'
                    type="primary" ghost
                    // danger
                    loading={btnLoadingStatus('delete')} 
                    disabled={btnDisabledStatus('delete')}  
                    // onClick={()=> handleACT('delete')}
                    icon={<DeleteOutlined key="deleteIcon" />}
                  >
                    删除
                  </Button>
              </Popconfirm> */}
            </div> 
          ]
      }}
      >
        {/* <Tabs
          onChange={onChange}
          type="card"
          items={items}
        /> */}
        <div className={styles.tabsBar}>
          {
            items.map(item=>{
              return (
                item.key!= 'WebUITab'?
                <div className={`${currentTab == item.key? styles.actived : ''}`} key={item.key} onClick={(e)=>onChange(item.key)}>
                  {item.label}
                </div>
                :
                <div key={item.key}>
                  <Popover content={
                    webUrls?.map(urlItem=>{
                      if(urlItem && (urlItem.ipUrl || urlItem.hostnameUrl)){
                        return <div key={urlItem.ipUrl || urlItem.hostnameUrl}>
                          <div>{urlItem.name}
                            <a href={urlItem.hostnameUrl} target="_blank">&nbsp;&nbsp;地址1 </a>
                            <a href={urlItem.ipUrl} target="_blank">&nbsp;地址2 &nbsp;</a>
                          </div>
                          {/* <div><a href={urlItem.ipUrl} target="_blank">{urlItem.name} <LinkOutlined /></a></div> */}
                        </div>
                      }
                    })
                  } title="" placement="bottom">
                    <Button type="text">{item.label}</Button>
                  </Popover>
                  
                </div>
              )
            })
          }
        </div>
        <div className={styles.tabContent}>
          { currentTab != 'WebUITab' && items.filter(item=>{return item.key == currentTab})[0].children}
        </div>
      </PageContainer>
    </div>
  );
};

export default serviceListDetail;
