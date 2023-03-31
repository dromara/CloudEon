// 集群管理页面
import { PageContainer, ProCard } from '@ant-design/pro-components';
import { SlackOutlined, DeleteOutlined, EditOutlined, EllipsisOutlined } from '@ant-design/icons';
import { Space, Popconfirm, Button, Modal, Form, Input, message, Spin, Select } from 'antd';
import { FormattedMessage, useIntl, history, useModel } from 'umi';
import { useState, useEffect, SetStateAction } from 'react';
import styles from './index.less';
import { getClusterListAPI, getStackListAPI, createClusterAPI } from '@/services/ant-design-pro/colony';

import AceEditor from "react-ace";
import "ace-builds/src-noconflict/mode-java";
import "ace-builds/src-noconflict/theme-xcode";
import "ace-builds/src-noconflict/ext-language_tools";


const { Option } = Select;

const Colony: React.FC = () => {
  const intl = useIntl();

  const [isModalOpen, setIsModalOpen] = useState(false);
  const [loading, setLoading] = useState(false);
  const [stackLoading, setStackLoading] = useState(false);
  const [clusterList, setClusterList] = useState<API.ColonyItem[]>();
  const [stackList, setStackList] = useState<API.StackItem[]>();
  const [kubeConfig, setKubeConfig] = useState('');
  const [colonyForm] = Form.useForm();

  const getClusterData = async (params: any) => {
    setLoading(true)
    const result: API.ColonyList =  await getClusterListAPI(params);
    setLoading(false)
    setClusterList(result?.data)
  };

  const getStackData = async (params: any) => {
    setStackLoading(true)
    const result: API.StackList =  await getStackListAPI(params);
    setStackLoading(false)
    setStackList(result?.data)
  };

  useEffect(() => {
    getClusterData({});
  }, []);

  const showModal = () => {
    getStackData({})
    setIsModalOpen(true);
  };

  const handleOk = () => {
    colonyForm
      .validateFields()
      .then(async (values) => {
        // console.log('values',values);
        const result: API.normalResult = await createClusterAPI(values)
        if(result && result.success){
          message.success('新增成功');
          getClusterData({});
          colonyForm.resetFields()
          setIsModalOpen(false);
        }
      })
      .catch((err) => {
        console.log('err: ', err);
      });
    // setIsModalOpen(false);
  };

  const handleCancel = () => {
    setIsModalOpen(false);
  };

  const confirm = (e: any) => {
    e.stopPropagation();
    console.log(e);
    message.success('已删除');
  };

  const cancel = (e: any) => {
    e.stopPropagation();
    console.log(e);
    message.error('已取消');
  };

  const onFinish = async (values: any) => {
    const result: API.normalResult = await createClusterAPI(values)
    if(result && result.success){
      message.success('新增成功');
      getClusterData({});
      setIsModalOpen(false);
    }
  };

  const onFinishFailed = (errorInfo: any) => {
    console.log('Failed:', errorInfo);
  };

  const onSelectChange = (value: string) => {
    console.log('onSelectChange:', value);
  };

  const onEditorChange = (value: any) => {
    console.log(value);
    
  }
  
  // const { colonyData, setClusterId } = useModel('colonyModel', model => ({ colonyData: model.colonyData, setClusterId: model.setClusterId }));

  // const gotoColony = (value: any) => {
  //   const { colonyData, setClusterId } = useModel('colonyModel', model => ({ colonyData: model.colonyData, setClusterId: model.setClusterId }));
  //   setClusterId(value)
  //   history.push('/colony/nodeList');
  // }

  return (
    <PageContainer
      header={{
        extra: [
          <Button
            type="primary"
            key="colonyadd"
            onClick={() => {
              showModal();
            }}
          >
            新增集群
          </Button>,
        ],
      }}
    >
      <Spin tip="Loading" size="small" spinning={loading}>
      <Space>
        {clusterList&&clusterList.map(cItem=>{
          return(
              <ProCard
                hoverable
                key={cItem.id}
                style={{ width: 250, height: 150, padding: 0, overflow: 'hidden' }}
                bodyStyle={{ padding: 0 }}
                actions={[
                  <Popconfirm
                    title="确定删除该集群吗?"
                    onConfirm={confirm}
                    onCancel={cancel}
                    okText="确定"
                    cancelText="取消"
                  >
                    <DeleteOutlined
                      key="setting"
                      onClick={(e) => {
                        e.stopPropagation();
                        console.log('SettingOutlined');
                      }}
                    />
                  </Popconfirm>,
                ]}
                onClick={() => {
                  // setClusterId(cItem.id || 0)
                  const sdata = {
                    clusterId: cItem.id,
                    clusterName: cItem.clusterName,
                    stackId: cItem.stackId
                  }
                  const getData = JSON.parse(sessionStorage.getItem('colonyData') || '{}')
                  sessionStorage.setItem('colonyData',JSON.stringify({...getData ,...sdata}) )
                  history.push('/colony/nodeList?clusterId='+cItem.id);
                }}
              >
                <div className={styles.stackWrap}>
                  <div className={styles.titleWrap}>
                    <SlackOutlined className={styles.cardIcon} />
                    <div className={styles.stackTitle}>{cItem.clusterName}</div>
                  </div>
                  <div className={styles.stackInfo}>
                    <div className={styles.stackDesc}>服务数：{cItem.serviceCnt}</div>
                    <div className={styles.stackDesc}>节点数：{cItem.nodeCnt}</div>
                  </div>
                </div>
              </ProCard>
          )
        })}
        
      </Space>
      </Spin>
      <Modal
        title="新增集群"
        forceRender={true}
        destroyOnClose={false}
        open={isModalOpen}
        onOk={handleOk}
        width={800}
        onCancel={handleCancel}
        // footer={null}
      >
        <div>
          <Form
            form={colonyForm}
            name="新增集群"
            preserve={false}
            labelCol={{ span: 6 }}
            wrapperCol={{ span: 15 }}
            initialValues={{ remember: true }}
            onFinish={onFinish}
            onFinishFailed={onFinishFailed}
            autoComplete="off"
          >
            <Form.Item
              label="集群名称"
              name="clusterName"
              rules={[{ required: true, message: '请输入集群名称!' }]}
            >
              <Input className={styles.inputWrap}/>
            </Form.Item>

            <Form.Item
              label="框架"
              name="stackId"
              rules={[{ required: true, message: '请选择框架!' }]}
            >
              <Select
                placeholder="请选择框架"
                onChange={onSelectChange}
                loading={stackLoading}
                className={styles.inputWrap}
                allowClear
              >
                {
                  stackList && stackList.map(sItem=>{
                    return (
                      <Option key={sItem.stackCode} value={sItem.id}>{sItem.stackCode}</Option>
                    )
                  })
                }
              </Select>
            </Form.Item>
            <Form.Item
              label="kubeConfig"
              name="kubeConfig"
              rules={[{ required: true, message: '请输入kubeConfig!' }]}
            >
              {/* <Input /> */}
              <AceEditor
                className={styles.codeEditorWrap}
                mode="java"
                theme="xcode"
                value={kubeConfig}
                onChange={onEditorChange}
                name="UNIQUE_ID_OF_DIV"
                editorProps={{ $blockScrolling: true }}
              />

            </Form.Item>

            {/* <Form.Item wrapperCol={{ offset: 8, span: 16 }}>
              <Button type="primary" htmlType="submit">
                确定
              </Button>
            </Form.Item> */}
          </Form>
        </div>
      </Modal>
    </PageContainer>
  );
};

export default Colony;
