// 集群管理页面
import { PageContainer, ProCard } from '@ant-design/pro-components';
import { Space, Button, Avatar, Card, Spin, Popconfirm, message,Popover } from 'antd';
import { FormattedMessage, useIntl, history } from 'umi';
import { PlayCircleOutlined, ReloadOutlined, PoweroffOutlined, DeleteOutlined } from '@ant-design/icons';
import { serviceListAPI, deleteServiceAPI, restartServiceAPI, stopServiceAPI, startServiceAPI } from '@/services/ant-design-pro/colony';
import { useState, useEffect } from 'react';
import { dealResult } from '../../../utils/resultUtil'

const { Meta } = Card;

const serviceList: React.FC = () => {
  const intl = useIntl();
  const [serviceList, setServiceList] = useState<any[]>();
  const [loading, setLoading] = useState(false);
  const [currentId, setCurrentId] = useState(0);
  const [btnLoading, setBtnLoading] = useState(false);
  const colonyData = JSON.parse(sessionStorage.getItem('colonyData') || '{}')

  const getServiceListData = async () => {
    const params = {
      clusterId: colonyData.clusterId
    }
    setLoading(true)
    const result = await serviceListAPI(params)
    setLoading(false)
    if(result?.success){
      setServiceList(result?.data)
    }
  }
  const handleDeleteService = async (params:any) => {
    setLoading(true)
    const result = await deleteServiceAPI(params)
    setLoading(false)
    if(result?.success){
      getServiceListData()
      message.success('删除成功！', 3)
    }
  }

  const handleACT = async (key: string, serviceId:number) => {
    if(!serviceId) return
    setCurrentId(serviceId)
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

  useEffect(()=>{
    getServiceListData()
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
      <Space>
        {
          (serviceList && serviceList.length > 0) ? (
            serviceList.map(sItem=>{
              return (
                <Spin tip="Loading" key={sItem.serviceName+'spin'} size="small" spinning={btnLoadingStatus(sItem.id)}>
                  <Card
                    hoverable
                    key={sItem.serviceName}
                    style={{ width: 300 }}
                    actions={[
                      <Popover content="启动" title="">
                        <PlayCircleOutlined onClick={()=>{ handleACT('start',sItem.id) }}/>
                      </Popover>,
                      <Popover content="停止" title="">
                        <PoweroffOutlined onClick={()=>{ handleACT('stop',sItem.id) }}/>
                      </Popover>,
                      <Popover content="重启" title="">
                        <ReloadOutlined onClick={()=>{ handleACT('restart',sItem.id) }}/>
                      </Popover>,
                      <Popover content="删除" title="">
                        <Popconfirm
                          title="确定删除该服务吗？"
                          onConfirm={()=>{
                            handleDeleteService({serviceInstanceId: sItem.id })
                          }}
                          onCancel={()=>{}}
                          okText="确定"
                          cancelText="取消"
                        >
                          <DeleteOutlined key="setting"/>
                        </Popconfirm>
                      </Popover>,
                      // <SettingOutlined />,
                      // <EditOutlined key="edit" />,
                      // <EllipsisOutlined key="ellipsis" />,
                    ]}
                  >
                    <div 
                      style={{cursor:'pointer'}}
                      onClick={() => {
                        history.push(`/colony/serviceList/detail?serviceName=${sItem.serviceName}&id=${sItem.id}`);
                      }}>
                        <Meta
                          avatar={<Avatar style={{width:'40px', height:'40px'}} src={'data:image/jpeg;base64,'+sItem.icon} />}
                          title={sItem.serviceName}
                          description={sItem.serviceStateValue}
                        />
                    </div>
                  </Card>
                </Spin>
                
              )

            })

          ):(
            <div>
              暂无数据，请新增服务
            </div>
            // <Card
            //   style={{ width: 300 }}
            //   actions={[
            //     <SettingOutlined key="setting" />,
            //     <EditOutlined key="edit" />,
            //     <EllipsisOutlined key="ellipsis" />,
            //   ]}
            // >
            //   <div 
            //   style={{cursor:'pointer'}}
            //   onClick={() => {
            //     history.push('/colony/serviceList/detail');
            //   }}>
            //       <Meta
            //         avatar={<Avatar src="https://gw.alipayobjects.com/zos/rmsportal/JiqGstEfoWAOHiTxclqi.png" />}
            //         title="测试 title"
            //         description="This is the description"
            //       />
            //   </div>
            // </Card>
          )
        }
      </Space>
      </Spin>
    </PageContainer>
  );
};

export default serviceList;
