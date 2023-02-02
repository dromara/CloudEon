// 集群管理页面
import { PageContainer, ProCard } from '@ant-design/pro-components';
import { Space, Button, Avatar, Card, Spin } from 'antd';
import { FormattedMessage, useIntl, history } from 'umi';
import { EditOutlined, EllipsisOutlined, SettingOutlined } from '@ant-design/icons';
import { serviceListAPI } from '@/services/ant-design-pro/colony';
import { useState, useEffect } from 'react';

const { Meta } = Card;

const serviceList: React.FC = () => {
  const intl = useIntl();
  const [serviceList, setServiceList] = useState<any[]>();
  const [loading, setLoading] = useState(false);
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
                <Card
                key={sItem.serviceName}
                style={{ width: 300 }}
                actions={[
                  <SettingOutlined key="setting" />,
                  <EditOutlined key="edit" />,
                  <EllipsisOutlined key="ellipsis" />,
                ]}
                onClick={() => {
                  history.push('/colony/serviceList/detail');
                }}
              >
                <Meta
                  avatar={<Avatar style={{width:'40px', height:'40px'}} src={'data:image/jpeg;base64,'+sItem.icon} />}
                  title={sItem.serviceName}
                  description={sItem.serviceStateValue}
                />
              </Card>
              )

            })

          ):(
            <Card
              style={{ width: 300 }}
              actions={[
                <SettingOutlined key="setting" />,
                <EditOutlined key="edit" />,
                <EllipsisOutlined key="ellipsis" />,
              ]}
              onClick={() => {
                history.push('/colony/serviceList/detail');
              }}
            >
              <Meta
                avatar={<Avatar src="https://gw.alipayobjects.com/zos/rmsportal/JiqGstEfoWAOHiTxclqi.png" />}
                title="测试 title"
                description="This is the description"
              />
            </Card>
          )
        }
      </Space>
      </Spin>
    </PageContainer>
  );
};

export default serviceList;
