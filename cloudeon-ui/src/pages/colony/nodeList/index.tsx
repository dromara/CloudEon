// 集群管理页面
import { PageContainer, ProCard } from '@ant-design/pro-components';
import { Space, Select, Table, Button, Modal, Form, Input, message, Spin } from 'antd';
import React, { useState, useEffect, useRef } from 'react';
import type { FormInstance } from 'antd/es/form';
import { FormattedMessage, useIntl, history } from 'umi';
import { getNodeListAPI, createNodeAPI, getListK8sNodeAPI } from '@/services/ant-design-pro/colony';

const nodeList: React.FC = () => {
  const intl = useIntl();
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [nodeListData, setNodeListData] = useState<any[]>();
  const [loading, setLoading] = useState(false);
  const [form] = Form.useForm();
  const [ipList, setIpList] = useState<any[]>()

  const getData = JSON.parse(sessionStorage.getItem('colonyData') || '{}')
  // formRef = React.createRef<FormInstance>();
  // const form = useRef();

  const getNodeData = async (params: any) => {
    setLoading(true)
    const result: API.NodeList =  await getNodeListAPI(params);
    setLoading(false)
    setNodeListData(result?.data)
  };

  const getk8sNodeList = async (params: any) => {
    setLoading(true)
    const result:API.nodeIpListResult = await getListK8sNodeAPI(params)
    setLoading(false)
    if(result?.data){
      const list = result.data.map(item=>{
        return {
          value: item.ip,
          label: `${item.hostname}(${item.ip})`,
        }
      })
      setIpList(list)
    }
    
  }


  // const onFinish = async (values: any) => {
  //   const result: API.normalResult = await createNodeAPI({...values, clusterId: getData.clusterId})
  //   if(result && result.success){
  //     message.success('新增成功');
  //     getNodeData({ clusterId: getData.clusterId });
  //     setIsModalOpen(false);
  //     form.resetFields()
  //   }
  // };
  const handleOk = () => {
    // console.log(form.getFieldsValue());
    form
      .validateFields()
      .then(async (values) => {
        const result: API.normalResult = await createNodeAPI({...values, clusterId: getData.clusterId})
        if(result && result.success){
          message.success('新增成功');
          getNodeData({ clusterId: getData.clusterId });
          setIsModalOpen(false);
          form.resetFields()
        }
      })
      .catch((err) => {
        console.log('err: ', err);
      });
  };

  const handleCancel = () => {
    form.resetFields()
    setIsModalOpen(false);
  };

  useEffect(() => {
    const params = { clusterId: getData.clusterId }
    getk8sNodeList(params);
    getNodeData(params);
  }, []);

  const columns = [
    {
      title: '主机名',
      dataIndex: 'hostname',
      key: 'hostname',
    },
    {
      title: 'IP地址',
      dataIndex: 'ip',
      key: 'ip',
    },
    {
      title: '总cpu',
      dataIndex: 'coreNum',
      key: 'coreNum',
    },
    {
      title: '总内存',
      dataIndex: 'totalMem',
      key: 'totalMem',
    },
    {
      title: '总硬盘',
      dataIndex: 'totalDisk',
      key: 'totalDisk',
    },
    {
      title: '容器版本',
      dataIndex: 'containerRuntimeVersion',
      key: 'containerRuntimeVersion',
    },
    {
      title: 'k8s版本',
      dataIndex: 'kubeletVersion',
      key: 'kubeletVersion',
    },
    {
      title: '系统内核',
      dataIndex: 'kernelVersion',
      key: 'kernelVersion',
    },
    {
      title: '操作系统',
      dataIndex: 'osImage',
      key: 'osImage',
    },
    {
      title: 'cpu架构',
      dataIndex: 'cpuArchitecture',
      key: 'cpuArchitecture',
    }
  ]


  const handleChange = (value: any)=>{
    console.log('value: ',value);
    
  }
  

  return (
    <PageContainer 
      header={{
      extra: [
        <Button
          key="addnode"
          type="primary"
          onClick={() => {
            setIsModalOpen(true)
          }}
        >
          新增节点
        </Button>,
      ],
    }}>
      <Table loading={loading} rowKey="id" columns={columns} dataSource={nodeListData} />
      <Modal
        key="addnodemodal"
        title="新增节点"
        forceRender={true}
        destroyOnClose={true}
        open={isModalOpen}
        onOk={handleOk}
        onCancel={handleCancel}
        // footer={null}
      >
        <div>
          <Form
            form={form}
            key="addnodeform"
            name="新增节点"
            preserve={false}
            labelCol={{ span: 6 }}
            wrapperCol={{ span: 16 }}
            initialValues={{ remember: true }}
            // onFinish={onFinish}
            autoComplete="off"
          >
            <Form.Item
              label="选择k8s节点"
              name="ip"
              rules={[{ required: true, message: '请选择k8s节点!' }]}
            >
               <Select
                  defaultValue=""
                  onChange={handleChange}
                  options={ipList}
                />
            </Form.Item>

            <Form.Item
              label="ssh账号"
              name="sshUser"
              rules={[{ required: true, message: '请输入ssh账号!' }]}
            >
              <Input />
            </Form.Item>

            <Form.Item
              label="ssh密码"
              name="sshPassword"
              rules={[{ required: true, message: '请输入ssh密码!' }]}
            >
              <Input />
            </Form.Item>

            <Form.Item
              label="ssh端口"
              name="sshPort"
              rules={[{ required: true, message: 'ssh端口!' }]}
            >
              <Input />
            </Form.Item>

            {/* <Form.Item wrapperCol={{ offset: 8, span: 16 }}>
              <Button key='addnodebtn' type="primary" htmlType="submit">
                确定
              </Button>
            </Form.Item> */}
          </Form>
        </div>
      </Modal>
    </PageContainer>
  );
};

export default nodeList;
