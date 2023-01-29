// 集群管理页面
import { PageContainer, ProCard } from '@ant-design/pro-components';
import { SlackOutlined, DeleteOutlined, EditOutlined, EllipsisOutlined } from '@ant-design/icons';
import { Space, Popconfirm, Button, Modal, Form, Input, message } from 'antd';
import { FormattedMessage, useIntl, history } from 'umi';
import { useState, useRef } from 'react';
import styles from './index.less';
import { values } from 'lodash';

const Colony: React.FC = () => {
  const intl = useIntl();

  const [isModalOpen, setIsModalOpen] = useState(false);
  const [form] = Form.useForm();

  const showModal = () => {
    setIsModalOpen(true);
  };

  const handleOk = () => {
    form
      .validateFields()
      .then((values) => {
        console.log('values: ', values);
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

  const onFinish = (values: any) => {
    console.log('Success:', values);
  };

  const onFinishFailed = (errorInfo: any) => {
    console.log('Failed:', errorInfo);
  };

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
      <Space>
        <ProCard
          key="card1"
          style={{ width: 150, height: 150, padding: 0, overflow: 'hidden' }}
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
            history.push('/colony/nodeList');
          }}
        >
          <div className={styles.cardIcon}>
            <SlackOutlined key="slack1" />
            <div className={styles.cardDescText}>集群1</div>
          </div>
        </ProCard>
        <ProCard
          key="card2"
          style={{ width: 150, height: 150, padding: 0, overflow: 'hidden' }}
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
            history.push('/colony/nodeList');
          }}
        >
          <div className={styles.cardIcon}>
            <SlackOutlined key="slack1" />
            <div className={styles.cardDescText}>集群2</div>
          </div>
        </ProCard>
      </Space>
      <Modal
        title="新增集群"
        forceRender={true}
        destroyOnClose={false}
        open={isModalOpen}
        onOk={handleOk}
        onCancel={handleCancel}
        footer={null}
      >
        <div>
          <Form
            name="新增集群"
            preserve={false}
            labelCol={{ span: 8 }}
            wrapperCol={{ span: 16 }}
            initialValues={{ remember: true }}
            onFinish={onFinish}
            onFinishFailed={onFinishFailed}
            autoComplete="off"
          >
            <Form.Item
              label="集群编码"
              name="code"
              rules={[{ required: true, message: '请输入集群编码!' }]}
            >
              <Input />
            </Form.Item>

            <Form.Item
              label="集群名称"
              name="name"
              rules={[{ required: true, message: '请输入集群名称!' }]}
            >
              <Input />
            </Form.Item>

            <Form.Item
              label="框架名"
              name="kjname"
              rules={[{ required: true, message: '请输入框架名!' }]}
            >
              <Input />
            </Form.Item>

            <Form.Item wrapperCol={{ offset: 8, span: 16 }}>
              <Button type="primary" htmlType="submit">
                确定
              </Button>
            </Form.Item>
          </Form>
        </div>
      </Modal>
    </PageContainer>
  );
};

export default Colony;
