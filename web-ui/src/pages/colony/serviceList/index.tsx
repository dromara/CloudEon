// 集群管理页面
import { PageContainer, ProCard } from '@ant-design/pro-components';
import { Space, Button, Typography } from 'antd';
import { FormattedMessage, useIntl, history } from 'umi';

const serviceList: React.FC = () => {
  const intl = useIntl();

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
      <Space>
        <div
          onClick={() => {
            history.push('/colony/serviceList/detail');
          }}
        >
          12222
        </div>
        <div
          onClick={() => {
            history.push('/colony/serviceList/detail');
          }}
        >
          12222
        </div>
        <div
          onClick={() => {
            history.push('/colony/serviceList/detail');
          }}
        >
          12222
        </div>
        <div
          onClick={() => {
            history.push('/colony/serviceList/detail');
          }}
        >
          12222
        </div>
      </Space>
    </PageContainer>
  );
};

export default serviceList;
