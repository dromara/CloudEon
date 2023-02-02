// 集群管理页面
import { PageContainer, ProCard } from '@ant-design/pro-components';
import { Space, Card, Table, Button, Modal, Form, Input, message, Spin } from 'antd';
import React, { useState, useEffect, useRef } from 'react';
import type { FormInstance } from 'antd/es/form';
import { FormattedMessage, useIntl, history } from 'umi';
import { getNodeListAPI, createNodeAPI } from '@/services/ant-design-pro/colony';

const nodeList: React.FC = () => {
  const intl = useIntl();
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [nodeListData, setNodeListData] = useState<any[]>();
  const [loading, setLoading] = useState(false);
  const [form] = Form.useForm();
  // formRef = React.createRef<FormInstance>();
  // const form = useRef();

  const getNodeData = async (params: any) => {
    setLoading(true)
    const result: API.NodeList =  await getNodeListAPI(params);
    setLoading(false)
    setNodeListData(result?.data)
  };

  const getData = JSON.parse(sessionStorage.getItem('colonyData') || '{}')

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
    getNodeData({ clusterId: getData.clusterId });
  }, []);

  const columns = [
    {
      title: '节点',
      dataIndex: 'hostname',
      key: 'hostname',
    },{
      title: 'IP地址',
      dataIndex: 'ip',
      key: 'ip',
    },
    {
      title: '核数',
      dataIndex: 'coreNum',
      key: 'coreNum',
    },
    // {
    //   title: '机柜',
    //   dataIndex: 'jigui',
    //   key: 'jigui',
    // },{
    //   title: '仓库',
    //   dataIndex: 'canku',
    //   key: 'canku',
    // },
    // {
    //   title: '角色',
    //   dataIndex: 'serviceRoleNum',
    //   key: 'serviceRoleNum',
    // },
    {
      title: '处理器',
      dataIndex: 'cpuArchitecture',
      key: 'cpuArchitecture',
    },{
      title: '内存',
      dataIndex: 'totalMem',
      key: 'totalMem',
    },{
      title: '磁盘',
      dataIndex: 'totalDisk',
      key: 'totalDisk',
    }
  ]
  

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
            labelCol={{ span: 8 }}
            wrapperCol={{ span: 16 }}
            initialValues={{ remember: true }}
            // onFinish={onFinish}
            autoComplete="off"
          >
            <Form.Item
              label="ip"
              name="ip"
              rules={[{ required: true, message: '请输入ip!' }]}
            >
              <Input />
            </Form.Item>

            <Form.Item
              label="sshUser"
              name="sshUser"
              rules={[{ required: true, message: '请输入sshUser!' }]}
            >
              <Input />
            </Form.Item>

            <Form.Item
              label="sshPassword"
              name="sshPassword"
              rules={[{ required: true, message: '请输入sshPassword!' }]}
            >
              <Input />
            </Form.Item>

            <Form.Item
              label="sshPort"
              name="sshPort"
              rules={[{ required: true, message: 'sshPort!' }]}
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
