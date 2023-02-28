import { PageContainer, ProCard } from '@ant-design/pro-components';
import { Space, Steps, Button, Spin, message } from 'antd';
import { BorderOuterOutlined } from '@ant-design/icons';
import { FormattedMessage, useIntl, history } from 'umi';
import { useState, useEffect } from 'react';
import styles from './index.less';
import { getServiceListAPI, checkServiceAPI, getServiceConfAPI, initServiceAPI, serviceListAPI } from '@/services/ant-design-pro/colony';
import ChooseService from './components/ChooseService'
import ConfigSecurity from'./components/ConfigSecurity'
import AssignRoles from'./components/AssignRoles'
import ConfigService from'./components/ConfigService'
// import ConfigSecurity from'./components/ConfigSecurity'


const serviceAdd: React.FC = () => {
  const intl = useIntl();
  const colonyData = JSON.parse(sessionStorage.getItem('colonyData') || '{}')
  // 参数：状态初始值比如,传入 0 表示该状态的初始值为 0
  // 返回值：数组,包含两个值：1 状态值（state） 2 修改该状态的函数（setState）
  const [current, setCurrent] = useState(0);
  const [serviceListData, setServiceListData] = useState<any[]>();
  const [loading, setLoading] = useState(false);
  const [allParams, setSubmitallParams] = useState<API.SubmitServicesParams>();
  const [serviceInfos, setServiceInfos] = useState<API.ServiceInfosItem[]>()

  const checkService = async (params: any) => {
    try {
      setLoading(true)
      const result: API.normalResult =  await checkServiceAPI(params);
      setLoading(false)
      if(!result.success){
        // message.error(result.message);
        return false
      }
      return true
    } catch (error) {
      // message.error(error);
      return false
    }
  }; 

  const getServiceData = async (params: any) => {
    setLoading(true)
    const result: API.ServiceList =  await getServiceListAPI(params);
    setLoading(false)
    const statusData = result?.data?.map(item=>{
      return {
        ...item,
        selected:false
      }
    })
    setServiceListData(statusData)
  };

  const getSelectedService = ()=>{
    const selectList = serviceListData?.filter(item=>{ return item.selected})
    return selectList
  }

  const changeStatus = (id:number) => {
    const statusData = serviceListData && serviceListData.map(item=>{
      return {
        ...item,
        selected: item.id==id ? !item.selected : item.selected 
      }
    })
    setServiceListData(statusData)

    // 选择服务的数据放到总数据
    const serList = getSelectedService()
    let params = {...allParams}
    serList?.map(sItem=>{
      const sData = {
        stackServiceId: sItem.id,
        stackServiceName: sItem.name,
        stackServiceLabel: sItem.label,
        roles: sItem.roles.map((role: any)=>{
          return {
            stackRoleName: role,
            nodeIds:[]
          }
        }),
        presetConfList:[],
        customConfList:[]
      }
      const pIndex = params?.serviceInfos?.findIndex(pItem=>{ return pItem.stackServiceId == id })
      if(pIndex && pIndex == -1){
        params?.serviceInfos?.push(sData)
      }
    })
    setSubmitallParams(params)
  }

  // 已选择服务的id集合
  const selectListIds = getSelectedService()?.map(stem=> { return stem.id })

  // 配置安全的数据放到总数据
  const setKerberosToParams = (value: boolean) => {
    let params = {...allParams}
    if(value){
      params.enableKerberos = true
    }
    setSubmitallParams(params)
  }

  const setServiceInfosToParams = (value: API.ServiceInfosItem[]) => {
    let params = {...allParams}
    if(value){
      params.serviceInfos = value
    }
    setSubmitallParams(params)
  }
  interface anyKey{
    [key:number]:any,
  }
  
  const setPresetConfListToParams = async() => {
    const allConfData = JSON.parse(sessionStorage.getItem('allConfData') || '{}')
    let presetConfData:anyKey = {}
    let customConfData:anyKey = {}
    for(let key in allConfData){
      presetConfData[key] = []
      customConfData[key] = []
      allConfData[key].forEach((item: { isCustomConf: any; name: any; value: any; confFile: any; sourceValue: any; recommendExpression: any; })=>{
        if(item.isCustomConf){
          customConfData[key].push({
            name: item.name,
            value: item.value,
            confFile: item.confFile
          })
        }else{
          presetConfData[key].push({
            name: item.name,
            recommendedValue: item.sourceValue,
            value: item.recommendExpression
          })
        }
      })
      // allConfData[key] = allConfData[key].map((item: { name: any; sourceValue: any; recommendExpression: any; })=>{
      //   return {
      //     name: item.name,
      //     recommendedValue: item.sourceValue,
      //     value: item.recommendExpression
      //   }
      // })
    }
    let params = {...allParams}
    params?.serviceInfos?.map(async psItem=>{
      if(psItem.stackServiceId){
        if(allConfData[psItem.stackServiceId]){
          psItem.presetConfList = presetConfData[psItem.stackServiceId] //allConfData[psItem.stackServiceId]
          psItem.customConfList = customConfData[psItem.stackServiceId]
        } else {
            setLoading(true)
            const params = {
                serviceId: psItem.stackServiceId,
                inWizard: true
            }
            const result =  await getServiceConfAPI(params);
            const confsList = result?.data?.confs || []
            const confsdata = confsList.map((cItem)=>{
              return {
                name: cItem.name,
                recommendedValue: cItem.recommendExpression,
                value: cItem.recommendExpression
              }
            })
            setLoading(false)
            psItem.presetConfList = confsdata
            psItem.customConfList = []

        }
      }
    })
    setSubmitallParams(params)
  }

  const checkNext = () => {
    switch(current){
      case 0:
        const selectList = getSelectedService()
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
    // { title: '服务总览', status: '' },
    { title: '安装', status: '' },
  ];
  const onChange = (value: number) => {
    // setCurrent(value);
  };

  const initServiceInfos = () =>{
    const serArr = getSelectedService()?.map(item=>{
        return {
            stackServiceId: item.id,
            stackServiceName: item.name,
            stackServiceLabel: item.label,
            roles: item.roles.map((role: any)=>{
                return {
                    stackRoleName: role,
                    nodeIds: []
                }
            }),
            presetConfList:[],
            customConfList:[]
        }
    })    
    setServiceInfos(serArr)
  }

  const installService = async( initParams: API.SubmitServicesParams) =>{
    setLoading(true)
    const result = await initServiceAPI(initParams)
    setLoading(false)
    if(result?.success){
      message.success('安装成功！', 3)
      setTimeout(()=>{
        history.replace('/colony/serviceList');
      },3)
    }
  }

  useEffect(() => {
    getServiceData({ clusterId: 1 });
  }, []);

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
            { current == 1 && <ConfigSecurity selectListIds={selectListIds || []} setKerberosToParams={setKerberosToParams} />}
            { current == 2 && <AssignRoles serviceList={ getSelectedService() || [] } sourceServiceInfos={serviceInfos || []} setServiceInfosToParams={setServiceInfosToParams} /> }
            { current == 3 && <ConfigService setPresetConfListToParams={setPresetConfListToParams} />}
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
                  if(current == 0){
                    const params = {
                      "clusterId":colonyData.clusterId,
                      "stackId":colonyData.stackId,
                      "installStackServiceIds": selectListIds
                    }
                    checkService(params).then(checkResult=>{
                      if(checkResult) {
                        sessionStorage.setItem('colonyData',JSON.stringify({...colonyData , selectedServiceList: getSelectedService()}) )
                        setCurrent(current + 1);
                        initServiceInfos()
                      }
                    })
                  } else if(current == 3){ // 安装
                    setPresetConfListToParams()
                    const initParams = {
                      ...allParams, 
                      stackId: colonyData?.stackId,
                      clusterId: colonyData?.clusterId,
                    }
                    console.log('--allParams: ', initParams);
                    installService(initParams)

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
