
import { ProCard } from '@ant-design/pro-components';
import { Tree, Table, notification, Alert } from 'antd';
import type { DataNode, TreeProps } from 'antd/es/tree';
import styles from './index.less'
import { useState, useEffect, useRef } from 'react';
import { getNodeListAPI } from '@/services/ant-design-pro/colony';

const AssignRoles : React.FC<{
    serviceList: any[]; // 原本选中的服务的树结构
    sourceServiceInfos: API.ServiceInfosItem[], // 原本选中的服务的树结构+节点(包括上一次选中的)
    setServiceInfosToParams: any,
    checkAllRolesRules:any,
}> = ({serviceList, sourceServiceInfos, setServiceInfosToParams, checkAllRolesRules})=>{

    const [selectedRowKeys, setSelectedRowKeys] = useState<React.Key[]>([]); // 选中的节点
    const [nodeListData, setNodeListData] = useState<any[]>();
    const [currentSelectRoles, setCurrentSelectRoles] = useState<selectRoleItem>();
    const [loading, setLoading] = useState(false);
    const [serviceInfos, setServiceInfos] = useState<API.ServiceInfosItem[]>();
    
    const getData = JSON.parse(sessionStorage.getItem('colonyData') || '{}')

    type NotificationType = 'success' | 'info' | 'warning' | 'error';

    type selectRoleItem = {
        validRule: any;
        stackServiceId: number,
        stackRoleName: any,
        nodeIds: any[]
    }

    const getNodeData = async (params: any) => {
        setLoading(true)
        const result: API.NodeList =  await getNodeListAPI(params);
        setLoading(false)
        setNodeListData(result?.data)
      };

    const initServiceInfos = () => {
        if(!sourceServiceInfos) return
        setServiceInfos(sourceServiceInfos)
        setServiceInfosToParams(sourceServiceInfos)
        const roleData = {
            stackServiceId: sourceServiceInfos[0].stackServiceId || 0,
            stackRoleName: sourceServiceInfos[0] && sourceServiceInfos[0].roles && sourceServiceInfos[0].roles[0].stackRoleName || '',
            nodeIds: sourceServiceInfos[0] && sourceServiceInfos[0].roles && sourceServiceInfos[0].roles[0].nodeIds || [],
            validRule: sourceServiceInfos[0] && sourceServiceInfos[0].roles && sourceServiceInfos[0].roles[0].validRule || {},
        }
        setCurrentSelectRoles(roleData)
        const newSelectedRowKeys = getNodeIds(sourceServiceInfos[0].stackServiceId, serviceList[0].roles[0])
        setSelectedRowKeys(newSelectedRowKeys);
    }

    const treeData = serviceList.map(item=>{
        let itemData = {
            title: item.label,
            key: item.id,
            selectable: false,
            children: item.roles? (item.roles.map((r: any)=>{
                return {
                    title: r,
                    key: r,
                    stackServiceId: item.id
                }
            })):[]
        }
        return itemData
    })

    const getNodeIds:any = (stackServiceId:number, stackRoleName:any) => {
        for (let sItem of (serviceInfos||sourceServiceInfos)) {
            if (sItem.stackServiceId == stackServiceId) {
                for (let role of sItem.roles||[]) {
                    if(role.stackRoleName == stackRoleName)
                    return role.nodeIds||[]
                }
            }
        }      
    }

    const getRoleRule:any = (stackServiceId:number, stackRoleName:any) => {
        for (let sItem of (serviceInfos||sourceServiceInfos)) {
            if (sItem.stackServiceId == stackServiceId) {
                for (let role of sItem.roles||[]) {
                    if(role.stackRoleName == stackRoleName)
                    return role.validRule||{}
                }
            }
        }      
    }

    const onSelectTree: TreeProps['onSelect'] = (selectedKeys, info) => {
        console.log('selected', selectedKeys, info);
        const newSelectedRowKeys = getNodeIds(info.selectedNodes[0].stackServiceId, selectedKeys[0])
        setSelectedRowKeys(newSelectedRowKeys);
        const roleData = {
            stackServiceId: info.selectedNodes[0].stackServiceId,
            stackRoleName: selectedKeys[0],
            nodeIds: newSelectedRowKeys,
            validRule: getRoleRule(info.selectedNodes[0].stackServiceId, selectedKeys[0]),
        }
        setCurrentSelectRoles(roleData)
        // console.log('--currentSelectRoles:', currentSelectRoles);
    };

    const columns = [
        {
            title: '节点',
            dataIndex: 'hostname',
            key: 'hostname',
        },
        {
            title: 'ip地址',
            dataIndex: 'ip',
            key: 'ip',
        },
    ];

    const [api, contextHolder] = notification.useNotification();

    // const openNotificationWithIcon = (type: NotificationType, str: string) => {
    //     api[type]({
    //     message: str,
    //     description:'',
    //     duration:null
    //     });
    // };

    const openNotificationWithIcon = (errList:string[]) =>{
        if(!errList || errList.length == 0) return
        const errDom = errList.map(item=>{
            return (<Alert type="error" message={item} banner />)
        })
        notification.open({
            message: '温馨提示',
            description:<>{errDom}</>,
            duration:null,
            style: {
                width: 500
            }
            // onClick: () => {
            //   console.log('Notification Clicked!');
            // },
          });
    }

    // 检验是否符合节点选择规则
    // const checkRule = (newSelectedRowKeys: number[], currentSelectRoles: selectRoleItem | undefined) => {
    //     if(!currentSelectRoles || !newSelectedRowKeys) return
    //     notification.destroy()
    //     if(currentSelectRoles?.validRule){
    //         // minNum为至少要选的节点数
    //         // fixedNum为固定的节点数，不能多不能少
    //         // needOdd 为节点数是否需要是奇数
    //         let {fixedNum, minNum,needOdd} = currentSelectRoles?.validRule
    //         let errList = []
    //         if(fixedNum && newSelectedRowKeys.length != fixedNum){
    //             errList.push(`${currentSelectRoles.stackRoleName} 要求选择${fixedNum}个节点数`) 
    //             // setErrInfo({[currentSelectRoles.stackRoleName]: errList})
    //         }
    //         if(minNum && newSelectedRowKeys.length < minNum){
    //             errList.push(`${currentSelectRoles.stackRoleName} 建议至少选择${minNum}个节点数`)
    //             // setWarnInfo({[currentSelectRoles.stackRoleName]: warnStr})
    //         }
    //         if(needOdd && newSelectedRowKeys.length%2 == 0){
    //             errList.push(`${currentSelectRoles.stackRoleName} 建议选择奇数个节点数`)
    //             // setErrInfo({[currentSelectRoles.stackRoleName]: errList})
    //         }            
    //         if(errList && errList.length > 0){
    //             openNotificationWithIcon(errList)
    //             checkRoleNext(false)
    //         }else{
    //             checkRoleNext(true)
    //         }
    //     }
    // }


    const onSelectChange = (newSelectedRowKeys: number[]) => { // 节点勾选触发的函数
        console.log('---onSelectChange:', newSelectedRowKeys, currentSelectRoles);
        // checkRule( newSelectedRowKeys, currentSelectRoles)
        
        
        setSelectedRowKeys(newSelectedRowKeys);
        let roles = {...currentSelectRoles}
        const newServiceInfos = [...(serviceInfos||[])]
        roles.nodeIds = newSelectedRowKeys
        for (let sItem of newServiceInfos||[]) {
            if (sItem.stackServiceId == roles.stackServiceId) {
                for (let role of sItem.roles||[]) {
                    if(role.stackRoleName == roles.stackRoleName)
                    role.nodeIds = newSelectedRowKeys
                }
            }
        } 
        setServiceInfos(newServiceInfos)
        checkAllRolesRules(newServiceInfos)
        setServiceInfosToParams(newServiceInfos)
    };

    const rowSelection = {
        selectedRowKeys,
        onChange: onSelectChange,
        getCheckboxProps: (record: { name: string; }) => ({
            disabled: !(currentSelectRoles && currentSelectRoles.stackRoleName), 
          }),
      };

    useEffect(() => {
        initServiceInfos()
        getNodeData({ clusterId: getData.clusterId })
    }, []);




    return (
        <>
        <ProCard split="vertical" bordered>
            <ProCard title="服务" colSpan="30%" headerBordered className={styles.rolesLeft}>
            <Tree
                defaultExpandAll
                blockNode
                defaultSelectedKeys={[serviceList[0].roles[0]]}
                onSelect={onSelectTree}
                treeData={treeData}
                style={{background: '#fbfbfe'}}
            />
            </ProCard>
            <ProCard title="节点分配" headerBordered className={styles.nodeWrap}>
            {contextHolder}
                <Table 
                    rowKey="id"
                    loading={loading}
                    rowSelection={rowSelection} 
                    columns={columns} 
                    dataSource={nodeListData} 
                />
            </ProCard>
            </ProCard>
        </>
    )
}

export default AssignRoles