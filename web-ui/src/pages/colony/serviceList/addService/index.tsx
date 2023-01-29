import { PageContainer, ProCard } from '@ant-design/pro-components';
import { Space, Steps, Button, Spin } from 'antd';
import { BorderOuterOutlined } from '@ant-design/icons';
import { FormattedMessage, useIntl, useRequest, history } from 'umi';
import { useState, useEffect } from 'react';
import styles from './index.less';
import { getListService } from '@/services/ant-design-pro/colony';
import ChooseService from './components/ChooseService'
import ConfigSecurity from'./components/ConfigSecurity'
import AssignRoles from'./components/AssignRoles'
import ConfigService from'./components/ConfigService'
// import ConfigSecurity from'./components/ConfigSecurity'


const serviceAdd: React.FC = () => {
  const intl = useIntl();
  // 参数：状态初始值比如,传入 0 表示该状态的初始值为 0
  // 返回值：数组,包含两个值：1 状态值（state） 2 修改该状态的函数（setState）
  const [current, setCurrent] = useState(0);
  const [serviceListData, setServiceListData] = useState<any[]>();
  const [loading, setLoading] = useState(false);

  const getServiceData = async (params: any) => {
    setLoading(true)
    const result: API.ServiceList =  await getListService(params);

    setLoading(false)
    const statusData = result.data && result.data.map(item=>{
      return {
        ...item,
        selected:false
      }
    })
    setServiceListData(statusData)
  };

  useEffect(() => {
    getServiceData({ clusterId: 1 });
  }, []);

  const changeStatus = (id:number) => {
    const statusData = serviceListData && serviceListData.map(item=>{
      return {
        ...item,
        selected: item.id==id ? !item.selected : item.selected 
      }
    })
    setServiceListData(statusData)
  }

  const checkNext = () => {
    switch(current){
      case 0:
        const selectList = serviceListData?.filter(item=>{ return item.selected})
        return (selectList && selectList.length > 0 ? true : false)
      ;break;
      case 1:
        return true
      ;break;
      case 2:
        return true
      ;break;
      case 3:
        return true
      ;break;
      case 4:
        return true
      ;break;
      case 5:
        return true
      ;break;
    }
  }

  


  let stepList: any[] = [
    { title: '选择服务', status: '' },
    { title: '配置安全', status: '' },
    { title: '分配角色', status: '' },
    { title: '配置服务', status: '' },
    { title: '服务总览', status: '' },
    { title: '安装', status: '' },
  ];
  const onChange = (value: number) => {
    // setCurrent(value);
  };

  return (
    <PageContainer header={{ title: '' }}>
      <Spin tip="Loading" size="small" spinning={loading}>
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
            {(current == 0 && serviceListData ) && <ChooseService serviceList={serviceListData} changeStatus={changeStatus} />}
            { current == 1 && <ConfigSecurity />}
            { current == 2 && <AssignRoles serviceList={ serviceListData?.filter(item=>{ return item.selected}) || [] } /> }
            { current == 3 && <ConfigService />}
            {/* { current == 1 && <ConfigSecurity />}
            { current == 1 && <ConfigSecurity />} */}
            <div className={styles.stepBottomBtns}>
              <Button style={{ marginRight: '5px' }}               
                onClick={()=>{
                  if(current==0){
                    history.replace('/colony/serviceList');
                  }else{
                    setCurrent(current - 1);
                  }
                }}
              >
                {current == 0 ? '取消' : '上一步'}
              </Button>
              <Button type="primary" 
                disabled={!checkNext()} 
                onClick={()=>{
                  if(current == 5){ // 安装

                  }else{
                    setCurrent(current + 1);
                  }
                }}
              >下一步</Button>
            </div>
          </div>
        </div>
      </Spin>
    </PageContainer>
  );
};

export default serviceAdd;
