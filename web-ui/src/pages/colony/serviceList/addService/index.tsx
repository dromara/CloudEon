import { PageContainer, ProCard } from '@ant-design/pro-components';
import { Space, Steps, Button } from 'antd';
import { BorderOuterOutlined } from '@ant-design/icons';
import { FormattedMessage, useIntl, history } from 'umi';
import { useState } from 'react';
import styles from './index.less';
import { getListService } from '@/services/ant-design-pro/colony';

const { Step } = Steps;
const description = 'This is a description.';

const getServiceData = async (params: any) => {
  const result = await getListService(params);
  return result;
};

const ChooseService: React.FC<{
  serviceList: any;
  setServiceList: any;
}> = ({ serviceList, setServiceList }) => (
  <>
    <div className={styles.notSelectWrap}>
      <div>请选择需要安装的服务</div>
      <div className={styles.serviceListWrap}>
        {serviceList.map((sitem: any) => {
          return <ServiceItem item={sitem} type={0} setServiceList={setServiceList} />;
        })}
      </div>
    </div>
    <div className={styles.selectedWrap}>
      <div style={{ paddingLeft: '15px' }}>待安装的服务</div>
      <div className={styles.serviceListWrap}>
        {serviceList
          .filter((fitem: any) => {
            return fitem.selected;
          })
          .map((sitem: any) => {
            return <ServiceItem item={sitem} type={1} setServiceList={setServiceList} />;
          })}
      </div>
    </div>
  </>
);

const changeStatus = (item: any, list: any) => {};

const ServiceItem: React.FC<{
  item: any;
  type: number;
  setServiceList: any;
}> = ({ item, type, setServiceList }) => (
  <div className={styles.serviceItem}>
    <div className={styles.serviceItemLeft}>
      <div className={styles.serviceItemIcon}>
        <BorderOuterOutlined />
      </div>
      <div className={styles.serviceItemCenter}>
        <div className={styles.serviceItemTitle}>{item.name}</div>
        <div className={styles.serviceItemDesc}>HDFS是Hadoop应用的基本存储系统</div>
      </div>
    </div>
    <div>
      <div>
        {type == 0 ? (
          item.selected ? (
            <div className={styles.disabledBtn}>已添加</div>
          ) : (
            <div
              className={styles.activeBtn}
              onClick={() => {
                setServiceList();
              }}
            >
              添加
            </div>
          )
        ) : (
          <div>移除</div>
        )}
      </div>
    </div>
  </div>
);

const serviceAdd: React.FC = () => {
  const intl = useIntl();
  const serviceData = getServiceData({ clusterId: 1 });
  console.log('serviceData: ', serviceData);

  const startServiceList = [
    { name: 'HDFS', desc: 'HDFS是Hadoop应用的基本存储系统', selected: false },
    { name: 'HDFS', desc: 'HDFS是Hadoop应用的基本存储系统', selected: false },
    { name: 'HDFS', desc: 'HDFS是Hadoop应用的基本存储系统', selected: false },
  ];
  // 参数：状态初始值比如,传入 0 表示该状态的初始值为 0
  // 返回值：数组,包含两个值：1 状态值（state） 2 修改该状态的函数（setState）
  const [current, setCurrent] = useState(0);
  const [serviceList, setServiceList] = useState(startServiceList);
  // let currentNum: number = 2;
  console.log('--current:', current);

  let stepList: any[] = [
    { title: '选择服务', status: '' },
    { title: '配置安全', status: '' },
    { title: '分配角色', status: '' },
    { title: '配置服务', status: '' },
    { title: '服务总览', status: '' },
    { title: '安装', status: '' },
  ];
  const onChange = (value: number) => {
    // console.log('onChange:', current);
    // setCurrent(value);
    // console.log('onChange2:', current);
  };

  return (
    <PageContainer header={{ title: '' }}>
      <div className={styles.stepsLayout}>
        <div className={styles.stepsWrap}>
          <Steps
            direction="vertical"
            size="small"
            current={current}
            items={stepList}
            onChange={onChange}
          ></Steps>
        </div>
        <div className={styles.stepsContent}>
          <ChooseService serviceList={serviceList} setServiceList={setServiceList} />
          {/* <div className={styles.notSelectWrap}>
            <div>请选择需要安装的服务</div>
            <div className={styles.serviceListWrap}>
              <ChooseService value={0} />
              <ChooseService value={0} />
              <ChooseService value={0} />
            </div>
          </div>
          <div className={styles.selectedWrap}>
            <div style={{ paddingLeft: '15px' }}>待安装的服务</div>
            <div className={styles.serviceListWrap}>
              <ChooseService value={0} />
              <ChooseService value={0} />
              <ChooseService value={0} />
            </div>
          </div> */}
          <div className={styles.stepBottomBtns}>
            <Button style={{ marginRight: '5px' }}>取消/上一步</Button>
            <Button type="primary">下一步</Button>
          </div>
        </div>
      </div>
      {/* {stepList.map((item, index) => {
            <Step title={item.name} description={statusDesc[item.status]} />;
          })} */}
      {/* <Step title="Finished" description={description} />
          <Step title="In Progress" description={description} subTitle="Left 00:00:08" />
          <Step title="Waiting" description={description} /> */}
    </PageContainer>
  );
};

export default serviceAdd;
