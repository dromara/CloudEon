import { arrayMoveImmutable, PageContainer, ProCard, ProTable } from '@ant-design/pro-components';
import { Space, Card, Table, Tag, Button, Modal, Form, Progress, message, Spin } from 'antd';
import React, { useState, useEffect, useRef } from 'react';
import type { FormInstance } from 'antd/es/form';
import { FormattedMessage, useIntl, history } from 'umi';
import { getAlertAPI } from '@/services/ant-design-pro/colony';
import { formatDate } from '@/utils/common'
import styles from './index.less'
import { RightOutlined } from '@ant-design/icons';
import { statusColor } from '../../../utils/colonyColor'

const alertList: React.FC = () => {
  const intl = useIntl();
  const [alertLevelOptions, setAlertLevelOptions] = useState<any[]>();
  const [serviceOptions, setServiceOptions] = useState<any[]>();
  const [roleOptions, setRoleOptions] = useState<any[]>();
  const [hostnameOptions, setHostnameOptions] = useState<any[]>();
  const [actionListData, setNodeListData] = useState<any[]>();
  const [loading, setLoading] = useState(false);
  const [form] = Form.useForm();
  // formRef = React.createRef<FormInstance>();
  // const form = useRef();

  const getTableData = async () => {
    setLoading(true)
    const result: API.alertListResult =  await getAlertAPI();
    if(result?.data){
      let levelOptions:any[] = []
      let serviceOptions:any[] = []
      let roleOptions:any[] = []
      let hostnameOptions:any[] = []
      for(let i = 0; i < result.data.length; i++){
        let item = result.data[i]
        const { alertLevelMsg, serviceInstanceName, serviceRoleLabel, hostname } = item
        
        if(levelOptions.indexOf(alertLevelMsg) == -1) { levelOptions.push(alertLevelMsg||'') }
        if(serviceOptions.indexOf(serviceInstanceName) == -1) { serviceOptions.push(serviceInstanceName||'') }
        if(roleOptions.indexOf(serviceRoleLabel) == -1) { roleOptions.push(serviceRoleLabel||'') }
        if(hostnameOptions.indexOf(hostname) == -1) { hostnameOptions.push(hostname||'') }
      }
      setAlertLevelOptions(levelOptions.map(item=>{return {text:item, value: item}}))
      setServiceOptions(serviceOptions.map(item=>{return {text:item, value: item}}))
      setRoleOptions(roleOptions.map(item=>{return {text:item, value: item}}))
      setHostnameOptions(hostnameOptions.map(item=>{return {text:item, value: item}}))

      // console.log('levelOptions:', levelOptions);
      // console.log('serviceOptions:', serviceOptions);
      // console.log('roleOptions:', roleOptions);
      // console.log('hostnameOptions:', hostnameOptions);
      
    }
    setLoading(false)
    setNodeListData(result?.data)
  };

  const getData = JSON.parse(sessionStorage.getItem('colonyData') || '{}')


  useEffect(() => {
    getTableData()
  }, []);




  const columns = [
    {
      title: '告警名称',
      dataIndex: 'alertName',
      key: 'alertName',
      render: (_: any, record:API.alertItem) => {
        return (
          <div style={{minWidth:'100px'}}>{record.alertName}</div>
        )
      },
    },
    {
      title: '告警级别',
      dataIndex: 'alertLevelMsg',
      key: 'alertLevelMsg',
      filters: alertLevelOptions,
      onFilter: true,
      render: (_: any, record:API.alertItem) => {
        return (
          <div style={{minWidth:'100px'}}>{record.alertLevelMsg}</div>
        )
      }
    },
    {
      title: '开始时间',
      dataIndex: 'createTime',
      key: 'createTime',
      render: (_: any, record:API.alertItem) => {
        return (
          <div>{formatDate(record.createTime, 'yyyy-MM-dd hh:mm:ss')}</div>
        )
      },
    },
    {
      title: '服务名',
      dataIndex: 'serviceInstanceName',
      key: 'serviceInstanceName',
      filters: serviceOptions,
      onFilter: true,
      render: (_: any, record:API.alertItem) => {
        return (
          <div style={{minWidth:'100px'}}>{record.serviceInstanceName}</div>
        )
      },
    },
    {
      title: '角色名',
      dataIndex: 'serviceRoleLabel',
      key: 'serviceRoleLabel',
      filters: roleOptions,
      onFilter: true,
      render: (_: any, record:API.alertItem) => {
        return (
          <div style={{minWidth:'100px'}}>{record.serviceRoleLabel}</div>
        )
      },
    },
    {
      title: '节点',
      dataIndex: 'hostname',
      key: 'hostname',
      filters: hostnameOptions,
      onFilter: true,
      render: (_: any, record:API.alertItem) => {
        return (
          <div style={{minWidth:'100px'}}>{record.hostname}</div>
        )
      },
    },
    {
      title: '告警内容',
      dataIndex: 'info',
      key: 'info',
    },
    {
      title: '建议',
      dataIndex: 'advice',
      key: 'advice',
      render: (_: any, record:API.alertItem) => {
        return (
          <div style={{maxWidth:'500px',display:'flex',flexWrap:'wrap'}}>{record.advice}</div>
        )
      },
    }
  ]
  

  return (
    <PageContainer>
      <ProTable 
        search={false} 
        rowKey="alertId" 
        columns={columns} 
        dataSource={actionListData}
        request={async (params = {}, sort, filter) => {
          return getAlertAPI({ });;
        }}
      />
    </PageContainer>
  );
};

export default alertList;
