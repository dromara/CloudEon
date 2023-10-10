// 集群管理页面
import { PageContainer, ProCard } from '@ant-design/pro-components';
import { Space, Button, Avatar, Card, Spin, Popconfirm, message,Popover,Tooltip,Dropdown } from 'antd';
import { FormattedMessage, useIntl, history } from 'umi';
import { PlayCircleOutlined, ReloadOutlined, PoweroffOutlined, DeleteOutlined, AlertFilled, DownOutlined } from '@ant-design/icons';
import { serviceListAPI, deleteServiceAPI, restartServiceAPI, stopServiceAPI, startServiceAPI, getListWebURLsAPI } from '@/services/ant-design-pro/colony';
import { useState, useEffect } from 'react';
import { dealResult } from '../../../utils/resultUtil'
import styles from './index.less'
import { statusColor,serviceStatusColor } from '@/utils/colonyColor'


const { Meta } = Card;

const serviceList: React.FC = () => {
  const intl = useIntl();
  const [serviceList, setServiceList] = useState<any[]>();
  const [loading, setLoading] = useState(false);
  const [currentId, setCurrentId] = useState(0);
  const [currentAction, setCurrentAction] = useState('');
  const [btnLoading, setBtnLoading] = useState(false);
  const [webUrlLoading, setWebUrlLoading] = useState(false);
  const colonyData = JSON.parse(sessionStorage.getItem('colonyData') || '{}')
  const [dropdpwnStatus, setDropdpwnStatus] = useState(false);
  const [webUrls, setWebUrls] = useState<API.webUrlsItem[]>();
  

  const getServiceListData = async (showLoading:boolean) => {
    const params = {
      clusterId: colonyData.clusterId
    }
    showLoading && setLoading(true)
    const result = await serviceListAPI(params)
    showLoading && setLoading(false)
    if(result?.success){
      setServiceList(result?.data)
    }
  }
  const handleDeleteService = async (params:any) => {
    setLoading(true)
    const result = await deleteServiceAPI(params)
    setLoading(false)
    if(result?.success){
      getServiceListData(true)
      message.success('操作成功！', 3)
    }
  }

  const handleOpenChange = (flag: boolean) => {
    setDropdpwnStatus(flag);
  };
  const handleCancel = () => {
    setDropdpwnStatus(false)
    setCurrentAction('');
  }

  const handleACT = async (key: string, serviceId:number) => {
    if(!serviceId) return
    setCurrentId(serviceId)
    setCurrentAction('');
    setDropdpwnStatus(false)
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
      case 'update':;break;
      default:;
    }    
    setBtnLoading(false)
  }

  const btnLoadingStatus = (id: number) => {
    return currentId == id && btnLoading
  }

  // 获取webUI地址
  const getListWebURLs = async (params:any) => {
    setWebUrlLoading(true)
    const result = await getListWebURLsAPI(params)
    setWebUrlLoading(false)
    if(result?.success){
      setWebUrls(result?.data)
    }else{
      setWebUrls([])
    }
  }

  useEffect(()=>{
    getServiceListData(true)
    const intervalId = setInterval(()=>{
      getServiceListData(false)
    },2000)
    return () => clearInterval(intervalId);
  },[])


  return (
    <PageContainer
      header={{
        extra: [
          <Button
            key="addservice"
            type="primary"
            onClick={() => {
              history.push('/colony/serviceList/addService');
            }}
          >
            新增服务
          </Button>,
        ],
      }}
    >
      <Spin tip="Loading" size="small" spinning={loading}>
      <div className={styles.serviceList}>
        {
          (serviceList && serviceList.length > 0) ? (
            serviceList.map(sItem=>{
              return (
                <div 
                  key={sItem.serviceName+'card'}
                  className={styles.cardBox}
                >
                  <Spin tip="Loading" key={sItem.serviceName+'spin'} size="small" spinning={btnLoadingStatus(sItem.id)}>
                    <Card
                      hoverable
                      key={sItem.serviceName}
                      actions={[ 
                        <Dropdown 
                          trigger={['click']}
                          arrow={true}
                          placement='bottom'
                          open={currentId == sItem.id && dropdpwnStatus}
                          onOpenChange={handleOpenChange}
                          dropdownRender={(menu) => (
                            <div className={styles.dropdownContent}>
                              <div className={styles.actionSelects}>
                                <Popconfirm
                                      key='startPop'
                                      placement="right"
                                      title="确定要启动吗?"
                                      onConfirm={()=>{ handleACT('start',sItem.id) }}
                                      onCancel={()=>{ handleCancel()} }
                                      okText="确定"
                                      cancelText="取消"
                                >
                                  <div className={
                                    `${styles.actionItem} ${currentAction=='start' && currentId===sItem.id ? styles.clickedItem : ''}`
                                  } onClick={()=>{setCurrentAction('start')}} >
                                    <PlayCircleOutlined/>&nbsp; 启动
                                  </div> 
                                </Popconfirm>
                                <Popconfirm
                                      key='stopPop'
                                      placement="right"
                                      title="确定要停止吗?"
                                      onConfirm={()=>{ handleACT('stop',sItem.id) }}
                                      onCancel={()=>{ handleCancel()} }
                                      okText="确定"
                                      cancelText="取消"
                                >
                                  <div 
                                    className={`${styles.actionItem} ${currentAction=='stop' && currentId===sItem.id ? styles.clickedItem : ''}`}
                                    onClick={()=>{setCurrentAction('stop')}}
                                  >
                                    <PoweroffOutlined/>&nbsp; 停止
                                  </div> 
                                </Popconfirm>
                                <Popconfirm
                                      key='restartPop'
                                      placement="right"
                                      title="确定要重启吗?"
                                      onConfirm={()=>{ handleACT('restart',sItem.id) }}
                                      onCancel={()=>{ handleCancel()} }
                                      okText="确定"
                                      cancelText="取消"
                                    >
                                    <div 
                                      className={`${styles.actionItem} ${currentAction=='restart' && currentId===sItem.id ? styles.clickedItem : ''}`}
                                      onClick={()=>{setCurrentAction('restart')}}
                                    >
                                      <ReloadOutlined/>&nbsp; 重启
                                    </div> 
                                </Popconfirm>
                                <Popconfirm
                                  placement="right"
                                  title="确定删除该服务吗？"
                                  onConfirm={()=>{
                                    handleDeleteService({serviceInstanceId: sItem.id })
                                  }}
                                  onCancel={()=>{ handleCancel()} }
                                  okText="确定"
                                  cancelText="取消"
                                >
                                  <div 
                                    className={`${styles.actionItem} ${currentAction=='delete' && currentId===sItem.id ? styles.clickedItem : ''}`}
                                    onClick={()=>{setCurrentAction('delete')}}
                                  >
                                    <DeleteOutlined/>&nbsp; 删除
                                  </div> 
                                </Popconfirm>
                              </div>
                            </div>
                          )}
                        >
                        <div 
                          className={styles.cardClickText}
                          onMouseEnter={()=>{ setCurrentId(sItem.id);setCurrentAction('');setDropdpwnStatus(true) }}
                        >服务操作 <DownOutlined /> </div>
                        </Dropdown>,     
                        <div 
                          onMouseEnter={()=>{setWebUrls([]);setDropdpwnStatus(false);setCurrentId(sItem.id);getListWebURLs({serviceInstanceId: sItem.id})}}
                          style={{width:'100%'}}
                        >
                          {/* webUrls */}
                          <Popover
                            
                            content={
                              <Spin spinning={currentId===sItem.id && webUrlLoading} tip="加载中" >
                                <div style={{minHeight:'30px',minWidth:'200px'}}>                                  
                                {
                                  webUrls?.map(urlItem=>{
                                    if(urlItem && (urlItem.ipUrl || urlItem.hostnameUrl)){
                                      return (
                                        <div key={urlItem.ipUrl || urlItem.hostnameUrl}>
                                          <div>{urlItem.name}
                                            <a href={urlItem.hostnameUrl} target="_blank">&nbsp;&nbsp;地址1 </a>
                                            <a href={urlItem.ipUrl} target="_blank">&nbsp;地址2 &nbsp;</a>
                                          </div>
                                          {/* <div><a href={urlItem.ipUrl} target="_blank">{urlItem.name} <LinkOutlined /></a></div> */}
                                        </div>
                                      )
                                              
                                    }
                                  })
                                }
                                </div>
                              </Spin>
                            } title="" placement="bottom">
                            <div className={styles.cardClickText}>webUrls <DownOutlined /></div>
                          </Popover>
                        </div>, 
                      ]}
                    >
                      {
                        sItem.alertMsgCnt ? 
                        <div className={styles.alertWrap}>
                          <Tooltip 
                            placement="top" 
                            color="#fff"
                            title={
                              <div className={styles.alertText}>
                                {`该服务当前有${sItem.alertMsgCnt}个告警：`}
                                {sItem.alertMsgName.map((msg:any,index:any)=>{
                                  return <div key={index}>{`${index+1}. ${msg}`}</div>
                                })}
                              </div>
                            }
                          >
                            <AlertFilled style={{fontSize:'18px'}} />
                          </Tooltip>                      
                        </div> :''
                      }
                      <div 
                        style={{cursor:'pointer'}}
                        onClick={() => {
                          history.push(`/colony/serviceList/detail?serviceName=${sItem.serviceName}&id=${sItem.id}`);
                        }}>
                          <Meta
                            avatar={<Avatar style={{width:'40px', height:'40px'}} src={'data:image/jpeg;base64,'+sItem.icon} />}
                            title={<div style={{paddingLeft:'2px', fontWeight:'500', fontSize:'18px'}}>{sItem.serviceName}</div>}
                            description={<div style={{paddingLeft:'2px'}}>
                            <span style={{backgroundColor: serviceStatusColor[sItem.serviceStateValue || 0]}} 
                                  className={`${styles.statusCircel} ${[1,2,3,4,5,8].includes(sItem.serviceStateValue) && styles.statusProcessing}`}>
                              </span>
                            {sItem.serviceState}
                            </div>}
                          />
                      </div>
                    </Card>
                  </Spin>
                </div>
              )
            })

          ):(
            <div>
              暂无数据，请新增服务
            </div>
          )
        }
      </div>
      </Spin>
    </PageContainer>
  );
};

export default serviceList;
